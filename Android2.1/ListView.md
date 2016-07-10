
# 学习目标：

1. 理解ListView的基础使用

2. 学会使用适配器（ArrayAdapter(数组适配器)、SimpleAdapter(简单适配器)、BaseAdapter）

3. 学会使用监听器（OnScrollListener(滚动事件的监听)、OnItemClickListener(监听单个列表事件)）

4. 学会使用适配器的数据刷新（notifyDataChanger）

## ListView的作用

用于在android系统中显示列表的控件，每一个ListView都可以包含很多个列表项。

#### 数据适配器：就是把复杂的数据（数组、链表、数据库、集合等）填充在指定的视图界面上

* ArrayAdapter(数组适配器)：

		用于绑定格式单一的数据
		数据源可以是集合或者数组

* SimpleAdapter(简单适配器)：
	
		用于绑定格式复杂的数据
		数据源只能 是特定泛型的集合

**数据适配器是连接数据源和视图界面的桥梁**

实现适配器的过程：新建适配器--添加数据源到适配器--视图加载适配器


##ArrayAdapter：

1. 在布局文件中添加LisetView

		<ListView
	        android:id="@+id/lv_show"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent">
    	</ListView>

2. 在Activity中把LisetView以及适配器声明出来

		private ListView mLv_show;
    	private ArrayAdapter<String> mArr_adapter;
		private SimpleAdapter mSimp_Adapter;

3. 在onCreate方法中完成数据与视图的绑定

		mLv_show = (ListView) findViewById(R.id.lv_show);

4. 新建适配器、加载数据源、加载适配器
		
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

