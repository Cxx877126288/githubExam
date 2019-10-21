package com.example.englishwords.myadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.englishwords.R;
import com.example.englishwords.pojo.Word;

import java.util.List;

/**
 * @author HX
 * @title: StrangeBookAdapter
 * @projectName Words_System
 * @date 2019/9/6  9:36
 * 生词本的适配器
 */
public class StrangeBookAdapter extends BaseAdapter {
	private List<Word> words;
	private Context context;

	public StrangeBookAdapter(List<Word> words,Context context) {
		this.words = words;
		this.context = context;
	}

	@Override
	public int getCount() {
		return words.size();
	}

	@Override
	public Object getItem(int i) {
		return words.get( i );
	}

	@Override
	public long getItemId(int i) {
		return words.get( i ).getTopic_id();
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewGroup vg =(ViewGroup)View.inflate( context, R.layout.strange_adpter,null );
		TextView word = vg.findViewById( R.id.word );
		TextView mean = vg.findViewById( R.id.word_mean );
		TextView id = vg.findViewById( R.id.word_id );
		TextView speak = vg.findViewById( R.id.word_speak );

		int topic_id = ((Word) getItem( i )).getTopic_id();
		word.setText( ((Word)getItem( i )).getWord() );
		mean.setText( ((Word)getItem( i )).getMean_cn() );
		id.setText( String.valueOf(topic_id) );
		speak.setText( ((Word)getItem( i )).getAccent()  );

		return vg;
	}
}
