package com.doubibi.superclubmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Aty_PeopleList extends Activity implements OnClickListener, OnRefreshListener<ListView>, OnItemClickListener {

	private ImageView ivCheckBox;
	private PullToRefreshListView lvPeopleList;
	private Db db;
	private SQLiteDatabase dbRead, dbWrite;
	private Adp_PeopleList adapter;
	private ArrayList<String> checkedPeopleNum;
	public final static int resultCodeDonePeopleList = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people_list);
		
		init();
		
	}

	private void init() {
		findViewById(R.id.btnDonePeopleList).setOnClickListener(this);
		findViewById(R.id.btnBackPeopleArrange).setOnClickListener(this);
		lvPeopleList = (PullToRefreshListView) findViewById(R.id.lvPeopleList);
		lvPeopleList.setOnRefreshListener(this);
		lvPeopleList.setOnItemClickListener(this);
		db = new Db(this);
		dbRead = db.getReadableDatabase();
		dbWrite = db.getWritableDatabase();
		
		adapter = new Adp_PeopleList(this, R.layout.list_cell_people_list, null, new String[]{}, new int[]{});
		lvPeopleList.setAdapter(adapter);
		
		checkedPeopleNum = new ArrayList<String>();
		
		refreshListView();
	}

	private void refreshListView() {
		Cursor c = dbRead.query("users", null, null, null, null, null, "userName");
		adapter.changeCursor(c);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDonePeopleList:
			Intent intent = new Intent();
			intent.putExtra("donePeopleList", checkedPeopleNum);
			setResult(0, intent);
			dbWrite.close();
			dbRead.close();
			finish();
			break;
		case R.id.btnBackPeopleArrange:
			dbWrite.close();
			dbRead.close();
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				refreshListView();
				lvPeopleList.onRefreshComplete();
			}
			
		}.execute();
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position,
			long id) {
		Cursor c = adapter.getCursor();
		c.moveToPosition(position-1);
		ivCheckBox = (ImageView) view.findViewById(R.id.ivUserCheckBox);
		if(view.getTag()==null){
			view.setTag(true);
			checkedPeopleNum.add(c.getString(c.getColumnIndex("userNum")));
			ivCheckBox.setImageResource(R.drawable.ic_cb_done);
		}else{
			view.setTag(null);
			checkedPeopleNum.remove(c.getString(c.getColumnIndex("userNum")));
			ivCheckBox.setImageResource(R.drawable.ic_cb_normal);
		}
//	int itemId = c.getInt(c.getColumnIndex("_id"));
//	dbWrite.delete("users", "_id=?", new String[]{itemId+""});
//	refreshListView();
		
	}
	
	static int i = 0;
	public void testAddPeople(View view){
		
		ContentValues cv = new ContentValues();
		cv.put("userName", "同学"+i);
		cv.put("userNum", i+"");
		if(i%4!=0)
			cv.put("userPosition", "干事");
		else
			cv.put("userPosition", "部长");
		cv.put("userDepartment", "神秘部门");
		cv.put("userClub", "神奇俱乐部");
		i++;
		
		dbWrite.insert("users", null, cv);
		
		refreshListView();
	}

	public void testDeletePeople(View view){
//		Cursor c = adapter.getCursor();
		dbWrite.delete("users", null, null);
		refreshListView();
	}

	
}
