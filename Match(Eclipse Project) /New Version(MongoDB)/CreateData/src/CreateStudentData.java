import java.io.Serializable;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

class Branch{
	boolean set_flg;
	int window;
	int priority;
	public String toString()
	{
		return "{ set_flg:"+set_flg+","+"window:"+window+","+"priority:"+priority+"}";
	}
	static String getbranches(Branch[] branches) {
		String s="[";
		int i=0;
		for(i=0;i<branches.length-1;i++) {
			s+=branches[i].toString();
			s+=",";
		}
		s+=branches[i].toString();
		s+="]";
		return s;
	}
}


class Student extends BasicDBObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int student_id;
	String name;
	int rank;
	String location;
	String preferences;
	Branch[] branches;
	boolean gender;
	boolean category;
	int fee_limit;
	int college_id;
	
	public String toString()
	{
		String s="{ name:"+"'"+name+"'"+","+
					"rank:"+rank+","+
					"location:"+"'"+location+"'"+","+
					"preferences:"+"'"+preferences+"',"+
					"branch:"+Branch.getbranches(branches)+","+
					"gender:"+gender+","+
					"category:"+category+","+
					"fee_limit:"+fee_limit+					
				"}";
		return s;
	}
}


class CreateStudentData {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			MongoClient mongoClient=new MongoClient();
			DB db=mongoClient.getDB("Match");
			DBCollection collection = db.getCollection("students");
			Student student=new Student();
			student.student_id=1;
			student.name="oj";
			student.rank=97;
			student.location="Sangli";
			student.preferences="1234567";
			student.gender=true;
			student.category=true;
			student.fee_limit=60000;
			student.branches=new Branch[2];
			student.branches[0]=new Branch();
			student.branches[1]=new Branch();
			student.branches[0].priority=1;
			student.branches[0].set_flg=true;
			student.branches[0].window=20;
			student.branches[1].priority=2;
			student.branches[1].set_flg=true;
			student.branches[1].window=10;
			String json=student.toString();
			DBObject dbObject = (DBObject)JSON.parse(json);
			collection.insert(dbObject);
			mongoClient.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}