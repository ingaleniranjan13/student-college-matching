import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SortBranchWise {
	
	int start,end;
	String a,c;
	String set_a[],set_c[];
	SortBranchWise(){
		
	}
	
	void sortBranchWise(ResultSet resultSet,College[] collegeList){
		
		boolean flags[]={false,false,false,false,false,false};
		
		@SuppressWarnings("unchecked")
		ArrayList<College> multiList[]=new ArrayList[6];
		for(int i=0;i<6;i++)
			multiList[i]=new ArrayList<College>();
		for(int i=0;i<collegeList.length;i++){
			if(collegeList[i].branch.equals("mech"))
				multiList[0].add(collegeList[i]);
			if(collegeList[i].branch.equals("civil"))
				multiList[1].add(collegeList[i]);
			if(collegeList[i].branch.equals("electrical"))
				multiList[2].add(collegeList[i]);
			if(collegeList[i].branch.equals("electronics"))
				multiList[3].add(collegeList[i]);
			if(collegeList[i].branch.equals("cs"))
				multiList[4].add(collegeList[i]);
			if(collegeList[i].branch.equals("it"))
				multiList[5].add(collegeList[i]);
		}
		try {
			a=resultSet.getString("set_a");
			c=resultSet.getString("set_c");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		int index=0;
		if(a!=null){
			for(int i=0;i<a.length();i++){
				String str=""+a.charAt(i);
				Integer integer=new Integer(Integer.parseInt(str));
				Collections.sort(multiList[integer.intValue()]);
				flags[integer.intValue()]=true;
				for(int j=0; j < multiList[integer.intValue()].size() ;j++){
					collegeList[index]=multiList[integer.intValue()].get(j);
					index++;
				}
			}
		}
		int reverseIndex=collegeList.length-1;
		if(c!=null){
			for(int i=c.length()-1;i>=0;i--){
				String str=""+c.charAt(i);
				Integer integer=new Integer(Integer.parseInt(str));
				Collections.sort(multiList[integer.intValue()]);
				flags[integer.intValue()]=true;
				for(int j=multiList[integer.intValue()].size()-1;j>=0;j--){
					collegeList[reverseIndex]=multiList[integer.intValue()].get(j);
					reverseIndex--;
				}
			}
		}
		int start=index;
		int end=reverseIndex+1;
		for(int i=0;i<flags.length;i++){
			if(flags[i]==false){
				for(int j=0;j<multiList[i].size();j++){
					collegeList[index]=multiList[i].get(j);
					index++;
				}
			}
		}
		Arrays.sort(collegeList,start,end);
	}
}
