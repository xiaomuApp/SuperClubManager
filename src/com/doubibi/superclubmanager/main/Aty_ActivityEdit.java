package com.doubibi.superclubmanager.main;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doubibi.superclubmanager.R;
import com.doubibi.superclubmanager.db.DbControl;

public class Aty_ActivityEdit extends Activity implements OnClickListener {

	private TextView tvAtyTime;
	private Calendar c;
	private EditText etAtyName, etAtyTheme, etAtyContext;
	private String atyId;
	public final static int resultCodeDoneAtyEdit = 0;
	private ArrayList<String> checkedPeopleNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_edit);
		init();
	}
	
	/*界面初始化操作，判断是否新建的活动，否则根据获得的atyId来进行初始化操作*/
	private void init() {
		tvAtyTime = (TextView) findViewById(R.id.tvAtyDate);
		tvAtyTime.setOnClickListener(this);
		c = Calendar.getInstance(); 
		findViewById(R.id.btnBackAtyList).setOnClickListener(this);
		findViewById(R.id.btnSave).setOnClickListener(this);
		findViewById(R.id.btnPeopleArrange).setOnClickListener(this);
		findViewById(R.id.btnRelease).setOnClickListener(this);
		etAtyName = (EditText) findViewById(R.id.etAtyName);
		etAtyTheme = (EditText) findViewById(R.id.etAtyTheme);
		tvAtyTime = (TextView) findViewById(R.id.tvAtyDate);
		etAtyContext = (EditText) findViewById(R.id.etAtyContext);

		checkedPeopleNum = new ArrayList<String>();

		atyId = this.getIntent().getStringExtra("atyId");
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
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvAtyDate:
			showDatePicker();
			break;
		case R.id.btnBackAtyList:
			setResult(resultCodeDoneAtyEdit);
			finish();
			break;
		case R.id.btnSave:
			saveAty();
			break;
		case R.id.btnPeopleArrange:
			Intent intent = new Intent(this, Aty_PeopleArrange.class);
			intent.putExtra("atyName", etAtyName.getText().toString());
			intent.putExtra("checkedPeopleNum", checkedPeopleNum);
			startActivityForResult(intent, 0);
			break;
		case R.id.btnRelease:
			releaseAty();
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
			checkedPeopleNum = data.getStringArrayListExtra("checkedPeopleNum");
			break;
		default:
			break;
		}
	}
	
	/*发布任务操作*/
	private void releaseAty() {
		boolean success = saveData(true);
		if(success)
			Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(this, "发布失败", Toast.LENGTH_SHORT).show();
		setResult(resultCodeDoneAtyEdit);
		finish();
	}



	/*保存任务操作*/
	private void saveAty() {
			boolean success = saveData(false);
			if(success)
				Toast.makeText(this, "本地保存成功", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(this, "本地保存失败", Toast.LENGTH_SHORT).show();
			setResult(resultCodeDoneAtyEdit);
			finish();
		
	}

	/*保存数据操作*/
	private boolean saveData(boolean release){
		boolean success = false;
		if(etAtyName.getText().toString().trim().length()<=1){
			Toast.makeText(this, "未填写活动主题", Toast.LENGTH_SHORT).show();
		}else{
			String atyName = etAtyName.getText().toString();
			String atyTheme = etAtyTheme.getText().toString();
			String atyTime = tvAtyTime.getText().toString();
			String atyContext = etAtyContext.getText().toString();
			boolean atyRelease = release;
			if(atyId == null){
				atyId = System.currentTimeMillis()+"";
				success = DbControl.insertActivity(this, atyId, atyName, atyTheme, atyTime, atyContext, atyRelease);
			}else{
				success = DbControl.updateActivity(this, atyId, atyName, atyTheme, atyTime, atyContext, atyRelease);
			}
			if(success)success = DbControl.insertPeopleArrange(this, atyId, checkedPeopleNum);
		}
		return success;
	}
	
	/*时间选择器*/
	int currentYear, currentMonth, currentDay;
	private void showDatePicker() {
		if(atyId==null){
			currentYear = c.get(Calendar.YEAR);
			currentMonth = c.get(Calendar.MONTH);
			currentDay = c.get(Calendar.DAY_OF_MONTH);
		}else{
			String []t = tvAtyTime.getText().toString().split("-");
			currentYear = Integer.parseInt(t[0]);
			currentMonth = Integer.parseInt(t[1]);
			currentDay = Integer.parseInt(t[2]);
		}
		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				tvAtyTime.setText(String.format("%d-%d-%d", year, monthOfYear+1, dayOfMonth));
			}
		}, currentYear, currentMonth, currentDay).show();
	}
}
