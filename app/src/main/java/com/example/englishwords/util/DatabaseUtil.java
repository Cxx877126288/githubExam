package com.example.englishwords.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.englishwords.db.EnglishSqlite;
import com.example.englishwords.pojo.Word;
import com.example.englishwords.service.WordService;
import com.example.englishwords.service.impl.WordServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author HX
 * @title: DatabaseUtil
 * @projectName Words_System
 * @date 2019/9/4  15:17
 */
public class DatabaseUtil {
	private static EnglishSqlite my;
	private static SQLiteDatabase rd;
	private static SQLiteDatabase wd;
	private static Cursor cu;
	private static WordService ws = new WordServiceImpl();
	public static void CloseConnect(){
		my.close();
	}
	public static int getWordsCount(Context context){
		my = new EnglishSqlite( context );
		rd = my.getReadableDatabase();
		cu = rd.query( "thesaurus", null, null, null, null, null, null );
		cu.moveToNext();
		return Integer.parseInt( cu.getString( cu.getColumnIndex( "thesaurus_Count" ) ) );
	}

	public static int getPerWords(Context context){
		my = new EnglishSqlite( context );
		rd = my.getReadableDatabase();
		cu = rd.query( "choice", null, null, null, null, null, null, null );
		cu.moveToNext();
		return Integer.parseInt( cu.getString( cu.getColumnIndex( "perwords" ) ) );
	}

	public static int getKnowListCount(Context context){
		my = new EnglishSqlite( context );
		rd = my.getReadableDatabase();
		cu = rd.query( "knowlist", null, null, null, null, null, null, null );
		return cu.getCount();
	}

	public static int getUnKnowListCount(Context context){
		my = new EnglishSqlite( context );
		rd =  my.getReadableDatabase();
		cu = rd.query( "unknowlist", null, null, null, null, null, null, null );
		return cu.getCount();
	}

	public static List<Integer> getKnowList(Context context){
		getKnowListCount( context );
		List<Integer> ret = new ArrayList<>(  );
		while (cu.moveToNext()){
			ret.add( Integer.parseInt( cu.getString( cu.getColumnIndex( "wordsid" ) ) ) );
		}
		return ret;
	}

	public static List<Integer> getUnKnowList(Context context){
		getUnKnowListCount( context );
		List<Integer> ret = new ArrayList<>(  );
		while (cu.moveToNext()){
			ret.add( Integer.parseInt( cu.getString( cu.getColumnIndex( "wordsid" ) ) ) );
		}
		return ret;
	}



	public static List<Integer> getStrangeBookList(Context context){
		my = new EnglishSqlite( context );
		rd = my.getReadableDatabase();
		cu = rd.query( "strange",null,null,null,null,null,null );
		List<Integer> ret = new ArrayList<>(  );
		while (cu.moveToNext()){
			ret.add( Integer.parseInt( cu.getString( cu.getColumnIndex( "wordsid" ) ) ) );
		}
		return ret;
	}

	public static Boolean KnowListHasWordsID(int wordsID,Context context){
		List<Integer> knowList = getKnowList( context );
		Boolean ret = false;
		for(int i = 0 ; i < knowList.size();i++){
			if(knowList.get( i ) == wordsID){
				ret = true;
				break;
			}
		}
		CloseConnect();
		return ret;
	}

	public static Boolean UnKnowListHasWordsID(int wordsID,Context context){
		List<Integer> unknowList = getUnKnowList( context );
		Boolean ret = false;
		for(int i = 0 ; i < unknowList.size();i++){
			if(unknowList.get( i ) == wordsID){
				ret = true;
				break;
			}
		}
		CloseConnect();
		return ret;
	}

	public static Boolean DayWordsHasWordsID(int wordsID,Context context){
		Boolean ret = false;
		my = new EnglishSqlite( context );
		rd = my.getReadableDatabase();
		cu = rd.query( "daywords",null,null,null,null,null,null );
		while (cu.moveToNext()){
			int id = Integer.parseInt( cu.getString( cu.getColumnIndex( "wordsid" ) ) );
			if(id == wordsID){
				ret = true;
				break;
			}
		}
		CloseConnect();
		return ret;
	}

	public static void clearDayWords(Context context){
		my = new EnglishSqlite( context );
		wd = my.getWritableDatabase();
		wd.delete( "daywords",null,null );
		wd.delete( "laststudy",null,null );
		CloseConnect();
	}

	public static Word selFromDaywords(Context context){
		my = new EnglishSqlite(context);
		rd = my.getReadableDatabase();
		cu = rd.query( "daywords",null,null,null,null,null,null);
		cu.moveToNext();
		int id = Integer.parseInt( cu.getString( cu.getColumnIndex( "wordsid" ) ) );
		return ws.selOne( id );
	}

	public static void insKnowList(int wordsID,Context context){
		my = new EnglishSqlite( context );
		wd = my.getWritableDatabase();
		ContentValues cv = new ContentValues(  );
		cv.put( "wordsid",wordsID );
		wd.insert( "knowlist",null,cv );
		deleteWords( wordsID,"daywords" );
		CloseConnect();
	}

	public static void insUnKnowList(int wordsID,Context context){
		if(!UnKnowListHasWordsID( wordsID,context )){
			//如果忘记列表没有这个单词  说明是从新单词提取的；
			wd = my.getWritableDatabase();
			ContentValues cv = new ContentValues(  );
			cv.put( "wordsid",wordsID );
			cv.put( "flag",2 );
			cv.put( "showtimes",2 );
			wd.insert( "unknowlist",null,cv );
//			deleteWords( wordsID,"daywords" );
		}else{
			//如果有，就减少显示次数
			wd = my.getWritableDatabase();
			ContentValues cv = new ContentValues(  );
			cu = wd.query( "unknowlist",null,"wordsid=?",new String[]{String.valueOf( wordsID )},null,null,null );
			int showTime = 0;
			if(cu.moveToNext()){
				//获取剩余显示次数
				showTime = Integer.parseInt( cu.getString( cu.getColumnIndex( "showtimes" ) ) );
			}
			//减少次数的方法
			if(showTime <= 0){
				//如果获取的显示次数小于等于0；就从每日列表删除；
				deleteWords( wordsID,"daywords" );
			}else {
				//否则就减少次数
				cv.put( "showtimes",showTime - 1 );
				wd.update( "unknowlist",cv,"wordsid=?",new String[]{String.valueOf( wordsID )} );
			}
		}
		CloseConnect();
	}

	public static int getDaywordsCount(Context context){
		my = new EnglishSqlite( context );
		rd = my.getReadableDatabase();
		cu = rd.query( "daywords",null,null,null,null,null,null );
		return cu.getCount();
	}

	public static void updUnKnowListFlagAndShowtimes(Context context){
		//每日开始的时候   更新忘记表所有的信息
		my = new EnglishSqlite( context );
		wd  = my.getWritableDatabase();
		cu = wd.query( "unknowlist",null, null,null,null,null,null);
		while (	cu.moveToNext()){
			int flag = Integer.parseInt( cu.getString( cu.getColumnIndex( "flag" ) ) );
			ContentValues cv = new ContentValues(  );
			cv.put( "flag" , (flag - 1) );
			cv.put( "showtimes" ,2);
			wd.update( "unknowlist",cv,"wordsid=?",new String[]{cu.getString( cu.getColumnIndex( "wordsid" ) )} );
		}
		CloseConnect();
	}

	public static void delUnKnowListFlagIsZero(Context context){
		my = new EnglishSqlite( context );
		wd  = my.getWritableDatabase();
		if(getUnKnowListCount( context ) != 0){
			cu = wd.query( "unknowlist",null, null,null,null,null,null);
			while (	cu.moveToNext()){
				int flag = Integer.parseInt( cu.getString( cu.getColumnIndex( "flag" ) ) );
				if(flag == 0){
					wd.delete( "unknowlist","wordsid=?",new String[]{cu.getString( cu.getColumnIndex( "wordsid" ) )} );
				}
			}
		}
		CloseConnect();
	}

//	public static Word randomAWord(Context context){
//		my = new EnglishSqlite( context );
//		rd = my.getReadableDatabase();
//		Random random = new Random(  );
//		int choiceFrom =  -1;
//		choiceFrom =  1 + ExpandUtil.NumberExpand(random.nextInt( 200000 ) );   //随机生成1 或 2    1代表从 单词列表  2代表从忘记列表
//		int unKnowListCount = getUnKnowListCount( context );
//		int daywordsCount = getDaywordsCount( context );
//		Word word = null;
//		String table = "";
//
//		if(choiceFrom == 1){
//			//从新单词列表提取
//			if(daywordsCount == 0){
//				//如果新单词提取完  就从忘记列表提取
//				table = "unknowlist";
//			}else {
//				table = "daywords";
//			}
//		}else if(choiceFrom == 2){
//			//从忘记列表获取
//			if(unKnowListCount == 0){
//				//如果忘记已提取完 就从新单词提取
//				table = "daywords";
//			}else{
//				table = "unknowlist";
//			}
//		}
//		Boolean countFlag = true;
//		if(daywordsCount == 0 && unKnowListCount == 0){
//			//如果都提取完  就返回空
//			word = null;
//		}else{
//			cu = rd.query( table,null,null,null,null,null,null);
//			cu.moveToNext();
//			int rowCount = ExpandUtil.randomTimes( cu.getCount() );   //2   生成的是 0，1 ； 1  生成的是 0
//			if(table.equals( "unknowlist" )){
//				//如果是从忘记表获取，就先判断 今日是否都已经出现过了
//				Boolean allTimesIsZeroFlag = true;  // true 代表全为 0，false 代表还有不为0的;
//				while (true){
//					if(Integer.parseInt( cu.getString( cu.getColumnIndex( "showtimes" ) ) ) > 0 ){
//						//如果有大于0的就移动到第一条，跳出判断循环
//						allTimesIsZeroFlag = false;
//						cu.moveToFirst();
//						break;
//					}
//					if(cu.isLast()){
//						//已经到最后一个  说明全为0；
//						break;
//					}else {
//						//如果不是就移动下一条记录
//						cu.moveToNext();
//					}
//
//				}
//				if(!allTimesIsZeroFlag){ //如果不全为0 , 就移动到随机的位置 开始查找
//					for (int i = 0 ; i < rowCount;i++){
//						cu.moveToNext();
//					}
//					while (true){
//						int time = Integer.parseInt( cu.getString( cu.getColumnIndex( "showtimes" ) ) );
//						if(time > 0 ){//如果该行 显示次数大于0 就跳出
//							ContentValues cv = new ContentValues(  );
//							cv.put( "showtimes",(time - 1) );
//							wd  = my.getWritableDatabase();
//							wd.update( "unknowlist",cv,"wordsid=?",new String[]{cu.getString( cu.getColumnIndex( "wordsid" ) )} );
//							break;
//						}else{   //先前已经判断了 是否全为0  就不会进入死循环；
//							if(cu.isLast()){
//								//最后一条记录就从头开始找
//								cu.moveToFirst();
//							}else {
//								//不是最后一条就移动下一条
//								cu.moveToNext();
//							}
//						}
//					}
//				}else{  //全为0  就从新单词提取
//					if(daywordsCount == 0){
//						cu.moveToFirst();
//						countFlag = false;
//					}else {
//						cu = rd.query( "daywords",null,null,null,null,null,null );
//						rowCount = ExpandUtil.randomTimes( cu.getCount() );
//						for(int i = 0 ; i < rowCount ;i++){
//							cu.moveToNext();
//						}
//					}
//
//				}
//			}else{
//				//如果不是从忘记表开始提取
//				for(int i = 0 ; i < rowCount ;i++){
//					cu.moveToNext();
//				}
//			}
//			if(countFlag){cu.moveToFirst();
//				int id =  Integer.parseInt( cu.getString( cu.getColumnIndex( "wordsid" ) ) );
//				while (id < 0){
//					id =  Integer.parseInt( cu.getString( cu.getColumnIndex( "wordsid" ) ) );
//				}
//				word = ws.selOne(id) ;
//			}else {
//				word = null;
//			}
//		}
//		CloseConnect();
//		return word;
//	}

	public static Word randomAWord(Context context){
				my = new EnglishSqlite( context );
		rd = my.getReadableDatabase();
		Random random = new Random(  );
		Word word = null;
		cu = rd.query( "daywords",null,null,null,null,null,null );
		if(cu.getCount() > 0){
			int rowCount = ExpandUtil.randomTimes( cu.getCount() );
			if(rowCount == 0){
				cu.moveToNext();
			}
			for(int i = 0;i < rowCount;i++){
				cu.moveToNext();
			}
			word = ws.selOne( Integer.parseInt( cu.getString( cu.getColumnIndex( "wordsid" ) ) ) );
		}
		CloseConnect();
		return word;
	}

	public static void delWordsFromDaywordsOrUnknowList(int wordsID,Context context){
		my = new EnglishSqlite( context );
		rd = my.getReadableDatabase();
		wd = my.getWritableDatabase();
		cu = rd.query( "daywords",null,null,null,null,null,null );
		Boolean flag = true;//用来判断是否要进入忘记列表查找
		while (cu.moveToNext()){
			if(wordsID == Integer.parseInt( cu.getString( cu.getColumnIndex( "wordsid" ) ) )){
				if(deleteWords( wordsID,"daywords" ) > 0){
					flag = false;
				}
			}
		}
		if(flag){
			cu = rd.query( "unknowlist",null,null,null,null,null,null );
			while (cu.moveToNext()){
				if(wordsID == Integer.parseInt( cu.getString( cu.getColumnIndex( "wordsid" ) ) )){
					int showFlag = Integer.parseInt( cu.getString( cu.getColumnIndex( "flag" ) ) );
					if(showFlag > 0 ){
						//如果显示天数的标志大于0；就将显示天数减1，显示次数置0；从每日列表里删除该单词
						ContentValues cv = new ContentValues(  );
						cv.put("flag",showFlag - 1);
						cv.put( "showtimes",0 );
						wd.update( "unknowlist",cv,"wordsid=?",new String[]{String.valueOf( wordsID )} );
						deleteWords( wordsID,"daywords" );
					}else {
						//否则就作为新的单词，从忘记列表中删除
						deleteWords( wordsID,"unknowlist" );
					}
					break;
				}
			}
		}

		CloseConnect();
	}

	private static int deleteWords(int wordsID,String table){
		wd = my.getWritableDatabase();
		return wd.delete( table, "wordsid=?", new String[]{String.valueOf( wordsID )} );
	}

	public static void clearLastStudy(Context context){
		my = new EnglishSqlite( context );
		wd = my.getWritableDatabase();
		wd.delete( "laststudy",null,null );
		CloseConnect();
	}

	private static Boolean StrangListHasWordsID(int wordsID,Context context){
		Boolean ret = false;
		my = new EnglishSqlite( context );
		rd = my.getReadableDatabase();
		cu = rd.query( "strange",null ,null,null,null,null,null);
		while (cu.moveToNext()){
			if(Integer.parseInt( cu.getString( cu.getColumnIndex( "wordsid" ) ) ) == wordsID)
			{
				ret = true;
				break;
			}
		}
		CloseConnect();
		return ret;
	}

	public static long JoinStrange(int wordsID,Context context){
		long ret = -1;
		if(!StrangListHasWordsID(wordsID, context )){
			wd = my.getWritableDatabase();
			ContentValues cv = new ContentValues(  );
			cv.put( "wordsid",wordsID );
			ret = wd.insert( "strange",null,cv );
		}
		CloseConnect();
		return ret;
	}

	public static int delFromStrange(int wordsID,Context context){
		my = new EnglishSqlite( context );
		wd = my.getWritableDatabase();
		int ret = wd.delete( "strange","wordsid=?",new String[]{String.valueOf( wordsID )} );
		CloseConnect();
		return  ret;
	}

	public static void ResetTable(Context context){
		my = new EnglishSqlite( context );
		wd = my.getWritableDatabase();
		wd.delete( "choice",null,null );
		wd.delete( "laststudy",null,null );
		wd.delete( "knowlist",null,null );
		wd.delete( "unknowlist",null,null );
		wd.delete( "daywords",null,null );
		wd.close();
		my.close();
	}
}
