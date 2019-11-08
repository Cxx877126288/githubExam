package com.example.englishwords.page;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.englishwords.R;
import com.example.englishwords.adapter.StrangeBookAdapter;
import com.example.englishwords.pojo.Word;
import com.example.englishwords.service.WordService;
import com.example.englishwords.service.impl.WordServiceImpl;
import com.example.englishwords.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author HX
 * @title: Strange
 * @projectName Words_System
 * @date 2019/9/6  9:22
 * 生词本
 */
public class Strange extends AppCompatActivity {
	private Context context;
	private WordService ws = new WordServiceImpl();
	private StrangeBookAdapter adapter;
	private TextToSpeech speak;
	private List<Word> words;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.strange );
		context = this;
		loadPage();
		loadSpeak();
	}

	public void loadPage(){
		List<Integer> wordsid = DatabaseUtil.getStrangeBookList( context );
		words = new ArrayList<>(  );
		for(int i = 0 ; i < wordsid.size();i++){
			words.add(ws.selOne( wordsid.get( i ) ));
		}
		adapter = new StrangeBookAdapter( words,context );
		ListView list = findViewById( R.id.stranglist );
		list.setAdapter( adapter );

		list.setOnCreateContextMenuListener( new View.OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
				contextMenu.add( 1,1,1,"删除" ) ;
			}
		} );

		list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Word word = words.get( i );
				if( speak != null && !speak.isSpeaking()){
					speak.setPitch(0.9f); // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
					speak.speak( word.getWord() ,TextToSpeech.QUEUE_FLUSH, null,null);
				}
			}
		} );
	}

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

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ContextMenu.ContextMenuInfo menuInfo = item.getMenuInfo();
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		Word word = (Word)adapter.getItem( info.position );
		switch (item.getItemId()){
			case 1:
				showDelete( word.getTopic_id() );
				break;
		}
		return super.onContextItemSelected( item );
	}

	private void showDelete(final int wordsID){
		AlertDialog.Builder dialog = new AlertDialog.Builder( context );
		Word word = ws.selOne( wordsID );
		dialog.setTitle( "提示" );
		dialog.setMessage( "要将"+ word.getWord() + "移出生词表吗？" );
		dialog.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				if(DatabaseUtil.delFromStrange( wordsID,context ) > 0){
					Toast.makeText( context ,"删除成功",Toast.LENGTH_LONG).show();
					loadPage();
				}
			}
		} );

		dialog.setNegativeButton( "取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {

			}
		} );
		dialog.show();
	}

	public void BackMain(View view){
		speak.stop();
		speak.shutdown();
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			speak.stop();
			speak.shutdown();
			finish();
		}
					//System.exit( 0 );
		return super.onKeyDown( keyCode, event );
	}
}
