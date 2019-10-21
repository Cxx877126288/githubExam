package com.example.englishwords.service;

import android.content.Context;
import com.example.englishwords.pojo.Choice;

/**
 * @author HX
 * @title: ChoiceService
 * @projectName Words_System
 * @date 2019/9/4  8:43
 * 对选择的书本的服务类
 */
public interface ChoiceService {

	/**
	 * 因为选择就一个；只需要查询一个
	 * */
	Choice selOne(Context context);
}
