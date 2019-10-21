package com.example.englishwords.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.example.englishwords.R;

/**
 * @author HX
 * @title: TestPage
 * @projectName Words_System
 * @date 2019/9/10  15:07
 */
public class TestPage extends AppCompatActivity {
	private Context context;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.testpage );
		context = this;
	}

	public void EnglishChooseChineseClick(View view){
		Intent intent = new Intent( context,ChooseTestTime.class );
		intent.putExtra( "testway",1 );
		startActivity( intent );
	}

	public void SpellClick(View view){
		Intent intent = new Intent( context,ChooseTestTime.class );
		intent.putExtra( "testway",2 );
		startActivity( intent );
	}

	public void ListenSpellClick(View view){
		Intent intent = new Intent( context,ChooseTestTime.class );
		intent.putExtra( "testway",3 );
		startActivity( intent );
	}

	public void TestPageBack(View view){
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

}
