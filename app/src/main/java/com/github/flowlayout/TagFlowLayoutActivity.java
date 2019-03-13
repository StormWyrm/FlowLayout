package com.github.flowlayout;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.StormWyrm.flowlayout_library.TagAdapter;
import com.github.StormWyrm.flowlayout_library.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TagFlowLayoutActivity extends AppCompatActivity {
    private TagFlowLayout tfl;
    private TagAdapter tagAdapter;
    private List<String> mDatas;
    private HashSet<Integer> selectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_flow_layout);
        tfl = findViewById(R.id.tfl);

        initData();
        initAdapter();
    }

    private void initData() {
        mDatas = new ArrayList<>();
        mDatas.add(new String("王者荣耀"));
        mDatas.add(new String("QQ飞车手游"));
        mDatas.add(new String("攻城掠地"));
        mDatas.add(new String("梦幻西游"));
        mDatas.add(new String("攻城地"));
        mDatas.add(new String("梦游"));
        mDatas.add(new String("梦"));
        mDatas.add(new String("火影忍者"));
        mDatas.add(new String("攻城地"));
        mDatas.add(new String("梦游"));
        mDatas.add(new String("火影忍者"));
        mDatas.add(new String("攻城地"));
        mDatas.add(new String("梦游"));
        mDatas.add(new String("梦"));
        mDatas.add(new String("穿越火线：枪战王者"));
        mDatas.add(new String("永恒纪元"));
        mDatas.add(new String("王者荣耀"));

        selectList = new HashSet<>();
        selectList.add(0);
        selectList.add(1);
        selectList.add(10);
        selectList.add(6);
    }

    private void initAdapter() {
        tagAdapter = new TagAdapter<String>(mDatas) {
            @Override
            public View getView(TagFlowLayout tagFlowLayout, int position, String item) {
                TextView textView = new TextView(TagFlowLayoutActivity.this);
                textView.setBackgroundResource(R.drawable.selector_tag_view);
                textView.setText(item);
                textView.setTextColor(Color.WHITE);
                return textView;
            }
        };
        tagAdapter.setSelectedList(selectList);
        tagAdapter.setOnTagClickListener(new TagAdapter.OnTagClickListener() {
            @Override
            public boolean onTagClick(TagFlowLayout tagFlowLayout, View view, int position) {
                Toast.makeText(TagFlowLayoutActivity.this, mDatas.get(position), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        tagAdapter.setOnTagSelectListener(new TagAdapter.OnTagSelectListener() {
            @Override
            public void onTagSelect(Set<Integer> selectViews) {
                String indexs = "";
                for (Integer index : selectViews){
                    indexs += index+",";
                }
                Toast.makeText(TagFlowLayoutActivity.this, indexs, Toast.LENGTH_SHORT).show();
            }
        });
        tfl.setAdapter(tagAdapter);
    }
}
