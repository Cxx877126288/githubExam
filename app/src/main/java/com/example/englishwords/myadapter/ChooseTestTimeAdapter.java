package com.example.englishwords.myadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.englishwords.R;

import java.util.List;

/**
 * @author HX
 * @title: ChooseTestTimeAdapter
 * @projectName Words_System
 * @date 2019/9/10  19:50
 * 选择测试日期的适配器
 */
public class ChooseTestTimeAdapter extends BaseAdapter {
	private List<String> fileName;
	private Context context;

	public ChooseTestTimeAdapter(List<String> fileName, Context context) {
		this.fileName = fileName;
		this.context = context;
	}

	@Override
	public int getCount() {
		return fileName.size();
	}

	@Override
	public Object getItem(int i) {
		return fileName.get( i );
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewGroup vg =(ViewGroup)View.inflate( context, R.layout.choosetesttime_adpter,null );
		TextView tv = vg.findViewById( R.id.choosetesttime_time );
		String get = (String)fileName.get( i );
		tv.setText( get  );

		return vg;
	}
}
