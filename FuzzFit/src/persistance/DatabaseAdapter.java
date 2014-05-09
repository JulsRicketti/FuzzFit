package persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//remember: whenever you are to change something from the database itself, try uninstalling and reinstalling the app!!
public class DatabaseAdapter {
    public static final String KEY_ROWID = "id";
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "jfitdb.txt";;
    private static final int DATABASE_VERSION = 2;

    
	public static final String USER_PROFILE_TABLE="UserProfile";
	public static final String AGE="age";
	public static final String WEIGHT="weight";
	public static final String HEIGHT="height";
	public static final String SEX="sex";

	public static final String RUNNER_HISTORY_TABLE = "RunnerHistoryTable";
	public static final String WALKER_HISTORY_TABLE = "WalkerHistoryTable";
	public static final String WEIGHT_LOSS_HISTORY_TABLE = "WeightLossHistoryTable";
	
	public static final String RECOMMENDATION = "recommendation";
	public static final String ACTIVITY_DATE = "activitydate";
	public static final String ACTIVITY_DISTANCE = "activity_distance";
	public static final String ACTIVITY_TIME = "activity_time";
	public static final String ACTIVITY_VELOCITY = "activity_velocity";
	public static final String MONITOR = "monitor";
	public static final String CALORIES = "calories";
	
    private static final String USER_TABLE_CREATE =
        "create table if not exists UserProfile ( id integer primary key autoincrement,"
        + "age VARCHAR not null, weight VARCHAR not null, height VARCHAR not null, sex VARCHAR not null);";
        
    private static final String RUNNER_TABLE_CREATE =        
    		"create table if not exists RunnerHistoryTable ( id integer primary key autoincrement,"
            + "recommendation VARCHAR , activitydate VARCHAR , activity_distance VARCHAR , activity_time VARCHAR, activity_velocity VARCHAR, monitor VARCHAR, calories VARCHAR);";
    private static final String WALKER_TABLE_CREATE =        
    		"create table if not exists WalkerHistoryTable ( id integer primary key autoincrement,"
    		+ "recommendation VARCHAR , activitydate VARCHAR , activity_distance VARCHAR , activity_time VARCHAR, activity_velocity VARCHAR, monitor VARCHAR, calories VARCHAR);";
    private static final String WEIGHT_LOSS_TABLE_CREATE =        
    		"create table if not exists WeightLossHistoryTable (id integer primary key autoincrement, "
    		+ " recommendation VARCHAR , activitydate VARCHAR, activity_distance VARCHAR , activity_time VARCHAR, activity_velocity VARCHAR, monitor VARCHAR, calories VARCHAR);";
    
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DatabaseAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
        	try {
        		db.execSQL(USER_TABLE_CREATE);
        		db.execSQL(RUNNER_TABLE_CREATE);
        		db.execSQL(WALKER_TABLE_CREATE);
        		db.execSQL(WEIGHT_LOSS_TABLE_CREATE);
        	} catch (SQLException e) {
        		e.printStackTrace();
        	}
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }    

    //---opens the database---
    public DatabaseAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert a record into the database---
      
    public long insertUser(String age, String weight, String height,String sex){
        ContentValues initialValues = new ContentValues();
  
        initialValues.put(AGE,age);
        initialValues.put(WEIGHT,weight);
        initialValues.put(HEIGHT,height);
        initialValues.put(SEX, sex);
        
        return db.insert(USER_PROFILE_TABLE, null, initialValues);
    }
    
    public long insertActivity(String tableName, String recommendation, String activityDate, String activityDistance, String activityTime, String activityVelocity, String monitor, String calories){
    	ContentValues initialValues = new ContentValues();
    	  
        initialValues.put(RECOMMENDATION,recommendation);
        initialValues.put(ACTIVITY_DATE,activityDate);
        initialValues.put(ACTIVITY_DISTANCE,activityDistance);
        initialValues.put(ACTIVITY_TIME, activityTime);
        initialValues.put(ACTIVITY_VELOCITY, activityTime);
        initialValues.put(MONITOR, monitor);
        initialValues.put(CALORIES, calories);
        
        return db.insert(tableName, null, initialValues);	
    }

    //---deletes a particular record---
    
    public boolean deleteUser(long rowId){
    	return db.delete(USER_PROFILE_TABLE, KEY_ROWID+"="+rowId, null)>0;	
    }
    
    public void deleteAll(String tableName){
    	db.delete(tableName, null, null);
    }
    
    //---retrieves all the records---
    
    public Cursor getAllUserRecords(){
        return db.query(USER_PROFILE_TABLE, new String[] { AGE,
                WEIGHT, HEIGHT, SEX}, null, null, null, null, null);
    	
    }
    
    public Cursor getAllActivityRecords(String tableName){
        return db.query(tableName, new String[] { RECOMMENDATION, ACTIVITY_DATE,ACTIVITY_DISTANCE,ACTIVITY_TIME, ACTIVITY_VELOCITY, MONITOR, CALORIES}, null, null, null, null, null);
    	
    }
    
    //---retrieves a particular record---
//orignal example:
    //    public Cursor getRecord(long rowId) throws SQLException 
//    {
//        Cursor mCursor =
//                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
//                KEY_TITLE, KEY_DUEDATE, KEY_COURSE, KEY_NOTES}, 
//                KEY_ROWID + "=" + rowId, null, null, null, null, null);
//        if (mCursor != null) {
//            mCursor.moveToFirst();
//        }
//        return mCursor;
//    }
    
    public Cursor getUser(long rowId) throws SQLException{
        Cursor mCursor =
                db.query(true, USER_PROFILE_TABLE, new String[] {KEY_ROWID,
                AGE, WEIGHT, HEIGHT, SEX}, 
                KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    	
    }
    
    public Cursor getPhysicalActivity(String tableName, long rowId) throws SQLException{
        Cursor mCursor =
                db.query(true, tableName, new String[] {KEY_ROWID,
                RECOMMENDATION, ACTIVITY_DATE, ACTIVITY_DISTANCE, ACTIVITY_TIME, ACTIVITY_VELOCITY, MONITOR, CALORIES}, 
                KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    	
    }

    //---updates a record---
//    public boolean updateRecord(long rowId, String title, String duedate, String course, String notes) 
//    {
//        ContentValues args = new ContentValues();
//        args.put(KEY_TITLE, title);
//        args.put(KEY_DUEDATE, duedate);
//        args.put(KEY_COURSE, course);
//        args.put(KEY_NOTES, notes);
//        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
//    }
    
    public boolean updateUser(long rowId, String age, String weight, String height,String sex,String weightLossObjective, String runningObjective, String walkingObjective, String parameter) 
    {
        ContentValues args = new ContentValues();
        args.put(AGE, age);
        args.put(WEIGHT, weight);
        args.put(HEIGHT, height);
        args.put(SEX, sex);
        return db.update(USER_PROFILE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    //we will use the date as the row id
    public boolean updatePhysicalActivity(String tableName, String recommendation, String activityDate, String activityDistance, String activityTime,  String activityVelocity, String monitor, String calories ){
    	 ContentValues args = new ContentValues();
         args.put(RECOMMENDATION,recommendation);
         args.put(ACTIVITY_DATE, activityDate);
         args.put(ACTIVITY_DISTANCE, activityDistance);
         args.put(ACTIVITY_TIME, activityTime);
         args.put(ACTIVITY_VELOCITY, activityVelocity);
         args.put(MONITOR, monitor);
         args.put(CALORIES, calories);
         return db.update(tableName, args, ACTIVITY_DATE + "= '" + activityDate+"'", null) > 0;
    }
    
    public void findId(String tableName, String date){
    	Cursor mCursor = db.rawQuery("SELECT * FROM "+tableName + "WHERE "+ACTIVITY_DATE+" = "+date, null);
    }
    //check if tables are empty
    
    public boolean userProfileIsEmpty(){
      	Cursor mCursor = db.rawQuery("SELECT * FROM " + USER_PROFILE_TABLE, null);

    	if (mCursor.moveToFirst())
    	  return false;
    	else
    	   return true;
    }
    
    public boolean isEmpty(String tableName){
    	Cursor mCursor = db.rawQuery("SELECT * FROM " + tableName, null);

    	if (mCursor.moveToFirst()) //is not empty
    	  return false;
    	else //is empty
    	   return true;
    }

}
