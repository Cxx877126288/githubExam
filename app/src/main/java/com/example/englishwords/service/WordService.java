package com.example.englishwords.service;

import com.example.englishwords.pojo.Word;

import java.util.List;

/**对单词查询的服务
 * @author HX
 * @title: WordService
 * @projectName Words_System
 * @date 2019/7/1  10:58
 */
public interface WordService {
	List<Word> selAll();
	Word selOne(int id);
	List<Word> selByWord(String word);
}
