package com.doubibi.superclubmanager.main;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.doubibi.superclubmanager.R;
import com.doubibi.superclubmanager.db.DbControl;

public class Aty_PerformerMyTask extends Activity implements OnClickListener {

	private TextView tvAtyTime;
	private Calendar c;
	private EditText etAtyName, etAtyTheme, etAtyContext,etAtypeople;
	private String atyId;
	public final static int resultCodeDoneAtyEdit = 0;
	public final static String EM_PEOPLE_NUM = "checkedPeopleNum";
	public final static String EM_ATY_NAME = "atyName";
	private ArrayList<String> checkedPeopleNum;
	private String checkedPeople="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_performer_assignment);
		init();
	}
	
	/*界面初始化操作，判断是否新建的活动，否则根据获得的atyId来进行初始化操作*/
	private void init() {
		tvAtyTime = (TextView) findViewById(R.id.tvAtyDate);
		tvAtyTime.setOnClickListener(this);
		c = Calendar.getInstance(); 
		findViewById(R.id.btnBackTaskList).setOnClickListener(this);

		etAtyName = (EditText) findViewById(R.id.etAtyName);
		etAtyTheme = (EditText) findViewById(R.id.etAtyTheme);
		tvAtyTime = (TextView) findViewById(R.id.tvAtyDate);
		etAtyContext = (EditText) findViewById(R.id.etAtyContext);
		etAtypeople=(EditText) findViewById(R.id.etAtypeople);

		checkedPeopleNum = new ArrayList<String>();

		atyId = this.getIntent().getStringExtra(Aty_PerformerTaskList.EM_ATY_ID);
		if(atyId!=null){
			Cursor c = DbControl.findActivityById(this, atyId);
			if(c!=null){
				etAtyName.setText(c.getString(c.getColumnIndex("atyName")));
				etAtyTheme.setText(c.getString(c.getColumnIndex("atyTheme")));
				tvAtyTime.setText(c.getString(c.getColumnIndex("atyTime")));
				etAtyContext.setText(c.getString(c.getColumnIndex("atyContext")));
			}
			Cursor cCheckedPeopleNum = DbControl.findPeopleArrangeByatyId(this, atyId);
			if(cCheckedPeopleNum!=null){
				do{
					checkedPeopleNum.add(cCheckedPeopleNum.getString(cCheckedPeopleNum.getColumnIndex("userNum")));
				}while(cCheckedPeopleNum.moveToNext());
			}
			for (int i = 0; i < checkedPeopleNum.size(); i++) {
				
				Cursor cCheckedPeople=DbControl.findUserByNum(this, checkedPeopleNum.get(i));
				if (cCheckedPeople!=null) {
					checkedPeople+=cCheckedPeople.getString(cCheckedPeople.getColumnIndex("userName"))+",";
				}
			}

			if (checkedPeople!=null) {
				etAtypeople.setText(checkedPeople);
			}
				

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnBackTaskList:
			setResult(resultCodeDoneAtyEdit);
			finish();
			break;



		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case Aty_PeopleArrange.resultCodeDonePeopleArrage:
			checkedPeopleNum = data.getStringArrayListExtra(Aty_PeopleArrange.EM_DONE);
			break;
		default:
			break;
		}
	}
	



	
}
