package com.jianglei.jllog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jianglei.jllog.aidl.LifeVo;

import java.util.LinkedList;
import java.util.List;


/**
 * 生命周期展示fragment
 *
 * @author jianglei
 */
public class LifeCyclerFragment extends Fragment {
    private List<LifeVo> mLifeVos = new LinkedList<>();

    private LIfeCyclerAdapter mAdapter;
    private ILogShowActivity logShowActivity;

    public LifeCyclerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ILogShowActivity) {
            logShowActivity = (ILogShowActivity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (logShowActivity != null) {
            mLifeVos = logShowActivity.getLifeVos();
        } else {
            mLifeVos = new LinkedList<>();
        }

    }

    public static LifeCyclerFragment newInstance() {
        return new LifeCyclerFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_life, container, false);
        RecyclerView mRvLife =  view.findViewById(R.id.rv_life);
        view.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLifeVos.clear();
                mAdapter.notifyDataSetChanged();
                if (logShowActivity != null) {
                    logShowActivity.clearNet();
                }
            }
        });
        mRvLife.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new LIfeCyclerAdapter(mLifeVos, getActivity());
        mRvLife.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
