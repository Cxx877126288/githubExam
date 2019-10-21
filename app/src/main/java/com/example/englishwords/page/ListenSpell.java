package com.example.englishwords.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.example.englishwords.R;
import com.example.englishwords.pojo.Word;
import com.example.englishwords.service.WordService;
import com.example.englishwords.service.impl.WordServiceImpl;
import com.example.englishwords.util.ExpandUtil;
import com.example.englishwords.util.fileOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * @author HX
 * @title: ListenSpel
 * @projectName Words_System
 * @date 2019/9/11  8:35
 * 听力拼写
 */
public class ListenSpell extends AppCompatActivity {
	private Context context;  //上下文参数
	private List<Word> words;  //存储要测试的单词列表
	private int btnText = 1; //用来控制显示按钮的内容  1：确定  2：下一个  3：退出测试
	private Word word;    //要显示的单词
	private TextToSpeech speak;  //语音控件
	private List<Integer> hasAppeardID;     //已经出现过的ID
	private int count = 1,errorCount = 0;   //count ：显示测试进度；errorCount：用来计算错误次数，来显示不同的提示信息
	private final String FinishTiShi = "测试结束，小伙子给力嗷";   //测试结束的提示语
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
		supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.listenspell );
		context = this;
		loadParameter();
		loadpage();
	}

	/**
	 * 初始化一些参数
	 * */
	private void loadParameter() {
		WordService ws = new WordServiceImpl();
		words = new ArrayList<>(  );

		hasAppeardID = new ArrayList<>(  );
		//获取选择时间的所有单词
		Intent intent = getIntent();
		String time = intent.getStringExtra( "time" );
		List<Integer> ids = fileOperator.getWordsIDByTime( context, time );
		for (int i = 0; i < ids.size(); i++) {
			Word word2 = ws.selOne( ids.get( i ) );
			words.add( word2 );
		}

		//初始化语音控件
		speak = new TextToSpeech( this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int i) {
				if (i == TextToSpeech.SUCCESS) {
					int supported = speak.setLanguage( Locale.US );
					if ((supported != TextToSpeech.LANG_AVAILABLE) && (supported != TextToSpeech.LANG_COUNTRY_AVAILABLE)) {
						Log.e( "Error", "不支持该语言" );
						//Toast.makeText( context,"初始化失败",Toast.LENGTH_LONG ).show();
					}
				}
			}
		} );
	}

	/**
	 * 初始化页面加载
	 * */
	private void loadpage() {

		if(hasAppeardID.size() < words.size()){
			int index = ExpandUtil.randomTimes( words.size() );
			while (haveAppeared( words.get( index ).getTopic_id() ) ){
				index = ExpandUtil.randomTimes( words.size() );
			}
			//获取一个单词 如果没有出现过  就显示，并添加到出现列表
			word = words.get( index );
			hasAppeardID.add( word.getTopic_id() );

			TextView lsword_mean = findViewById( R.id.lsword_mean );
			lsword_mean.setText( word.getMean_cn() );
			TextView ls_count = findViewById( R.id.ls_count );
			ls_count.setText("测试进度：" +  count + "/" + words.size() );
			if(btnText == 2){
				btnText = 1;
				Button lsbtn = findViewById( R.id.lsbtn );
				lsbtn.setText( "确定" );
				EditText et = findViewById( R.id.ls_inword );
				et.setText( "" );
				LinearLayout ly = findViewById( R.id.ls_second );
				ly.setVisibility( View.INVISIBLE );

				ly = findViewById( R.id.ls_first );
				ly.setVisibility( View.VISIBLE );

			}
		}else {
			word = null;
			TextView tv = findViewById( R.id.lsfinish );
			tv.setText( FinishTiShi );
			tv.setVisibility( View.VISIBLE );
			btnText = 3;
			LinearLayout ly = findViewById( R.id.ls_second );
			ly.setVisibility( View.INVISIBLE );

			ly = findViewById( R.id.ls_first );
			ly.setVisibility( View.INVISIBLE );
			ImageButton ls_mic = findViewById( R.id.ls_mic );
			ls_mic.setVisibility( View.INVISIBLE );
			Button lsbtn = findViewById( R.id.lsbtn );
			lsbtn.setText( "退出测试" );
		}
	}

	/**
	 * 判断某个单词是否已经出现过
	 * @param wordid 要出现的单词ID
	 * @return 出现过：true；未出现：false；
	 * */
	private Boolean haveAppeared(int wordid){
		Boolean ret = false;
		for (int i = 0 ;i < hasAppeardID.size();i++){
			if(hasAppeardID.get( i ) == wordid){
				ret = true;
				break;
			}
		}
		return ret;
	}

	public void lsSpeakClick(View view){
		if( speak != null && !speak.isSpeaking()){
			speak.setPitch(0.9f); // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
			speak.speak( word.getWord() ,TextToSpeech.QUEUE_FLUSH, null,null);
		}
	}

	/**
	 * 整合退出方法
	 * */
	public void Quit(){
		speak.shutdown();
		speak.stop();
		finish();
	}

	/**
	 * 按钮确认点击的时候
	 * */
	public void lsSureClick(View view){
		if(btnText == 1){
			EditText et = findViewById( R.id.ls_inword );

			if(word.getWord().toLowerCase().equals( et.getText().toString().toLowerCase() )){
				//输入文字统一按小写处理  做到不区分大小写
				btnText = 2;
				LinearLayout ly = findViewById( R.id.ls_first );
				ly.setVisibility( View.INVISIBLE );

				TextView ls_word = findViewById( R.id.ls_word );
				ls_word.setText( word.getWord() );

				TextView ls_word_accent = findViewById( R.id.ls_word_accent );
				ls_word_accent.setText( word.getAccent() );

				TextView ls_word_cn = findViewById( R.id.ls_word_cn );
				ls_word_cn.setText( word.getMean_cn() );

				Button lsbtn = findViewById( R.id.lsbtn );
				lsbtn.setText( "下一个" );

				ly = findViewById( R.id.ls_second );
				ly.setVisibility( View.VISIBLE );

			}else{
				String message = "";
				switch (errorCount){
					case 0:
						message = "单词的长度为" + word.getWord().length();
						break;
					case 1:
						message = "单词的第一个字母为" + word.getWord().toCharArray()[0];
						break;
					default:
						message = "单词的前两个个字母为" + word.getWord().toCharArray()[0] + word.getWord().toCharArray()[1] ;
						break;
				}
				errorCount++;
				Toast.makeText( context,message,Toast.LENGTH_LONG ).show();
			}

		}else if(btnText == 2){
			count++;
			loadpage();
		}else {
			Quit();
		}
	}

	/**
	 * 退出按钮事件
	 * */
	public void lsFinishTest(View view){
		Quit();
	}

	/**
	 * 真机返回按钮
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Quit();
			return true;
		}
		return super.onKeyDown( keyCode, event );
	}
}
