import java.io.BufferedReader;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

class Locations implements Comparable<Locations>{
	String collegeCity;
	String studentCity;
	double distance;
	double score;
	Locations(String cCity,String sCity,int d){
		collegeCity=cCity;
		studentCity=sCity;
		distance=d;
		score=0;
	}
	
	public int compareTo(Locations l){
		return (int)(this.distance-l.distance);
	}
}

public class CreateLocationData {
	Statement stmt;
	private Connection connect;
    final private String url = "jdbc:mysql://localhost/student-college mapper?useSSL=false";
    final private String user = "root";
    final private String passwd = "niranjan";
    private PreparedStatement preparedStatement = null;
//  String cities[]={"Solapur","Sangli","Satara","Nashik","Mumbai","Pune","Nagpur","Karad","Kolhapur","Aurangabad","Amravati","Yawatmal","Washim","Thane","Ratnagiri","Raigad","Sindhudurg","Wardha","Jalgoan","Dhule","Chandrapur","Bhandara","Pandharpur","Latur","Usmanabad","Beed"};
    String cities[]={"Solapur","Sangli","Satara","Nashik","Mumbai","Pune","Nagpur","Karad","Kolhapur","Aurangabad"};
//  Random rand=new Random();
    FileReader reader;
    
    public void connectToDatabase() throws Exception{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url,user,passwd);
			stmt=connect.createStatement();
			stmt.executeUpdate("TRUNCATE TABLE `student-college mapper`.`LocationScores`");
		}
		catch (Exception e) {
			throw e;
		} 
	}
    
    void createLocationTable() {
    	
    	try {
    		ArrayList<Locations> locations=new ArrayList<Locations>();
       // 	reader=new FileReader("/home/niranjan/locations.txt");
        	reader=new FileReader("locations.txt");
			BufferedReader br=new BufferedReader(reader);
			String str="";
			while((str=br.readLine())!=null){
				String tmp[]=str.split(" ");
				locations.add(new Locations(tmp[0],tmp[1],Integer.parseInt(tmp[2])));
			}
			
			Collections.sort(locations);
			double reference=(double)((locations.get((locations.size()/2))).distance);
			double max=-1;
			
			for(int i=0;i<locations.size();i++){
				Locations tmp=locations.get(i);
				
				if(tmp.distance==0)
					tmp.score=100;
				else{
					tmp.score=100*reference/tmp.distance;
					if(tmp.score>max)
						max=tmp.score;
				}
			}
			max=max/100;
			for(int i=0;i<locations.size();i++){
				Locations tmp=locations.get(i);
				if(tmp.distance!=0)
					tmp.score=(tmp.score/max);
			}
			for(int i=0;i<locations.size();i++){
				
				Locations tmp=locations.get(i);
				String query="INSERT INTO `student-college mapper`.`LocationScores` (`CollegeCity`, `StudentCity` ,`Score`) VALUES (CITY1, CITY2 , ? );";
				query=query.replaceFirst("CITY1", "'"+tmp.collegeCity+"'");
				query=query.replaceFirst("CITY2", "'"+tmp.studentCity+"'");
				try {
					preparedStatement=connect.prepareStatement(query);
					preparedStatement.setInt(1,(int)tmp.score);
					preparedStatement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    }
}
