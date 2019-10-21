package com.example.englishwords.service;

import android.content.Context;
import com.example.englishwords.pojo.Thesaurus;

import java.util.List;

/**
 * @author HX
 * @title: ThesaurusService
 * @projectName Words_System
 * @date 2019/9/3  9:58
 * 对单词本的服务类
 */
public interface ThesaurusService {
	List<String> selAllBookName(Context context);
	List<Thesaurus> selAllThesaurus(Context context);
	Thesaurus selByThesaurusID(Context context,int _id);
}
