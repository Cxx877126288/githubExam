package com.example.englishwords.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author HX
 * @title: StringUtils
 * @projectName Words_System
 * @date 2019/9/3  15:35
 */
public class StringUtils {

	/**
	 * 判断字符串是否是全数字
	 * @param str 要判断的字符串
	 * @return 是：true；否：false；
	 * */
	public static Boolean isNumeric(String str){
		Boolean ret = false;
		int flag = 1;
		char[] chars = str.toCharArray();//将字符串转为字符数组，对每一个字符进行判断

		for(int i = 0 ; i < chars.length;i++){
			if(chars[i] < '0' || chars[i] > '9'){
				flag = -1;
				break;
			}
		}
		if(flag == 1){
			ret = true;
		}else{
			ret = false;
		}
		return ret;
	}

	/**
	 * 将时间转化为 yyyy-MM-dd 格式的字符串
	 * @param date 要转化的时间
	 * @return 转化后的字符串
	 * */
	public static String DateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		return sdf.format( date );
	}

	/**
	 * 判断字符串里是否含有中文
	 * @param str 需要判断的字符串
	 * @return 存在：true；不存在：false
	 * */
	public static Boolean containChinese(String str){
		Boolean ret =false;
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			ret =  true;
		}
		return ret;
	}

}
