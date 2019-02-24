import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CalculateLocationScore {
	ResultSet resultSet;
	PreparedStatement preparedStatement;
	CalculateLocationScore(){
		
	}
	void calculateLocationScore(Connection connect,String city,College[] collegeList){
		for(int i=0;i<collegeList.length;i++){
			try {
				preparedStatement=connect.prepareStatement("select Score from `student-college mapper`.`LocationScores` where CollegeCity=? and StudentCity=? ;");
				preparedStatement.setString(1,collegeList[i].city);
				preparedStatement.setString(2,city);
				resultSet=preparedStatement.executeQuery();
				resultSet.next();
				collegeList[i].locationScore=resultSet.getInt(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
