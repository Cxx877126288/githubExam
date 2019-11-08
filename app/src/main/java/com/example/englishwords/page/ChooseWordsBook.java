package com.example.englishwords.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.example.englishwords.R;
import com.example.englishwords.pojo.Thesaurus;
import com.example.englishwords.service.ThesaurusService;
import com.example.englishwords.service.impl.ThesaurusServiceImpl;

import java.util.List;

/**
 * @author HX
 * @title: ChooseWordsBook
 * @projectName Words_System
 * @date 2019/9/2  14:22
 * 选择背诵书本的下拉框
 */
public class ChooseWordsBook extends AppCompatActivity {
	private Context context;
	private Spinner spinner;
	private ThesaurusService ts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.choosewordsbook );
		context = this;

		loadpage();
	}

	/**
	 * 初始化界面
	 * */
	private void loadpage() {
		ts = new ThesaurusServiceImpl();
		List<String> names = ts.selAllBookName( context );
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);  //创建一个数组适配器
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);     //设置下拉列表框的下拉选项样式
		spinner = findViewById(R.id.set_book);
		spinner.setAdapter(adapter);
	}

/**
 * 选择书本的按钮确定之后
 * */
	public void ChooseBookBtnSure(View view){
		List<Thesaurus> thesauruses = ts.selAllThesaurus( context );
		for(int i = 0 ; i < thesauruses.size();i++){
			if(thesauruses.get( i ).getThesaurus_Name().equals(  spinner.getSelectedItem().toString())){
				//如果选择的名字  与存储的书本名相同  就存入书本ID  并进入选择每日背单词个数界面
				int id = thesauruses.get( i ).getThesaurus_ID();
				Intent intent = new Intent( context,ChooseNumberOfEveryday.class );
				intent.putExtra( "id",id );
				startActivity( intent );
				finish();
			}
		}
	}


/**
 * 返回按钮
 * */
	public void QuitSystem(View view){

		Intent intent = new Intent( context,SearchPage.class );
		startActivity( intent );
		finish();
	}

	/**
	 * 真机返回按钮
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent = new Intent( context,SearchPage.class );
			startActivity( intent );
			finish();
		}
		return super.onKeyDown( keyCode, event );
	}

}
