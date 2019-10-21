package com.example.englishwords.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.example.englishwords.R;
import com.example.englishwords.pojo.Choice;
import com.example.englishwords.pojo.Thesaurus;
import com.example.englishwords.pojo.Word;
import com.example.englishwords.service.ChoiceService;
import com.example.englishwords.service.ThesaurusService;
import com.example.englishwords.service.WordService;
import com.example.englishwords.service.impl.ChoiceServiveImpl;
import com.example.englishwords.service.impl.ThesaurusServiceImpl;
import com.example.englishwords.service.impl.WordServiceImpl;
import com.example.englishwords.util.ExpandUtil;
import com.example.englishwords.util.fileOperator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author HX
 * @title: EnglishChooseChinese
 * @projectName Words_System
 * @date 2019/9/11  8:35
 * 根据英语选择释义的界面
 */
public class EnglishChooseChinese extends AppCompatActivity {
	private Context context;    //上下文参数
	private List<Word> words = new ArrayList<>(  );    //获取的单词列表 用来显示
	private WordService ws = new WordServiceImpl();  //通过单词的ID  来查询每一个单词显示
	private List<Integer> appearID;    //用来存储显示的单词ID，随机显示的时候用
	private ChoiceService cs = new ChoiceServiveImpl();   //用来查询每日单词数目

	private int[] anotherWordsID;    //用来显示其他答案的单词ID
	private int testWordsID;   //真正的测试ID
	private int finishiCount = 0;  //判断是否已复习完成
	private int count = 1;   //用来显示已测试单词数量信息
	private Boolean finishFlag = false;   //判断是否已经结束
	private final String FinishTiShi = "测试结束，小伙子给力嗷";   //结束提示语
	private List<Integer> totalWordsId;     //该书所有的ID

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.englishchoosechinese );
		context = this;
		loadParameter();
		loadpage();
	}

	/**
	 * 初始化参数方法
	 * */
	public void loadParameter(){
		Intent intent = getIntent();
		//获取要选择的测试日期
		String time = intent.getStringExtra( "time" );
		//获取该日期的所有的单词ID
		List<Integer> wordsids = fileOperator.getWordsIDByTime( context, time );
		for(int i = 0;i < wordsids.size();i++){
			//为测试单词列表  填入信息
			Word word = ws.selOne( wordsids.get( i ) );
			words.add( word );
		}
//获取书本的单词总数
		Choice choice = cs.selOne( context );
		//初始化已出现单词列表
		appearID = new ArrayList<>(  );
		//初始化所有单词ID列表
		totalWordsId = new ArrayList<>(  );
		//随机生成一个单词
		InputStream is = context.getResources().openRawResource( choice.getBookID() );
		BufferedReader br = new BufferedReader( new InputStreamReader( is ) );
		String str = "";
		try {
			while ((str = br.readLine()) != null ){
				totalWordsId.add( Integer.parseInt( str ) );
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 页面初始化方法
	 * */
	private void loadpage() {
		RadioGroup rg = findViewById( R.id.ecc_mean_group );
		testWordsID = getTestWordsID();
		if(testWordsID > 0){
			//如果返回的不为0  说明还有要测试的单词
			anotherWordsID = getAnotherWordsID( testWordsID );
			Random random = new Random(  );
			//正确答案放在哪个选项
			int correctChoice = ExpandUtil.randomTimes( 4 );
			//获取单选选项组
			List<RadioButton> rbs = new ArrayList<>(  );
			rbs.add( (RadioButton)rg.findViewById( R.id.choose_a ) );
			rbs.add( (RadioButton)rg.findViewById( R.id.choose_b ) );
			rbs.add( (RadioButton)rg.findViewById( R.id.choose_c ) );
			rbs.add( (RadioButton)rg.findViewById( R.id.choose_d ) );
			int index = 0;   //错误单词列表的数组下标
			TextView ecc_count = findViewById( R.id.ecc_count );
			ecc_count.setText( "测试进度：" + count + "/" + words.size() );
			for(int i = 0 ; i < rbs.size();i++){
				rbs.get( i ).setChecked( false );
				Word word = null;
				if(correctChoice == i ){
					//下标与随机出来正确答案位置相同 就将释义填入
					word = ws.selOne( testWordsID );
					rbs.get( i ).setText( word.getMean_cn() );
					TextView tv = findViewById( R.id.ecc_word );
					tv.setText( word.getWord() );
				}else {
					word =  (ws.selOne( anotherWordsID[index] ));
					rbs.get( i ).setText(word.getMean_cn() );
					index++;
				}
			}
		}else {
			TextView tv = findViewById( R.id.ecc_word );
			tv.setText( FinishTiShi );
			RadioGroup ecc_mean_group = findViewById( R.id.ecc_mean_group );
			ecc_mean_group.setVisibility( View.INVISIBLE );
			Button ecc_btn = findViewById( R.id.ecc_btn );
			ecc_btn.setText( "退出测试" );

			finishFlag = true;
		}
	}

	/**
	 * 判断某个单词是否已经测试过
	 * @param id 即将要显示的ID
	 * @return 已测试：true；未测试：false
	 * */
	public Boolean haveTest(int id){
		Boolean ret = false;
		for(int i = 0 ;i <appearID.size();i++){
			if(appearID.get( i ) == id){
				ret = true;
				break;
			}
		}
		return ret;
	}

	/**
	 * 随机获得一个单词的ID
	 * @return 要测试的ID
	 * */
	public int getTestWordsID(){
		int id = 0;
		if (finishiCount < words.size()) {
			//已背诵个数小于要测试单词个数，就开始随机
			int index = ExpandUtil.randomTimes( words.size() );
			id = words.get( index ).getTopic_id();
			while (haveTest( id )) {
				//如果出现过  就再次随机
				index = ExpandUtil.randomTimes( words.size() );
				id = words.get( index ).getTopic_id();
			}
			//向已出现的ID添加该ID
			appearID.add( id );
			//已背诵个数+1
			finishiCount++;
		}
		//已经全部测试就返回0

		return id;
	}

	/**
	 * 获得其他错误答案的ID数组
	 * @param testID 即将要测试的ID
	 * @return 其余错误答案的ID数组
	 * */
	public int[] getAnotherWordsID(int testID){

		int[] anotherWordsID = new int[3];
		int count = 0;
		while (count < 3){
			Boolean addFlag = true;
			int wordId = totalWordsId.get( ExpandUtil.randomTimes( totalWordsId.size() ) );

			if(testID == wordId){
				//先判断生成的  与 要测试的是否相同  相同就设标志为flag
				addFlag = false;
			}
			if(addFlag){   //如果生成的与测试的不同，再判断是否已经生成过
				for(int i = 0 ; i < count;i++){
					//如果生成出来的 与之前的相同  就重新生成
					if(anotherWordsID[i] == wordId )
						addFlag = false;
				}
			}

			if(addFlag){
				//如果都成立，就加入错误释义数组，并将count后移一位
				anotherWordsID[count] = wordId;
				count++;
			}
		}
		//获取单词的ID,并返回
		return anotherWordsID;
	}

	/**
	 * 确定按钮
	 * */
	public void SureAnswer(View view) {
		if (finishFlag) {
			//如果结束标志为true  就将按钮设为退出测试，方法也改为退出
			Back( view );
		} else {
			RadioGroup rg = findViewById( R.id.ecc_mean_group );
			RadioButton rb = rg.findViewById( rg.getCheckedRadioButtonId() );
			if (rb != null) {
				Word word = ws.selOne( testWordsID );
				if (word.getMean_cn().equals( rb.getText().toString() )) {
					count++;
					loadpage();
				} else {
					Toast.makeText( context, "选择有误，请三思", Toast.LENGTH_LONG ).show();
				}
			}
		}
	}

	/**
	 * 结束返回
	 * */
	public void Back(View view){
		finish();
	}

	/**
	 * 真机返回按钮
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown( keyCode, event );
	}


}
