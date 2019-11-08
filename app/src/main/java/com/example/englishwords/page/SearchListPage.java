package com.example.englishwords.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.example.englishwords.R;
import com.example.englishwords.adapter.SearchListAdapter;
import com.example.englishwords.pojo.Word;
import com.example.englishwords.service.WordService;
import com.example.englishwords.service.impl.WordServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HX
 * @title: SearchListPage
 * @projectName Words_System
 * @date 2019/9/10  8:42
 * 搜索完成的列表显示页面
 */
public class SearchListPage extends AppCompatActivity {
	private Context context;
	private SearchListAdapter adapter;   //搜索结果适配器
	private WordService ws = new WordServiceImpl();    //单词服务，搜索单词的
	private ListView lv;       //显示结果列表
	private Boolean flag = false;
	private Boolean ClickFlag = true;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.searchlist );
		context = this;
		loadpage();
	}

	/**
	 * 初始化界面
	 * */
	private void loadpage() {
		Intent intent = getIntent();
//		获得查询的关键字
		String search = intent.getStringExtra( "search" );
		EditText et = findViewById( R.id.searchbar );
		et.setText( search );
		et.setSelection( search.length() );

		//监听小键盘的搜索按键是否按下
		et.setOnEditorActionListener( new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
				flag = true;
				SearchAgain(textView);
				return false;
			}
		} );

		//监听文本变化
		et.addTextChangedListener( new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence text, int start, int before, int count) {
			}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				EditText et = findViewById( R.id.searchbar );
				if(!et.getText().toString().equals( "" )){
//					不为空串就显示小键盘
					flag = false;
					SearchAgain( et );
				}else {
					//如果所搜的是空串就什么都不显示
					List<Word> s = new ArrayList<>(  );
					adapter = new SearchListAdapter( s,context );
					lv.setAdapter( adapter );
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		} );

		List<Word> words = ws.selByWord( search);
		lv = findViewById( R.id.search_list );
		adapter = new SearchListAdapter( words,context );
		lv.setAdapter( adapter );
		lvSetClick( words );
	}

	public void SearchListBack(View view){
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown( keyCode, event );
	}

	/**
	 * 列表适配器点击后显示详细信息
	 * */
	public void lvSetClick(final List<Word> words){
		lv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Intent intent = new Intent( context,SearchDetailPage.class );
				intent.putExtra( "id",words.get( i ).getTopic_id() );
				startActivity( intent );
			}
		} );
	}

	public void SearchAgain(View view){
		EditText et = findViewById( R.id.searchbar );
		if(flag){
			//是否隐藏键盘
			InputMethodManager inputMethodManager =(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(), 0);
		}
		String s = et.getText().toString();
//通过关键字来查询单词
		List<Word> words = ws.selByWord( s);
		lv = findViewById( R.id.search_list );
		adapter = new SearchListAdapter( words,context );
		lv.setAdapter( adapter );
		lvSetClick( words );
	}
}
