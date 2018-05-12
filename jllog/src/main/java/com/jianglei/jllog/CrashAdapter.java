package com.jianglei.jllog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jianglei.jllog.aidl.CrashVo;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by jianglei on 5/5/18.
 */

public class CrashAdapter extends RecyclerView.Adapter<CrashAdapter.NetInfoViewHolder> {
    private Context context;
    private List<CrashVo> crashVos;

    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CrashAdapter(Context context) {
        this.context = context;
    }

    public CrashAdapter(Context context, List<CrashVo> crashVos) {
        this.context = context;
        this.crashVos = crashVos;
    }

    public void notifyDataChange(List<CrashVo>data){
        this.crashVos = data;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public NetInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.jl_listitem_crash, parent, false);
        return new NetInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NetInfoViewHolder holder, int position) {
        if (crashVos == null) {
            return;
        }
        final CrashVo crashVo = crashVos.get(position);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(crashVo.getTime());
        holder.tvCrash.setText(context.getString(R.string.jl_crash_item, time));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null){
                    itemClickListener.onItemClick(crashVo);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return crashVos == null ? 0 : crashVos.size();
    }

    class NetInfoViewHolder extends RecyclerView.ViewHolder {
        TextView tvCrash;

        LinearLayout mainLayout;
        public NetInfoViewHolder(View itemView) {
            super(itemView);
            tvCrash = (TextView)itemView.findViewById(R.id.tv_crash);
            mainLayout = (LinearLayout) itemView.findViewById(R.id.layout_main);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(CrashVo crashVo);
    }
}
