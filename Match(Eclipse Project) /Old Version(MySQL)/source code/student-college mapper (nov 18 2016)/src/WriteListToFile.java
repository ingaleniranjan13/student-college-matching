import java.io.FileWriter;
import java.io.IOException;

public class WriteListToFile {

	FileWriter writer;
	
	WriteListToFile(){
		
	}
	
	void writeListToFile(int student_id,int fee_limit,College[] collegeList){
		try {
			writer=new FileWriter("lists.txt",true);
//			writer=new FileWriter("/home/niranjan/lists.txt",true);
		String record="";
		record+=(new Integer(student_id)).toString();
		for(int i=0;i<collegeList.length;i++){
			if(fee_limit>= collegeList[i].fees)
				record=record+" "+(new Integer(collegeList[i].college_id)).toString();
		}
		record+="\n";
		writer.write(record);
		writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
