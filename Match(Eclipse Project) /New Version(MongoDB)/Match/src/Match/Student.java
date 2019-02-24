package Match;

public class Student implements Comparable<Student>{
	int rank;
	String location;
	String preferenceString;
	Branch[] branches;
	int[] colleges;
	int fee_limit;
	int college_id;
	@Override
	public int compareTo(Student o) {
		// TODO Auto-generated method stub
		if(this.rank<o.rank) {
			return -1;
		}
		else 
			return 1;
	}
	
}
