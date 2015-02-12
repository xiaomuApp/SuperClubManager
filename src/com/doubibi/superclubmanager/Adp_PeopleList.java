package com.doubibi.superclubmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
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
	
	public Context getContext() {
		return context;
	}
	
	@SuppressLint("InflateParams") @Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		super.bindView(convertView, context, cursor);
		if(convertView==null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_people_list, null);
		}
		TextView tvUserNameDepartment = (TextView) convertView.findViewById(R.id.tvUserNameDepartment);
		TextView tvUserPosition = (TextView) convertView.findViewById(R.id.tvUserPosition);
		tvUserNameDepartment.setText(cursor.getString(cursor.getColumnIndex("userName"))+
				" ("+cursor.getString(cursor.getColumnIndex("userDepartment"))+")");
		tvUserPosition.setText("职位："+cursor.getString(cursor.getColumnIndex("userPosition")));
	}
	

}
