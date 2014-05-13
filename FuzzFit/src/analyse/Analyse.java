package analyse;

import others.Mediator;

public interface Analyse {
	
	public void analyse();
	public void enterActivity(float time, float distance, float velocity);
	public void updateActivity(float time, float distance, float velocity);
	public String getMonitorResult();
}
