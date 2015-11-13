package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ThrowOrReport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throw_or_report);
        Button Throw = (Button) findViewById(R.id.ThrowButton);
        Throw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Throw();
            }
        });
        Button Report = (Button) findViewById(R.id.ReportButton);
        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Report();
            }
        });
    }
    private void Throw(){
        Boolean report = false;
        Intent intent = new Intent(this, ThrowCategory.class);
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("report", report.booleanValue());

        //TOAST FOR DEBUGGING
        Bundle parseInfo = intent.getExtras();
        Toast boom = new Toast(getApplicationContext());
        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        boom.makeText(ThrowOrReport.this, parseInfo.toString(), boom.LENGTH_SHORT).show();

        startActivity(intent);
    }
    private void Report(){
        Boolean report = true;
        Intent intent = new Intent(this, ReportSuccess.class);
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("report", report.booleanValue());

        //TOAST FOR DEBUGGING
        Bundle parseInfo = intent.getExtras();
        Toast boom = new Toast(getApplicationContext());
        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        boom.makeText(ThrowOrReport.this, parseInfo.toString(), boom.LENGTH_SHORT).show();

        startActivity(intent);
    }

}