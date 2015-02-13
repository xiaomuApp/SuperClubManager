package com.doubibi.superclubmanager.main;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.doubibi.superclubmanager.R;
import com.doubibi.superclubmanager.adapter.Adp_ActivityList;
import com.doubibi.superclubmanager.db.DbControl;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Aty_ActivityList extends Activity implements OnClickListener, OnRefreshListener<ListView>, OnItemClickListener {
	
	private PullToRefreshListView lvAtyList;
	private Adp_ActivityList adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_list);
		init();
	}
	/*界面初始化，从本地数据库获得活动信息*/
	private void init() {
		findViewById(R.id.btnBackMain).setOnClickListener(this);
		findViewById(R.id.btnNewEvent).setOnClickListener(this);
		lvAtyList = (PullToRefreshListView) findViewById(R.id.lvAtyList);
		lvAtyList.setOnRefreshListener(this);
		
		adapter = new Adp_ActivityList(this, R.layout.list_cell_activity_list, null, new String[]{}, new int[]{});
		
		lvAtyList.setAdapter(adapter);
		lvAtyList.setOnItemClickListener(this);
		
		refreshListView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBackMain:
			DbControl.colseDbControl();
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
	/*下拉刷新，异步处理同时可以获得服务器的数据*/
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
	/*刷新界面*/
	private void refreshListView() {
		Cursor c = DbControl.findActivitiesAll(this);
		adapter.changeCursor(c);
		adapter.notifyDataSetChanged();
	}
	/*列表项的单机事件，传递活动ID到编辑活动页面*/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position,
			long id) {
		Cursor c = adapter.getCursor();
		c.moveToPosition(position-1);
		Intent i = new Intent(this, Aty_ActivityEdit.class);
		i.putExtra("atyId", c.getString(c.getColumnIndex("atyId")));
		startActivity(i);
	}
	/*清空所有的本地活动和人员安排信息*/
	public void btnClearAtyList(View view){
		DbControl.deleteActivitiesAll(this);
		DbControl.deletePeopleArrangeAll(this);
		refreshListView();
	}
}
