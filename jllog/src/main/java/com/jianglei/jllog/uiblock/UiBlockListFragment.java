package com.jianglei.jllog.uiblock;

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

import com.jianglei.jllog.CrashAdapter;
import com.jianglei.jllog.CrashDetailActivity;
import com.jianglei.jllog.ILogShowActivity;
import com.jianglei.jllog.R;
import com.jianglei.jllog.aidl.CrashVo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * @author jianglei
 */
public class UiBlockListFragment extends Fragment {
    private List<UiBlockVo> uiBlockVos;

    private UiTracerAdapter adapter;
    private RecyclerView rvUiBlock;
    private ILogShowActivity logShowActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ILogShowActivity) {
            logShowActivity = (ILogShowActivity) context;
        }
    }

    public UiBlockListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (logShowActivity == null) {
            uiBlockVos = new LinkedList<>();
        } else {
            uiBlockVos = logShowActivity.getUiTraces();
        }
    }

    public static UiBlockListFragment newInstance() {
        return new UiBlockListFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jl_fragment_list, container, false);
        rvUiBlock= (RecyclerView) view.findViewById(R.id.rv_content);
        rvUiBlock.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new UiTracerAdapter(getActivity(), uiBlockVos);
        rvUiBlock.setAdapter(adapter);
        adapter.setOnItemClickListener(new UiTracerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, UiBlockVo uiBlockVo) {
                Intent intent = new Intent(getActivity(),UiBlockDetailActivity.class);
                intent.putExtra("uiBlockVo",uiBlockVo);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uiBlockVos.clear();
                adapter.notifyDataSetChanged();
                if (logShowActivity != null) {
                    logShowActivity.clearUi();
                }
            }
        });
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
