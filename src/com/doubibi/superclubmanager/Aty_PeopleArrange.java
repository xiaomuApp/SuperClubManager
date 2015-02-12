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
	private ArrayList<String> checkedPeopleNum;
	public final static int resultCodeDonePeopleArrage = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people_arrange);
		init();
	}

	private void init() {
		findViewById(R.id.btnDonePeopleArrage).setOnClickListener(this);
		findViewById(R.id.btnBackActivityEdit).setOnClickListener(this);
		findViewById(R.id.btnAddPerson).setOnClickListener(this);
		lvPeopleArrange = (ListView) findViewById(R.id.lvPeopleArrange);
		checkedPeopleNum = new ArrayList<String>();
		if(getIntent().getStringArrayListExtra("checkedPeopleNum")!=null){
			checkedPeopleNum = getIntent().getStringArrayListExtra("checkedPeopleNum");
			System.out.println("从活动编辑获得checkedPeopleNum的个数："+checkedPeopleNum.size());
		}else{
			System.out.println("从活动编辑不能获得数据");
		}
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
			intent.putExtra("checkedPeopleNum", checkedPeopleNum);
			startActivityForResult(intent, 0);
			break;
		case R.id.btnBackActivityEdit:
			finish();
			break;
		case R.id.btnDonePeopleArrage:
			Intent iCheckedPeopleList = new Intent();
			iCheckedPeopleList.putExtra("checkedPeopleNum", checkedPeopleNum);
			setResult(resultCodeDonePeopleArrage, iCheckedPeopleList);
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
		case Aty_PeopleList.resultCodeDonePeopleList:
			checkedPeopleNum = data.getExtras().getStringArrayList("donePeopleList");
			System.out.println("执行人员列表的返回人员选择数据");
			refrefreshListView();
			break;

		default:
			System.out.println("不能执行人员列表的返回数据");
			break;
		}
	}
}
