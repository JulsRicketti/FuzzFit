package monitor;

import java.util.HashMap;

import monitor.Monitor.FuzzySet;

public class RunningMonitor extends Monitor{

	//output definitions:
	static final float perfect = 100;
	static final float poor = perfect/4; //(25)
	static final float average_bad = perfect/2; //(50)
	static final float average_good = average_bad + poor; //(75)
	
	//distance definitions 
	static float dailyMinDistance = MonitorObserver.currentRunningDistance; //something is wrong with the MonitorObserver
	static float goodDistance = dailyMinDistance + dailyMinDistance/2; // in case of testing, put 1000
	
	//time definitions
	static float dailyTime = MonitorObserver.currentRunningTime; //best
	static float goodDailyTime = dailyTime/2 + dailyTime/4; //next best
	static float averageDailyTime = dailyTime/2; //middle
	static float badDailyTime = dailyTime/4; //worst (before 0)
	
	FuzzySet[] timeSet;
	FuzzySet[] distanceSet;
	HashMap <String, FuzzySet> timeMap = new HashMap<String, Monitor.FuzzySet>();
	HashMap <String, FuzzySet>distanceMap = new HashMap<String, Monitor.FuzzySet>();
	


	public RunningMonitor(float timeInput, float distanceInput){
		//distance definitions 
		dailyMinDistance = MonitorObserver.currentRunningDistance; //something is wrong with the MonitorObserver
		goodDistance = dailyMinDistance + dailyMinDistance/2; // in case of testing, put 1000
		
		//time definitions
		dailyTime = MonitorObserver.currentRunningTime; //best
		goodDailyTime = dailyTime/2 + dailyTime/4; //next best
		averageDailyTime = dailyTime/2; //middle
		badDailyTime = dailyTime/4; //worst (before 0)
		
		fuzzifier(timeInput, distanceInput, 0);
		fuzzyRules(null);
		agreggation();
		defuzzifier();
	}
	
	@Override
	void fuzzifier(float timeInput, float distanceInput, float caloriesInput) {
		FuzzySet []timeFuzzySet = new FuzzySet[3];
		FuzzySet []distanceFuzzySet = new FuzzySet[3];
		
		timeFuzzySet = timeFuzzifier(timeInput);
		distanceFuzzySet = distanceFuzzifier(distanceInput);
		
	}

	FuzzySet[] timeFuzzifier(float timeInput){
		float []mAndB = new float[2];
		float membershipBad=0, membershipAverage=0, membershipGood=0;
		
		float [] badInterval = new float[2];
		float []averageInterval = new float[2];
		float []goodInterval = new float[2];
		badInterval[0]=0; badInterval[1]=averageDailyTime;
		averageInterval[0] = badDailyTime; averageInterval[1]=goodDailyTime;
		goodInterval[0]=goodDailyTime; goodInterval[1]=999;
		
		
		if(timeInput<=badDailyTime){
			membershipBad = 1;
			membershipAverage=0;
			membershipGood = 0;
		}
		if(timeInput>badDailyTime && timeInput<=averageDailyTime){
			mAndB = findLinearFunction(badDailyTime, 1, averageDailyTime, 0);
			membershipBad = findDegreeOfMembership(timeInput, mAndB);
			
			mAndB = findLinearFunction(badDailyTime, 0, averageDailyTime, 1);
			membershipAverage = findDegreeOfMembership(timeInput, mAndB);
			
			membershipGood =0;
		}
		if(timeInput>averageDailyTime && timeInput<=goodDailyTime){
			membershipBad =0;
			membershipAverage= 1;
			membershipGood = 0;
		}
		if(timeInput>goodDailyTime && timeInput<=dailyTime){
			membershipBad =0;
			
			mAndB = findLinearFunction(goodDailyTime, 1, dailyTime, 0);
			membershipAverage = findDegreeOfMembership(timeInput, mAndB);
			
			mAndB = findLinearFunction(goodDailyTime, 0, dailyTime, 1);
			membershipGood = findDegreeOfMembership(timeInput, mAndB);
		}
		if(timeInput>dailyTime){
			membershipBad=0;
			membershipAverage=0;
			membershipGood=1;
		}
		
		FuzzySet[] timeInputs = new FuzzySet[3];
		timeInputs[0]= new FuzzySet(BAD, membershipBad);
		timeInputs[0].setInterval(badInterval);
		timeInputs[1] = new FuzzySet(AVERAGE, membershipAverage);
		timeInputs[1].setInterval(averageInterval);
		timeInputs[2] = new FuzzySet(GOOD, membershipGood);
		timeInputs[2].setInterval(goodInterval);
		
		timeMap.put(BAD, timeInputs[0]);
		timeMap.put(AVERAGE, timeInputs[1]);
		timeMap.put(GOOD, timeInputs[2]);

		return timeInputs;
		
	}
	
	FuzzySet[] distanceFuzzifier(float distanceInput){
		FuzzySet result;
		float []mAndB = new float[2];
		float membershipBad=0, membershipAverage=0, membershipGood=0;
		float [] badInterval = new float[2];
		float []averageInterval = new float[2];
		float []goodInterval = new float[2];
		badInterval[0] = 0; badInterval[1]=dailyMinDistance/2;
		averageInterval[0] = dailyMinDistance/2; averageInterval[1]=dailyMinDistance;
		goodInterval[0]=dailyMinDistance; goodInterval[1]=goodDistance;
		if(distanceInput<=dailyMinDistance/2){
			membershipBad = 1;
			membershipAverage = 0;
			membershipGood=0;
			
		}
		if(distanceInput>dailyMinDistance/2 && distanceInput<=dailyMinDistance){
			mAndB = findLinearFunction(dailyMinDistance/2, 1, dailyMinDistance, 0);
			membershipBad = findDegreeOfMembership(distanceInput, mAndB);
			
			mAndB = findLinearFunction(dailyMinDistance/2, 0, dailyMinDistance, 1);
			membershipAverage =findDegreeOfMembership(distanceInput, mAndB);
			
			membershipGood =0;
			
		}
		if(distanceInput>dailyMinDistance && distanceInput<=goodDistance){
			membershipBad =0;
			
			mAndB = findLinearFunction(dailyMinDistance, 1, goodDistance, 0);
			membershipAverage = findDegreeOfMembership(distanceInput, mAndB);
			
			
			mAndB = findLinearFunction(dailyMinDistance, 0, goodDistance, 1);
			membershipGood= findDegreeOfMembership(distanceInput, mAndB);
			
		}
		if(distanceInput>goodDistance){
			membershipBad =0;
			membershipAverage=0;
			membershipGood=1;
			
		}

		FuzzySet[] distanceInputs = new FuzzySet[3];
		distanceInputs [0]= new FuzzySet(BAD, membershipBad);
		distanceInputs[0].setInterval(badInterval);
		distanceInputs [1] = new FuzzySet(AVERAGE, membershipAverage);
		distanceInputs[1].setInterval(averageInterval);
		distanceInputs [2] = new FuzzySet(GOOD, membershipGood);
		distanceInputs[2].setInterval(goodInterval);
				
		distanceMap.put(BAD, distanceInputs[0]);
		distanceMap.put(AVERAGE, distanceInputs[1]);
		distanceMap.put(GOOD,distanceInputs[2]);

		return distanceInputs;
		
	}
	

	
	@Override
	void fuzzyRules(FuzzySet[] inputSet) {
		FuzzySet []outputs = new FuzzySet[9];
		
		outputs[0] = new FuzzySet(INSUFFICIENT, min(timeMap.get(BAD).getDegreeOfMembership(), distanceMap.get(BAD).getDegreeOfMembership()));
		outputs[1] = new FuzzySet(INSUFFICIENT, min(timeMap.get(AVERAGE).getDegreeOfMembership(), distanceMap.get(AVERAGE).getDegreeOfMembership()));//here
		outputs[2] = new FuzzySet(SUFFICIENT, min(timeMap.get(GOOD).getDegreeOfMembership(), distanceMap.get(GOOD).getDegreeOfMembership()));
		outputs[3] = new FuzzySet(AVERAGE_SUFICIENT, min(timeMap.get(GOOD).getDegreeOfMembership(), distanceMap.get(BAD).getDegreeOfMembership()));
		outputs[4] = new FuzzySet(SUFFICIENT, min(timeMap.get(GOOD).getDegreeOfMembership(), distanceMap.get(AVERAGE).getDegreeOfMembership())); //here
		outputs[5] = new FuzzySet(AVERAGE_SUFICIENT, min(timeMap.get(AVERAGE).getDegreeOfMembership(), distanceMap.get(GOOD).getDegreeOfMembership()));
		outputs[6] = new FuzzySet(INSUFFICIENT, min(timeMap.get(AVERAGE).getDegreeOfMembership(), distanceMap.get(BAD).getDegreeOfMembership()));
		outputs[7] = new FuzzySet(INSUFFICIENT, min(timeMap.get(BAD).getDegreeOfMembership(), distanceMap.get(GOOD).getDegreeOfMembership()));
		outputs[8] = new FuzzySet(INSUFFICIENT, min(timeMap.get(BAD).getDegreeOfMembership(), distanceMap.get(AVERAGE).getDegreeOfMembership())); //here
		
		auxOutputs = outputs;
		
	}

	@Override
	void agreggation() {
		insufficientOutputs = max(auxOutputs[0], auxOutputs[6]);
		insufficientOutputs = max(insufficientOutputs, auxOutputs[8]);
		
		System.out.println("Insufficient: "+insufficientOutputs.degreeOfMembership);
		
		//Average ones:
		averageOutputs = max(auxOutputs[1], auxOutputs[3]);
		averageOutputs = max(averageOutputs, auxOutputs[7]);
		System.out.println("Average: "+averageOutputs.degreeOfMembership);
		
		//Sufficient ones:
		sufficientOutputs = max(auxOutputs[2], auxOutputs[4]);
		sufficientOutputs = max(sufficientOutputs, auxOutputs[5]);
		System.out.println("sufficient: "+sufficientOutputs.degreeOfMembership);
		
	}

	@Override
	void defuzzifier() {
		//COG = sum(W*x)/sum(W)
		///we need to find the intervals of each one to find the X
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
		//for insufficient outputs
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
