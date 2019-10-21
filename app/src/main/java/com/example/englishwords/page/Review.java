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
import android.widget.TextView;
import android.widget.Toast;
import com.example.englishwords.R;
import com.example.englishwords.pojo.Word;
import com.example.englishwords.service.WordService;
import com.example.englishwords.service.impl.WordServiceImpl;
import com.example.englishwords.util.fileOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author HX
 * @title: Review
 * @projectName Words_System
 * @date 2019/9/6  16:42
 */
public class Review extends AppCompatActivity {
	private int index = 0;
	private TextToSpeech speak;
	private Context context = null;    //上下文参数
	private List<Word> words = new ArrayList<>(  );   //复习的单词列表

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.review);
		context = this;
		loadPage();
		loadSpeak();
	}

/**
 * 页面初始化方法
 * */
	public void loadPage() {
		WordService ws = new WordServiceImpl();
		Intent intent = getIntent();
		//获取要测试的时间
		String time = intent.getStringExtra( "time" );
		List<Integer> reviewWordsID = fileOperator.getWordsIDByTime( context, time );
		for (int i = 0; i < reviewWordsID.size(); i++) {
			Word word = (Word) ws.selOne( reviewWordsID.get( i ) );
			words.add( word );
		}
		Show();
	}

/**
 * 初始化语音控件
 * */
	public void loadSpeak() {
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
	 * 播放发音
	 * */
	public void ReviewSpeakClick(View view) {
		if( speak != null && !speak.isSpeaking()){
			speak.setPitch(0.9f); // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
			Word word = (Word)words.get( index );
			speak.speak( word.getWord() ,TextToSpeech.QUEUE_FLUSH, null,null);
		}
	}

	public void BeforeWord(View view) {
		if(index > 0){
			index--;
			Show();
		}else {
			Toast.makeText( context,"已经是第一个",Toast.LENGTH_LONG ).show();
		}
	}

	public void ReviewBackToMain(View view) {
		Quit();
	}

	public void AfterWord(View view) {
		if(index < words.size() -1 ){
			index++;
			Show();
		}else {
			Toast.makeText( context,"已经是最后一个",Toast.LENGTH_LONG ).show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Quit();
			return true;
		}
		return super.onKeyDown( keyCode, event );
	}

	private void Quit(){
		speak.stop();
		speak.shutdown();
		finish();
	}

	public void Show() {
		TextView tv = findViewById( R.id.review_word_infor );
		String show = "单词拼写：" + words.get( index ).getWord() + "\n\n\n单词发音：" + words.get( index ).getAccent() + "\n\n\n单词释义：\n\b\b" + words.get( index ).getMean_cn();
		tv.setText( show );
	}
}
