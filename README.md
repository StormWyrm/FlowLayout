# FlowLayout
Android流式布局,支持单选/多选操作,支持默认选择

##特点功能：
1. 支持设置行间距，Child View间距
2. 支持setAdapter()方法注入数据
4. 支持adapter.notifyDataChanged来属性数据
5. 支持adapter.setSelectList()设置默认选择列表
5. 支持控制选择tag的数量，支持多选和单选操作
6. 支持onTagClickListener点击监听事件和onTagSelectListenter选择监听事件

##项目使用：  

 Step1. 根项目build.gradle 添加：
 
 
     repositories {
         maven { url "https://jitpack.io" }
     }  
     
     
 Step2. 项目moudle build.gradle添加依赖：
    
    
    dependencies {
        implementation 'com.github.StormWyrm:FlowLayout:1.0.3'
    }

##用法：
 布局文件中声明：
 
 
     <com.qingfeng.flowlayout_ibrary.TagFlowLayout xmlns:android="http://schemas.android.com/apk/res/android"
         xmlns:app="http://schemas.android.com/apk/res-auto"
         xmlns:tools="http://schemas.android.com/tools"
         android:id="@+id/tfl"
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         android:padding="20dp"
         app:item_margin="10dp"
         app:line_margin="10dp"
         app:max_select="3"
         tools:context=".TagFlowLayoutActivity">
     </com.qingfeng.flowlayout_ibrary.TagFlowLayout>
     
     
 支持属性：  
 item_margin:设置之间的间距  
 line_margin:设置行间距  
 max_select:设置最大支持选择tag的数目,当小于0时不限制选择的数量 等于0时不支持选择（默认） 大于0时为支持选择的tag的个数
 
 设置数据：
 
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
            
    tfl.setAdapter(tagAdapter);
   
   
 设置默认选择(默认从开始到遍历符合条件的角标)：
 
     selectList = new HashSet<>();
            selectList.add(0);
            selectList.add(1);
            selectList.add(10);
            selectList.add(6);
            
    tagAdapter.setSelectedList(selectList);
    
 设置点击事件（当两个事件同时存在时，onTagClick返回true,选择事件才会响应）：
    
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


