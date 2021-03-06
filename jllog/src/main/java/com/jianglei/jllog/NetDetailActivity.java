package com.jianglei.jllog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.jianglei.jllog.aidl.NetInfoVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * 显示网络详细信息的activity
 * @author jianglei
 */
public class NetDetailActivity extends JlBaseActivity {

    private JustifyTextView tvHeader, tvQueryParams, tvPostParams;
    private TextView tvResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_request_show);
        tvHeader = (JustifyTextView) findViewById(R.id.tv_header);
        tvQueryParams = (JustifyTextView) findViewById(R.id.tv_query_params);
        tvPostParams = (JustifyTextView) findViewById(R.id.tv_post_params);
        tvResponse =  findViewById(R.id.tv_response);
        NetInfoVo netInfoVo = getIntent().getParcelableExtra("netInfoVo");
        showNetInfo(netInfoVo);
    }

    private void showNetInfo(NetInfoVo netInfoVo) {
        tvHeader.setText(formatKeyValue(netInfoVo.getRequestHeader()));
        tvQueryParams.setText(formatKeyValue(netInfoVo.getRequsetUrlParams()));
        tvPostParams.setText(formatKeyValue(netInfoVo.getRequestForm()));
        if(!netInfoVo.isSuccessful()){
            tvResponse.setText(netInfoVo.getErrorMsg());
            return;
        }
        try {
            //这样做是为了处理json中unicode编码问题，jsonobject会自动解码
            String json = netInfoVo.getResponseJson();
            if (TextUtils.isEmpty(json)) {
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                tvResponse.setText(formatJson(jsonArray.toString()));
            } else {
                JSONObject jsonObject = new JSONObject(netInfoVo.getResponseJson());
                tvResponse.setText(formatJson(jsonObject.toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            tvResponse.setText(formatJson(netInfoVo.getResponseJson()));
        }

    }

    private String formatKeyValue(Map<String, String> params) {
        if (params == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    /**
     * 返回格式化JSON字符串。
     *
     * @param json 未格式化的JSON字符串。
     * @return 格式化的JSON字符串。
     */
    public String formatJson(String json) {
        StringBuilder result = new StringBuilder();

        int length = json.length();
        int number = 0;
        char key = 0;
        //遍历输入字符串。
        for (int i = 0; i < length; i++) {
            //1、获取当前字符。
            key = json.charAt(i);

            //2、如果当前字符是前方括号、前花括号做如下处理：
            if ((key == '[') || (key == '{')) {
                //（2）打印：当前字符。
                result.append(key);

                //（3）前方括号、前花括号，的后面必须换行。打印：换行。
                result.append('\n');

                //（4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
                number++;
                result.append(indent(number));

                //（5）进行下一次循环。
                continue;
            }

            //3、如果当前字符是后方括号、后花括号做如下处理：
            if ((key == ']') || (key == '}')) {
                //（1）后方括号、后花括号，的前面必须换行。打印：换行。
                result.append('\n');

                //（2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
                number--;
                result.append(indent(number));

                //（3）打印：当前字符。
                result.append(key);

                //（4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
                if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
                    result.append('\n');
                }

                //（5）继续下一次循环。
                continue;
            }

            //4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
            if ((key == ',')) {
                result.append(key);
                result.append('\n');
                result.append(indent(number));
                continue;
            }

            //5、打印：当前字符。
            result.append(key);
        }

        return result.toString();
    }

    /**
     * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
     *
     * @param number 缩进次数。
     * @return 指定缩进次数的字符串。
     */
    private String indent(int number) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            result.append("   ");
        }
        return result.toString();
    }


}
