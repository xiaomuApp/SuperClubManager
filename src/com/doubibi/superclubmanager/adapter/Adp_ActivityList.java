package com.doubibi.superclubmanager.adapter;


import com.doubibi.superclubmanager.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Adp_ActivityList extends SimpleCursorAdapter {

	@SuppressWarnings("deprecation")
	public Adp_ActivityList(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.context = context;
	}
	
	private Context context;

	@SuppressLint("InflateParams") @Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		super.bindView(convertView, context, cursor);
		if(convertView==null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_activity_list, null);
		}
		TextView tvAtyName = (TextView) convertView.findViewById(R.id.tvAtyName);
		TextView tvAtyTheme = (TextView) convertView.findViewById(R.id.tvAtyTheme);
		TextView tvAtyTime = (TextView) convertView.findViewById(R.id.tvAtyTime);
		TextView tvAtyRelease = (TextView) convertView.findViewById(R.id.tvAtyRelease);
		
		tvAtyName.setText(cursor.getString(cursor.getColumnIndex("atyName")));
		tvAtyTheme.setText(cursor.getString(cursor.getColumnIndex("atyTheme")));
		tvAtyTime.setText(cursor.getString(cursor.getColumnIndex("atyTime")));
		if(cursor.getString(cursor.getColumnIndex("atyRelease")).equals("1")){
			tvAtyRelease.setTextColor(context.getResources().getColor(R.color.releasegreen));
			tvAtyRelease.setText("已发布");
		}else{
			tvAtyRelease.setTextColor(context.getResources().getColor(R.color.listgrey));
			tvAtyRelease.setText("未发布");
		}
		
	}

	public Context getContext() {
		return context;
	}
}
