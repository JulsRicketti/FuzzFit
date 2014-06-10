package monitor;

import java.util.HashMap;

import monitor.Monitor.FuzzySet;

public class WeightLossMonitor extends Monitor {

	//set names
	static final String BAD = "bad";
	static final String AVERAGE = "average";
	static final String GOOD = "good";
	
	//output set names
	static final String INSUFFICIENT = "insufficient";
	static final String SUFFICIENT = "sufficient";
	static final String AVERAGE_SUFICIENT = "average";
	
	//output definitions:
	static final float perfect = 100;
	static final float poor = perfect/4; //(25)
	static final float average_bad = perfect/2; //(50)
	static final float average_good = average_bad + poor; //(75)
	
	static float dailyCalories = MonitorObserver.calories;
	static float dailyAverageCalories = dailyCalories/2;
	
	FuzzySet[] caloriesSet;
	HashMap <String, FuzzySet> caloriesMap = new HashMap<String, Monitor.FuzzySet>();
	
	FuzzySet[] auxOutputs; //just for now
	FuzzySet insufficientOutputs;
	FuzzySet averageOutputs;
	FuzzySet sufficientOutputs;
	
	
	public WeightLossMonitor(float calories){
		dailyCalories = MonitorObserver.calories;
		dailyAverageCalories = dailyCalories/2;
		
		fuzzifier(0, 0, calories);
		fuzzyRules(null);
		agreggation();
		defuzzifier();
	}
	
//	public WeightLossMonitor(float calories){
//		if(calories>=MonitorObserver.calories)
//			result = (float) 100.0;
//		if(calories<MonitorObserver.calories)
//			result = (float) 60.0;
//	}
	
	@Override
	void fuzzifier(float timeInput, float distanceInput, float caloriesInput) {
		FuzzySet []caloriesFuzzySet = new FuzzySet[3];		
		caloriesFuzzySet = caloriesFuzzifier(caloriesInput);
		
	}

	FuzzySet[] caloriesFuzzifier(float caloriesInput){
		float []mAndB = new float[2];
		float membershipBad=0, membershipAverage=0, membershipGood=0;
		
		float [] badInterval = new float[2];
		float []averageInterval = new float[2];
		float []goodInterval = new float[2];
		//continue HERE!
		badInterval[0]=0; badInterval[1]=dailyAverageCalories;
		averageInterval[0] = dailyAverageCalories/2; averageInterval[1]=(dailyAverageCalories/2)+ dailyAverageCalories;
		goodInterval[0]=dailyAverageCalories; goodInterval[1]=dailyCalories;
		
		if(caloriesInput<=dailyAverageCalories/2){
			membershipBad = 1;
			membershipAverage = 0;
			membershipGood=0;
			
		}
		if(caloriesInput>dailyAverageCalories/2 && caloriesInput<=dailyAverageCalories){
			mAndB = findLinearFunction(dailyAverageCalories/2, 1, dailyAverageCalories, 0);
			membershipBad = findDegreeOfMembership(caloriesInput, mAndB);
			
			mAndB = findLinearFunction(dailyAverageCalories/2, 0, dailyAverageCalories, 1);
			membershipAverage =findDegreeOfMembership(caloriesInput, mAndB);
			
			membershipGood =0;
			
		}
		if(caloriesInput>dailyAverageCalories && caloriesInput<=((dailyAverageCalories/2)+ dailyAverageCalories)){
			membershipBad =0;
			
			mAndB = findLinearFunction(dailyAverageCalories, 1, ((dailyAverageCalories/2)+ dailyAverageCalories), 0);
			membershipAverage = findDegreeOfMembership(caloriesInput, mAndB);
			
			
			mAndB = findLinearFunction(dailyAverageCalories, 0, ((dailyAverageCalories/2)+ dailyAverageCalories), 1);
			membershipGood= findDegreeOfMembership(caloriesInput, mAndB);
			
		}
		if(caloriesInput>((dailyAverageCalories/2)+ dailyAverageCalories)){
			membershipBad =0;
			membershipAverage=0;
			mAndB = findLinearFunction(dailyAverageCalories, 0, ((dailyAverageCalories/2)+ dailyAverageCalories), 1);
			membershipGood= findDegreeOfMembership(caloriesInput, mAndB);
			
		}

		FuzzySet[] caloriesInputs = new FuzzySet[3];
		caloriesInputs [0]= new FuzzySet(BAD, membershipBad);
		caloriesInputs[0].setInterval(badInterval);
		caloriesInputs [1] = new FuzzySet(AVERAGE, membershipAverage);
		caloriesInputs[1].setInterval(averageInterval);
		caloriesInputs [2] = new FuzzySet(GOOD, membershipGood);
		caloriesInputs[2].setInterval(goodInterval);
				
		caloriesMap.put(BAD, caloriesInputs[0]);
		caloriesMap.put(AVERAGE, caloriesInputs[1]);
		caloriesMap.put(GOOD,caloriesInputs[2]);

		return caloriesInputs;
	}
	
	@Override
	void fuzzyRules(FuzzySet[] inputSet) {
		FuzzySet []outputs = new FuzzySet[9]; //these are the rules
		
		outputs[0] = new FuzzySet(INSUFFICIENT, caloriesMap.get(BAD).getDegreeOfMembership());
		outputs[1] = new FuzzySet(AVERAGE_SUFICIENT, caloriesMap.get(AVERAGE).getDegreeOfMembership());
		outputs[2] = new FuzzySet(SUFFICIENT, caloriesMap.get(GOOD).getDegreeOfMembership());
		
		auxOutputs = outputs;
	}

	@Override
	void agreggation() {
		insufficientOutputs = auxOutputs[0];
		averageOutputs = auxOutputs[1];
		sufficientOutputs = auxOutputs[2];
		
	}

	@Override
	void defuzzifier() {
		float[] mAndB = new float[2];

		float[] x = new float[6];

		x[0] = 0;
		
		mAndB = findLinearFunction(poor, 1, average_bad, 0);
		x[1] = findInterval(insufficientOutputs.getDegreeOfMembership(), mAndB);
		System.out.println("X[1]"+x[1]);
		
		mAndB = findLinearFunction(poor, 0, average_bad, 1);
		x[2] = findInterval(averageOutputs.getDegreeOfMembership(), mAndB);
		System.out.println("X[2]"+x[2]);
		
		mAndB = findLinearFunction(average_good, 1, perfect, 0);
		x[3] = findInterval(averageOutputs.getDegreeOfMembership(), mAndB);
		System.out.println("X[3]"+x[3]);
		
		mAndB = findLinearFunction(average_good, 0, perfect, 1);
		x[4] = findInterval(sufficientOutputs.getDegreeOfMembership(), mAndB);
		System.out.println("X[4]"+x[4]);
	
		x[5] = perfect;

		float cog = 0;
		float auxInsuf, auxAvg, auxSuf;
		
		float  ux=0, sum=0, sumWeight =0;
		//loop through all xs
		for(int i =0; i<average_bad; i++){
			if(i<=x[1]){
				ux=insufficientOutputs.getDegreeOfMembership();
			}
			else{
			//	if(insufficientOutputs.getDegreeOfMembership()>=averageOutputs.getDegreeOfMembership()){
					mAndB = findLinearFunction(poor, 1, average_bad, 0);
					ux = findDegreeOfMembership(i, mAndB);
				//}
			}
			sumWeight+=i*ux;
			sum += ux;
		}
		//for average outputs
		for(int i=(int) poor; i<perfect; i++){
			if(i<=x[2]){
				//if(averageOutputs.getDegreeOfMembership()>=insufficientOutputs.getDegreeOfMembership()){
				mAndB = findLinearFunction(poor, 0, average_bad, 1);
				ux = findDegreeOfMembership(i, mAndB);
				//}
			}
			if(i>x[2] && i<=x[3]){
				ux = averageOutputs.getDegreeOfMembership();
			}
			if(i>x[3]){
				//if(averageOutputs.getDegreeOfMembership()>=sufficientOutputs.getDegreeOfMembership()){
				mAndB = findLinearFunction(average_good, 1, perfect, 0);
				ux = findDegreeOfMembership(i, mAndB);
				//}
			}
			sumWeight += i*ux;
			sum+=ux;
		}
		//for sufficient outputs
		for(int i=(int)average_good; i<perfect; i++){
			mAndB = findLinearFunction(average_good, 0, perfect, 1);
			ux=findDegreeOfMembership(i, mAndB);
			sumWeight += i*ux;
			sum+=ux;

		}
		
		cog = sumWeight/sum;
		System.out.println("cog: "+cog);
		setResult(cog);
		
	}

}
