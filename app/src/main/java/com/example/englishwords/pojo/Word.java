package com.example.englishwords.pojo;

/**
 * @author HX
 * @title: twords
 * @projectName Words_System
 * @date 2019/7/1  10:36
 */
public class Word {
	private int topic_id;   //单词ID
	private String word;   //单词
	private String accent;  //发音
	private String mean_cn;  //释义
	private double freq;  //重要程度
	private int word_length;   //单词长度

	public int getTopic_id() {
		return topic_id;
	}

	public void setTopic_id(int topic_id) {
		this.topic_id = topic_id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getAccent() {
		return accent;
	}

	public void setAccent(String accent) {
		this.accent = accent;
	}

	public String getMean_cn() {
		return mean_cn;
	}

	public void setMean_cn(String mean_cn) {
		this.mean_cn = mean_cn;
	}

	public double getFreq() {
		return freq;
	}

	public void setFreq(double freq) {
		this.freq = freq;
	}

	public int getWord_length() {
		return word_length;
	}

	public void setWord_length(int word_length) {
		this.word_length = word_length;
	}

	@Override
	public String toString() {
		return "Word{" +
				"topic_id=" + topic_id +
				", word='" + word + '\'' +
				", accent='" + accent + '\'' +
				", mean_cn='" + mean_cn + '\'' +
				", freq=" + freq +
				", word_length=" + word_length +
				'}';
	}
}
