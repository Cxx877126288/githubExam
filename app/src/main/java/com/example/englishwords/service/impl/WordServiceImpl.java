package com.example.englishwords.service.impl;

import android.database.Cursor;
import com.example.englishwords.dao.WordDAO;
import com.example.englishwords.dao.impl.WordDaoImpl;
import com.example.englishwords.pojo.Word;
import com.example.englishwords.service.WordService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HX
 * @title: WordServiceImpl
 * @projectName Words_System
 * @date 2019/7/1  10:59
 */
public class WordServiceImpl implements WordService {
	private WordDAO dao = new WordDaoImpl();

	@Override
	public List<Word> selAll() {
		List<Word> words = new ArrayList<>();
		String table = "dict_a_b";
		String limit = "0,10";
		Cursor cu = dao.selByLimit( table, limit );
		while (cu.moveToNext()) {
			Word word = new Word();
			word.setTopic_id( Integer.parseInt( cu.getString( cu.getColumnIndex( "topic_id" ) ) ) );
			word.setWord( cu.getString( cu.getColumnIndex( "word" ) ) );
			word.setAccent( cu.getString( cu.getColumnIndex( "accent" ) ) );
			word.setMean_cn( cu.getString( cu.getColumnIndex( "mean_cn" ) ) );
			word.setFreq( Double.parseDouble( cu.getString( cu.getColumnIndex( "freq" ) ) ) );
			word.setWord_length( Integer.parseInt( cu.getString( cu.getColumnIndex( "word_length" ) ) ) );
			words.add( word );
		}
		return words;
	}

	@Override
	public Word selOne(int id) {
		Word word = new Word();
		Cursor cu = dao.SelectWord( id );
		if (cu.moveToNext()) {
			word.setTopic_id( Integer.parseInt( cu.getString( cu.getColumnIndex( "topic_id" ) ) ) );
			word.setWord( cu.getString( cu.getColumnIndex( "word" ) ) );
			word.setAccent( cu.getString( cu.getColumnIndex( "accent" ) ) );
			word.setMean_cn( cu.getString( cu.getColumnIndex( "mean_cn" ) ) );
			word.setFreq( Double.parseDouble( cu.getString( cu.getColumnIndex( "freq" ) ) ) );
			word.setWord_length( Integer.parseInt( cu.getString( cu.getColumnIndex( "word_length" ) ) ) );
		}
		return word;
	}

	@Override
	public List<Word> selByWord(String word) {
		List<Word> words = new ArrayList<>();
		Cursor cu = dao.selByWord( word );
		int i = 0;
		while (cu.moveToNext() && i <= 30) {
			Word get = new Word();
			get.setTopic_id( Integer.parseInt( cu.getString( cu.getColumnIndex( "topic_id" ) ) ) );
			get.setWord( cu.getString( cu.getColumnIndex( "word" ) ) );
			get.setAccent( cu.getString( cu.getColumnIndex( "accent" ) ) );
			get.setMean_cn( cu.getString( cu.getColumnIndex( "mean_cn" ) ) );
			get.setFreq( Double.parseDouble( cu.getString( cu.getColumnIndex( "freq" ) ) ) );
			get.setWord_length( Integer.parseInt( cu.getString( cu.getColumnIndex( "word_length" ) ) ) );
			words.add( get );
			i++;
		}
		return words;
	}
}
