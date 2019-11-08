package com.example.englishwords.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库类，用于初始化的时候创建表
 * @author HX
 * @title: EnglishSqlite
 * @projectName Words_System
 * @date 2019/6/27  9:37
 */
public class EnglishSqlite extends SQLiteOpenHelper {
	//创建书籍表
	private String create_Thesaurus = "create table thesaurus(thesaurus_ID INTEGER PRIMARY KEY AUTOINCREMENT,thesaurus_Name varchar(30),thesaurus_Count INTEGER,BookID INTEGER)";
	//创建选择的书本，和每日单词的表
	private String create_Choose = "create table choice(thesaurus_id INTEGER PRIMARY KEY,perwords INTEGER,BookID INTEGER)";
	//创建每日最后所背单词的ID
	private String create_LastWordsID = "create table laststudy(wordsid INTEGER)";
	//创建已认识的单词列表
	private String create_KnowList = "create table knowlist(wordsid INTEGER PRIMARY KEY)";
	//创建忘记的单词列表
	private String create_unKnowList = "create table unknowlist(wordsid INTEGER PRIMARY KEY,flag INTEGER,showtimes INTEGER)";//flag代表是要复习几天，默认为2  当为0的时候 删除加入KnowList
	//创建每日需背单词列表
	private String create_DayWords = "create table daywords(wordsid INTEGER PRIMARY KEY)";   //记录每天要背的单词
	//创建生词本
	private String create_Strange = "create table strange(wordsid INTEGER PRIMARY KEY)";  //生词本

	public EnglishSqlite(Context context) {
		super( context, "englishwords", null, 1 );
	}

	public EnglishSqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super( context, name, factory, version );
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL( create_Thesaurus );
		sqLiteDatabase.execSQL( create_Choose );
		sqLiteDatabase.execSQL( create_LastWordsID );
		sqLiteDatabase.execSQL( create_KnowList );
		sqLiteDatabase.execSQL( create_unKnowList );
		sqLiteDatabase.execSQL( create_DayWords );
		sqLiteDatabase.execSQL( create_Strange );
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

	}
}
