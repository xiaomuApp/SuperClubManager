package com.doubibi.superclubmanager.main;

import com.doubibi.superclubmanager.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;


public class Aty_Main extends Activity implements OnClickListener{

	Intent intent = new Intent();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btnOrganizer).setOnClickListener(this);
        findViewById(R.id.btnPerformer);
        findViewById(R.id.btnAdressList);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnOrganizer:
			intent.setClass(this, Aty_ActivityList.class);
			startActivity(intent);
			
			break;

		default:
			break;
		}
		
	}


}
