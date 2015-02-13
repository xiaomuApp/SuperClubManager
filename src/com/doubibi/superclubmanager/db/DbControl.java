package com.doubibi.superclubmanager.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbControl {
	
	private static Db db;
	private static SQLiteDatabase dbRead,dbWrite;
	
	/*获得所有的本地活动，按截止时间的后先顺序，需要自己手动关闭数据库
	 *返回值为activities的Cursor*/
	public static Cursor findActivitiesAll(Context context){
		db = new Db(context);
		dbRead = db.getReadableDatabase();
		Cursor c = dbRead.query("activities", null, null, null, null, null, "atyTime DESC");
		c.moveToNext();
		return c;
	}
	
	/*删除所有的本地活动信息*/
	public static void deleteActivitiesAll(Context context){
		db = new Db(context);
		dbRead = db.getReadableDatabase();
		dbRead.delete("activities", null, null);
		dbRead.close();
	}
	
	/*通过活动Id来查找某个活动，需要手动关闭数据库连接，返回值为activities的Cursor，不存在则返回null*/
	public static Cursor findActivityById(Context context, String atyId){
		db =  new Db(context);
		dbRead = db.getReadableDatabase();
		String[] str = new String[]{atyId};
		Cursor c = dbRead.query("activities", null, "atyId=?", str, null, null, null);
		if(c.moveToNext())
			return c;
		else
			return null;
	}
	
	/*通过atyId来查找所有的有关此活动的人，需要手动关闭数据库连接
	 * 返回的是peopleArrange表的Cursor，没有查到则返回null*/
	public static Cursor findPeopleArrangeByatyId(Context context, String atyId){
		db =  new Db(context);
		dbRead = db.getReadableDatabase();
		String[] str = new String[]{atyId};
		Cursor c = dbRead.query("peopleArrange", null, "atyId=?", str, null, null, null);
		if(c.moveToNext())
			return c;
		else
			return null;
	}
	
	/*在activities表中插入活动，成功则返回true，否则false*/
	public static boolean insertActivity(Context context, String atyId, String atyName,
			String atyTheme, String atyTime, String atyContext, Boolean atyRelease){
		Db db = new Db(context);
		dbWrite = db.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("atyId", atyId);
		cv.put("atyName", atyName);
		cv.put("atyTheme", atyTheme);
		cv.put("atyTime", atyTime);
		cv.put("atyContext", atyContext);
		cv.put("atyRelease", atyRelease);
		Long success = dbWrite.insert("activities", null, cv);
		dbWrite.close();
		if(success!=-1)
			return true;
		else
			return false;
	}
	
	/*在activities表中根据atyID更新活动，成功则返回true，否则false*/
	public static boolean updateActivity(Context context, String atyId, String atyName,
			String atyTheme, String atyTime, String atyContext, Boolean atyRelease){
		Db db = new Db(context);
		dbWrite = db.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("atyId", atyId);
		cv.put("atyName", atyName);
		cv.put("atyTheme", atyTheme);
		cv.put("atyTime", atyTime);
		cv.put("atyContext", atyContext);
		cv.put("atyRelease", atyRelease);
		int success = dbWrite.update("activities", cv, "atyId=?", new String[]{atyId});
		dbWrite.close();
		if(success!=-1)
			return true;
		else
			return false;
	}
	
	/*在peopleArrange表中插入或更新人员安排，根据atyId，成功返回true*/
	public static boolean insertPeopleArrange(Context context, String atyId, ArrayList<String> userNum){
		Db db = new Db(context);
		dbWrite = db.getWritableDatabase();
		dbWrite.delete("peopleArrange", "atyId=?", new String[]{atyId});
		long success = -1;
		ContentValues cvPeopleArrange = new ContentValues();
		for(String num: userNum){
			cvPeopleArrange.put("userNum", num);
			cvPeopleArrange.put("atyId", atyId);
			success = dbWrite.insert("peopleArrange", null, cvPeopleArrange);
			if(success==-1)break;
		}
		dbWrite.close();
		if(success==-1)
			return false;
		else
			return true;
	}
	
	/*删除所有的本地人员安排信息*/
	public static void deletePeopleArrangeAll(Context context){
		db = new Db(context);
		dbRead = db.getReadableDatabase();
		dbRead.delete("peopleArrange", null, null);
		dbRead.close();
	}
	
	/*获得所有的本地活动，按截止时间的后先顺序，需要自己手动关闭数据库
	 *返回值为users的Cursor*/
	public static Cursor findUsersAll(Context context){
		db = new Db(context);
		dbRead = db.getReadableDatabase();
		Cursor c = dbRead.query("users", null, null, null, null, null, "userNum");
		if(c.moveToNext())
			return c;
		else
			return null;
	}
	
	/*通过userNum来查找所有的有关的人，需要手动关闭数据库连接
	 * 返回的是users表的Cursor，没有查到则返回null*/
	public static Cursor findUserByNum(Context context, String userNum){
		db =  new Db(context);
		dbRead = db.getReadableDatabase();
		String[] str = new String[]{userNum};
		Cursor c = dbRead.query("users", null, "userNum=?", str, null, null, null);
		if(c.moveToNext())
			return c;
		else
			return null;
	}
	
	/*在users表中插入人员，成功则返回true，否则false*/
	public static boolean insertUser(Context context, String userName, String userNum,
			String userPosition, String userDepartment, String userClub){
		db = new Db(context);
		dbWrite = db.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("userName", userName);
		cv.put("userNum", userNum);
		cv.put("userPosition", userPosition);
		cv.put("userDepartment", userDepartment);
		cv.put("userClub", userClub);
		Long success = dbWrite.insert("users", null, cv);
		dbWrite.close();
		if(success!=-1)
			return true;
		else
			return false;
	}
	
	/*删除所有的本地活动信息和人员安排信息*/
	public static void deleteUsersAll(Context context){
		db = new Db(context);
		dbWrite = db.getReadableDatabase();
		dbWrite.delete("users", null, null);
		dbWrite.close();
	}
	
	/*关闭所有的dbControl连接*/
	public static void colseDbControl(){
		db.close();
	}
	
}
