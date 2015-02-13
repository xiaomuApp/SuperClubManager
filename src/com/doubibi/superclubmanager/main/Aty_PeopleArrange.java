package com.doubibi.superclubmanager.main;

import java.util.ArrayList;

import com.doubibi.superclubmanager.R;
import com.doubibi.superclubmanager.adapter.Adp_PeopleArrange;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

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
		((TextView) findViewById(R.id.tvAtyNamePeopleArrange)).setText(getIntent().getStringExtra("atyName"));
		lvPeopleArrange = (ListView) findViewById(R.id.lvPeopleArrange);
		checkedPeopleNum = new ArrayList<String>();
		if(getIntent().getStringArrayListExtra("checkedPeopleNum")!=null){
			checkedPeopleNum = getIntent().getStringArrayListExtra("checkedPeopleNum");
		}
		refrefreshListView();
	}

	private void refrefreshListView() {
		adapter = new Adp_PeopleArrange(checkedPeopleNum, this);
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
			refrefreshListView();
			break;
		default:
			break;
		}
	}
}
