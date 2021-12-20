package com.test.sns_project.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.test.sns_project.R;

public class NotificationActivity extends BasicActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        CharSequence msg = "MainActivity에서 전달 받은 값";
        int id = 0;

        Bundle extras = getIntent().getExtras();
        if (extras == null){
            msg = "error";
        }else{
            id = extras.getInt("noti_Id");
        }

        TextView tv_ac_noti = (TextView) findViewById(R.id.tv_ac_noti);
        msg = msg+" "+id;
        tv_ac_noti.setText(msg);
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        nm.cancel(id);
    }
}
