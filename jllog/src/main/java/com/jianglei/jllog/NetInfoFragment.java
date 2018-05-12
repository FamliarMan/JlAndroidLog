package com.jianglei.jllog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jianglei.jllog.aidl.NetInfoVo;

import java.util.LinkedList;
import java.util.List;


public class NetInfoFragment extends Fragment {
    private List<NetInfoVo> netInfoVos = new LinkedList<>();

    private NetInfoAdapter adapter;
    private RecyclerView rvNet;
    private UIReceiver receiver;
    private ILogShowActivity logShowActivity;

    public NetInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ILogShowActivity) {
            logShowActivity = (ILogShowActivity) context;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (logShowActivity != null) {
            netInfoVos = logShowActivity.getNetInfo();
        } else {
            netInfoVos = new LinkedList<>();
        }

    }

    public static NetInfoFragment newInstance() {
        return new NetInfoFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_net_info, container, false);
        rvNet = (RecyclerView) view.findViewById(R.id.rv_net);
        view.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netInfoVos.clear();
                adapter.notifyDataSetChanged();
//                JlLog.clearNetInfo();
                if (logShowActivity != null) {
                    logShowActivity.clearNet();
                }
            }
        });
        rvNet.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NetInfoAdapter(getActivity(), netInfoVos);
        adapter.setItemClickListener(new NetInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NetInfoVo netInfoVo) {
                Intent intent = new Intent(getActivity(), NetDetailActivity.class);
                intent.putExtra("netInfoVo", netInfoVo);
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
            if (netInfoVo == null) {
                return;
            }
            adapter.notifyDataChange(netInfoVos);
            rvNet.scrollToPosition(netInfoVos.size() - 1);

        }
    }
}
