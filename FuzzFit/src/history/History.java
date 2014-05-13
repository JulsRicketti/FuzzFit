package history;
import java.util.ArrayList;

import persistance.DatabaseAdapter;
import android.content.Context;
import android.database.Cursor;

public class History {
	DatabaseAdapter dbAdapter ;
	
	ActivityHistory walkerHistory;
	ActivityHistory runnerHistory;
	ActivityHistory weightLossHistory;
	
	public History(Context context){	
		dbAdapter = new DatabaseAdapter(context);
	}
	public History(Context context, String tableName, String recommendationDate, String recommendation, String activityDate, String activityDistance, String activityTime, String activityVelocity, String monitor, String calories){
		dbAdapter = new DatabaseAdapter(context);
		dbAdapter.open();
		dbAdapter.insertActivity(tableName, recommendation, activityDate, activityDistance, activityTime, activityVelocity, monitor, calories);
		dbAdapter.close();
	}
	
	public void insert(String tableName, String recommendation, String activityDate, String activityDistance, String activityTime, String activityVelocity, String monitor, String calories){
		dbAdapter.open();
		dbAdapter.insertActivity(tableName, recommendation, activityDate, activityDistance, activityTime, activityVelocity, monitor, calories);
		dbAdapter.close();
	}
	
	public boolean isWalkerEmpty(){
		dbAdapter.open();
		return dbAdapter.isEmpty(DatabaseAdapter.WALKER_HISTORY_TABLE);
	}
	
	public boolean isRunnerEmpty(){
		dbAdapter.open();
		return dbAdapter.isEmpty(DatabaseAdapter.RUNNER_HISTORY_TABLE);
	}
	
	public boolean isWeightLossEmpty(){
		dbAdapter.open();
		return dbAdapter.isEmpty(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE);
	}
	
	public ActivityHistory getWalkerHistory(){
		dbAdapter.open();
		
		if(dbAdapter.isEmpty(DatabaseAdapter.WALKER_HISTORY_TABLE)){
			dbAdapter.close();
			return null;
		}
		else{
			walkerHistory = getHistory(DatabaseAdapter.WALKER_HISTORY_TABLE);
	
			dbAdapter.close();
			return walkerHistory;
		}
	}
	
	public ActivityHistory getRunnerHistory(){
		dbAdapter.open();
		
		if(dbAdapter.isEmpty(DatabaseAdapter.RUNNER_HISTORY_TABLE)){
			return null;
		}
		else{
			runnerHistory = getHistory(DatabaseAdapter.RUNNER_HISTORY_TABLE);		
	
			dbAdapter.close();
			return runnerHistory;
		}
	}
	
	public ActivityHistory getWeightLossHistory(){
		dbAdapter.open();
		
		if(dbAdapter.isEmpty(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE)){
			return null;
		}
		else{
			weightLossHistory = getHistory(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE);		
	
			dbAdapter.close();
			return weightLossHistory;
		}
	}
	
	public ActivityHistory getHistory(String tableName){
		dbAdapter.open();
		Cursor history = dbAdapter.getAllActivityRecords(tableName);
		ActivityHistory activity = new ActivityHistory();
		history.moveToFirst();
		while(!history.isAfterLast()){
			activity.recommendation.add(history.getString(history.getColumnIndex(DatabaseAdapter.RECOMMENDATION)));
			activity.activityDate.add(history.getString(history.getColumnIndex(DatabaseAdapter.ACTIVITY_DATE)));
			activity.activityDistance.add(history.getString(history.getColumnIndex(DatabaseAdapter.ACTIVITY_DISTANCE)));
			activity.activityTime.add(history.getString(history.getColumnIndex(DatabaseAdapter.ACTIVITY_TIME)));
			activity.monitor.add(history.getString(history.getColumnIndex(DatabaseAdapter.MONITOR)));
			activity.calories.add(history.getString(history.getColumnIndex(DatabaseAdapter.CALORIES))); //does not work! Figure it out!
			history.moveToNext();			
		}
		return activity;
	}
	
	public void updateDatabase(String tableName, String recommendation, String activityDate, String activityDistance, String activityTime, String activityVelocity,String monitor, String calories ){
		dbAdapter.open();
		dbAdapter.updatePhysicalActivity(tableName, recommendation, activityDate, activityDistance, activityTime, activityVelocity, monitor, calories);
		dbAdapter.close();
	}

}
