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
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jianglei.jllog.R;
import com.jianglei.jllog.chart.MethodMarkView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class MethodFragment extends Fragment {
    /**
     * 当前柱状图展示的是某个类的方法耗时情况
     */
    public static final int STATUS_CLASS = 0;
    /**
     * 当前柱状图展示的是某个方法下级的所有方法耗时情况
     */
    public static final int STATUS_METHOD = 1;
    /**
     * 当前柱状图展示的是某个两个方法之间的所有方法调用情况
     */
    public static final int STATUS_STATICS = 2;
    private Spinner spinner;
    private BarChart barChart;
    private TextView tvLastLevel, tvDetail;
    private MethodMarkView methodMarkView;
    private String curClassName;
    private MethodStack.MethodNode curSelectedNode;
    private int status = STATUS_CLASS;

    /**
     * 记录方法调用栈
     */
    private Stack<StackNode> nodeStack = new Stack<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jl_fragment_method, container, false);
        spinner = view.findViewById(R.id.sp_name);
        barChart = view.findViewById(R.id.bar_chart);
        tvLastLevel = view.findViewById(R.id.tv_last_level);
        tvDetail = view.findViewById(R.id.tv_detail);
        tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMarkView();
                if (curSelectedNode == null || curSelectedNode.getChildNodes().size() == 0) {
                    return;
                }
                if (status == STATUS_METHOD) {
                    nodeStack.push(new StackNode(status, curSelectedNode));
                } else {
                    List<MethodStack.MethodNode> nodes = new ArrayList<>();
                    BarDataSet set = (BarDataSet) barChart.getBarData().getDataSetByIndex(0);
                    for (int i = 0; i < set.getEntryCount(); ++i) {
                        nodes.add((MethodStack.MethodNode) set.getEntryForIndex(i).getData());
                    }
                    nodeStack.push(new StackNode(status, nodes));
                }
                switchToMethodBar(curSelectedNode);
                tvLastLevel.setVisibility(View.VISIBLE);
                tvDetail.setVisibility(View.GONE);
            }
        });
        tvLastLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMarkView();
                if (nodeStack.size() == 0) {
                    tvLastLevel.setVisibility(View.GONE);
                    return;
                }
                StackNode stackNode = nodeStack.pop();
                if (stackNode.status == STATUS_METHOD) {
                    switchToMethodBar((MethodStack.MethodNode) stackNode.data);
                } else {
                    //noinspection unchecked
                    updateChart((List<MethodStack.MethodNode>) stackNode.data);
                    status = stackNode.status;
                }
                tvDetail.setVisibility(View.GONE);
                if (nodeStack.size() == 0) {
                    tvLastLevel.setVisibility(View.GONE);
                }
            }
        });

        initChar();
        initSpinner();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void closeMarkView(){
        barChart.highlightValue(null);
        barChart.invalidate();
    }
    private void initSpinner() {
        final String[] classNames = MethodStack.getInstance().getIndex().keySet().toArray(new String[0]);
        Arrays.sort(classNames);
        SpinnerAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, classNames);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curClassName = classNames[position];
                switchToClassBar(curClassName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvDetail.setVisibility(View.GONE);

            }
        });
    }

    private void initChar() {
        barChart.getDescription().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setTextColor(ContextCompat.getColor(getActivity(), R.color.jl_white));
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                return (int) value + " ms";
            }
        });
        barChart.getAxisRight().setEnabled(false);

        methodMarkView = new MethodMarkView(getActivity());
        methodMarkView.setChartView(barChart);
        barChart.setMarker(methodMarkView);

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                curSelectedNode = (MethodStack.MethodNode) e.getData();
                if (curSelectedNode.getChildNodes().size() == 0) {
                    tvDetail.setVisibility(View.GONE);
                } else {
                    tvDetail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected() {
                tvDetail.setVisibility(View.GONE);

            }
        });


    }

    /**
     * 根据方法节点绘制柱状图，如果方法 节点为空就绘制当前选中类的
     */
    private void updateChart(List<MethodStack.MethodNode> nodes) {
        List<IBarDataSet> barDataSets = new ArrayList<>(nodes.size());
        List<BarEntry> entries = new ArrayList<>(1);
        for (int i = 0; i < nodes.size(); ++i) {
            MethodStack.MethodNode node = nodes.get(i);
            if (node.getTime() < 100000) {
                continue;
            }
            BarEntry barEntry = new BarEntry(i, node.getTime() / 100000f);
            barEntry.setData(node);
            entries.add(barEntry);
        }
        BarDataSet barDataSet;
        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            barDataSet = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            barDataSet.setValues(entries);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        } else {
            barDataSet = new BarDataSet(entries, getString(R.string.jl_method_cost));
            barDataSet.setValueTextColors(Arrays.asList(ContextCompat.getColor(getActivity(), R.color.jl_white)));
            barDataSets.add(barDataSet);
            BarData barData = new BarData(barDataSets);
            barChart.setData(barData);
            barChart.invalidate();
        }
    }

    /**
     * 将柱状图切换到类模式
     *
     * @param className 类名
     */
    private void switchToClassBar(String className) {
        MethodStack stack = MethodStack.getInstance();
        List<MethodStack.MethodNode> nodes = stack.getIndex().get(className);
        if (nodes == null || nodes.size() == 0) {
            barChart.clear();
        } else {
            updateChart(nodes);
        }
        status = STATUS_CLASS;
    }

    private void switchToMethodBar(MethodStack.MethodNode node) {
        if (node == null || node.getChildNodes().size() == 0) {
            barChart.clear();
            return;
        }
        updateChart(node.getChildNodes());
        status = STATUS_METHOD;
    }

    private static class StackNode {
        int status;
        Object data;

        public StackNode(int status, Object data) {
            this.status = status;
            this.data = data;
        }
    }


    public static MethodFragment getInstance() {
        return new MethodFragment();
    }
}
