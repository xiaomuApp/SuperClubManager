package com.doubibi.superclubmanager;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Aty_ActivityEdit extends Activity implements OnClickListener {

	private TextView tvAtyTime;
	private Calendar c;
	private EditText etAtyName, etAtyTheme, etAtyContext;
	private Db db;
	private SQLiteDatabase dbRead;
	private String atyId;
	public final static int resultCodeDoneAtyEdit = 0;
	private ArrayList<String> checkedPeopleNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_edit);
		init();
	}

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
		
		db =  new Db(this);
		dbRead = db.getReadableDatabase();
		atyId = this.getIntent().getStringExtra("atyId");
		if(atyId!=null){
			System.out.println("能得到数据"+atyId);
			Cursor c = dbRead.query("activities", null, "atyId=?", new String[]{atyId}, null, null, null);
			c.moveToNext();
			etAtyName.setText(c.getString(c.getColumnIndex("atyName")));
			etAtyTheme.setText(c.getString(c.getColumnIndex("atyTheme")));
			tvAtyTime.setText(c.getString(c.getColumnIndex("atyTime")));
			etAtyContext.setText(c.getString(c.getColumnIndex("atyContext")));
			Cursor cCheckedPeopleNum = dbRead.query("peopleArrange", null, "atyId=?", new String[]{atyId}, null, null, null);
//			Cursor cCheckedPeopleNum = dbRead.rawQuery("SELECT * FROM peopleArrange WHERE atyId=?", new String[]{atyId});
			while(cCheckedPeopleNum.moveToNext()){
				checkedPeopleNum.add(cCheckedPeopleNum.getString(cCheckedPeopleNum.getColumnIndex("userNum")));
			}
			System.out.println("获得的人员个数："+checkedPeopleNum.size());
			for(String s: checkedPeopleNum){
				System.out.println("学号为："+s);
			}
			System.out.println("人员安排的数据库有："+cCheckedPeopleNum.getCount());
		}else{
			System.out.println("不能得到数据");
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
			if(atyId!=null){
				intent.putExtra("checkedPeopleNum", checkedPeopleNum);
				System.out.println("向人员安排发送已选的人员学号");
			}else{
				System.out.println("没有已选的人员学号需要发送");
			}
			startActivityForResult(intent, 0);
			break;
		case R.id.btnRelease:
			releaseAty();
			break;
		default:
			break;
		}
		
	}

	private void releaseAty() {
		if(etAtyName.getText().toString().trim().length()<=1){
			Toast.makeText(this, "未填写活动主题，不能发布", Toast.LENGTH_SHORT).show();
		}else{
			Db db = new Db(this);
			SQLiteDatabase dbWrite = db.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("atyName", etAtyName.getText().toString());
			cv.put("atyTheme", etAtyTheme.getText().toString());
			cv.put("atyTime", tvAtyTime.getText().toString());
			cv.put("atyContext", etAtyContext.getText().toString());
			cv.put("atyRelease", true);
			if(atyId == null){
				cv.put("atyId", System.currentTimeMillis()+"");
				dbWrite.insert("activities", null, cv);
				Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
			}else{
				dbWrite.update("activities", cv, "atyId=?", new String[]{atyId});
				Toast.makeText(this, "此活动已经再次发布", Toast.LENGTH_SHORT).show();
			}
			dbWrite.close();
			setResult(resultCodeDoneAtyEdit);
			finish();
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case Aty_PeopleArrange.resultCodeDonePeopleArrage:
			checkedPeopleNum = data.getStringArrayListExtra("checkedPeopleNum");
			System.out.println("已经从人员安排获得人员学号：");
			for(String s:checkedPeopleNum){
				System.out.println(s);
			}
			break;
		default:
			break;
		}
	}

	private void saveAty() {
		if(etAtyName.getText().toString().trim().length()==0){
			Toast.makeText(this, "未填写活动主题，不能保存", Toast.LENGTH_SHORT).show();
		}else{
			Db db = new Db(this);
			SQLiteDatabase dbWrite = db.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("atyName", etAtyName.getText().toString());
			cv.put("atyTheme", etAtyTheme.getText().toString());
			cv.put("atyTime", tvAtyTime.getText().toString());
			cv.put("atyContext", etAtyContext.getText().toString());
			cv.put("atyRelease", false);
			ContentValues cvPeopleArrange = new ContentValues();
			String atyIdTemp;
			atyIdTemp = atyId;
			if(atyId==null){
				atyId = new String(System.currentTimeMillis()+"");
			}
			if(atyIdTemp == null){
				cv.put("atyId", System.currentTimeMillis()+"");
				dbWrite.insert("activities", null, cv);
				for(String userNum: checkedPeopleNum){
					cvPeopleArrange.put("userNum", userNum);
					cvPeopleArrange.put("atyId", atyId);
					dbWrite.insert("peopleArrange", null, cvPeopleArrange);
				}
				Toast.makeText(this, "本地保存成功", Toast.LENGTH_SHORT).show();
			}else{
				dbWrite.delete("peopleArrange", "atyId=?", new String[]{atyId});
				for(String userNum: checkedPeopleNum){
					cvPeopleArrange.put("userNum", userNum);
					cvPeopleArrange.put("atyId", atyId);
					dbWrite.insert("peopleArrange", null, cvPeopleArrange);
				}
				dbWrite.update("activities", cv, "atyId=?", new String[]{atyId});
				Toast.makeText(this, "本地数据更新成功", Toast.LENGTH_SHORT).show();
			}
			dbWrite.close();
			setResult(resultCodeDoneAtyEdit);
			finish();
		}
	}
	
	

	private void showDatePicker() {
		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				tvAtyTime.setText(String.format("%d-%d-%d", year, monthOfYear+1, dayOfMonth));
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
	}
}
