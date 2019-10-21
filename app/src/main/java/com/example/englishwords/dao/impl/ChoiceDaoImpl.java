package com.example.englishwords.dao.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.englishwords.dao.ChoiceDAO;
import com.example.englishwords.db.EnglishSqlite;

/**
 * @author HX
 * @title: ChoiceDaoImpl
 * @projectName Words_System
 * @date 2019/9/4  8:45
 */
public class ChoiceDaoImpl implements ChoiceDAO {
	@Override
	public Cursor selOne(Context context) {
		EnglishSqlite my = new EnglishSqlite( context );
		SQLiteDatabase rd = my.getReadableDatabase();
		Cursor cu = rd.query( "choice", null, null, null, null, null, null );
		return cu;
	}
}
