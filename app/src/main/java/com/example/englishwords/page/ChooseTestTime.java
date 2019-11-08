package com.example.englishwords.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.englishwords.R;
import com.example.englishwords.adapter.ChooseTestTimeAdapter;
import com.example.englishwords.util.fileOperator;

import java.util.List;

/**
 * @author HX
 * @title: ChooseTestTime
 * @projectName Words_System
 * @date 2019/9/10  16:21
 * 选择测试时间界面
 */
public class ChooseTestTime extends AppCompatActivity {
	private Context context;   //上下文参数
	private ChooseTestTimeAdapter adapter;   //选择测试时间的适配器
	private ListView list;   //显示测试时间的List
	private Intent intent;   //用来启动测试界面

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
		supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.choosetesttime );
		context = this;
		ChoiceNewIntent();
		loadPage();
	}

	/**
	 * 界面初始化方法
	 * */
	public void loadPage() {
		List<String> fileName = fileOperator.getFileName( context );
		list = findViewById( R.id.choosetesttime_list );

		adapter = new ChooseTestTimeAdapter( fileName, context );
		list.setAdapter( adapter );
		//项目点击的方法
		list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				TextView tv = view.findViewById( R.id.choosetesttime_time );
				String time = tv.getText().toString();
				intent.putExtra( "time", time );
				startActivity( intent );
			}
		} );
	}

	/**
	 * 根据选择不同的测试方式，来启动不同的页面
	 * */
	public void ChoiceNewIntent() {
		Intent get = getIntent();
		int testway = get.getIntExtra( "testway", 0 );
		switch (testway) {
			case 1:
				//根据英语来选择意思
				intent = new Intent( context, EnglishChooseChinese.class );
				break;
			case 2:
				//根据释义来拼写单词
				intent = new Intent( context, Spell.class );
				break;
			case 3:
				//根据发音来写单词
				intent = new Intent( context, ListenSpell.class );
				break;
		}
	}

	/**
	 * 返回方法
	 * */
	public void BackToChooseTestWay(View view)
	{
		finish();
	}

	/**
	 * 真机返回按钮
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown( keyCode, event );
	}
}
