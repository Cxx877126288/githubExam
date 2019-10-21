package com.example.englishwords.dao.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.englishwords.dao.ThesaurusDAO;
import com.example.englishwords.db.EnglishSqlite;

/**
 * @author HX
 * @title: ThesaurusDaoImpl
 * @projectName Words_System
 * @date 2019/9/3  9:50
 */
public class ThesaurusDaoImpl implements ThesaurusDAO {
	private Cursor cu;

	@Override
	public Cursor selAll(Context context) {
		EnglishSqlite my = new EnglishSqlite( context );
		SQLiteDatabase rd = my.getReadableDatabase();
		cu = rd.query( "thesaurus", null, null, null, null, null, null );
		return cu;
	}
}
