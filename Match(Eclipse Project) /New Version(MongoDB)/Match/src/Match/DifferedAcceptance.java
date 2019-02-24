package Match;

import java.util.Collections;
import java.util.LinkedList;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class DifferedAcceptance {
	
	LinkedList<College> collegeList;
	MongoClient mongoClient;
	DB db;
	DBCollection collection1;
	DBCollection collection2;
	LinkedList<Student> students;	
	
	void readStudents(DBCollection students_collection)
	{
		DBCursor cursor=students_collection.find();
		while(cursor.hasNext())
		{
			DBObject student=cursor.next();
			int rank=(int)student.get("rank");
			BasicDBList collegeList=(BasicDBList)student.get("colleges");
			int[] colleges=new int[collegeList.size()];
			for(int i=0;i<colleges.length;i++)
				colleges[i]=(int)collegeList.get(i);
			Student studentObject=new Student();
			studentObject.rank=rank;
			studentObject.colleges=colleges;
			students.addFirst(studentObject);
			
			
		//	System.out.println("rank="+studentObject.rank);
			/*
			for(int k=0;k<studentObject.colleges.length;k++) {
				System.out.println("college id="+studentObject.colleges[k]);
			}
			*/
			
		}
		Collections.sort(students);
		/*
		for(int x=0;x<students.size();x++)
		{
			System.out.println("rank="+students.get(x).rank);
		}
		*/
		System.out.println("Number of students:"+students.size());
	}

	void readColleges(DBCollection colleges_collection)
	{
		DBCursor cursor=colleges_collection.find();
		while(cursor.hasNext())
		{
			DBObject college=cursor.next();
			int college_id=(int)college.get("college_id");
			College clg=new College();
			clg.college_id=college_id;
			clg.remainingCapacity=100;
			collegeList.addFirst(clg);
		}
		/*
		for(int k=0;k<collegeList.size();k++) {
			System.out.println("college_id="+collegeList.get(k).college_id);
			System.out.println("remaining capacity="+collegeList.get(k).remainingCapacity);
		}
		*/
	}
	
	void assignment()
	{
		for(int i=0;i<students.size();i++)
		{
			int flg=0;
			for(int j=0;j<students.get(i).colleges.length;j++)
			{
				for(int k=0;k<collegeList.size();k++)
				{
					if(students.get(i).colleges[j]==collegeList.get(k).college_id)
					{
						if(collegeList.get(k).remainingCapacity>0) {
							students.get(i).college_id=collegeList.get(k).college_id;
							collegeList.get(k).remainingCapacity-=1;
							
							System.out.println("student_rank:"+students.get(i)+" college_id:"+collegeList.get(k).college_id);
							
							flg=1;
							break;
						}
						else
							break;
					}
				}
				if(flg==1) {
					break;
				}
			}	
		}
	}
	
	void writeMatchesToDatabase()
	{
		for(int i=0;i<students.size();i++)
		{
			int student_rank=students.get(i).rank;
			int college_alloted=students.get(i).college_id;
			System.out.println("Rank:"+student_rank+" college_alloted:"+college_alloted);
			BasicDBObject query=new BasicDBObject();
			query.put("rank", student_rank);
			DBCursor cursor=collection2.find(query);
			while(cursor.hasNext())
			{
				DBObject student=cursor.next();
				String onj1="{\"rank\":"+student.get("rank")+"}";
				String obj2="{$set:{\"college_alloted\":"+college_alloted+"}}";
				collection2.update((DBObject)JSON.parse(onj1),(DBObject)JSON.parse(obj2));
			
			}	
		}
		
	}
	
	public static void main(String[] args)
	{
		new DifferedAcceptance();
	}
	
	DifferedAcceptance()
	{
		try
		{
			mongoClient=new MongoClient();
			db=mongoClient.getDB("Match");	
			collection1=db.getCollection("colleges");
			collection2=db.getCollection("students");
			students=new LinkedList<Student>();
			collegeList=new LinkedList<College>();
			
			readStudents(collection2);
			readColleges(collection1);
			assignment();
			writeMatchesToDatabase();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
