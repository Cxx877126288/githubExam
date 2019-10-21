package com.example.englishwords.pojo;

/**
 * @author HX
 * @title: Choice
 * @projectName Words_System
 * @date 2019/9/4  8:42
 */
public class Choice {
	private int Thesaurus_id;    //书本id
	private int perwords;     //每日背单词数
	private int BookID;       //该书在系统中的ID

	public int getThesaurus_id() {
		return Thesaurus_id;
	}

	public void setThesaurus_id(int thesaurus_id) {
		Thesaurus_id = thesaurus_id;
	}

	public int getPerwords() {
		return perwords;
	}

	public void setPerwords(int perwords) {
		this.perwords = perwords;
	}

	public int getBookID() {
		return BookID;
	}

	public void setBookID(int bookID) {
		BookID = bookID;
	}
}
