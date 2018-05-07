package com.jianglei.jllog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jianglei.jllog.aidl.NetInfoVo;

import java.util.List;

/**
 * Created by jianglei on 5/5/18.
 */

public class NetInfoAdapter extends RecyclerView.Adapter<NetInfoAdapter.NetInfoViewHolder> {
    private Context context;
    private List<NetInfoVo> netInfoVos;
    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public NetInfoAdapter(Context context) {
        this.context = context;
    }

    public NetInfoAdapter(Context context, List<NetInfoVo> netInfoVos) {
        this.context = context;
        this.netInfoVos = netInfoVos;
    }

    @NonNull
    @Override
    public NetInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.jl_listitem_net, parent, false);
        return new NetInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NetInfoViewHolder holder, int position) {
        if (netInfoVos == null) {
            return;
        }
        final NetInfoVo netInfoVo = netInfoVos.get(position);
        if(netInfoVo == null){
            return;
        }
        holder.tvUrl.setText(netInfoVo.getUrl());
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null){
                    itemClickListener.onItemClick(netInfoVo);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return netInfoVos == null ? 0 : netInfoVos.size();
    }

    class NetInfoViewHolder extends RecyclerView.ViewHolder {
        TextView tvUrl;
        LinearLayout mainLayout;

        public NetInfoViewHolder(View itemView) {
            super(itemView);
            tvUrl = itemView.findViewById(R.id.tv_url);
            mainLayout = itemView.findViewById(R.id.layout_main);
        }
    }

    public interface  OnItemClickListener{
        void onItemClick(NetInfoVo netInfoVo);
    }
}
