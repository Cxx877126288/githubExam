package com.example.englishwords.util;

import android.content.Context;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author HX
 * @title: fileOperator
 * @projectName Words_System
 * @date 2019/9/9  9:21
 * 对于复习文件的生成与操作
 */
public class fileOperator {

	/**
	 * 添加复习的文件
	 * @param WordsID 需要添加的单词ID
	 * */
	public static void writeReview(Context context,int WordsID){
		String time = StringUtils.DateToString( new Date( System.currentTimeMillis() ) );
		String filePath = (context.getFilesDir()).getPath();
		String total_Path = filePath + "/" + time;
		if(!FileHasID( context,WordsID,time )){
			File file = new File( total_Path );
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream( file, true );
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			try {
				fos.write( (String.valueOf( WordsID ) + "\r\n").getBytes() );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 通过时间来获取对应里所有的单词ID
	 * @param time 要获取单词的时间
	 * @return 该天单词列表
	 * */
	public static List<Integer> getWordsIDByTime(Context context, String time){
		String filePath = (context.getFilesDir()).getPath();
		String total_Path = filePath + "/" + time;
		List<Integer> ret = new ArrayList<>(  );
		try{
			InputStream is = new FileInputStream( total_Path );
			BufferedReader br = new BufferedReader( new InputStreamReader( is ) );
			String s = "";
			while((s = br.readLine()) != null){
				ret.add( Integer.parseInt( s.trim() ) );
			}
			br.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 判断文件里是否已经添加过该单词
	 * @param ID 即将添加的单词ID
	 * @param time 时间
	 * @return 存在：true;不存在：false
	 * */
	private static Boolean FileHasID(Context context,int ID,String time){
		String filePath = (context.getFilesDir()).getPath();
		String total_Path = filePath + "/" + time;
		Boolean ret = false;
		try{
			InputStream is = new FileInputStream( total_Path );
			BufferedReader br = new BufferedReader( new InputStreamReader( is ) );
			String s = "";
			while((s = br.readLine()) != null){
				if(s.trim().equals( String.valueOf( ID ) )){
					ret = true;
					break;
				}
			}
			br.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * 获取复习时间的列表
	 * @return 复习时间列表
	 * */
	public static List<String> getFileName(Context context){
		String filePath = (context.getFilesDir()).getPath();
		File file = new File( filePath );
		File[] files = file.listFiles();
		List<String> ret = new ArrayList<>(  );
		for(int i = 0;i < files.length;i++){
			if(files[i].getName().equals( "time.txt" ) || files[i].getName().equals( "load.txt" )){}
			else{
				ret.add( files[i].getName() );
			}
		}
		return ret;
	}

	/**
	 * 删除某天的复习文件
	 * @param time 日期
	 * @return 成功：true；失败：false；
	 * */
	public static Boolean delReview(Context context,String time){
		Boolean ret = false;
		String filePath = (context.getFilesDir()).getPath();
		String total_Path = filePath + "/" + time;
		File file = new File( total_Path );
		if(file.exists()){
			ret = file.delete();
		}
		return ret;
	}

	/**
	 * 当用户点击重置后 删除所有的日期文件
	 * */
	public static void ResetAllTime(Context context){
		String filePath = (context.getFilesDir()).getPath();
		File file = new File( filePath );
		File[] files = file.listFiles();
		for(int i = 0;i < files.length;i++){
			if(files[i].getName().equals( "time.txt" ) || files[i].getName().equals( "load.txt" )){}
			else{
				files[i].delete();
			}
		}
	}
}
