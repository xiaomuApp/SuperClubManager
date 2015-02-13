package com.doubibi.superclubmanager.main;

import java.util.ArrayList;
import java.util.List;

import com.doubibi.superclubmanager.R;
import com.doubibi.superclubmanager.adapter.Adp_Guide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Aty_Guide extends Activity {

	private ViewPager vp;
	private Adp_Guide vpAdapter;
	private List<View> views;
	private Button btnStart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initViews();
	}

	@SuppressLint("InflateParams")
	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);

		views = new ArrayList<View>();
		views.add(inflater.inflate(R.layout.item_guide_one, null));
		views.add(inflater.inflate(R.layout.item_guide_two, null));
		views.add(inflater.inflate(R.layout.item_guide_tree, null));

		vpAdapter = new Adp_Guide(views, this);
		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setAdapter(vpAdapter);
		btnStart = (Button) views.get(2).findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Aty_Guide.this, Aty_Main.class);
				startActivity(i);
				finish();
			}
		});
	}
}
