package com.doubibi.superclubmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db extends SQLiteOpenHelper {

	public Db(Context context) {
		super(context, "db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE users("+
				"_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"userName TEXT DEFAULT NONE,"+
				"userNum TEXT DEFAULT NONE,"+
				"userPosition TEXT DEFAULT NONE,"+
				"userDepartment TEXT DEFAULT NONE,"+
				"userClub TEXT DEFAULT NONE"+
				")");
		
		db.execSQL("CREATE TABLE activities("+
				"_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"club TEXT DEFAULT NONE,"+
				"atyName TEXT DEFAULT NONE,"+
				"atyTime TEXT DEFAULT NONE,"+
				"atyTheme TEXT DEFAULT NONE,"+
				"atyContext TEXT DEFAULT NONE,"+
				"atyRelease BOOLEAN DEFAULT NONE"+
				")");
		
		db.execSQL("CREATE TABLE peopleArrange("+
				"_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"atyName TEXT DEFAULT NONE,"+
				"time TEXT DEFAULT NONE,"+
				"name TEXT DEFAULT NONE"+
				")");
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
