package com.jianglei.jllog.methodtrace;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
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
import com.jianglei.jllog.utils.LogUtils;
import com.jianglei.jllog.utils.MethodUtils;

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
     * 当前柱状图展示的是一系列不相干的方法
     */
    public static final int STATUS_METHODS = 3;
    private BarChart barChart;
    private TextView tvLastLevel, tvDetail;
    private String curProcessName;
    private MethodStack.MethodNode curSelectedNode;
    private int status = STATUS_METHODS;

    /**
     * 记录方法调用栈
     */
    private Stack<StackNode> nodeStack = new Stack<>();

    private Stack<String> descStack = new Stack<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jl_fragment_method, container, false);
        barChart = view.findViewById(R.id.bar_chart);
        tvLastLevel = view.findViewById(R.id.tv_last_level);
        tvDetail = view.findViewById(R.id.tv_detail);
        TextView tvConfig = view.findViewById(R.id.tv_config);
        tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMarkView();
                if (curSelectedNode == null || curSelectedNode.getChildNodes().size() == 0) {
                    return;
                }
                descStack.push(barChart.getDescription().getText());
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
                String desc = descStack.pop();
                barChart.getDescription().setText(desc);
            }
        });

        tvConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getChildFragmentManager();
                MethodFilterDialog dialog = MethodFilterDialog.newInstance(curProcessName);
                dialog.setOnConfigListener(new MethodFilterDialog.OnConfigListener() {
                    @Override
                    public void onClassSelected(String className) {
                        switchToClassBar(className);
                    }
                });
                dialog.show(fm, "dialog");
            }
        });

        view.findViewById(R.id.tv_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToInit();
                tvDetail.setVisibility(View.GONE);
                tvLastLevel.setVisibility(View.GONE);
            }
        });

        Spinner spProcessName;

        spProcessName = view.findViewById(R.id.sp_process_name);
        final List<String> processNames = MethodStack.getProcessNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.jl_simple_list_item,
                R.id.tvContent,processNames);
        spProcessName.setAdapter(adapter);
        spProcessName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                closeMarkView();
                curProcessName = processNames.get(position);
                updateChart(MethodStack.getInstance(curProcessName).getFirstLevelNode());
                tvDetail.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        initChar();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void closeMarkView() {
        barChart.highlightValue(null);
        barChart.invalidate();
    }

    private void initChar() {
//        barChart.getDescription().setEnabled(false);
        barChart.setMaxVisibleValueCount(60);
        barChart.getDescription().setTextColor(ContextCompat.getColor(getActivity(), R.color.jl_white));
        barChart.getLegend().setTextColor(ContextCompat.getColor(getActivity(), R.color.jl_white));
        barChart.setNoDataText(getString(R.string.jl_chart_no_data));
        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMinimum(0);
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

        MethodMarkView methodMarkView = new MethodMarkView(getActivity());
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
        barChart.getDescription().setText(getString(R.string.jl_first_level_node));
//        updateChart(MethodStack.getInstance().getFirstLevelNode());

    }

    /**
     * 根据方法节点绘制柱状图，如果方法 节点为空就绘制当前选中类的
     */
    private void updateChart(List<MethodStack.MethodNode> nodes) {
        List<BarEntry> entries = new ArrayList<>(1);
        int k = 0;
        for (int i = 0; i < nodes.size(); ++i) {
            MethodStack.MethodNode node = nodes.get(i);
            if (node.getTime() < 500000) {
                LogUtils.w(node.getClassNameAndHash() + ":" + node.getMethodName() + " " + node.getTime() + "ns 小于1ms，不展示");
                continue;
            }
            BarEntry barEntry = new BarEntry(++k, node.getTime() / 1000000f);
            barEntry.setData(node);
            entries.add(barEntry);
        }
        if (entries.size() == 0) {

            barChart.clear();
            return;
        }
        Log.d("longyi", "bar数量：" + entries.size());
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
            List<IBarDataSet> barDataSets = new ArrayList<>(nodes.size());
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
        barChart.setNoDataText(getString(R.string.jl_chart_no_data1, MethodUtils.getSimpleClassName(className)));
        MethodStack stack = MethodStack.getInstance(curProcessName);
        List<MethodStack.MethodNode> nodes = stack.getIndex().get(className);
        if (nodes == null || nodes.size() == 0) {
            barChart.clear();
        } else {

            updateChart(nodes);
        }
        Description desc = barChart.getDescription();
        desc.setText(className);
        barChart.setDescription(desc);
        status = STATUS_CLASS;
    }

    private void switchToMethodBar(MethodStack.MethodNode node) {
        if (node == null || node.getChildNodes().size() == 0) {
            barChart.clear();
            return;
        }
        barChart.getDescription().setText(getString(R.string.jl_method_child, node.getMethodName()));
        updateChart(node.getChildNodes());
        status = STATUS_METHOD;
    }

    private void switchToInit() {
        barChart.getDescription().setText(getString(R.string.jl_first_level_node));
        updateChart(MethodStack.getInstance(curProcessName).getFirstLevelNode());
        status = STATUS_METHODS;
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
