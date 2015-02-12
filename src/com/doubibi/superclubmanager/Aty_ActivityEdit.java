package com.doubibi.superclubmanager;

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
	private static int atyId;
	
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
		atyId = -1;
		
		db =  new Db(this);
		dbRead = db.getReadableDatabase();
		if((this.getIntent().getIntExtra("atyId", -1))!=-1){
			atyId = this.getIntent().getIntExtra("atyId", -1);
			System.out.println("能得到数据"+atyId);
			Cursor c = dbRead.query("activities", null, "_id=?", new String[]{atyId+""}, null, null, null);
			c.moveToNext();
			etAtyName.setText(c.getString(c.getColumnIndex("atyName")));
			etAtyTheme.setText(c.getString(c.getColumnIndex("atyTheme")));
			tvAtyTime.setText(c.getString(c.getColumnIndex("atyTime")));
			etAtyContext.setText(c.getString(c.getColumnIndex("atyContext")));
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
			setResult(1);
			finish();
			break;
		case R.id.btnSave:
			saveAty();
			break;
		case R.id.btnPeopleArrange:
			Intent intent = new Intent(this, Aty_PeopleArrange.class);
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
			if(atyId == -1){
				dbWrite.insert("activities", null, cv);
				Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
			}else{
				dbWrite.update("activities", cv, "_id=?", new String[]{atyId+""});
				Toast.makeText(this, "此活动已经再次发布", Toast.LENGTH_SHORT).show();
			}
			dbWrite.close();
			setResult(1);
			finish();
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
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
			if(atyId == -1){
				dbWrite.insert("activities", null, cv);
				Toast.makeText(this, "本地保存成功", Toast.LENGTH_SHORT).show();
			}else{
				dbWrite.update("activities", cv, "_id=?", new String[]{atyId+""});
				Toast.makeText(this, "本地数据更新成功", Toast.LENGTH_SHORT).show();
			}
			dbWrite.close();
			setResult(1);
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
