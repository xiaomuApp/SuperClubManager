package com.doubibi.superclubmanager.adapter;

import java.util.ArrayList;

import com.doubibi.superclubmanager.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Adp_PeopleList extends SimpleCursorAdapter {

	@SuppressWarnings("deprecation")
	public Adp_PeopleList(Context context, int layout, Cursor c, String[] from,
			int[] to) {
		super(context, layout, c, from, to);
		this.context = context;
	}

	private Context context;
	private ArrayList<String>userNumList;
	
	public Context getContext() {
		return context;
	}
	
	public void initView(ArrayList<String>userNumList){	//此函数是初始化列表视图，通过传入的学号列表，标记相应的列表为已勾选状态
		this.userNumList = userNumList;
	}
	
	@SuppressLint("InflateParams") @Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		super.bindView(convertView, context, cursor);
		if(convertView==null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_people_list, null);
		}
		TextView tvUserNameDepartment = (TextView) convertView.findViewById(R.id.tvUserNameDepartment);
		TextView tvUserPosition = (TextView) convertView.findViewById(R.id.tvUserPosition);
		ImageView ivCheckBox = (ImageView) convertView.findViewById(R.id.ivUserCheckBox);
		tvUserNameDepartment.setText(cursor.getString(cursor.getColumnIndex("userName"))+
				" ("+cursor.getString(cursor.getColumnIndex("userDepartment"))+")");
		tvUserPosition.setText("职位："+cursor.getString(cursor.getColumnIndex("userPosition")));
		if(userNumList!=null){
			for(String num:userNumList){
				if(cursor.getString(cursor.getColumnIndex("userNum")).equals(num)){
					convertView.setTag(true);
					ivCheckBox.setImageResource(R.drawable.ic_cb_done);
					break;
				}
			}
		}
	}
	

}
