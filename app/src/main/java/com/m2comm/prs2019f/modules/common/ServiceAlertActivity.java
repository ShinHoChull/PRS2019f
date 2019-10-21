package com.m2comm.prs2019f.modules.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.m2comm.prs2019f.R;


public class ServiceAlertActivity extends AppCompatActivity implements View.OnClickListener {

    Globar g;
    private LinearLayout l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_alert);
        this.g = new Globar(this);

        Intent intent = new Intent(this.getIntent());
            this.g.notiAlertMessage("대한신경정신의학회",intent.getStringExtra("content"),this,intent.getStringExtra("paramUrl"));

        l = findViewById(R.id.servcie_alert_v);
        l.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
