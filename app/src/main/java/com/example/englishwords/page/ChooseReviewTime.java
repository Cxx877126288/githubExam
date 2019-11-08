package com.example.englishwords.page;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.*;
import com.example.englishwords.R;
import com.example.englishwords.adapter.ReviewTimeAdapter;
import com.example.englishwords.util.fileOperator;

import java.util.List;

/**
 * @author HX
 * @title: ChooseReviewTime
 * @projectName Words_System
 * @date 2019/9/9  10:55
 * 选择复习时间界面
 */
public class ChooseReviewTime extends AppCompatActivity {
	private Context context;   //上下文参数
	private ReviewTimeAdapter adapter;   //复习时间适配器
	private ListView list;     //时间显示的List
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		context = this;
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.choosereviewtime );
		loadPage();
	}
	/**
	 * 初始化页面
	 * */
	public void loadPage(){
		List<String> fileName = fileOperator.getFileName( context );
		list = findViewById( R.id.review_list );
		adapter = new ReviewTimeAdapter( fileName,context );
		list.setAdapter( adapter );

		list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				TextView tv = view.findViewById( R.id.review_time );
				String time = tv.getText().toString();
				Intent intent = new Intent( context,Review.class );
				intent.putExtra( "time",time );
				startActivity( intent );
			}
		} );
		//列表的长按事件  弹出删除选项
		list.setOnCreateContextMenuListener( new View.OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
				contextMenu.add( 1,1,1,"删除" ) ;
			}
		} );
	}
	/**
	 * 返回界面
	 * */
	public void Chooseback(View view){
		finish();
	}

	/**
	 * 真机返回按钮
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown( keyCode, event );
	}

	/**
	 * 当长按菜单的删除被点了
	 * */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ContextMenu.ContextMenuInfo menuInfo = item.getMenuInfo();
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		final String time = (String)adapter.getItem( info.position );
		switch (item.getItemId()){
			case 1:
//				if(fileOperator.delReview( context,time )){  应该先确定  再做删除

					AlertDialog.Builder dialog = new AlertDialog.Builder( context );
					dialog.setTitle( "提示" );
					dialog.setMessage( "要删除"+ time + "的复习计划吗？" );
					dialog.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							if(fileOperator.delReview( context,time )){
								loadPage();
								Toast.makeText( context,"删除成功", Toast.LENGTH_LONG).show();
							}else {
								loadPage();
								Toast.makeText( context,"删除失败", Toast.LENGTH_LONG).show();
							}
						}
					} );

					dialog.setNegativeButton( "取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					} );
					dialog.show();
//				}
				break;
		}
		return super.onContextItemSelected( item );
	}
}
