package com.example.englishwords.page;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.example.englishwords.R;
import com.example.englishwords.db.EnglishSqlite;
import com.example.englishwords.pojo.Choice;
import com.example.englishwords.pojo.Word;
import com.example.englishwords.service.ChoiceService;
import com.example.englishwords.service.ThesaurusService;
import com.example.englishwords.service.WordService;
import com.example.englishwords.service.impl.ChoiceServiveImpl;
import com.example.englishwords.service.impl.ThesaurusServiceImpl;
import com.example.englishwords.service.impl.WordServiceImpl;
import com.example.englishwords.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class MainActivity extends AppCompatActivity {
	private ChoiceService cs = new ChoiceServiveImpl();
	private WordService ws = new WordServiceImpl();
	private TextToSpeech speak;
	private Context context;    //上下文参数
	private Word word;   //要显示的单词
	private SQLiteDatabase wd;   //对数据库进行写操作
	private SQLiteDatabase rd;   //对数据库进行读操作
	private  int count = 1;     //显示进度
	private Boolean finishFlag = false;    //是否结束学习
	private List<Integer> wordsids = new ArrayList<>(  );   //用来存取要背诵单词的列表
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.activity_main );
		context = this;
		EnglishSqlite my = new EnglishSqlite( context );
		wd = my.getWritableDatabase();
		rd = my.getReadableDatabase();
		loadWordsids();
		loadSpeak();
		LoadPage();

	}

	private void LoadPage() {
		if("true".equals( loadOperator.readLoad( context ).trim() )){
			//非第一次加载
			if(loadOperator.ProduceNewWordsFlag( context )){
				//已经过了一天
				//清空daywords 和 laststudy
				DatabaseUtil.clearDayWords( context );
				//再生成
				produceNewWrodsList();
			//忘记列表的flag全部减1；showtimes置为2；
				DatabaseUtil.updUnKnowListFlagAndShowtimes( context );
				word = DatabaseUtil.randomAWord(context);
				loadOperator.WirteUseTime( context );
			}else{  //未经过一天
				if(getLastCount() == 0){
					//没有有退出过；
					word = DatabaseUtil.randomAWord(context);
				}else{
					Cursor last = wd.query( "laststudy", null, null, null, null, null, null );
					last.moveToNext();
					int id = Integer.parseInt ( last.getString( last.getColumnIndex( "wordsid" ) ) );
					word = ws.selOne( id );
				}
			}
		}else {
			//第一次加载
			produceNewWrodsList();
			Cursor daywords = rd.query( "daywords", null, null, null, null, null, null );
			for(int i = 0;i < count;i++){
				daywords.moveToNext();
			}
			word = ws.selOne( Integer.parseInt( daywords.getString( daywords.getColumnIndex( "wordsid" ) ) ) );
			loadOperator.WirteUseTime( context );
			updateLastWord();
			loadOperator.update( context );   //将加载标志 改为true
		}
		if(word == null){
			FinishStudy();
		}else{
			showWordInfor();
		}
	}

	public void showWordInfor(){
		TextView tx = findViewById( R.id.show_word_infor );
		String show = "单词拼写：" + word.getWord() +
				"\n\n\n单词发音：" + word.getAccent() +
				"\n\n\n单词释义：\n\b\b" + word.getMean_cn();
		tx.setText( show );
	}

	public void FinishStudy(){
		DatabaseUtil.clearLastStudy( context );
		ImageButton ib = findViewById( R.id.speak );
		ib.setVisibility( View.INVISIBLE );
		TextView tx = findViewById( R.id.show_word_infor );
		String show = "\n\n\n小伙子，太棒了！\n今日的学习已完成";
		tx.setText( show );
		Button realize = findViewById( R.id.realize );
		Button join = findViewById( R.id.join );
		Button forget = findViewById( R.id.forget );

		realize.setVisibility( View.INVISIBLE );
		join.setVisibility( View.INVISIBLE  );
		forget.setVisibility( View.INVISIBLE  );

		Button follow_test = findViewById( R.id.follow_test );
		follow_test.setVisibility( View.VISIBLE );

		Button back = findViewById( R.id.back );
		back.setVisibility( View.VISIBLE );

		finishFlag = true;
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

	public void speakClick(View view){
		if( speak != null && !speak.isSpeaking()){
			speak.setPitch(0.9f); // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
			speak.speak( word.getWord() ,TextToSpeech.QUEUE_FLUSH, null,null);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
				quit();
			return true;
		}
		return super.onKeyDown( keyCode, event );
	}

	public void updateLastWord(){
		ContentValues cv = new ContentValues(  );
		cv.put( "wordsid",word.getTopic_id() );
		if(getLastCount() == 0){
			wd.insert( "laststudy",null,cv );
		}else{
			wd.update( "laststudy",cv,null,null );
		}
	}

	public int getLastCount(){
		Cursor last = wd.query( "laststudy", null, null, null, null, null, null );
		return last.getCount();
	}

	public void knowBtnClick(View view){
		//按下认识的按钮;
		//加入认识的列表。 随机生成从  忘记或者每日单词  获取一个单词
		DatabaseUtil.insKnowList( word.getTopic_id(),context );
		fileOperator.writeReview( context,word.getTopic_id() );
		DatabaseUtil.delWordsFromDaywordsOrUnknowList( word.getTopic_id(),context );
		Word nextword = DatabaseUtil.randomAWord( context );
		int count = 0;
		while (count <= 3){
			if(nextword != null && nextword.getTopic_id() == word.getTopic_id()){
				nextword = DatabaseUtil.randomAWord( context );
			}else {
				break;
			}
			count++;
		}
		this.word = nextword;
		if(this.word != null){ //有值就显示 并更新最后访问
			showWordInfor();
			updateLastWord();
		}else {
			//没有就提示今天已经背完单词
			DatabaseUtil.delUnKnowListFlagIsZero( context );
			FinishStudy();
		}
	}

	public void unKnowBtnClick(View view){
		//加入忘记表
		DatabaseUtil.insUnKnowList( word.getTopic_id(),context );
		fileOperator.writeReview( context,word.getTopic_id() );
		Word nextword = DatabaseUtil.randomAWord( context );
		int count = 0;
		while (count <= 3){
			if(nextword != null && nextword.getTopic_id() == word.getTopic_id()){
				nextword = DatabaseUtil.randomAWord( context );
			}else {
				break;
			}
			count++;
		}
		this.word = nextword;
		if(word != null){ //有值就显示 并更新最后访问
			showWordInfor();
			updateLastWord();
		}else {
			//没有就提示今天已经背完单词
			DatabaseUtil.delUnKnowListFlagIsZero( context );
			FinishStudy();
		}
	}

	public void quit(){
		if(!finishFlag){
			AlertDialog.Builder dialog = new AlertDialog.Builder( context );
			dialog.setTitle( "提示" );
			dialog.setMessage( "小伙子，坚持背下去，别退出" );
			dialog.setPositiveButton( "取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {

				}
			} );
			dialog.setNegativeButton( "退出", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					speak.stop();
					speak.shutdown();
					if(word != null)
						updateLastWord();
					finish();
				}
			} );
			dialog.show();
		}else {
			speak.stop();
			speak.shutdown();
			if(word != null)
				updateLastWord();
			finish();
		}
	}

	public void backClick(View view){
		speak.stop();
		speak.shutdown();
		finish();
	}

	public void loadWordsids(){
		Choice choice = cs.selOne( context );
		//获取书本的单词总数

		//随机生成一个单词
		InputStream is = context.getResources().openRawResource( choice.getBookID() );
		BufferedReader br = new BufferedReader( new InputStreamReader( is ) );

		String str = "";

		try {
			while ((str = br.readLine()) != null ){
				wordsids.add( Integer.parseInt( str.trim() ) );
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public int getWordsByRowNumber(int WordsCount){
		return wordsids.get( ExpandUtil.randomTimes(WordsCount) );
	}

	public void produceNewWrodsList(){
		Random random = new Random(  );
		int wordsCount = DatabaseUtil.getWordsCount( context );   //单词总数
		int knowListCount = DatabaseUtil.getKnowListCount( context );  //认识总数
		int unKnowListCount = DatabaseUtil.getUnKnowListCount( context );  //忘记总数
		int perWords = DatabaseUtil.getPerWords( context );  //设置的每日背单词总数

		if(wordsCount - knowListCount + unKnowListCount > perWords){
			//总单词数 - 已背单词数 + 忘记单词数 如果大于每日所背单词数，就正常操作
			int produceCount = perWords - unKnowListCount;
			if(produceCount <= 0){
				//忘记总数 大于等于 每日背单词总数；就随机一个新的单词数；在10 - 20个之间
				produceCount = 10 + random.nextInt(11);
			}
			List<Integer> knowList = DatabaseUtil.getKnowList( context );
			List<Integer> unKnowList = DatabaseUtil.getUnKnowList( context );
			//先添加新单词  到每日要背的单词的列表
			for(int i = 0;i < produceCount;i++){
				int wordsId = getWordsByRowNumber(wordsCount);
//		无限循环需要重新优化
				while (ExpandUtil.hasWordsId( wordsId,knowList ) || ExpandUtil.hasWordsId( wordsId,unKnowList ) || DatabaseUtil.DayWordsHasWordsID( wordsId,context )){
					//已背 跟 忘记的列表里  都存在
					wordsId = getWordsByRowNumber(wordsCount);
				}
				ContentValues cv = new ContentValues(  );
				cv.put( "wordsid",wordsId );
				wd.insert( "daywords",null,cv );//新的单词
			}
//再提取 unKnowList  并添加到 daywords

			List<Integer> indexs = new ArrayList<>(  );
			for(int i = 0;i < perWords - produceCount;i++){
				int index = ExpandUtil.randomTimes( unKnowList.size() );
				while (ExpandUtil.hasIndex( index,indexs )){
					index = ExpandUtil.randomTimes( unKnowList.size() );
				}
				ContentValues cv = new ContentValues(  );
				cv.put( "wordsid",unKnowList.get( index ) );
				wd.insert( "daywords",null,cv );//忘记的单词
			}
		}else{  //(暂未测试)
			//将剩余所有的单词加入每日单词本中
			for(int i = 0;i < wordsCount - knowListCount - unKnowListCount;i++){
				int wordsId = getWordsByRowNumber(wordsCount);

				while (DatabaseUtil.KnowListHasWordsID( wordsId,context ) || DatabaseUtil.UnKnowListHasWordsID( wordsId,context ) || DatabaseUtil.DayWordsHasWordsID( wordsId,context )){
					//已背 跟 忘记的列表里  都存在
					wordsId = getWordsByRowNumber(wordsCount);
				}
				ContentValues cv = new ContentValues(  );
				cv.put( "wordsid",wordsId );
				wd.insert( "daywords",null,cv );//新的单词
			}
			List<Integer> unKnowList = DatabaseUtil.getUnKnowList( context );
			for(int i = 0;i < unKnowList.size();i++){
				int wordsId = unKnowList.get( i );
				ContentValues cv = new ContentValues(  );
				cv.put( "wordsid",wordsId );
				wd.insert( "daywords",null,cv );//新的单词
			}
		}
	}

	public void JoinBtnClick(View view){
		if(DatabaseUtil.JoinStrange( word.getTopic_id(),context ) > 0){
			Toast.makeText( context,"加入生词本成功",Toast.LENGTH_LONG ).show();
		}else {
			Toast.makeText( context,"该单词已加入生词本",Toast.LENGTH_LONG ).show();
		}
	}

	public void test(View view){
		String s = StringUtils.DateToString( new Date( System.currentTimeMillis() ) );
		Intent intent = new Intent( context,EnglishChooseChinese.class );
		intent.putExtra( "time",s );
		startActivity( intent );
	}

	public void resetClick(View view){
		AlertDialog.Builder dialog = new AlertDialog.Builder( context );
		dialog.setTitle( "提示" );
		dialog.setMessage( "重置将删除，生词本以外的内容！" );
		dialog.setPositiveButton( "取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {

			}
		} );
		dialog.setNegativeButton( "确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				//将load.txt设置为false
				loadOperator.ResetLoadFile( context );
				//将除了生词表，书本表以外的表清空
				DatabaseUtil.ResetTable( context );
				//file文件下的文件都删除
				fileOperator.ResetAllTime( context );
				speak.shutdown();
				speak.stop();
				finish();
			}
		} );
		dialog.show();
	}
}


//		speak = new TextToSpeech( this, new TextToSpeech.OnInitListener() {
//@Override
//public void onInit(int i) {
//		if(i == TextToSpeech.SUCCESS){
//		int supported = speak.setLanguage(Locale.US);
//		if((supported != TextToSpeech.LANG_AVAILABLE)&&(supported != TextToSpeech.LANG_COUNTRY_AVAILABLE))
//		{
//		Log.e( "Error","不支持该语言" );
//		Toast.makeText( context,"初始化失败",Toast.LENGTH_LONG ).show();
//		}
//		}
//		}
//		} );
//
