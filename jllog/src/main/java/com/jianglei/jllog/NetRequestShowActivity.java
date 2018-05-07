package com.jianglei.jllog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jianglei.jllog.aidl.NetInfoVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class NetRequestShowActivity extends AppCompatActivity {

    private TextView tvHeader,tvQueryParams,tvPostParams,tvResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_request_show);
        tvHeader = findViewById(R.id.tv_header);
        tvQueryParams = findViewById(R.id.tv_query_params);
        tvPostParams = findViewById(R.id.tv_post_params);
        tvResponse = findViewById(R.id.tv_response);
        NetInfoVo netInfoVo = getIntent().getParcelableExtra("netInfoVo");
        showNetInfo(netInfoVo);
    }

    private void showNetInfo(NetInfoVo netInfoVo){
        tvHeader.setText(formatKeyValue(netInfoVo.getRequestHeader()));
        tvQueryParams.setText(formatKeyValue(netInfoVo.getRequsetUrlParams()));
        tvPostParams.setText(formatKeyValue(netInfoVo.getRequestForm()));
        tvResponse.setText(formatJson(netInfoVo.getResponseJson()));

    }

    private String formatKeyValue(Map<String,String>params){
        if(params == null){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,String>entry : params.entrySet()){
            sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
    private String formatJson(String jsonStr){
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }
        return sb.toString();
    }

    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
}
