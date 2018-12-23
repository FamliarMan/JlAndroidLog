package com.jianglei.jllog.uiblock;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jianglei.jllog.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author jianglei on 12/23/18.
 */

public class UiTracerAdapter extends RecyclerView.Adapter<UiTracerAdapter.TracerHolder> {
    private Context context;
    private List<UiBlockVo> uiBlockVos;
    private OnItemClickListener onItemClickListener;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public UiTracerAdapter(Context context, List<UiBlockVo> uiBlockVos) {
        this.context = context;
        this.uiBlockVos = uiBlockVos;
    }

    @NonNull
    @Override
    public TracerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.jl_listitem_only_text, parent, false);
        return new TracerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TracerHolder holder, final int position) {
        final UiBlockVo uiBlockVo = uiBlockVos.get(position);
        String time = dataFormat.format(new Date(uiBlockVo.getTime()));
        holder.tvContent.setText(context.getString(R.string.jl_ui_block_holder,
                uiBlockVo.getBlockTime(), time));
        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position, uiBlockVo);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return uiBlockVos == null ? 0 : uiBlockVos.size();
    }

    public static class TracerHolder extends RecyclerView.ViewHolder {
        private TextView tvContent;
        private LinearLayout layoutMain;

        public TracerHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            layoutMain = itemView.findViewById(R.id.layout_main);
        }
    }

    public interface OnItemClickListener {
        /**
         * item被点击时触发
         *
         * @param position  item位置
         * @param uiBlockVo 详细数据
         */
        void onClick(int position, UiBlockVo uiBlockVo);
    }
}
