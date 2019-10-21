package com.example.englishwords.util;

import java.util.List;
import java.util.Random;

/**
 * @author HX
 * @title: ExpandUtil
 * @projectName Words_System
 * @date 2019/9/6  11:32
 */
public class ExpandUtil {

	/**
	 * 对随机进行多次随机  来生成奇偶数 减小重复性
	 * @param number 要随机的范围
	 * @return 0：:奇数；1：偶数
	 * */
	public static int NumberExpand(int number){
		int ret = -1;
		Random random = new Random(  );
		for(int i = 0;i < 3;i++){
			number = random.nextInt( number );
		}
		if(number % 2 != 0){
			//奇数
			ret = 0;
		}else{
			ret = 1;
		}
		return ret;
	}

	/**
	 * 多次随机  减小重复性；
	 * @param listCount 范围数字
	 * */
	public static int randomTimes(int listCount){
		Random random = new Random(  );
		int ret = 0;
		for(int i = 0 ;i < 3;i++){
			ret = random.nextInt(listCount);
		}
		return ret;
	}

	/**
	 * 下标在某个下标列表里是否存在
	 * @param index 生成的下标
	 * @param indexs 存储下标的列表
	 * @return 存在：true；不存在：false
	 * */
	public static Boolean hasIndex(int index, List<Integer> indexs){
		Boolean ret = false;
		for(int i = 0;i < indexs.size();i++){
			if(index == indexs.get( i )){
				ret = true;
				break;
			}
		}
		return ret;
	}

	/**
	 * 判断某个单词列表里是否已经存在某个单词
	 * @param wordsid 要添加的单词ID
	 * @param list 已经添加的单词ID
	 * @return 存在：true；不存在：false
	 * */
	public static Boolean hasWordsId(int wordsid, List<Integer> list){
		Boolean ret = false;
		for(int i = 0;i < list.size();i++){
			if(wordsid == list.get( i )){
				ret = true;
				break;
			}
		}
		return ret;
	}

}
