import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class StudentCollegeMapper {

	HashMap<String,Integer> map;
	Connection connect;
	final private String url;
	final private String user;
    final private String passwd;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    College[] collegeList;
    int priorities[];
    double[] weights;
    Fuzzy fuzzy;
    CalculateLocationScore locationScoreCalculator;
    SortBranchWise sortBranches;
    WriteListToFile fileWriter;
    
    StudentCollegeMapper(){
    	fuzzy=new Fuzzy();
    	locationScoreCalculator=new CalculateLocationScore();
    	sortBranches=new SortBranchWise(); 
    	fileWriter=new WriteListToFile();
    	try {
			FileWriter writer=new FileWriter("/home/niranjan/lists.txt",false);
			writer.write("");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		url = "jdbc:mysql://localhost/student-college mapper?useSSL=false";
	    user = "root";
	    passwd = "niranjan";
	}
	
    public void connectToDatabase() throws Exception{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(url,user,passwd);
        }
        catch (Exception e) {
        	throw e;
        } 
    }
    
    public void importColleges(){
        try {
			statement = connect.createStatement();
			resultSet = statement
			    .executeQuery("SELECT count(*) FROM `student-college mapper`.`colleges`;");
			resultSet.next();
			int size=resultSet.getInt(1);
			collegeList=new College[size];
			resultSet = statement
				    .executeQuery("SELECT * FROM `student-college mapper`.`colleges`;");
			int itr=0;
			map=new HashMap<String,Integer>();
			while(resultSet.next()){
	    		map.put( new String( (new Integer(resultSet.getInt("college_id"))).toString() ),new Integer(resultSet.getInt("intake")));
        		collegeList[itr]=new College(
						resultSet.getInt("college_id"),
						resultSet.getString("college_name"),
						resultSet.getString("branch"),
						resultSet.getInt("intake"),
						resultSet.getString("city"),
						resultSet.getInt("quality_of_education"),
						resultSet.getInt("placement"),
						resultSet.getInt("research_and_professional_practices"),
						resultSet.getInt("infrastructure"),
						resultSet.getInt("fee_structure"),
						resultSet.getInt("fees")
						);
    			itr++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    void createPrefferenceLists(){
    	try {
    
    		int skipCount=0;
    		GetDefaultPreferences defaultPreferences=new GetDefaultPreferences();
    		int offset=defaultPreferences.offset();
    		int numberOfFactors=defaultPreferences.numberOfFactorsSelected();
    		resultSet = statement
  			    .executeQuery("SELECT * FROM `student-college mapper`.student ORDER BY rank;");
    		priorities=new int[numberOfFactors];
    		while(resultSet.next()){
    			weights=defaultPreferences.getSelectedFactorsWeights();
    			skipCount=0;
        		String allPriorities=resultSet.getString("priorities");
        		for(int i=0;i < numberOfFactors-offset ;i++)
        			priorities[i]=Integer.parseInt(""+allPriorities.charAt(i));
            	for(int i=numberOfFactors-offset;i<numberOfFactors;i++){
        			if(allPriorities.charAt(i)=='-'){
            			priorities[i]=-1;
            			skipCount++;
            		}
            		else
            			priorities[i]=Integer.parseInt(""+allPriorities.charAt(i));
            	}
        		int permutation[]=new int[numberOfFactors-skipCount];
    			double customWeights[]=new double[numberOfFactors-skipCount];
    			double sum=0;
    			for(int i=0;i<numberOfFactors-offset;i++){
    				permutation[priorities[i]-1]=i+1;
    				customWeights[i]=weights[i];
    				sum+=customWeights[i];
    			}
    			for(int i=numberOfFactors-offset,j=numberOfFactors-offset;i<numberOfFactors;i++){
    				if(priorities[i]!=-1){
    					permutation[priorities[i]-1]=j+1;
    					customWeights[j]=weights[i];
    					sum+=customWeights[j];
    					j++;
    				}
    			}
    			for(int i=0;i<customWeights.length;i++)
    				customWeights[i]/=sum;
    			fuzzy.fuzzyComputation(permutation,customWeights);
    			for(int j=0,i=0;i<weights.length;i++){
    				
    				if(priorities[i]!=-1){
    					weights[i]=customWeights[j];
    					j++;
    				}
    				else
    					weights[i]=0;
    			}
    			locationScoreCalculator.calculateLocationScore(connect,resultSet.getString("city"),collegeList);
    			for(int i=0;i<collegeList.length;i++){
    				College tmp=collegeList[i];
    				tmp.score=0;
    				int scores[]=tmp.getScores();
    				for(int i1=0;i1<scores.length;i1++)
    					tmp.score+=scores[i1]*weights[i1];
    			}
    			sortBranches.sortBranchWise(resultSet,collegeList);
    			fileWriter.writeListToFile(resultSet.getInt("student_id"),resultSet.getInt("fee_limit"),collegeList);
    		}
    	} catch (SQLException e) {
  			e.printStackTrace();
  		}
    }
    
    void allocateCollege(){
    	try {
    		int allocatedCollegeID=-1;
        	FileReader reader=new FileReader("/home/niranjan/lists.txt");
			BufferedReader br=new BufferedReader(reader);
			String str="",list[];
			while((str=br.readLine())!=null){
				list=str.split(" ");
				for(int i=1;i<list.length;i++){
					Integer college=((Integer)map.get(list[i]));
					int intake=college.intValue();
					if(intake > 0){
						map.remove(list[i]);
						map.put(list[i],new Integer(intake-1));
						allocatedCollegeID=Integer.parseInt(list[i]);
						break;
					}
				}
				if(allocatedCollegeID!=-1){
		    		try {
						preparedStatement=connect.prepareStatement("UPDATE `student-college mapper`.`student` SET `college_id`=? WHERE `student_id`=?;");
						preparedStatement.setInt(1,allocatedCollegeID);
						preparedStatement.setInt(2,Integer.parseInt(list[0]));
						preparedStatement.executeUpdate();
		    		} catch (SQLException e) {
						e.printStackTrace();
					}
		    	}
				allocatedCollegeID=-1;
			}
			br.close();
	    	} catch (Exception e1) {
			e1.printStackTrace();
		}finally{
  				try {
  					connect.close();
  				} catch (SQLException e) {
  					e.printStackTrace();
  			}
  		}
    }
}
