package monitor;

public abstract class Monitor {

	
	
	public class FuzzySet{
		String name;
		float degreeOfMembership;
		float []interval = new float[2];
				
		FuzzySet(String name, float degreeOfMembership){
			this.name = name;
			this.degreeOfMembership = degreeOfMembership;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public float getDegreeOfMembership() {
			return degreeOfMembership;
		}

		public void setDegreeOfMembership(float degreeOfMembership) {
			this.degreeOfMembership = degreeOfMembership;
		}

		public float[] getInterval() {
			return interval;
		}

		public void setInterval(float[] interval) {
			this.interval = interval;
		}
		
		
	}
	
	protected float result; //result of the fuzzy inference stuff
	
	public float getResult(){
		return result;
	}
	public void setResult(float result){
		this.result = result;
	}

	
	abstract void fuzzifier(float timeInput, float distanceInput, float caloriesInput);
	abstract void fuzzyRules(FuzzySet[] inputSet);
	abstract void agreggation();
	abstract void defuzzifier();
	
	float min(float input1, float input2){
		if(input1<=input2)
			return input1;
		else
			return input2;
	}
	
	FuzzySet min(float input1, String name1, float input2, String name2){
		FuzzySet fuzzySet1 = new FuzzySet(name1, input1);
		FuzzySet fuzzySet2 = new FuzzySet(name2, input2);
		if(fuzzySet1.getDegreeOfMembership()<=fuzzySet2.getDegreeOfMembership())
			return fuzzySet1;
		else
			return fuzzySet2;
		
	}
	
	FuzzySet max(float input1, String name1, float input2, String name2){
		FuzzySet fuzzySet1 = new FuzzySet(name1, input1);
		FuzzySet fuzzySet2 = new FuzzySet(name2, input2);
		if(fuzzySet1.getDegreeOfMembership()>=fuzzySet2.getDegreeOfMembership())
			return fuzzySet1;
		else
			return fuzzySet2;
		
	}
	
	FuzzySet max(FuzzySet fuzzySet1, FuzzySet fuzzySet2){
		if(fuzzySet1.getDegreeOfMembership()>=fuzzySet2.getDegreeOfMembership())
			return fuzzySet1;
		else
			return fuzzySet2;
		
	}
	
	float max(float input1, float input2){
		if(input1>=input2)
			return input1;
		else
			return input2;
	}
	
	//x = (y-b)/m
	//remember mAndB[0]->m mAndB[1]->b
	float findInterval(float y, float []mAndB){
		return (y-mAndB[1])/mAndB[0];
	}
	
	float findDegreeOfMembership(float x, float []mAndB){
		return mAndB[0]*x +mAndB[1];
	}
	
	//remember: y = xm + b
	float[] findLinearFunction(float x1, float y1, float x2, float y2){
		float [] mAndB = new float[2];
		mAndB[0] = findM(x1, y1, x2, y2);
		mAndB[1] = findB(x1, y1, mAndB[0]);
		
		
		return mAndB;
	}
	
	float findM(float x1, float y1, float x2, float y2){
		return (y1 - y2)/(x1-x2);
	}
	
	float findB(float x, float y, float m){
		return y - m*x;
	}
	



}
