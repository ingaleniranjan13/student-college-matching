package Fuzzy;

public class Factor {
	
	private String name;
	private double defaultWeight;
	private double currentWeight;
	private int defaultIndex;
	private int currentIndex;
	private double upperLimit;
	private double lowerLimit;
	
	public Factor(String name,double defaultWeight,double currentWeight,int defaultIndex,int currentIndex)	{
		this.name=name;
		this.defaultWeight=defaultWeight;
		this.currentWeight=currentWeight;
		this.defaultIndex=defaultIndex;
		this.currentIndex=currentIndex;
	}
	
	public String toString()
	{
		return "{"+
				"name:"+"'"+name+"'"+","+
				"defaultWeight:"+defaultWeight+","+
				"defaultIndex:"+defaultIndex+
			"}";
	}
	
	public void setCurrentWeight(double currentWeight) {
		this.currentWeight=currentWeight;
	}
	
	public void setDefaultIndex(int defaultIndex)
	{
		this.defaultIndex=defaultIndex;
	}
	
	public void setUpperLimit(double upperlimit) {
		this.upperLimit=upperlimit;
	}
	
	public void setLowerLimit(double lowerLimit) {
		this.lowerLimit=lowerLimit;
	}
	
	public void setCurrentIndex(int currentindex)
	{
		this.currentIndex=currentindex;
	}
	
	public double getUpperLimit() {
		return this.upperLimit;
	}
	
	public double getLowerLimit() {
		return this.lowerLimit;
	}
	
	public int getDefaultIndex() {
		return this.defaultIndex;
	}
	
	public double getDefaultWeight() {
		return this.defaultWeight;
	}
	
	public double getCurrentWeight() {
		return this.currentWeight;
	}
	
	public int getCurrentIndex() {
		return this.currentIndex;
	}

	public String getName() {
		return this.name;
	}	
}
