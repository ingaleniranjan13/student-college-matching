import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StudentCollegeMapper{

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
//			FileWriter writer=new FileWriter("/home/niranjan/lists.txt",false);
    		FileWriter writer=new FileWriter("lists.txt",false);
			writer.write("");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		url = "jdbc:mysql://localhost/student-college mapper?useSSL=false";
	    user = "root";
	    passwd = "niranjan";
	}
	
    void rankGenerator(){
		try {
			Statement stmt=connect.createStatement();
			ResultSet resultSet=stmt.executeQuery("select * from `student-college mapper`.`student` order by totalScore desc");
			int rank=1;
			while(resultSet.next()){
				String query="UPDATE `student-college mapper`.`student` SET `rank`=? WHERE `student_id`=?;";
				preparedStatement=connect.prepareStatement(query);
				preparedStatement.setInt(1, rank);
				preparedStatement.setInt(2, resultSet.getInt("student_id"));
				preparedStatement.executeUpdate();
				rank++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
    
    void terminate(String str){
    	
		JFrame frame=new JFrame("Terminate");
		frame.setVisible(true);
		JPanel panel=new JPanel();
		JLabel label=new JLabel("Matches Generated Successfully");
		panel.add(label);
		frame.add(panel);
		panel.add(new JLabel(str));
		frame.setSize(350, 120);
		frame.addWindowListener(new WindowListener(){

			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("It's Working!!");
				System.exit(0);
				
			}

			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
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
						resultSet.getInt("placement_and_median_salary"),
						resultSet.getInt("research_and_professional_practices"),
						resultSet.getInt("teaching_and_learning_resources"),
						resultSet.getInt("infrastructure"),
						resultSet.getInt("fee_structure"),
						resultSet.getInt("competitiveness"),
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
    			
    			System.out.println("Inside createPreferenceList()\n");
    			
    			weights=defaultPreferences.getSelectedFactorsWeights();
    			skipCount=0;
        		String allPriorities=resultSet.getString("priorities");
        		for(int i=0;i < numberOfFactors-offset ;i++){
        			priorities[i]=Integer.parseInt(""+allPriorities.charAt(i));
        			System.out.println("Inside createPreferenceList(), line 193\n");
        				
        		}
            	for(int i=numberOfFactors-offset;i<numberOfFactors;i++){
        			if(allPriorities.charAt(i)=='0'){
            			priorities[i]=-1;
            			skipCount++;
            		}
            		else
            			priorities[i]=Integer.parseInt(""+allPriorities.charAt(i));
            	
        			System.out.println("student-college mapper line 204\n");
            		
            	}
        		int permutation[]=new int[numberOfFactors-skipCount];
    			double customWeights[]=new double[numberOfFactors-skipCount];
    			double sum=0;
    			for(int i=0;i<numberOfFactors-offset;i++){
    				permutation[priorities[i]-1]=i+1;
    				customWeights[i]=weights[i];
    				sum+=customWeights[i];
    			
    			
    				System.out.println("student-college mapper line 216\n");
            	}
    			for(int i=numberOfFactors-offset,j=numberOfFactors-offset;i<numberOfFactors;i++){
    				System.out.println("student-college mapper line 219\n");
            		
    				if(priorities[i]!=-1){
    					permutation[priorities[i]-1]=j+1;
    					customWeights[j]=weights[i];
    					sum+=customWeights[j];
    					j++;
    				}
    			}
    			for(int i=0;i<customWeights.length;i++){
    				customWeights[i]/=sum;
    				System.out.println("student-college mapper line 230\n");
            	}
    			
    			
    			fuzzy.fuzzyComputation(permutation,customWeights);
    			
    			
    			for(int j=0,i=0;i<weights.length;i++){
    				System.out.println("student-college mapper line 234\n");
            		
    				if(priorities[i]!=-1){
    					weights[i]=customWeights[j];
    					j++;
    				}
    				else
    					weights[i]=0;
    			}
    			
    			System.out.println("Student-college mapper ,last chech, line 246");
    			
    			locationScoreCalculator.calculateLocationScore(connect,resultSet.getString("city"),collegeList);
    		
    			System.out.println("Line 244,chech\n");
    			for(int i=0;i<weights.length;i++)
    				System.out.println(weights[i]);
    			
    			for(int i=0;i<collegeList.length;i++){
    				
    				System.out.println("student-college mapper line 245\n");
            		
    				College tmp=collegeList[i];
    				tmp.score=0;
    				int scores[]=tmp.getScores();
    				for(int i1=0;i1<scores.length;i1++)
    					tmp.score+=scores[i1]*weights[i1];
    			}
    			sortBranches.sortBranchWise(resultSet,collegeList);
    			fileWriter.writeListToFile(resultSet.getInt("student_id"),resultSet.getInt("fee_limit"),collegeList);
    	//		allocateCollege();
    		}
    	} catch (SQLException e) {
  			e.printStackTrace();
  		}/*finally{
  			try {
				connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
  		}*/
    }
    
  /*  void allocateCollege(){
		try{
			int allocatedCollegeID=-1;

    		for(int i=0;i<collegeList.length;i++){
    		if(collegeList[i].vacancies>0 && collegeList[i].fees <= resultSet.getInt("fee_limit")){
    				collegeList[i].vacancies--;
    				allocatedCollegeID=collegeList[i].college_id;
    				break;
    			}
    		}
    		if(allocatedCollegeID!=-1){
    			try {
					preparedStatement=connect.prepareStatement("UPDATE `student-college mapper`.`student` SET `college_id`=?, `status`='2' WHERE `student_id`=?;");
					preparedStatement.setInt(1,allocatedCollegeID);
					preparedStatement.setInt(2,resultSet.getInt("student_id"));
					preparedStatement.executeUpdate();
    			} catch (SQLException e) {
					e.printStackTrace();
				}
    		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
    }
    */
    void allocateCollege(){
    	try {
    		int allocatedCollegeID=-1;
      //  	FileReader reader=new FileReader("/home/niranjan/lists.txt");
    		FileReader reader=new FileReader("lists.txt");
			BufferedReader br=new BufferedReader(reader);
			String str="",list[];
			while((str=br.readLine())!=null){
				
				System.out.println("student-college mapper line 303\n");
        		
				list=str.split(" ");
				for(int i=1;i<list.length;i++){
					
					System.out.println("student-college mapper line 308\n");
            		
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
						preparedStatement=connect.prepareStatement("UPDATE `student-college mapper`.`student` SET `college_id`=?, `status`='2' WHERE `student_id`=?;");
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
