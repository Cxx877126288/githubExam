package com.example.englishwords.dao;

import android.content.Context;
import android.database.Cursor;

/**
 * @author HX
 * @title: ThesaurusDAO
 * @projectName Words_System
 * @date 2019/9/3  9:50
 */
public interface ThesaurusDAO {
	Cursor selAll(Context context);
}
