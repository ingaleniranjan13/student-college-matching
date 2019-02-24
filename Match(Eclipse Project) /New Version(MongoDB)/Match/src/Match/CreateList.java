package Match;

import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class CreateList {

		
		public void addList1()
		{
			try
			{
				MongoClient mongoclient=new MongoClient("localhost",27017);
				DB db=mongoclient.getDB("Match");
				System.out.println("connected");
				
				DBCollection collection1=db.getCollection("colleges");
				DBCollection collection2=db.getCollection("students");
				DBCursor college_cursor=collection1.find();
				System.out.println("College count:"+college_cursor.count());
				
				while(college_cursor.hasNext())
				{
					DBObject college=college_cursor.next();
					String college_city=(String)college.get("college_city");
					System.out.println("College:"+college.get("college_name"));
					
					BasicDBObject query = new BasicDBObject();
					 query.put("location", college_city);
					DBCursor student_cursor=collection2.find(query);
					System.out.println("Home:"+student_cursor.count());
		
					ArrayList<Integer> home_university_open=new ArrayList<Integer>();
					
					while(student_cursor.hasNext())
					{
					
						DBObject student=student_cursor.next();
						//System.out.println(student);
						//System.out.println(student.get("rank"));
						
						home_university_open.add((Integer)student.get("rank"));
						//System.out.println(student.get("location")+" "+(Integer)student.get("rank"));
					}
					
					BasicDBObject nequery = new BasicDBObject();
					 
					nequery.put("location", new BasicDBObject("$ne", college_city));
					student_cursor=collection2.find(nequery);
					System.out.println("Non-Home:"+student_cursor.count());
					
					while(student_cursor.hasNext())
					{
						DBObject student=student_cursor.next();
					//	System.out.println(student.get("location")+" "+(Integer)student.get("rank"));
						home_university_open.add((Integer)student.get("rank"));
						
						//System.out.println(student);
					}
					
					if(home_university_open.size()!=0)
					{
						System.out.println("size of AL:"+home_university_open.size());
						String onj1="{\"college_name\":"+"'"+college.get("college_name")+"'"+"}";
						String obj2="{$set:{\"students\":"+getJSONAttribute(home_university_open)+"}}";
						collection1.update((DBObject)JSON.parse(onj1),(DBObject)JSON.parse(obj2));
					
					}
					
					
				}
			//	System.out.println(home_university_open);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		
		public void addList2()
		{
			try
			{
				MongoClient mongoclient=new MongoClient("localhost",27017);
				DB db=mongoclient.getDB("Match");
				System.out.println("connected");
				
				DBCollection collection1=db.getCollection("colleges");
				DBCollection collection2=db.getCollection("students");
				
				DBCursor college_cursor=collection1.find();
				while(college_cursor.hasNext())
				{
					DBObject college=college_cursor.next();
					String college_city=(String)college.get("college_city");
					//home university girls
					ArrayList<Integer> home_university_open_girls=new ArrayList<Integer>();
					
					BasicDBObject query1 = new BasicDBObject();
					 query1.put("location", college_city);
					 query1.put("gender", false);
					 DBCursor student_cursor=collection2.find(query1);
					 System.out.println("home girls:"+student_cursor.count());
					while(student_cursor.hasNext())
					{
						
						DBObject student=student_cursor.next();
						home_university_open_girls.add((Integer)student.get("rank"));
						System.out.println((String)student.get("location")+" "+(Integer)student.get("rank"));
					}
					//home university boys
					BasicDBObject query2 = new BasicDBObject();
					 
					 query2.put("location", college_city);
					 query2.put("gender", true);
					 
					student_cursor=collection2.find(query2);
					 System.out.println("home boys:"+student_cursor.count());
						
					while(student_cursor.hasNext())
					{
						DBObject student=student_cursor.next();
						home_university_open_girls.add((Integer)student.get("rank"));
						//System.out.println(student.get("location")+" "+(Integer)student.get("rank"));
						
						//System.out.println(student);
					}
					
					//all boys and girls from non-home-university
					BasicDBObject query3 = new BasicDBObject();
					 
					 query3.put("location", new BasicDBObject("$ne", college_city));
					student_cursor=collection2.find(query3);
					 System.out.println("All non-home:"+student_cursor.count());
						
					while(student_cursor.hasNext())
					{
						DBObject student=student_cursor.next();
						home_university_open_girls.add((Integer)student.get("rank"));
						//System.out.println(student.get("loacation")+" "+(Integer)student.get("rank"));
						
						//System.out.println(student);
					}
					
					if(home_university_open_girls.size()!=0)
					{
						System.out.println("size of AL:"+home_university_open_girls.size());
						String onj1="{\"college_name\":"+"'"+college.get("college_name")+"'"+"}";
						String obj2="{$set:{\"students2\":"+getJSONAttribute(home_university_open_girls)+"}}";
						collection1.update((DBObject)JSON.parse(onj1),(DBObject)JSON.parse(obj2));
					
					}
		
				}
			//	System.out.println(home_university_open_girls);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		
		public void addList3()
		{
			try
			{
				MongoClient mongoclient=new MongoClient("localhost",27017);
				DB db=mongoclient.getDB("Match");
				System.out.println("connected");
				
				DBCollection collection1=db.getCollection("colleges");
				DBCollection collection2=db.getCollection("students");
				
				DBCursor college_cursor=collection1.find();
				while(college_cursor.hasNext())
				{
					DBObject college=college_cursor.next();
					String college_city=(String)college.get("college_city");
					//home university resreved
					System.out.println("College:"+college.get("college_name"));
						
					ArrayList<Integer> home_university_reserved=new ArrayList<Integer>();
					
					BasicDBObject query1 = new BasicDBObject();
					 query1.put("location", college_city);
					 query1.put("category", false);
					 DBCursor student_cursor=collection2.find(query1);
					 System.out.println("home reserved:"+student_cursor.count());
					while(student_cursor.hasNext())
					{
						
						DBObject student=student_cursor.next();
						home_university_reserved.add((Integer)student.get("rank"));
					//	System.out.println((String)student.get("location")+" "+(Integer)student.get("rank"));
					}
			  		//home university open
					BasicDBObject query2 = new BasicDBObject();
					 
					 query2.put("location", college_city);
					 query2.put("category", true);
					 
					student_cursor=collection2.find(query2);
					 System.out.println("home open:"+student_cursor.count());
						
					while(student_cursor.hasNext())
					{
						DBObject student=student_cursor.next();
						home_university_reserved.add((Integer)student.get("rank"));
						//System.out.println(student.get("location")+" "+(Integer)student.get("rank"));
						
						//System.out.println(student);
					}
					
					//all boys and girls from non-home-university
					BasicDBObject query3 = new BasicDBObject();
					 
					 query3.put("location", new BasicDBObject("$ne", college_city));
					student_cursor=collection2.find(query3);
					 System.out.println("All non-home:"+student_cursor.count());
						
					while(student_cursor.hasNext())
					{
						DBObject student=student_cursor.next();
						home_university_reserved.add((Integer)student.get("rank"));
						//System.out.println(student.get("loacation")+" "+(Integer)student.get("rank"));
						
						//System.out.println(student);
					}
					
					if(home_university_reserved.size()!=0)
					{
						System.out.println("size of AL:"+home_university_reserved.size());
						String onj1="{\"college_name\":"+"'"+college.get("college_name")+"'"+"}";
						String obj2="{$set:{\"students3\":"+getJSONAttribute(home_university_reserved)+"}}";
						collection1.update((DBObject)JSON.parse(onj1),(DBObject)JSON.parse(obj2));
					
					}
		
				}
			//	System.out.println(home_university_open_girls);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}


	
		String getJSONAttribute(ArrayList<Integer> home_university_open)
		{
			String returnvalue="[";
			int i=0;
			for(i=0;i<(home_university_open.size()-1);i++)
			{
				returnvalue+=home_university_open.get(i);
				returnvalue+=",";
			}
			returnvalue+=home_university_open.get(i);
			returnvalue+="]";
			return returnvalue;
		}
		
		public static void main(String []args)
		{
			CreateList c=new CreateList();
			c.addList1();
			c.addList2();
			c.addList3();
		}	
}