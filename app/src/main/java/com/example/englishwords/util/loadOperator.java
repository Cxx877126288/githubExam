package com.example.englishwords.util;

import android.content.Context;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 判断程序是否已经加载过了
 * @author HX
 * @title: loadOperator
 * @projectName Words_System
 * @date 2019/6/28  9:30
 */
public class loadOperator {
	public static String DB_PATH = null;
	public static String DB_NAME = null;

	/**
	 * 判断加载文件是否已经存在
	 * @return 存在：true；不存在：false；
	 * */
	public static Boolean fileExist(Context context){
		String filePath = (context.getFilesDir()).getPath();
		String fileName = "load.txt";
		String total_Path = filePath + "/" + fileName;
		File file = new File( total_Path );
		return file.exists();
	}

	/**
	 * 加载文件不存在就生成加载文件；
	 * 默认载入flase;标志是第一次加载
	 * */
	public static void loadFile(Context context){
		String filePath = (context.getFilesDir()).getPath();
		String fileName = "load.txt";
		String total_Path = filePath + "/" + fileName;
		String load = "false";
		File file = new File( total_Path );
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "rwd");
			raf.write(load.getBytes());
			raf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取加载文件来判断是否加载过
	 * @return 文件的内容  是true还是false
	 * */
	public static String readLoad(Context context){
		String filePath = (context.getFilesDir()).getPath();
		String fileName = "load.txt";
		String total_Path = filePath + "/" + fileName;
		String ret = null;
		try{
			InputStream is = new FileInputStream( total_Path );
			BufferedReader br = new BufferedReader( new InputStreamReader( is ) );
			String s = "";
			while((s = br.readLine()) != null){
				ret = s;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 加载过后修改加载文件内容，将false改为true
	 * */
	public static void update(Context context) {
		String filePath = (context.getFilesDir()).getPath();
		String fileName = "load.txt";
		String total_Path = filePath + "/" + fileName;
		File file = new File( total_Path );
		if(file.exists()){
			if(file.delete()){
				String test = "true";
				RandomAccessFile raf = null;
				try {
					raf = new RandomAccessFile(file, "rwd");
					raf.write(test.getBytes());
					raf.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 重置加载文件
	 * */
	public static void ResetLoadFile(Context context){
		String filePath = (context.getFilesDir()).getPath();
		String fileName = "load.txt";
		String total_Path = filePath + "/" + fileName;
		File file = new File( total_Path );
		if(file.exists()){
			if(file.delete()){
				String test = "false";
				RandomAccessFile raf = null;
				try {
					raf = new RandomAccessFile(file, "rwd");
					raf.write(test.getBytes());
					raf.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将所有单词导入到本地文件
	 * */
	public static Boolean loadDataBase(Context context){
		Boolean ret = false;
		DB_PATH = "/data/data/"+context.getPackageName()+ "/databases/";
		DB_NAME = "twords.db";
		try {
			InputStream is = context.getAssets().open( "lookup.db" );
			String outFileName = DB_PATH + DB_NAME;
			//Open the empty db as the output stream
			File dir = new File( DB_PATH );
			if(!dir.exists()){
				dir.mkdir();
			}
			if(!(new File(outFileName)).exists()){
				OutputStream myOutput = new FileOutputStream(outFileName);
				//transfer bytes from the inputfile to the outputfile
				byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer))>0){
					myOutput.write(buffer, 0, length);
				}
				//Close the streams
				myOutput.flush();
				myOutput.close();
				is.close();
				ret = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * 写入使用时间
	 * */
	public static void WirteUseTime(Context context){
		String filePath = (context.getFilesDir()).getPath();
		String fileName = "time.txt";
		String total_Path = filePath + "/" + fileName;
		Date date = new Date(System.currentTimeMillis());
		String test = StringUtils.DateToString( date );
		File file = new File( total_Path );
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "rwd");
			raf.write(test.getBytes());
			raf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取使用时间，用来判断是否已经过了一天
	 * */
	public static String ReadUseTime(Context context){
		String filePath = (context.getFilesDir()).getPath();
		String fileName = "time.txt";
		String total_Path = filePath + "/" + fileName;
		String ret = null;
		try{
			InputStream is = new FileInputStream( total_Path );
			BufferedReader br = new BufferedReader( new InputStreamReader( is ) );
			String s = "";
			while((s = br.readLine()) != null){
				ret = s;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static Boolean UseFileExist(Context context){
		String filePath = (context.getFilesDir()).getPath();
		String fileName = "time.txt";
		String total_Path = filePath + "/" + fileName;
		File file = new File( total_Path );
		return file.exists();
	}

	/**
	 * 用来判断是否已经过了一天，以生成新单词
	 * @return 过了：true；没有过：false
	 * */
	public static Boolean ProduceNewWordsFlag(Context context){
		Boolean ret = false;
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		Date date = new Date( System.currentTimeMillis() );
		try {
			Date useTime = sdf.parse( ReadUseTime( context ) );
			date = sdf.parse( StringUtils.DateToString( date ) );
			if( useTime.before( date ) ){
				ret = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
