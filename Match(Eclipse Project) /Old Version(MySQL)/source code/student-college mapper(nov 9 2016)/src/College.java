
public class College implements Comparable<College>{
	
	int college_id;
	String college_name;
	String branch;
	int intake;
	String city;
	int quality_of_education;
	int placement;
	int research_and_professional_practices;
	int infrastructure;
	int fee_structure;
	int fees;
	
	int locationScore=0;
	int score=0;
	int vacancies;
	
	College(
			int college_id,
			String college_name,
			String branch,
			int intake,
			String city,
			int quality_of_education,
			int placement,
			int research_and_professional_practices,
			int infrastructure,
			int fee_structure,
			int fees
			){
		this.college_id=college_id;
		this.college_name=college_name;
		this.branch=branch;
		this.intake=intake;
		this.city=city;
		this.quality_of_education=quality_of_education;
		this.placement=placement;
		this.research_and_professional_practices=research_and_professional_practices;
		this.infrastructure=infrastructure;
		this.fee_structure=fee_structure;
		this.fees=fees;
		
		vacancies=intake;
	}
	
	public int compareTo(College clg){
		return clg.score-this.score;
	}

	int[] getScores(){
		int scores[]={quality_of_education,placement,research_and_professional_practices,infrastructure,locationScore,fee_structure};
		return scores;
	}
}