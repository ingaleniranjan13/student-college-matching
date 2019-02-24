package CreateData;


import java.util.*;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import java.io.*;
class Student{
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
					"branches:"+Branch.getbranches(branches)+","+
					"gender:"+gender+","+
					"category:"+category+","+
					"fee_limit:"+fee_limit+					
				"}";
		return s;
	}
}

class Branch{
	boolean set_flg;
	double score;
	public String toString()
	{
		return "{ set_flg:"+set_flg+","+"score:"+score+"}";
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


class DataCreation
{
	int n;
	int factors;
	int compulsory_factors;
	MongoClient mongoClient;
	DB db;
	DBCollection collection;

	DataCreation(int n,int factors,int compulsory_factors)
	{
		this.n=n;
		this.factors=factors;
		this.compulsory_factors=compulsory_factors;
		try {
			mongoClient=new MongoClient();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db=mongoClient.getDB("Match");
		collection = db.getCollection("students");
	
	}
	public void createData()
	{
		Student student[]=new Student[n];
		String name="omkar";
		int rank=1;
		//gender is true means MALE
		boolean gender=true;
		//category is true means open
		boolean category=false;
		String locations[]=new String[1];
		
		try
		{
		HashSet<String> set=new HashSet<String>();
		FileReader r=new FileReader("/home/niranjan/Eclipse-Workspace/CreateData/src/Location/cities.txt");
		BufferedReader b=new BufferedReader(r);
		String stu_city=b.readLine();
		
		while(stu_city!=null)
		{
			set.add(stu_city);
			stu_city=b.readLine();
		}
		locations=new String[set.size()];
		Iterator iterator =set.iterator(); 
		int k=0;
      // check values
		while (iterator.hasNext()){
         locations[k]=(String)iterator.next();
		 k++;
		 }
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		int no_of_cities=locations.length;
		int city_no,index,branch_value;
		boolean flg[]=new boolean[2];
		flg[0]=false;
		flg[1]=true;

		//int fees[]={10000,20000,25000,30000,35000,40000,60000,70000,100000};
		int fees[]={100000,100000,100000,100000,100000};
		Random random_number=new Random();
		
		for(int i=0;i<n;i++)
		{
			student[i]=new Student();
			//name of student
			student[i].name=name;
			//rank of student
			student[i].rank=rank;
			rank++;
			//gender of student
			
			index=random_number.nextInt(2);
			student[i].gender=flg[index];

			//category of student
			index=random_number.nextInt(2);
			student[i].category=flg[index];
			
			
			//assigning city to student
			city_no=random_number.nextInt(no_of_cities);
			student[i].location=locations[city_no];
			
			//branches
			student[i].branches=new Branch[6];
			for(int j=0;j<6;j++)
			{
				student[i].branches[j]=new Branch();
				index=random_number.nextInt(2);
				student[i].branches[j].set_flg=flg[index];
				branch_value=random_number.nextInt(101);
				student[i].branches[j].score=branch_value;
			}
			
			//fee limit
			index=random_number.nextInt(5);
			student[i].fee_limit=fees[index];
			
			char []preferences_list=new char[factors];
			int max_optional=factors-compulsory_factors;
			int optional=random_number.nextInt(max_optional);
			for(int j=0;j<optional;j++)
			{
				index=compulsory_factors+random_number.nextInt(max_optional);
				preferences_list[index]=(char)('0');
			}
			ArrayList<Integer> list=new ArrayList<Integer>(factors-optional);
			for(int j=0;j<factors;j++)
			{
				if(preferences_list[j]!='0')
				{
					list.add(j);
				}
			}
			int preference_number=1;
			while(list.size()!=0)
			{
				
				index=random_number.nextInt(list.size());
				int ind=(Integer)list.get(index);
				preferences_list[ind]=(char)(48+preference_number);
				preference_number++;
				list.remove(index);
			}
			//String preferences=new String(preferences_list);
			student[i].preferences=new String(preferences_list);
			System.out.println(student[i].toString());
			DBObject dbObject = (DBObject)JSON.parse(student[i].toString());
			collection.insert(dbObject);
	
		}
	
		mongoClient.close();
		/*for(int i=0;i<n;i++)
		{
			System.out.println(student[i].name+" Rank:"+student[i].rank+" Location:"+student[i].location);
		}*/
	}
	
	public static void main(String []args)
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter number of students:");
		int number_of_students=sc.nextInt();
		System.out.println("Enter number of factors:");
		int factors=sc.nextInt();
		System.out.println("Enter number of compulsory factors:");
		int compulsory_factors=sc.nextInt();

		DataCreation d=new DataCreation(number_of_students,factors,compulsory_factors);
		d.createData();
	}
}