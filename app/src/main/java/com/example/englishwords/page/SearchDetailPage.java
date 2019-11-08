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
import com.example.englishwords.R;
import com.example.englishwords.pojo.Word;
import com.example.englishwords.service.WordService;
import com.example.englishwords.service.impl.WordServiceImpl;

import java.util.Locale;

/**
 * @author HX
 * @title: SearchDetailPage
 * @projectName Words_System
 * @date 2019/9/10  14:19
 * 查询页面点击项目后 显示详细信息页面
 */
public class SearchDetailPage extends AppCompatActivity {
	private Context context;
	private WordService ws = new WordServiceImpl();
	private TextToSpeech speak;
	private Word word;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.searchdetail );
		context = this;
		loadpage();
		loadSpeak();
	}

	/**
	 * 初始化页面
	 * */
	private void loadpage() {
		Intent intent = getIntent();
		//获取显示详细的单词ID
		int id = intent.getIntExtra( "id", 0 );
		word = ws.selOne( id );
		TextView tx = findViewById( R.id.search_word_infor );
		String show = "单词拼写：" + word.getWord() +
				"\n\n\n单词发音：" + word.getAccent() +
				"\n\n\n单词释义：\n\b\b" + word.getMean_cn();
		tx.setText( show );
	}

	/**
	 * 初始化语音控件
	 * */
	public void loadSpeak(){
		speak = new TextToSpeech( this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int i) {
				if(i == TextToSpeech.SUCCESS){
					int supported = speak.setLanguage(Locale.US);
					if((supported != TextToSpeech.LANG_AVAILABLE)&&(supported != TextToSpeech.LANG_COUNTRY_AVAILABLE))
					{
						Log.e( "Error","不支持该语言" );
						//Toast.makeText( context,"初始化失败",Toast.LENGTH_LONG ).show();
					}
				}
			}
		} );
	}

	/**
	 * 发音
	 * */
	public void SearchSpeakClick(View view){
		if( speak != null && !speak.isSpeaking()){
			speak.setPitch(0.9f); // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
			speak.speak( word.getWord() ,TextToSpeech.QUEUE_FLUSH, null,null);
		}
	}

	/**
	 * 整合退出方法
	 * */
	private void Quit(){
		speak.shutdown();
		speak.stop();
		finish();
	}

	/**
	 * 退出
	 * */
	public void SearchDetailBack(View view){
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
