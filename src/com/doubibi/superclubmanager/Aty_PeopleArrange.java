package com.doubibi.superclubmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class Aty_PeopleArrange extends Activity implements OnClickListener {

	ListView lvPeopleArrange;
	Adp_PeopleArrange adapter;
	private static ArrayList<String> checkedPeopleNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people_arrange);
		init();
	}

	private void init() {
		findViewById(R.id.btnAddPerson).setOnClickListener(this);
		findViewById(R.id.btnBackActivityEdit).setOnClickListener(this);
		lvPeopleArrange = (ListView) findViewById(R.id.lvPeopleArrange);
		checkedPeopleNum = new ArrayList<String>();
		refrefreshListView();
	}

	private void refrefreshListView() {
		adapter = new Adp_PeopleArrange(checkedPeopleNum, this);
		System.out.println("adapter的大小为："+adapter.getCount());
		System.out.println("checkedPeopleNum的大小为："+checkedPeopleNum.size());
		lvPeopleArrange.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAddPerson:
			Intent intent = new Intent(this, Aty_PeopleList.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.btnBackActivityEdit:
			finish();
			break;
		default:
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			checkedPeopleNum = data.getExtras().getStringArrayList("donePeopleList");
				
			refrefreshListView();
			break;

		default:
			break;
		}
	}
}
