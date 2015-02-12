package com.doubibi.superclubmanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Aty_ActivityList extends Activity implements OnClickListener, OnRefreshListener<ListView>, OnItemClickListener {
	
	private PullToRefreshListView lvAtyList;
	private Adp_ActivityList adapter;
	private Db db;
	private SQLiteDatabase dbRead;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_list);
		init();
	}
	
	private void init() {
		findViewById(R.id.btnBackMain).setOnClickListener(this);
		findViewById(R.id.btnNewEvent).setOnClickListener(this);
		lvAtyList = (PullToRefreshListView) findViewById(R.id.lvAtyList);
		
		lvAtyList.setOnRefreshListener(this);
		
		db = new Db(this);
		dbRead = db.getReadableDatabase();
		
		adapter = new Adp_ActivityList(this, R.layout.list_cell_activity_list, null, new String[]{}, new int[]{});
		
		lvAtyList.setAdapter(adapter);
		lvAtyList.setOnItemClickListener(this);
		
		refreshListView();
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBackMain:
			finish();
			break;
		case R.id.btnNewEvent:
			Intent intent = new Intent(this, Aty_ActivityEdit.class);
			startActivityForResult(intent, 0);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case Aty_ActivityEdit.resultCodeDoneAtyEdit:
			refreshListView();
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
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {

				refreshListView();
				lvAtyList.onRefreshComplete();
			}
			
		}.execute();
		
	}
	
	private void refreshListView() {
		Cursor c = dbRead.query("activities", null, null, null, null, null, "atyTime");
		adapter.changeCursor(c);
		adapter.notifyDataSetChanged();
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position,
			long id) {
		Cursor c = adapter.getCursor();
		c.moveToPosition(position-1);
		Intent i = new Intent(this, Aty_ActivityEdit.class);
		System.out.println(c.getString(c.getColumnIndex("atyName")));
		i.putExtra("atyId", c.getString(c.getColumnIndex("atyId")));
		startActivity(i);
		
	}
	
	public void btnClearAtyList(View view){
		dbRead.delete("activities", null, null);
		dbRead.delete("peopleArrange", null, null);
		refreshListView();
	}
}
