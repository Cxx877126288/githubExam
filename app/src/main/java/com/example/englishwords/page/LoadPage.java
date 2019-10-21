package com.example.englishwords.page;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import com.example.englishwords.R;
import com.example.englishwords.db.EnglishSqlite;
import com.example.englishwords.myView.ProgressView;
import com.example.englishwords.myView.ProgressViewThread;
import com.example.englishwords.util.loadOperator;

import java.io.*;
import java.lang.reflect.Field;

/**
 * @author HX
 * @title: LoadPage
 * @projectName Words_System
 * @date 2019/7/1  9:31
 */
public class LoadPage extends AppCompatActivity {
	private Context context;
	private String[] names = new String[] {"四级专业词汇","六级专业词汇"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
		context = this;
		loadOperator.loadDataBase( context );
		if (!loadOperator.fileExist( context )) {
			//文件不存在就生成加载文件并设成false
			loadOperator.loadFile( context );
		}else{
			//如果为true则为加载过
			Intent intent = new Intent( context, SearchPage.class );
			context.startActivity( intent );  //能跳转
			finish();
		}
		if ("false".equals( loadOperator.readLoad( context ).trim() )) {
			//没有加载过
			setContentView( R.layout.loadpage );
			try {
				EnglishSqlite my = new EnglishSqlite( context );
				String sql = "insert into thesaurus values(?,?,?,?)";
				SQLiteDatabase wd = my.getWritableDatabase();
				SQLiteStatement ss = wd.compileStatement( sql );
				Field[] declaredFields = R.raw.class.getDeclaredFields();
				int index = 0;
				for(int i = 0 ; i < declaredFields.length;i++){
					int count = 0;
					String name = declaredFields[i].getName();
					if(name.equals( "$change" ) || name.equals( "serialVersionUID" )){
						continue;
					}
					else {
						int anInt = declaredFields[i].getInt( R.raw.class );
						InputStream inputStream = context.getResources().openRawResource( anInt );
						BufferedReader br = new BufferedReader( new InputStreamReader( inputStream ) );
						while (br.readLine() != null) {
							count++;
						}
						ss.bindNull( 1 );
						ss.bindString( 2,names[index] );
						ss.bindLong( 3,count );
						ss.bindLong( 4,anInt );
						ss.execute();
						index++;
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			//开始加载数据，并且将标志文件设为true
			ProgressView progress = findViewById( R.id.progressbar );
			ProgressViewThread pvt = new ProgressViewThread( progress, context, this );
			pvt.start();
		}
	}

}
