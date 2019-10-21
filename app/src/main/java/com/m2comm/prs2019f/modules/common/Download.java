package com.m2comm.prs2019f.modules.common;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

public class Download {


    String dlpath = "m2";
    String newFilename;
    DownloadManager dm;
    Context c;
    long id;

    public Download(String fUrl, Context c , String filename) {
        this.c = c;
        newFilename = dlpath+"/"+filename;
        // Make a request
        DownloadManager.Request request
                = new DownloadManager.Request(Uri.parse(fUrl))
                .setAllowedOverRoaming(false)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(newFilename)
                .setDescription("Download file");

        if (android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED)) {
            request.setDestinationInExternalPublicDir(
                    android.os.Environment.DIRECTORY_DOWNLOADS, newFilename);
        }

        // enqueue
        dm = (DownloadManager) this.c.getSystemService(Context.DOWNLOAD_SERVICE);
        id = dm.enqueue(request);
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        this.c.registerReceiver(downloadReceiver, intentFilter);
    }

    private final BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            Cursor cursor = dm.query(query);
            if (cursor.moveToFirst()) {
                int columIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                int status = cursor.getInt(columIndex);
                //다운로드되었는지 파악함.
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    Toast.makeText(c, "Download Success", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
