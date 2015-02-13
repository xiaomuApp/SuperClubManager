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
	public final static String EM_PEOPLE_NUM = "checkedPeopleNum";
	public final static String EM_DONE = "checkedPeopleNum";
	
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
		((TextView) findViewById(R.id.tvAtyNamePeopleArrange)).setText(getIntent().getStringExtra(Aty_ActivityEdit.EM_ATY_NAME));
		lvPeopleArrange = (ListView) findViewById(R.id.lvPeopleArrange);
		checkedPeopleNum = new ArrayList<String>();
		if(getIntent().getStringArrayListExtra(Aty_ActivityEdit.EM_PEOPLE_NUM)!=null){
			checkedPeopleNum = getIntent().getStringArrayListExtra(Aty_ActivityEdit.EM_PEOPLE_NUM);
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
			intent.putExtra(EM_PEOPLE_NUM, checkedPeopleNum);
			startActivityForResult(intent, 0);
			break;
		case R.id.btnBackActivityEdit:
			finish();
			break;
		case R.id.btnDonePeopleArrage:
			Intent iCheckedPeopleList = new Intent();
			iCheckedPeopleList.putExtra(EM_DONE, checkedPeopleNum);
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
			checkedPeopleNum = data.getExtras().getStringArrayList(Aty_PeopleList.EM_DONE);
			refrefreshListView();
			break;
		default:
			break;
		}
	}
}
