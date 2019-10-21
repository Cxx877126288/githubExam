package com.example.englishwords.dao;

import android.database.Cursor;

/**
 * @author HX
 * @title: WordDAO
 * @projectName Words_System
 * @date 2019/7/1  11:01
 */
public interface WordDAO {
	Cursor selByLimit(String tableName, String limit);
	Cursor SelectWord(int id);
	Cursor selByWord(String word);
}
