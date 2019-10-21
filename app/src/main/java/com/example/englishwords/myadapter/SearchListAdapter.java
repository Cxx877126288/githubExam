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
 * @title: SearchListAdapter
 * @projectName Words_System
 * @date 2019/9/10  8:57
 * 搜索页面的适配器
 */
public class SearchListAdapter extends BaseAdapter {
	private List<Word> words;
	private Context context;

	public SearchListAdapter(List<Word> words, Context context) {
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
		ViewGroup vg =(ViewGroup)View.inflate( context, R.layout.searchlist_adpter,null );
		TextView word = vg.findViewById( R.id.search_word );
		TextView mean = vg.findViewById( R.id.search_word_mean );
		word.setText( ((Word)getItem( i )).getWord() );
		mean.setText( ((Word)getItem( i )).getMean_cn() );
		return vg;
	}
}
