package com.example.englishwords.page;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import com.example.englishwords.R;
import com.example.englishwords.db.EnglishSqlite;
import com.example.englishwords.pojo.Thesaurus;
import com.example.englishwords.service.ThesaurusService;
import com.example.englishwords.service.impl.ThesaurusServiceImpl;
import com.example.englishwords.util.StringUtils;

/**
 * @author HX
 * @title: ChooseNumberOfEveryday
 * @projectName Words_System
 * @date 2019/9/3  11:14
 * 选择书本后，选择每日需背单词数量的页面
 */
public class ChooseNumberOfEveryday extends AppCompatActivity {
	private ThesaurusService ts = new ThesaurusServiceImpl();    //书本服务类
	private Context context;   //上下文参数
	private int id;   //选择书本的ID
	private Thesaurus thesaurus;   //选择书本的信息
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.choose_number_of_everyday );
		context = this;

		loadpage();
	}

	/**
	 * 初始化界面，显示书本名称与书本的单词数
	 * */
	private void loadpage() {
		Intent intent = getIntent();
		id = intent.getIntExtra( "id",-1 );
		thesaurus = ts.selByThesaurusID( context, id );
		TextView BookName = findViewById( R.id.bookname );
		TextView BookCount = findViewById( R.id.bookcount );

		BookName.setText( "所选书籍："+thesaurus.getThesaurus_Name() );
		BookCount.setText( "该书总词数:" + String.valueOf( thesaurus.getThesaurus_Count() ) );
	}

	/**
	 * 确定按钮点击后
	 * */
	public void RememberWordsNumber(View view){
		AlertDialog.Builder dialog = new AlertDialog.Builder( context );
		EditText et = findViewById( R.id.set_number );
		String s = et.getText().toString();
		dialog.setTitle( "提示" );
		if(s.equals( "" )){
			//判断是否输入内容
			dialog.setMessage( "请先输入每日要背的单词数目" );
			dialog.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
				}
			} );
		}else if(StringUtils.isNumeric( s )){
			//判断输入的内容是否为全数字
			final int per = Integer.parseInt( s );
			if(per >= thesaurus.getThesaurus_Count() ){
				//判断输入的每日要背单词数是否超过单词总数
				dialog.setMessage( "输入的单词数目已超过最大值,请重新输入");
				dialog.setNegativeButton( "返回", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {

					}
				} );
			}else{
				//提示信息，并点击确认后，向数据库写入信息，并跳到背单词页面
				dialog.setMessage( "背" + thesaurus.getThesaurus_Count() + "共需要约" +  (thesaurus.getThesaurus_Count() / per + 2) + "天");
				dialog.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						EnglishSqlite my = new EnglishSqlite( context);
						SQLiteDatabase wd = my.getWritableDatabase();
						//向选择表插入信息
						String sql = "insert into choice values(?,?,?)";
						SQLiteStatement ss = wd.compileStatement( sql );
						Thesaurus thesaurus = ts.selByThesaurusID( context, id );
						//添加参数存入背诵的单词本，书本编号，每日背的单词数目，项目里书的ID
						ss.bindLong( 1,id );
						ss.bindLong( 2,per );
						ss.bindLong( 3,thesaurus.getBookID() );
						ss.execute();
						my.close();
						//跳到背单词的界面
						Intent intent = new Intent( context,MainActivity.class );
						startActivity( intent );
						finish();
					}
				} );
				dialog.setNegativeButton( "返回", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {

					}
				} );
			}

		}else{
			//有输入内容，但是非数字
			dialog.setMessage( "请输入正确的数字（整数）！" );
			dialog.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
				}
			} );
		}
		dialog.show();
	}

	/**
	 * 返回按钮
	 * */
	public void BackChooseBook(View view){
		Intent intent = new Intent( context,ChooseWordsBook.class );
		startActivity( intent );
		finish();
	}

	/**
	 * 真机返回按钮
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent( context,ChooseWordsBook.class );
			startActivity( intent );
			finish();
		}
		return super.onKeyDown( keyCode, event );
	}
}
