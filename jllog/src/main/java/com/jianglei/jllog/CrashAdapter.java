package com.jianglei.jllog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public CrashAdapter(Context context) {
        this.context = context;
    }

    public CrashAdapter(Context context, List<CrashVo> crashVos) {
        this.context = context;
        this.crashVos = crashVos;
    }

    @NonNull
    @Override
    public NetInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.jl_listitem_crash,parent,false);
        return new NetInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NetInfoViewHolder holder, int position) {
        if(crashVos == null){
            return;
        }
        CrashVo crashVo = crashVos.get(position);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(crashVo.getTime());
        holder.tvCrash.setText(context.getString(R.string.jl_crash_item,time));
    }


    @Override
    public int getItemCount() {
        return crashVos == null ? 0 : crashVos.size();
    }

    class NetInfoViewHolder extends RecyclerView.ViewHolder{
        TextView tvCrash;
        public NetInfoViewHolder(View itemView) {
            super(itemView);
            tvCrash= itemView.findViewById(R.id.tv_crash);
        }
    }
}
