package com.xn.listviewrefresh;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements RefreshListView.IReflashListener {

    private RefreshListView mLv_show;
    private ArrayAdapter<String> mArr_adapter;
    private SimpleAdapter mSimp_Adapter;
    private List<Map<String, Object>> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
//        setArrayAdapter();
        setSimpleAdapter();

    }

    private void initView() {
        mLv_show = (RefreshListView) findViewById(R.id.lv_show);
    }

    private void setArrayAdapter() {
        //1、新建一个数据适配器
        /**参数：
         * 第一个：上下文
         * 第二个：当前ListView加载的每一个列表项所对应的布局文件(先使用android自带的)
         * 第三个：数据源
         */
        //2、加载数据源
        String[] datas = {"数据源1", "数据源2", "数据源3", "数据源4", "数据源5"};
        mArr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);

        //3、使用视图(ListView)加载适配器
        mLv_show.setAdapter(mArr_adapter);
    }

    private void setSimpleAdapter() {
        /**SimpleAdapter参数
         * (Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
         * 第一个：context 上下文
         * 第二个：data 数据源(List<? extends Map<String,?>> data)一个Map所组成的List集合
         *              每一个Map都会去对应ListView列表中的一行
         *              每一个Map(键值对)中的键必须包含所有在from中所指定的键
         * 第三个：resource 列表项的布局文件ID
         * 第四个：from Map中的键名
         * 第五个：to 绑定数据视图中的ID，与from成对应关系
         */

        mDataList = new ArrayList<>();
        mSimp_Adapter = new SimpleAdapter(this,
                getDatas(),
                R.layout.layout_item,
                new String[]{"pic", "text"},
                new int[]{R.id.iv_pic, R.id.tv_text});
        mLv_show.setInterface(this);

        mLv_show.setAdapter(mSimp_Adapter);
    }

    public List<Map<String, Object>> getDatas() {

        for (int i = 0; i < 20; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("pic", R.mipmap.ic_launcher);
            map.put("text", "demo" + i);
            mDataList.add(map);
        }
        return mDataList;
    }

    @Override
    public void onReflash() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getReflashDatas();

                setSimpleAdapter();

                mLv_show.reflashComplete();
            }
        }, 2000);
    }

    public List<Map<String, Object>> getReflashDatas() {

        for (int i = 0; i < 2; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("pic", R.mipmap.ic_launcher);
            map.put("text", "刷新的数据" + i);
            mDataList.add(0, map);
        }
        return mDataList;
    }
}
