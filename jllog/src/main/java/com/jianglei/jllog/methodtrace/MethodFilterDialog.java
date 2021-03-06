package com.jianglei.jllog.methodtrace;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.jianglei.jllog.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author longyi created on 19-6-19
 */
public class MethodFilterDialog extends DialogFragment {
    private AutoCompleteTextView tvClassName;
    private Button btnConfirm;
    private OnConfigListener onConfigListener;
    private Set<String> allClassNames;
    private MyArrayAdapter<String> classAdapter;
    private String curProcessName;

    public void setOnConfigListener(OnConfigListener onConfigListener) {
        this.onConfigListener = onConfigListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            curProcessName = getArguments().getString("processName");
        }
        View view = inflater.inflate(R.layout.jl_method_filter_fragment, container, false);
        initView(view);
        return view;
    }

    public void initView(View view) {

        btnConfirm = view.findViewById(R.id.tv_class_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!allClassNames.contains(tvClassName.getText().toString())) {
                    Toast.makeText(getActivity(), getString(R.string.jl_invalid_class_name),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onConfigListener != null && tvClassName.getText().toString().length() != 0) {
                    onConfigListener.onClassSelected(tvClassName.getText().toString());
                }
                if (!TextUtils.isEmpty(tvClassName.getText())) {
                    dismiss();
                }
            }
        });

        tvClassName = view.findViewById(R.id.tv_class_name);
        tvClassName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    btnConfirm.setVisibility(View.GONE);
                } else {
                    btnConfirm.setVisibility(View.VISIBLE);
                }

            }
        });
        allClassNames = MethodStack.getInstance(curProcessName).getAllClassName();
        List<String> classNames = new ArrayList<>(allClassNames);
        MyArrayAdapter<String> adapter = new MyArrayAdapter<String>(getActivity(),
                R.layout.jl_simple_list_item, R.id.tvContent,classNames);
        tvClassName.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.95), (int) (dm.heightPixels * 0.75));
        }
    }

    public static MethodFilterDialog newInstance(String processName) {
        MethodFilterDialog dialog = new MethodFilterDialog();
        Bundle bundle = new Bundle();
        bundle.putString("processName", processName);
        dialog.setArguments(bundle);
        return dialog;
    }

    public interface OnConfigListener {
        /**
         * 某个类被选中
         *
         * @param className
         */
        void onClassSelected(String className);
    }
}
