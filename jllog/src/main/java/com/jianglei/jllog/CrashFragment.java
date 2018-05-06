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

import com.jianglei.jllog.aidl.CrashVo;

import java.util.ArrayList;
import java.util.List;


public class CrashFragment extends Fragment {
    private List<CrashVo> crashVos;

    private CrashAdapter adapter;
    private UIReceiver receiver;
    private RecyclerView rvCrash;

    public CrashFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        crashVos = bundle.getParcelableArrayList("crashVos");
    }

    public static CrashFragment newInstance(List<CrashVo> crashVos) {
        CrashFragment fragment = new CrashFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("crashVos", (ArrayList<? extends Parcelable>) crashVos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crash, container, false);
        rvCrash = view.findViewById(R.id.rv_crash);
        rvCrash.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CrashAdapter(getActivity(), crashVos);
        rvCrash.setAdapter(adapter);

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
            CrashVo crashVo = intent.getParcelableExtra("crashVo");
            if(crashVo == null){
                return;
            }
            crashVos.add(crashVo);
            if (crashVos.size() == JlLog.getMaxNetRecord()) {
                crashVos.remove(0);
                adapter.notifyDataSetChanged();
            } else {
                adapter.notifyItemInserted(crashVos.size() - 1);
            }
            rvCrash.scrollToPosition(crashVos.size() - 1);

        }
    }
}
