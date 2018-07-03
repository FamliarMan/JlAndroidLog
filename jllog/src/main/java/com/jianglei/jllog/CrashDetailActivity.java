package com.jianglei.jllog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jianglei.jllog.aidl.CrashVo;

/**
 * @author jianglei
 */
public class CrashDetailActivity extends JlBaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_show);
        CrashVo crashVo = getIntent().getParcelableExtra("crashVo");
        if(crashVo == null){
            return;
        }
        TextView tvCrashDetail = (TextView) findViewById(R.id.tv_crash);
        tvCrashDetail.setText(crashVo.getCrashInfo());
    }
}
