package com.m2comm.prs2019f.modules.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.github.barteksc.pdfviewer.PDFView;
import com.m2comm.prs2019f.R;

import java.io.File;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;

public class Download_PDFViewerActivity extends Activity {
    private Globar g;
    private ImageView closeBt;
    // circle progress Bar
    CircleProgressView mCircleView;

    //file
    String filePath;
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_pdfviewer);
        this.g = new Globar(this);
        this.closeBt = findViewById(R.id.pdf_close);
        this.mCircleView = findViewById(R.id.pdf_circleView);

        this.closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                AndroidNetworking.cancelAll();
            }
        });

        AndroidNetworking.initialize(getApplicationContext());
        this.mCircleView.setSpinningBarLength(180);
        //this.mCircleView.setShowTextWhileSpinning(true); // Show/hide text in spinning mode
        this.mCircleView.setTextMode(TextMode.TEXT);
        this.mCircleView.setUnitVisible(false);

        Intent intent = new Intent(this.getIntent());
        String url = intent.getStringExtra("url");
        String[] urlCut = url.split("/");

        this.filePath = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));//getRootDirPath(getApplicationContext());
        this.filename = urlCut[urlCut.length - 1];
        this.mCircleView.setVisibility(View.VISIBLE);
        this.mCircleView.spin();

        Log.d("fileName ", filePath + "/" + filename);
        File file = new File(filePath + "/" + filename);
        if (file.exists()) {
            this.downloadSuccess();
            return;
        } else {
            this.pdfDownload(url);
        }
    }

    private void pdfDownload(String url) {
        AndroidNetworking.download(url, this.filePath, this.filename)
                .setTag("download")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d("MAIN_TAG", " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d("MAIN_TAG", " bytesSent : " + bytesSent);
                        Log.d("MAIN_TAG", " bytesReceived : " + bytesReceived);
                        Log.d("MAIN_TAG", " isFromCache : " + isFromCache);
                    }
                })
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        // do anything with progress'
                        mCircleView.setMaxValue(totalBytes);
                        mCircleView.setValue(bytesDownloaded);
                        Log.d("LOGO_PROGRESS", "Downloaded: " + bytesDownloaded + "/" + totalBytes);
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        // do anything after completion
                        mCircleView.stopSpinning();
                        mCircleView.setVisibility(View.GONE);
                        downloadSuccess();
                    }

                    @Override
                    public void onError(ANError error) {
                        mCircleView.stopSpinning();
                        mCircleView.setVisibility(View.GONE);
                        Log.e("LOGO_ERROR", error.getErrorCode() + "    " + error.toString());
                        // handle error
                    }
                });
    }

    private void downloadSuccess() {
        Log.d("MAIN_TAG_COMPLETE", this.filePath + "/" + this.filename);
        File excelFile = new File(this.filePath, this.filename);
        String extension = MimeTypeMap.getFileExtensionFromUrl(this.filename);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());

        if (filename.toLowerCase().contains("pdf")) {
            PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
            File localFile = new File(filePath + "/" + filename);

            pdfView.fromFile(localFile).load();
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".com.m2comm.prs2019f.kingka.provider", excelFile);
                intent.setDataAndType(contentUri, mimeType);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            } else {
                intent.setDataAndType(Uri.fromFile(excelFile), mimeType);
            }
            startActivity(intent);
            finish();
        }

        mCircleView.stopSpinning();
        mCircleView.setVisibility(View.GONE);
    }

    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(), null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }
}
