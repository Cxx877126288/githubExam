package com.example.englishwords.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.example.englishwords.R;
import com.example.englishwords.pojo.Word;
import com.example.englishwords.service.WordService;
import com.example.englishwords.service.impl.WordServiceImpl;
import com.example.englishwords.util.ExpandUtil;
import com.example.englishwords.util.fileOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author HX
 * @title: Spell
 * @projectName Words_System
 * @date 2019/9/11  8:35
 * 单词拼写测试
 */
public class Spell extends AppCompatActivity {
	private Context context;
	private List<Integer> hasAppeared = new ArrayList<>();   //已经出现过的单词ID
	private List<Integer> wordsID;   //要测试的所有单词的ID
	private WordService ws = new WordServiceImpl();
	private List<Integer> views;   //显示的单词视图ID
	private Word word;
	private List<Integer> widgetID;//编辑框的ID
	private Boolean nextContextFlag = true;
	private Boolean finishFlag = false,toLower = false;
	private int reviewCount = 1;
	private final String FinishTiShi = "测试结束，小伙子给力嗷";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
		supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.spell );
		context = this;
		Intent intent = getIntent();
		String time = intent.getStringExtra( "time" );
		wordsID = fileOperator.getWordsIDByTime( context, time );
		loadpage();
	}

	public String Count(){
		return "测试进度：" + reviewCount + "/" + wordsID.size();
	}

	private void loadpage() {

		TextView t = findViewById( R.id.spellcount );
		t.setText( Count() );
		if (hasAppeared.size() < wordsID.size()) {
			int index = ExpandUtil.randomTimes( wordsID.size()  );
			while ((haveAppeared( index ))) {
				index = ExpandUtil.randomTimes( wordsID.size()  );
			}
			word = ws.selOne( wordsID.get( index ) );
			hasAppeared.add( index );
		} else {
			word = null;
		}
		if (word != null) {
			final LinearLayout ly = findViewById( R.id.spellword );
			views = new ArrayList<>();
			widgetID = new ArrayList<>();

			char[] chars = word.getWord().toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if (ExpandUtil.NumberExpand( 200000  ) == 0) {
					//为0就生成编辑框
					final EditText  et = new EditText( context );
					int id = View.generateViewId();
					widgetID.add( id );
					et.setSingleLine();
					et.setTextSize( 23f );
					et.setId( id );
					et.setImeOptions( EditorInfo.IME_ACTION_NEXT );

					et.setOnKeyListener( new View.OnKeyListener() {
						@Override
						public boolean onKey(View v, int keyCode, KeyEvent event) {
							if (keyCode == KeyEvent.KEYCODE_DEL) {
								//键盘上的删除按钮点击了
								if(et.getText().toString().length() == 0){
									for(int i = 0; i < widgetID.size();i++){
										//遍历编辑框ID
										if(et.getId() == widgetID.get( i ) && i > 0){
											//如果当前ID等于某个ID 且不为第一个
											//将光标移向前一项
												EditText e = ly.findViewById( widgetID.get( i - 1 ) );
												e.requestFocus();
												break;

										}
									}
								}
							}
							if(keyCode == KeyEvent.KEYCODE_ENTER){
								if(et.getText().toString().length() == 1){

									for(int i = 0 ; i < widgetID.size();i++){
										if(et.getId() == widgetID.get( i ) && i < widgetID.size() - 1){
												EditText e = ly.findViewById( widgetID.get( i + 1 ) );
												e.requestFocus();
												return true;
										}
									}

								}
							}
							return false;
						}
					} );

					//编辑框文本改变监听器
					et.addTextChangedListener( new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {
							if(s.length() == 0){
								toLower = true;
							}

						}

						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							if(s.length() == 0){
								//判断字符长度，用来判断是否需要回退一格还是前进一格
								nextContextFlag = false;
							}else {
								nextContextFlag = true;
							}
							if (s.length() > 1) {
								et.setText( String.valueOf( (s.toString().toCharArray())[0] ) );
								et.setSelection( 1 );
							}
						}

						@Override
						public void afterTextChanged(Editable s) {
							if(nextContextFlag){
								int i;
								for(i = 0 ; i < widgetID.size();i++){
									if(et.getId() == widgetID.get( i )){
										if(toLower){
											toLower = false;
											et.setText( s.toString().toLowerCase() );
											et.setSelection( 1 );
										}
										if(i < widgetID.size() - 1){
											EditText e = ly.findViewById( widgetID.get( i + 1 ) );
											e.requestFocus();
											break;
										}

									}
								}
								if(i == widgetID.size()){
									InputMethodManager inputMethodManager =(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
									inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(), 0);
								}
							}
						}
					} );
					ly.addView( et );
					views.add( 0 );
				} else {
					TextView tv = new TextView( context );
					tv.setTextSize( 30f );
					tv.setText( String.valueOf( chars[i] ) );
					ly.addView( tv );
					views.add( 1 );
				}
			}
			TextView tv = findViewById( R.id.spell_mean );
			tv.setText( word.getMean_cn() );
		} else {
			LinearLayout ly = findViewById( R.id.spellword );
			for(int i = 0;i < ly.getChildCount();i++){
				ly.getChildAt( i ).setVisibility( View.INVISIBLE );
			}
			TextView tv = findViewById( R.id.spell_mean );
			tv.setText( FinishTiShi );
			Button btn = findViewById( R.id.spell_sure );
			btn.setText( "退出测试" );
			finishFlag = true;
		}
	}

	public Boolean haveAppeared(int index) {
		Boolean ret = false;
		for (int i = 0; i < hasAppeared.size(); i++) {
			if (index == hasAppeared.get( i )) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return super.onKeyDown( keyCode, event );
	}

	public void SureNext(View view) {
		if(finishFlag){
			finish();
		}else {
			LinearLayout ly = findViewById( R.id.spellword );
			String s = "";
			for (int i = 0; i < ly.getChildCount(); i++) {
				if (views.get( i ) == 0) {
					EditText et = (EditText) ly.getChildAt( i );
					s += et.getText().toString();
				} else {
					TextView tv = (TextView) ly.getChildAt( i );
					s += tv.getText().toString();
				}
			}
			if (s.toLowerCase().equals( word.getWord().toLowerCase() )) {
				reviewCount++;
				ly.removeAllViews();
				widgetID = new ArrayList<>(  );
				views = new ArrayList<>(  );
				nextContextFlag = true;
				toLower = false;
				loadpage();
			} else {
				Toast.makeText( context, "拼写错误，仔细想想哦", Toast.LENGTH_LONG ).show();
			}
		}
	}

	public void spellTestFinishi(View view){
		finish();
	}
}
