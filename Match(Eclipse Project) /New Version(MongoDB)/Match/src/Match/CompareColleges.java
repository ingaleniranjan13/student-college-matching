package Match;

import java.util.Comparator;

public class CompareColleges implements Comparator<College>{

	@Override
	public int compare(College c1, College c2) {
		// TODO Auto-generated method stub
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

}
