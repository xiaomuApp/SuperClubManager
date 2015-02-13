package com.doubibi.superclubmanager.main;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.doubibi.superclubmanager.R;
import com.doubibi.superclubmanager.adapter.Adp_PeopleList;
import com.doubibi.superclubmanager.db.DbControl;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Aty_PeopleList extends Activity implements OnClickListener, OnRefreshListener<ListView>, OnItemClickListener {

	private ImageView ivCheckBox;
	private PullToRefreshListView lvPeopleList;
	private Adp_PeopleList adapter;
	private ArrayList<String> checkedPeopleNum;
	public final static int resultCodeDonePeopleList = 1;
	public final static String EM_DONE = "donePeopleList";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people_list);
		init();
	}

	/*界面初始化，如果上个界面是已经存在勾选名单的则初始为勾选状态*/
	private void init() {
		findViewById(R.id.btnDonePeopleList).setOnClickListener(this);
		findViewById(R.id.btnBackPeopleArrange).setOnClickListener(this);
		lvPeopleList = (PullToRefreshListView) findViewById(R.id.lvPeopleList);
		lvPeopleList.setOnRefreshListener(this);
		lvPeopleList.setOnItemClickListener(this);
		
		adapter = new Adp_PeopleList(this, R.layout.list_cell_people_list, null, new String[]{}, new int[]{});
		lvPeopleList.setAdapter(adapter);
		
		refreshListView();
		checkedPeopleNum = getIntent().getStringArrayListExtra(Aty_PeopleArrange.EM_PEOPLE_NUM);
		if(checkedPeopleNum == null){
			checkedPeopleNum = new ArrayList<String>();
		}else{
				adapter.initView(checkedPeopleNum);
		}
	}
	
	/*刷新*/
	private void refreshListView() {
		Cursor c = DbControl.findUsersAll(this);
		adapter.changeCursor(c);
		adapter.notifyDataSetChanged();
		lvPeopleList.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDonePeopleList:
			Intent intent = new Intent();
			intent.putExtra(EM_DONE, checkedPeopleNum);
			setResult(resultCodeDonePeopleList, intent);
			DbControl.colseDbControl();
			finish();
			break;
		case R.id.btnBackPeopleArrange:
			DbControl.colseDbControl();
			finish();
			break;
		default:
			break;
		}
	}

	/*下拉刷新*/
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
	}
	
	/*该功能仅为测试才有*/
	static int i = 0;
	public void testAddPeople(View view){
		i++;
		boolean b = DbControl.insertUser(this, "同学"+i, "20132100"+i, "干事", "逗比比", "小木社团");
		if(b)System.out.println("添加人员");
		else System.out.println("*******");
		refreshListView();
	}

	/*该功能仅为测试才有*/
	public void testDeletePeople(View view){
		DbControl.deleteUsersAll(this);
		refreshListView();
	}

	
}
