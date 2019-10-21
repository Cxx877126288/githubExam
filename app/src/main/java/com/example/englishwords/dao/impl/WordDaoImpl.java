package com.example.englishwords.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.englishwords.util.StringUtils;
import com.example.englishwords.util.loadOperator;
import com.example.englishwords.dao.WordDAO;

/**
 * @author HX
 * @title: WordDaoImpl
 * @projectName Words_System
 * @date 2019/7/1  11:03
 */
public class WordDaoImpl implements WordDAO {
	private SQLiteDatabase myDataBase;
	private Cursor cu;
	private String myPath;
	private void openDatabase(){
		myPath = loadOperator.DB_PATH + loadOperator.DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	}
	private void closeDatabase(){
		myDataBase.close();
	}
	@Override
	public Cursor selByLimit(String tableName, String limit) {
		openDatabase();
		Cursor cu = myDataBase.query( true, tableName, null, null, null, null, null, null, limit);
		return cu;
	}

	@Override
	public Cursor SelectWord(int id) {
		openDatabase();
		Cursor cu = myDataBase.query( "dict_bcz", null, "topic_id=?", new String[]{String.valueOf( id )}, null, null, null );

		return cu;
	}

	@Override
	public Cursor selByWord(String word) {
		openDatabase();
		Cursor cu = null;
		if(StringUtils.containChinese( word.trim() )){
			//包含了汉字
			cu =  myDataBase.query( "dict_bcz", null, "mean_cn like ?", new String[]{ "%" + word + "%"}, null, null, null );
		}else{
			//就输入了一个字符；
			cu =  myDataBase.query( "dict_bcz", null, "word like ?", new String[]{ word.trim() + "%"}, null, null, "word" );
		}
		return cu;
	}
}
