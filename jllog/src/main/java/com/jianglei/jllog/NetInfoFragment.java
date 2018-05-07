package com.jianglei.jllog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jianglei.jllog.aidl.NetInfoVo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class NetInfoFragment extends Fragment {
    private List<NetInfoVo> netInfoVos = new LinkedList<>();

    private NetInfoAdapter adapter;
    private RecyclerView rvNet;
    private UIReceiver receiver;

    public NetInfoFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        List<NetInfoVo> vos= bundle.getParcelableArrayList("netInfoVos");
        if(vos == null){
            return;
        }
        for(NetInfoVo vo : vos){
            netInfoVos.add(vo);
        }

    }

    public static NetInfoFragment newInstance(List<NetInfoVo> netInfoVos) {
        NetInfoFragment fragment = new NetInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("netInfoVos", (ArrayList<? extends Parcelable>) netInfoVos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_net_info, container, false);
        rvNet = view.findViewById(R.id.rv_net);
        view.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netInfoVos.clear();
                adapter.notifyDataSetChanged();
                JlLog.clearNetInfo();
            }
        });
        rvNet.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NetInfoAdapter(getActivity(), netInfoVos);
        adapter.setItemClickListener(new NetInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NetInfoVo netInfoVo) {
                Intent intent = new Intent(getActivity(),NetRequestShowActivity.class);
                intent.putExtra("netInfoVo",netInfoVo);
                startActivity(intent);
            }
        });
        rvNet.setAdapter(adapter);
        IntentFilter filter = new IntentFilter();
        filter.addAction("updateUI");
        receiver = new UIReceiver();
        if (getActivity() != null) {
            getActivity().registerReceiver(receiver, filter);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }

    class UIReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NetInfoVo netInfoVo = intent.getParcelableExtra("netInfoVo");
            if(netInfoVo == null){
                return;
            }
            netInfoVos.add(netInfoVo);
            if(netInfoVos.size() == JlLog.getMaxNetRecord()){
                netInfoVos.remove(0);
                adapter.notifyDataSetChanged();
            }else {
                adapter.notifyItemInserted(netInfoVos.size() - 1);
            }
            rvNet.scrollToPosition(netInfoVos.size()-1);

        }
    }
}
