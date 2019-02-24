
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Random;

public class CreateStudentData {
	Statement stmt;
	private Connection connect;
    final private String url = "jdbc:mysql://localhost/student-college mapper?useSSL=false";
    final private String user = "root";
    final private String passwd = "niranjan";
    private PreparedStatement preparedStatement = null;
  //  String cities[]={"Solapur","Sangli","Satara","Nashik","Mumbai","Pune","Nagpur","Karad","Amravati","Yawatmal","Washim","Aurangabad","Thane","Ratnagiri","Raigad","Sindhudurg","Wardha","Jalgoan","Dhule","Chandrapur","Kolhapur","Bhandara","Pandharpur","Latur","Usmanabad","Beed"};
    String cities[]={"Solapur","Sangli","Satara","Nashik","Mumbai","Pune","Nagpur","Karad","Kolhapur","Aurangabad"};

    Random rand=new Random();
    LinkedList<String> branches;
    LinkedList<Integer> permutation;
    LinkedList<Integer> permutation1;
    LinkedList<Integer> permutation2;
    LinkedList<Integer> permutation3;
    LinkedList<Integer> permutation4;
    
    private final int numberOfStudents;

    int getFeeLimit(){
    	int fees[]={40000,60000,100000,150000,200000};
    	return fees[rand.nextInt(fees.length)];
    }
    
    CreateStudentData(int students){
    	branches=new LinkedList<String>();
    	permutation=new LinkedList<Integer>();
    	permutation1=new LinkedList<Integer>();
    	permutation2=new LinkedList<Integer>();
    	permutation3=new LinkedList<Integer>();
    	permutation4=new LinkedList<Integer>();
    	
    	numberOfStudents=students;
    	
    	String branchSet[]={"1","2","3","4","5","6","7","8","9"};
        for(int i=0;i<branchSet.length;i++)
        	branches.add(branchSet[i]);
        int priorities[]={1,2,3,4,5,6,7,8};
        for(int i=0;i<priorities.length;i++)
        	permutation.add(new Integer(priorities[i]));
        int priorities1[]={1,2,3,4,5,6,7};
        for(int i=0;i<priorities1.length;i++)
        	permutation1.add(new Integer(priorities1[i]));
        int priorities2[]={1,2,3,4,5,6};
        for(int i=0;i<priorities2.length;i++)
        	permutation2.add(new Integer(priorities2[i]));
        int priorities3[]={1,2,3,4,5};
        for(int i=0;i<priorities3.length;i++)
        	permutation3.add(new Integer(priorities3[i]));
        int priorities4[]={1,2,3,4};
        for(int i=0;i<priorities4.length;i++)
        	permutation4.add(new Integer(priorities4[i]));
    }
    
    String allocateCity(){
    	int index=rand.nextInt(cities.length);
    	return cities[index];
    }
    
    int getSubjectScore(){
    	return 40+rand.nextInt(60);
    }
    
    int[] setPriorities(){
    	int returnValue[]={};
    	int selectPremutation=rand.nextInt(5);
    	if(selectPremutation==0){
    		returnValue=new int[permutation.size()];
    		@SuppressWarnings("unchecked")
    		LinkedList<Integer> tmpPermutation=(LinkedList<Integer>)permutation.clone();
    		int i=0;
    		while(i<returnValue.length){
    			int index=rand.nextInt(tmpPermutation.size());
    			returnValue[i]=((Integer)(tmpPermutation.remove(index))).intValue();
    			i++;
    		}
    	}
    	else if(selectPremutation==1){
    		returnValue=new int[permutation1.size()];
        	@SuppressWarnings("unchecked")
    		LinkedList<Integer> tmpPermutation=(LinkedList<Integer>)permutation1.clone();
        	int i=0;
        	while(i<returnValue.length){
        		int index=rand.nextInt(tmpPermutation.size());
        		returnValue[i]=((Integer)(tmpPermutation.remove(index))).intValue();
        		i++;
        	}
    	}else if(selectPremutation==2){
    		returnValue=new int[permutation2.size()];
        	@SuppressWarnings("unchecked")
    		LinkedList<Integer> tmpPermutation=(LinkedList<Integer>)permutation2.clone();
        	int i=0;
        	while(i<returnValue.length){
        		int index=rand.nextInt(tmpPermutation.size());
        		returnValue[i]=((Integer)(tmpPermutation.remove(index))).intValue();
        		i++;
        	}
    	}else if(selectPremutation==3){
    		returnValue=new int[permutation3.size()];
        	@SuppressWarnings("unchecked")
    		LinkedList<Integer> tmpPermutation=(LinkedList<Integer>)permutation3.clone();
        	int i=0;
        	while(i<returnValue.length){
        		int index=rand.nextInt(tmpPermutation.size());
        		returnValue[i]=((Integer)(tmpPermutation.remove(index))).intValue();
        		i++;
        	}
    	}
    	else{
    		returnValue=new int[permutation4.size()];
        	@SuppressWarnings("unchecked")
    		LinkedList<Integer> tmpPermutation=(LinkedList<Integer>)permutation4.clone();
        	int i=0;
        	while(i<returnValue.length){
        		int index=rand.nextInt(tmpPermutation.size());
        		returnValue[i]=((Integer)(tmpPermutation.remove(index))).intValue();
        		i++;
        	}
    	}
    	return returnValue;
    }
    
    String[][] setBranches(){
    	String returnValue[][],set_a[],set_c[];
    	returnValue=new String[2][];
    	int setSize=rand.nextInt(10);
    	int cardinalitya=0;
    	if(setSize!=0)
    		cardinalitya=setSize;
    	int cardinalityc=0;
    	if(9-setSize!=0)
    		cardinalityc=9-setSize;
   
        set_a=new String[cardinalitya];
    	set_c=new String[cardinalityc];
    	@SuppressWarnings("unchecked")
		LinkedList<String> tmpBranches=(LinkedList<String>) branches.clone();
    	for(int i=0;i<cardinalitya;i++){
    		int index=rand.nextInt(tmpBranches.size());
    		set_a[i]=(String)tmpBranches.remove(index);
    	}
    	for(int i=0;i<cardinalityc;i++){
    		int index=rand.nextInt(tmpBranches.size());
    		set_c[i]=(String)tmpBranches.remove(index);
    	}
    	returnValue[0]=set_a;
    	returnValue[1]=set_c;
    	return returnValue;
    }
    
	public void connectToDatabase() throws Exception{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url,user,passwd);
			stmt=connect.createStatement();
			stmt.executeUpdate("TRUNCATE TABLE `student-college mapper`.`student`");
		}
		catch (Exception e) {
			throw e;
		} 
		
	}
	
	void createStudentTable(){
		try{
   			for(int i=1;i<=numberOfStudents;i++){
   				int priorities[]=setPriorities();
   				String sets[][]=setBranches();
   				String set_a[]=sets[0];
   				String set_c[]=sets[1];
   				String city=allocateCity();
   				int student_id=i;
   				int feeLimit=getFeeLimit();
   				String query="INSERT INTO `student-college mapper`.`student` (`student_id`, `username`, `password`, `firstname`, `middlename`, `lastname`, `city`, `email_id`, `contact_number`, `maths_marks`, `physics_marks`, `chemistry_marks`,`totalScore`, `priorities` , `set_a`, `set_c` ,`fee_limit`,`status`) VALUES (STUDENTID, 'Omkar', 'OmJegO6588#', 'Omkar', 'Sanjay', 'Jadhaw', CITY , 'omjego@gmail.com', '8983616877', ? , ? , ? , ? , PERMUTATION , SET_A , SET_C , FEELIMIT , '1');";
				String stra="'";
				String strc="'";
				int itr=0;
				if(set_a.length!=0){
					for(itr=0;itr<set_a.length;itr++)
	    				stra+=set_a[itr];
					stra+="'";
				}
				else
					stra="null";
				if(set_c.length!=0){
					for(itr=0;itr<set_c.length;itr++)
						strc+=set_c[itr];
					strc+="'";
				}
				else
					strc="null";
				    			
    			query=query.replaceFirst("SET_A", stra);
    			query=query.replaceFirst("SET_C", strc);
    		//	query=query.replaceFirst("RANK", "'"+(new Integer(rank)).toString()+"'" );
    			query=query.replaceFirst("STUDENTID","'" + (new Integer(student_id)).toString()+"'");
    			query=query.replaceFirst("CITY", "'"+city+"'");
    			query=query.replaceFirst("FEELIMIT", "'"+feeLimit+"'");
    			String permutation="";
    			
    			for(itr=0;itr<priorities.length;itr++)
					permutation+=priorities[itr];
			
    			StringBuilder str=new StringBuilder(permutation);
    			
    			for(itr=0;itr<this.permutation.size()-priorities.length;itr++){
    				int index=rand.nextInt(str.length());
    				if(index<4){
    					int offset=0;
    					if((str.length()-4)!=0)
    						offset=rand.nextInt(str.length()-4);
    					index=4+offset;
    				}
    				str.insert(index, "0");
    			}
    			
    			permutation=new String(str);
    			permutation="'"+permutation+"'";
        		query=query.replaceFirst("PERMUTATION",permutation);
    			preparedStatement = connect.prepareStatement(query);
    			
    			int totalScore=0;
    			int reference=getSubjectScore();
    			for(itr=1;itr<=3;itr++){
    	    			int tmp=5+rand.nextInt(20);
    					double difference=(((double)tmp)/100)*reference;
    					int flg=rand.nextInt(2);
    					if(flg==0&&(reference+difference)<101){
    						preparedStatement.setInt(itr,(int)(reference+difference));
    						totalScore+=(int)(reference+difference);
    					}
    	    			else{
    	    				preparedStatement.setInt(itr,(int)(reference-difference));
    	    				totalScore+=(int)(reference-difference);
    	    			}
    			}
    			
    			preparedStatement.setInt(4,totalScore);
    			preparedStatement.executeUpdate();
   			}
   		}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
/*	void rankGenerator(){
		try {
			stmt=connect.createStatement();
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
		
	}*/
}
