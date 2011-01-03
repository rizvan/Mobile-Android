package org.scielo.search;
import android.content.Context;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
public final class MySQLiteOpenHelper extends SQLiteOpenHelper {
	//private String DATABASE_NAME = "scielo.db";
	//private int DATABASE_VERSION = 1;
	private String DATABASE_CREATE = "";
	private String DATABASE_TABLE = "";
	
	
	public MySQLiteOpenHelper(Context context, String name, 
	            CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	public void setTableData(String table, String expression) {
		DATABASE_TABLE = table;
		DATABASE_CREATE = "CREATE TABLE " + DATABASE_TABLE + " (" + expression + ");";
	}

	// Called when no database exists in disk and the helper class needs
	// to create a new one. 
	@Override
	public void onCreate(SQLiteDatabase _db) {
		_db.execSQL(DATABASE_CREATE);
	}
	
	// Called when there is a database version mismatch meaning that the version
	// of the database on disk needs to be upgraded to the current version.
	@Override
	public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
		// Log the version upgrade.
		Log.w("TaskDBAdapter", "Upgrading from version " + 
	                   _oldVersion + " to " +
	                   _newVersion + ", which will destroy all old data");
	
		// Upgrade the existing database to conform to the new version. Multiple 
		// previous versions can be handled by comparing _oldVersion and _newVersion
		// values.
		
		// 	The simplest case is to drop the old table and create a new one.
		_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		// Create a new one.
		onCreate(_db);
	}
}
