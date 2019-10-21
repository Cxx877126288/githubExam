package com.example.englishwords.service.impl;

import android.content.Context;
import android.database.Cursor;
import com.example.englishwords.dao.ThesaurusDAO;
import com.example.englishwords.dao.impl.ThesaurusDaoImpl;
import com.example.englishwords.pojo.Thesaurus;
import com.example.englishwords.service.ThesaurusService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HX
 * @title: ThesaurusServiceImpl
 * @projectName Words_System
 * @date 2019/9/3  9:58
 */
public class ThesaurusServiceImpl implements ThesaurusService {
	private ThesaurusDAO td = new ThesaurusDaoImpl(  );
	private Cursor cu;
	@Override
	public List<String> selAllBookName(Context context) {
		cu = td.selAll(context);
		List<String> names = new ArrayList<>(  );
		while (cu.moveToNext()){
			String name = cu.getString( cu.getColumnIndex( "thesaurus_Name" ) );
			names.add( name );
		}
		return names;
	}

	@Override
	public List<Thesaurus> selAllThesaurus(Context context) {
		cu = td.selAll(context);
		List<Thesaurus> tss = new ArrayList<>(  );
		while (cu.moveToNext()){
			Thesaurus t = new Thesaurus();
			t.setThesaurus_ID( Integer.parseInt( cu.getString( cu.getColumnIndex( "thesaurus_ID" ) ) ) );
			t.setThesaurus_Name( cu.getString( cu.getColumnIndex( "thesaurus_Name" ) )  );
			t.setThesaurus_Count( Integer.parseInt( cu.getString( cu.getColumnIndex( "thesaurus_Count" ) ) ) );
			t.setBookID( Integer.parseInt( cu.getString( cu.getColumnIndex( "BookID" ) ) ) );
			tss.add( t );
		}
		return tss;
	}

	@Override
	public Thesaurus selByThesaurusID(Context context, int _id) {
		cu = td.selAll( context );
		Thesaurus t = new Thesaurus();
		while (cu.moveToNext()){
			if(cu.getInt( cu.getColumnIndex( "thesaurus_ID" ) ) == _id){
				t.setThesaurus_ID( _id );
				t.setThesaurus_Name( cu.getString( cu.getColumnIndex( "thesaurus_Name" ) ) );
				t.setThesaurus_Count( cu.getInt( cu.getColumnIndex( "thesaurus_Count" ) ) );
				t.setBookID( Integer.parseInt( cu.getString( cu.getColumnIndex( "BookID" ) ) ) );
				break;
			}else {
				continue;
			}
		}
		return t;
	}
}