package com.jianglei.jllog.uiblock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.jianglei.jllog.JlBaseActivity;
import com.jianglei.jllog.R;

/**
 * @author jianglei on 12/23/18.
 */

public class UiBlockDetailActivity extends JlBaseActivity {
    private TextView tvContent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jl_activity_ui_block_detail);
        tvContent = findViewById(R.id.tv_stack_content);
        UiBlockVo uiBlockVo = getIntent().getParcelableExtra("uiBlockVo");
        tvContent.setText(uiBlockVo.getStackTrace());
    }
}
