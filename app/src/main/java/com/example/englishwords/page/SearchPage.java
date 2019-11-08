package com.example.englishwords.page;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import com.example.englishwords.R;
import com.example.englishwords.util.loadOperator;

/**
 * @author HX
 * @title: SearchPage
 * @projectName Words_System
 * @date 2019/9/10  8:39
 * 加载后的主界面
 */
public class SearchPage extends AppCompatActivity {
	private Context context;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.searchpage );
		context = this;

	}

	public void quit(){
		AlertDialog.Builder dialog = new AlertDialog.Builder( context );
		dialog.setTitle( "提示" );
		dialog.setMessage( "确定退出吗？" );
		dialog.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
//				System.exit( 0 );
				finish();
			}
		} );
		dialog.setNegativeButton( "取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {

			}
		} );

		dialog.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			quit();
			return true;
		}
		return super.onKeyDown( keyCode, event );
	}

//	搜索按钮
	public void searchClick(View view){
		EditText et = findViewById( R.id.search_text );
		String s = et.getText().toString();
		Intent intent = new Intent( context,SearchListPage.class );
		intent.putExtra( "search",s );
		startActivity( intent );
	}

	//背单词按钮
	public void startRemberWords(View view){
		Intent intent = null;
		if ("true".equals( loadOperator.readLoad( context ).trim() )) {
			 intent = new Intent( context, MainActivity.class );
		}else {
			intent = new Intent( context,ChooseWordsBook.class );
		}
		startActivity( intent );
	}

	// 复习按钮
	public void startReviewWords(View view){
		Intent intent = new Intent( context,ChooseReviewTime.class );
		startActivity( intent );
	}

	//生词本
	public void startStrange(View view){
		Intent intent = new Intent( context,Strange.class );
		startActivity( intent );
	}

	//测试
	public void startTest(View view){
		Intent intent = new Intent( context,TestPage.class );
		startActivity( intent );
	}

	//退出
	public void Quit(View view){
		quit();
	}
}
