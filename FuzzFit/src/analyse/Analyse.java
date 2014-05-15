package analyse;

import others.Mediator;

public interface Analyse {
	
	public void analyse();
	public void enterActivity(float time, float distance, float calories);
	public void enterCalories(float calories);
	public void updateActivity(float time, float distance, float calories);
	public String getMonitorResult();
}
