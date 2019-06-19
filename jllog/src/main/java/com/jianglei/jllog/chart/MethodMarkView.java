package com.jianglei.jllog.chart;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.jianglei.jllog.R;
import com.jianglei.jllog.methodtrace.MethodStack;

/**
 * @author longyi created on 19-6-19
 */
public class MethodMarkView extends MarkerView {
    private TextView tvMarker;

    public MethodMarkView(Context context) {
        super(context, R.layout.jl_custom_marker_view);
        tvMarker = findViewById(R.id.tv_marker);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvMarker.setText(getContext().getString(R.string.jl_mark_view,
                ((MethodStack.MethodNode)e.getData()).getMethodName(), e.getY()));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2f), -getHeight());
    }

}
