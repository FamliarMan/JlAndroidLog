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

import com.jianglei.jllog.aidl.CrashVo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * @author jianglei
 */
public class CrashListFragment extends Fragment {
    private List<CrashVo> crashVos = new ArrayList<>();

    private CrashAdapter adapter;
    private UiReceiver receiver;
    private RecyclerView rvCrash;
    private ILogShowActivity logShowActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ILogShowActivity) {
            logShowActivity = (ILogShowActivity) context;
        }
    }

    public CrashListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (logShowActivity == null) {
            crashVos = new LinkedList<>();
        } else {
            crashVos = logShowActivity.getCrashVo();
        }
    }

    public static CrashListFragment newInstance() {
        return new CrashListFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crash, container, false);
        rvCrash = (RecyclerView) view.findViewById(R.id.rv_crash);
        rvCrash.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CrashAdapter(getActivity(), crashVos);
        rvCrash.setAdapter(adapter);
        adapter.setItemClickListener(new CrashAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CrashVo crashVo) {
                Intent intent = new Intent(getActivity(),CrashDetailActivity.class);
                intent.putExtra("crashVo",crashVo);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crashVos.clear();
                adapter.notifyDataSetChanged();
                if (logShowActivity != null) {
                    logShowActivity.clearNet();
                }
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction("updateUI");
        receiver = new UiReceiver();
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

    class UiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            CrashVo crashVo = intent.getParcelableExtra("crashVo");
            if (crashVo == null) {
                return;
            }
            //数据在service中已经更新，此处直接更新界面即可
            adapter.notifyDataSetChanged();
            rvCrash.scrollToPosition(crashVos.size() - 1);

        }
    }
}
