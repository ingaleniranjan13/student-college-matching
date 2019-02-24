import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class CreateCollegeData {

	Statement stmt;
	private Connection connect;
    final private String url = "jdbc:mysql://localhost/student-college mapper?useSSL=false";
    final private String user = "root";
    final private String passwd = "niranjan";
    private PreparedStatement preparedStatement = null;
    private final int numberOfColleges;
    String cities[]={"Solapur","Sangli","Satara","Nashik","Mumbai","Pune","Nagpur","Karad","Kolhapur","Aurangabad"};
    Random rand=new Random();
    String branches[]={"civil","mech","electrical","electronics","cs","it","entc","production","chemical"};
    
    CreateCollegeData(int colleges){
    	numberOfColleges=colleges;
    }
    
    public void connectToDatabase() throws Exception{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url,user,passwd);
			stmt=connect.createStatement();
			stmt.executeUpdate("TRUNCATE TABLE `student-college mapper`.`colleges`");
		}
		catch (Exception e) {
			throw e;
		} 
	}
    
    int getFees(){
    	int fees[]={20000,30000,40000,50000,60000,70000,80000,90000,100000};
    	return fees[rand.nextInt(fees.length)];
    }
    
    String allocateCity(){
    	int index=rand.nextInt(cities.length);
    	return cities[index];
    }
    
    String allocateBranch(){
    	int index=rand.nextInt(branches.length);
    	return branches[index];
    }
    
    void createCollegeTable(){
    	for(int i=0;i<numberOfColleges;i++){
    		String str_id="'"+(new Integer(i+1)).toString()+"'";
    		String city="'"+allocateCity()+"'";
    		String branch="'"+allocateBranch()+"'";
    		String query="INSERT INTO `student-college mapper`.`colleges` (`college_id`, `college_name`, `branch`, `intake`, `city`, `quality_of_education`, `placement_and_median_salary`, `research_and_professional_practices`,`teaching_and_learning_resources`, `infrastructure`,`competitiveness`, `fee_structure` ,`fees`) VALUES (ID, 'ENG_CLG', BRANCH , '60', CITY, ?, ?, ?, ?, ?, ?, ?, FEES);";
    		int fees=getFees();
    		query=query.replaceFirst("ID", str_id);
    		query=query.replaceFirst("CITY", city);
    		query=query.replaceFirst("BRANCH", branch);
    		query=query.replaceFirst("FEES", "'"+(new Integer(fees)).toString()+"'");
    		try {
				preparedStatement = connect.prepareStatement(query);
				int itr=1;
				int reference=rand.nextInt(101);
				for(itr=1;itr<7;itr++){
	    		//	preparedStatement.setInt(itr, rand.nextInt(100));
					int tmp=5+rand.nextInt(20);
					double difference=(((double)tmp)/100)*reference;
					int flg=rand.nextInt(2);
					if(flg==0 && (reference+difference)<101)
						preparedStatement.setInt(itr, (int)(reference+difference));
					else
						preparedStatement.setInt(itr, (int)(reference-difference));
				}
				
				if(fees==20000)
					preparedStatement.setInt(itr,100);
				else if(fees==30000)
					preparedStatement.setInt(itr,90);
				else if(fees==40000)
					preparedStatement.setInt(itr,80);
				else if(fees==50000)
					preparedStatement.setInt(itr,70);
				else if(fees==60000)
					preparedStatement.setInt(itr,60);
				else if(fees==70000)
					preparedStatement.setInt(itr,40);
				else if(fees==80000)
					preparedStatement.setInt(itr,30);
				else if(fees==90000)
					preparedStatement.setInt(itr,20);
				else
					preparedStatement.setInt(itr,10);
		/*		if(fees>=40000 && fees <=80000)
					preparedStatement.setInt(itr,100);
				if(fees>=80000 && fees <=120000)
					preparedStatement.setInt(itr,90);
				if(fees>=120000 && fees <=160000)
					preparedStatement.setInt(itr,80);
				if(fees>=160000 && fees <=200000)
					preparedStatement.setInt(itr,70);
				if(fees>=200000 && fees <=240000)
					preparedStatement.setInt(itr,60);
				if(fees>=240000 && fees <=280000)
					preparedStatement.setInt(itr,50);
				if(fees>=280000 && fees <=320000)
					preparedStatement.setInt(itr,40);
				if(fees>=320000 && fees <=360000)
					preparedStatement.setInt(itr,30);
				if(fees>=360000 && fees <=400000)
					preparedStatement.setInt(itr,20);
				if(fees>=400000 )
					preparedStatement.setInt(itr,10);*/
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
}
