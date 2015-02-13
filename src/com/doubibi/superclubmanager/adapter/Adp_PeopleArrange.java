package com.doubibi.superclubmanager.adapter;

import java.util.ArrayList;

import com.doubibi.superclubmanager.R;
import com.doubibi.superclubmanager.db.Db;
import com.doubibi.superclubmanager.db.DbControl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adp_PeopleArrange extends BaseAdapter {

	
	private ArrayList<String> list;
	private Context context;
	Db db;
	SQLiteDatabase dbRead;
	
	public Adp_PeopleArrange(ArrayList<String> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		for(int i = 0; i<list.size(); i++){
			System.out.println(list.get(i));
		}
	}

	public Context getContext() {
		return context;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public String getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams") @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_people_arrange, null);
		}
		db = new Db(getContext());
		dbRead = db.getReadableDatabase();
		Cursor c = DbControl.findUserByNum(getContext(), list.get(position));
		if(c!=null){
			TextView tvUserDepartmentName = (TextView) convertView.findViewById(R.id.tvUserDepartmentName);
			tvUserDepartmentName.setText(c.getString(c.getColumnIndex("userName"))+" - "+c.getString(c.getColumnIndex("userDepartment")));
		}else{
			TextView tvUserDepartmentName = (TextView) convertView.findViewById(R.id.tvUserDepartmentName);
			tvUserDepartmentName.setText("抱歉，该人员已被你本地删除");
			/*此处写从服务器获得本地被删除的人员信息*/
		}
		return convertView;
	}
	

}
