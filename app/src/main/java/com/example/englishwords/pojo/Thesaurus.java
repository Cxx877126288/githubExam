package com.example.englishwords.pojo;

/**
 * @author HX
 * @title: Thesaurus
 * @projectName Words_System
 * @date 2019/9/3  9:51
 */
public class Thesaurus {
	private int thesaurus_ID;     //书本ID
	private String thesaurus_Name;  //书本名称
	private int thesaurus_Count;   //单词总数
	private int BookID;    //系统中文件

	public int getBookID() {
		return BookID;
	}

	public void setBookID(int bookID) {
		BookID = bookID;
	}

	public int getThesaurus_ID() {
		return thesaurus_ID;
	}

	public void setThesaurus_ID(int thesaurus_ID) {
		this.thesaurus_ID = thesaurus_ID;
	}

	public String getThesaurus_Name() {
		return thesaurus_Name;
	}

	public void setThesaurus_Name(String thesaurus_Name) {
		this.thesaurus_Name = thesaurus_Name;
	}

	public int getThesaurus_Count() {
		return thesaurus_Count;
	}

	public void setThesaurus_Count(int thesaurus_Count) {
		this.thesaurus_Count = thesaurus_Count;
	}
}
