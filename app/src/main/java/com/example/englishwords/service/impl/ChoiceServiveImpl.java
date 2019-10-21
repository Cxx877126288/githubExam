package com.example.englishwords.service.impl;

import android.content.Context;
import android.database.Cursor;
import com.example.englishwords.pojo.Choice;
import com.example.englishwords.dao.ChoiceDAO;
import com.example.englishwords.dao.impl.ChoiceDaoImpl;
import com.example.englishwords.service.ChoiceService;

/**
 * @author HX
 * @title: ChoiceServiveImpl
 * @projectName Words_System
 * @date 2019/9/4  8:44
 */
public class ChoiceServiveImpl implements ChoiceService {
	private Cursor cu;
	private ChoiceDAO cd = new ChoiceDaoImpl();
	@Override
	public Choice selOne(Context context) {
		cu = cd.selOne( context );
		Choice ch = new Choice();
		if(cu.moveToNext()){
			ch.setThesaurus_id( Integer.parseInt( cu.getString( cu.getColumnIndex( "thesaurus_id" ) ) ) );
			ch.setPerwords( Integer.parseInt( cu.getString( cu.getColumnIndex( "perwords" ) ) ) );
			ch.setBookID( Integer.parseInt( cu.getString( cu.getColumnIndex( "BookID" ) ) ) );
		}
		return ch;
	}
}
