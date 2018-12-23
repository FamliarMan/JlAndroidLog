package com.jianglei.jllog.life;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jianglei.jllog.R;
import com.jianglei.jllog.aidl.LifeVo;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by jianglei on 7/24/18.
 */

public class LIfeCyclerAdapter extends RecyclerView.Adapter<LIfeCyclerAdapter.LifeViewHolder> {
    private List<LifeVo> mLifeVos;
    private Context mContext;
    private SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());

    public LIfeCyclerAdapter(List<LifeVo> mLifeVos, Context mContext) {
        this.mLifeVos = mLifeVos;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public LifeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(mContext).inflate(R.layout.jl_listitem_life,parent,false);
        return new LifeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LifeViewHolder holder, int position) {
        LifeVo lifeVo = mLifeVos.get(position);
        holder.tvHostClass.setText(lifeVo.getHostClass());
        holder.tvLife.setText(lifeVo.getLifeType());
        holder.tvTime.setText(sdft.format(lifeVo.getTime()));
    }

    @Override
    public int getItemCount() {
        return mLifeVos == null ? 0 : mLifeVos.size();
    }

    static class LifeViewHolder extends RecyclerView.ViewHolder {
        TextView tvHostClass, tvLife, tvTime;

        public LifeViewHolder(View itemView) {
            super(itemView);
            tvHostClass = itemView.findViewById(R.id.tv_host_class);
            tvLife = itemView.findViewById(R.id.tv_life);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
