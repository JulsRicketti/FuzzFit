package monitor;

public class WeightLossMonitor extends Monitor {

	public WeightLossMonitor(){
		
	}
	
	public WeightLossMonitor(float calories){
		if(calories>=MonitorObserver.calories)
			result = (float) 100.0;
		if(calories<MonitorObserver.calories)
			result = (float) 60.0;
	}
	
	@Override
	void fuzzifier(float timeInput, float distanceInput, float speedInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void fuzzyRules(FuzzySet[] inputSet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void inferenceEngine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	void defuzzifier() {
		// TODO Auto-generated method stub
		
	}

}
