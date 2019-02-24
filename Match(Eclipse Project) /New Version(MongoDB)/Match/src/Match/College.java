package Match;

//import java.util.Comparator;

public class College implements Comparable<College>{
	int college_id;
	String college_city;
	String branch;
	FactorScore[] factors;
	double totalScore;
	int Fees;
	int remainingCapacity=100;
	/*
	public int compare(College c1,College c2)
	{
		if(c1.totalScore>c2.totalScore)
		{
			return -1;
		}
		else if(c1.totalScore<c2.totalScore)
		{
			return +1;
		}
		else
			return 0;
	}
	*/
	
	public int compareTo(College c)
	{
		
		if(this.totalScore>c.totalScore)
		{
			return 1;
		}
		else if(this.totalScore<c.totalScore)
		{
			return -1;
		}
		else
			return 0;
		
	//	return (int)(this.totalScore-c.totalScore);
	}
		
}
