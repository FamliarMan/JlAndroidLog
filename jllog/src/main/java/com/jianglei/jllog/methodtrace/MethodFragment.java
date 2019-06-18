package com.jianglei.jllog.methodtrace;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.jianglei.jllog.R;
import com.jianglei.jllog.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodFragment extends Fragment {
    private Spinner spinner;
    private BarChart barChart;
    private String curClassName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jl_fragment_method, container, false);
        spinner = view.findViewById(R.id.sp_name);
        barChart = view.findViewById(R.id.bar_chart);

        initChar();
        initSpinner();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        barCharUpdate();
    }

    private void initSpinner() {
        final String[] classNames = (String[]) MethodStack.getInstance().getIndex().keySet().toArray(new String[0]);
        Arrays.sort(classNames);
        SpinnerAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, classNames);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curClassName = classNames[position];
                barCharUpdate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initChar(){
        Description desc = new Description();
        desc.setText(getString(R.string.jl_method_cost));
        barChart.setDescription(desc);
    }
    private void barCharUpdate() {
        if(curClassName == null){
            return;
        }
        List<MethodStack.MethodNode> nodes = MethodStack.getInstance().getIndex().get(curClassName);
        List<IBarDataSet> barDataSets = new ArrayList<>(nodes.size());
        for (int i = 0; i < nodes.size(); ++i) {
            MethodStack.MethodNode node = nodes.get(i);
            List<BarEntry> entries = new ArrayList<>(1);
            entries.add(new BarEntry(i, node.getTime()/100000f));
            BarDataSet barDataSet = new BarDataSet(entries, node.getMethodName());
            barDataSet.setValueTextColors(Arrays.asList(ContextCompat.getColor(getActivity(),R.color.jl_white)));
            barDataSets.add(barDataSet);
        }
        BarData barData = new BarData(barDataSets);
        barChart.setData(barData);
        barChart.invalidate();
    }

    public static MethodFragment getInstance() {
        return new MethodFragment();
    }
}
