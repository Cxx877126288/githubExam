package com.example.englishwords.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import com.example.englishwords.page.SearchPage;


/**
 * @author HX
 * @title: ProgressViewThread
 * @projectName Words_System
 * @date 2019/7/1  15:04
 * 进度条进行改变的进程
 */
public class ProgressViewThread extends Thread {
	private Context context;
	private ProgressView progress;
	private Activity ac;
	private final int Max = 100;
	private Boolean jump = true;
	public ProgressViewThread(ProgressView progress,Context context,Activity ac){
		this.progress = progress;
		this.context = context;
		this.ac = ac;
	}
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==0x01){
				progress.setCurrentCount(msg.arg1);
			}
		};
	};

	@Override
	public void run() {
		progress.setMaxCount(Max);
		Boolean flag = true;
		int i = 0;
		while (flag){
			Message msg = new Message();
			msg.arg1 = i;
			msg.what = 0x01;
			handler.sendMessage(msg);
			try {
				//每隔0.1秒进度前进1
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(i <= progress.getMaxCount()){
				i++;
			}else {
				flag = false;
			}
		}
//		loadOperator.update( context );
//		Intent intent = new Intent( context,ChooseWordsBook.class );
		if(jump){
			jump =false;

			Intent intent = new Intent( context,SearchPage.class );
			context.startActivity( intent );  //能跳转
			ac.finish();
		}

	}
}
