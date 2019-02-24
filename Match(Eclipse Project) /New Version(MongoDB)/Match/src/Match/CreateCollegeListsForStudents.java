package Match;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import Fuzzy.Factor;
import Fuzzy.Fuzzy;

public class CreateCollegeListsForStudents {
	
	LinkedList<College> colleges;
	MongoClient mongoClient;
	DB db;
	DBCollection collection;
	Student student;
	FactorSettings factorSettings;
	
	public CreateCollegeListsForStudents() {
		// TODO Auto-generated constructor stub
		factorSettings=new FactorSettings();
		colleges=new LinkedList<College>();
		student=new Student();
		try
		{
			mongoClient=new MongoClient();
			db=mongoClient.getDB("Match");	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	

	void closeDBConnection()
	{
		mongoClient.close();		
	}
	
	void readCollegeData()
	{
		collection = db.getCollection("colleges");
		DBCursor cursor=collection.find();
		while(cursor.hasNext())
		{
			DBObject record=cursor.next();
			int fees=(int)record.get("Fees");
			int college_id=(int)record.get("college_id");
			String college_city=(String)record.get("college_city");
			String branch=(String)record.get("branch");
			BasicDBList factorScoreList=(BasicDBList)record.get("factors");
			FactorScore[] factorScores=new FactorScore[factorScoreList.size()];
			for(int i=0;i<factorScores.length;i++)
			{
				factorScores[i]=new FactorScore();
				DBObject tmp=(DBObject) factorScoreList.get(i);
				factorScores[i].factor=(String)tmp.get("factor");
				factorScores[i].score=(double)tmp.get("score");
			}
			College clg=new College();
			clg.college_id=college_id;
			clg.college_city=college_city;
			clg.branch=branch;
			clg.factors=factorScores;
			clg.Fees=fees;
			colleges.add(clg);
		}
		System.out.println("No of Colleges:"+colleges.size());
	}
	
	void getFactorSettings()
	{
		collection = db.getCollection("factorsettings");
		DBObject factorSettingsDocument=collection.findOne();
		int Number_of_Factors=(int)factorSettingsDocument.get("Number_of_Factors");
		int Number_of_Optional_Factors=(int)factorSettingsDocument.get("Number_of_Optional_Factors");
		BasicDBList factorList=(BasicDBList)factorSettingsDocument.get("Factors");
		Factor[] factors=new Factor[factorList.size()];
		for(int i=0;i<factors.length;i++)
		{
			DBObject tmp=(DBObject)factorList.get(i);
			String name=(String)tmp.get("name");
			double defaultWeight=(double)tmp.get("defaultWeight");
			int defaultIndex=(int)tmp.get("defaultIndex");
			factors[i]=new Factor(name,defaultWeight,0,defaultIndex,defaultIndex);
		}
		factorSettings=new FactorSettings();
		factorSettings.Number_of_Factors=Number_of_Factors;
		factorSettings.Number_of_Optional_Factors=Number_of_Optional_Factors;
		factorSettings.Factors=factors;
	}
	
	void generateCollegeListForCurrentStudent()
	{
		generateLocationScores();
		generateBranchScores();
		Factor[] factorsClone=factorSettings.Factors.clone();
		Factor[] allFactorsClone=factorSettings.Factors.clone();
		LinkedList<Factor> factorList=new LinkedList<Factor>();
		LinkedList<Factor> allFactorList=new LinkedList<Factor>();
		int j=0;
		for(int i=0;i<student.preferenceString.length();i++)
		{
			allFactorsClone[i].setDefaultIndex(i);
			allFactorsClone[i].setCurrentWeight(0);
			allFactorList.add(allFactorsClone[i]);
			if(student.preferenceString.charAt(i)!='0')
			{
	//			System.out.println("preferenceString.charAt(i)="+student.preferenceString.charAt(i));
				factorsClone[i].setCurrentIndex(Integer.parseInt(""+student.preferenceString.charAt(i))-1);
	//			int x=Integer.parseInt(""+student.preferenceString.charAt(i))-1;
	//			System.out.println("Integer.parseInt(\"\"+student.preferenceString.charAt(i))-1="+x);
				factorsClone[i].setDefaultIndex(j);
				j++;
				factorList.add(factorsClone[i]);
			}
		}
		Fuzzy fuzzy=new Fuzzy(1);
		factorList=fuzzy.fuzzyI(factorList);
		for(int i=0;i<factorList.size();i++)
		{
			for(j=0;j<allFactorList.size();j++)
			{
				if(factorList.get(i).getName().equals(allFactorList.get(j).getName()))
				{
					allFactorList.get(j).setCurrentWeight(factorList.get(i).getCurrentWeight());
				}
			}
		}
		
		for(int i=0;i<colleges.size();i++)
		{
			for(j=0;j<colleges.get(i).factors.length;j++)
			{
				colleges.get(i).totalScore+=colleges.get(i).factors[j].score*allFactorList.get(j).getCurrentWeight();
			}
		}
	    Collections.sort(colleges);
		/*
		
		College[] arr=new College[colleges.size()];
		for(int x=0;x<arr.length;x++)
		{
			arr[x]=colleges.get(x);
		}
		
		Arrays.sort(arr);
		*/
	    
	    /*
		for(int j1=0;j1<colleges.size();j1++) {
			for(int i1=0;i1<colleges.size()-j1-1;i1++)
			{
					if(colleges.get(i1).totalScore<colleges.get(i1+1).totalScore)
					{	
						swap(i1,i1+1);
					}
			}	
		}
		*/
	
		
		
		/*
		for(int i=0;i<colleges.size();i++) {
			System.out.println("college_id:"+colleges.get(i).college_id+"\ntotal_score:"+colleges.get(i).totalScore);
		}*/
		/*
		for(int i=0;i<arr.length;i++) {
			System.out.println("college_id:"+arr[i].college_id+"\ntotal_score:"+arr[i].totalScore);
		}
		*/
		//System.out.println("First score:"+arr[0].totalScore);
		/*colleges.clear();
		for(int x=0;x<arr.length;x++)
		{
			colleges.addFirst(arr[x]);
		}
		*/
		
		HashSet<String> set=new HashSet<String>();
		if(!student.branches[0].set_flg)
			set.add("CS");
		if(!student.branches[1].set_flg)
			set.add("IT");
		if(!student.branches[2].set_flg)
			set.add("MECH");
		if(!student.branches[3].set_flg)
			set.add("CIV");
		if(!student.branches[4].set_flg)
			set.add("ENTC");
		if(!student.branches[5].set_flg)
			set.add("ELE");
		LinkedList<College> set_b_colleges=new LinkedList<College>();
		LinkedList<College> set_a_colleges=new LinkedList<College>();
		for(int i=0;i<colleges.size();i++)
		{
			if(set.contains(colleges.get(i).branch))
				set_b_colleges.addFirst(colleges.get(i));
			else
				set_a_colleges.addFirst(colleges.get(i));
		}
		System.out.println("1 "+colleges.size());
		 
		colleges.clear();
		for(int i=0;i<set_a_colleges.size();i++)
		{
			if(student.fee_limit>=set_a_colleges.get(i).Fees)
				colleges.addFirst(set_a_colleges.get(i));
		}
		System.out.println("2 "+colleges.size());
		for(int i=0;i<set_b_colleges.size();i++)
		{
			if(student.fee_limit>=set_b_colleges.get(i).Fees)
				colleges.addFirst(set_b_colleges.get(i));
		}
		System.out.println("3 "+colleges.size());
		writeCurrentStudentListToDatabase();
	}
	
	void swap(int x,int y)
	{
		College tmp=colleges.remove(x);
		colleges.add(y, tmp);	
	}
	
	void writeCurrentStudentListToDatabase()
	{
		collection = db.getCollection("students");
		String onj1="{\"rank\":"+student.rank+"}";
		String obj2="{$set:{\"colleges\":"+getJSONAttribute()+"}}";
		collection.update((DBObject)JSON.parse(onj1),(DBObject)JSON.parse(obj2));
	}
	
	void run()
	{
		getFactorSettings();
		readCollegeData();
		getStudents();
		closeDBConnection();
		System.out.println("All Done!");
	}
	
	String getJSONAttribute()
	{
		String returnvalue="[";
		int i=0;
		for(;i<colleges.size()-1;i++)
		{
			returnvalue+=colleges.get(i).college_id;
			returnvalue+=",";
		}
		returnvalue+=colleges.get(i).college_id;
		returnvalue+="]";
		return returnvalue;
	}
	
	
	void generateBranchScores()
	{
		Factor[] factors=factorSettings.Factors;
		for(int i=0;i<factors.length;i++)
		{
			if(factors[i].getName().equals("Branch")||factors[i].getName().equals("branch")) 
			{
				if(student.preferenceString.charAt(i)!='0')
				{
					for(int j=0;j<colleges.size();j++)
					{
						if(colleges.get(j).branch.equals("CS"))
						{
							colleges.get(j).factors[i].score=student.branches[0].score;
						}
						else if(colleges.get(j).branch.equals("IT"))
						{
							colleges.get(j).factors[i].score=student.branches[1].score;
						}
						else if(colleges.get(j).branch.equals("MECH"))
						{
							colleges.get(j).factors[i].score=student.branches[2].score;
						}
						else if(colleges.get(j).branch.equals("CIV"))
						{
							colleges.get(j).factors[i].score=student.branches[3].score;
						}
						else if(colleges.get(j).branch.equals("ENTC"))
						{
							colleges.get(j).factors[i].score=student.branches[4].score;
						}
						else
						{
							colleges.get(j).factors[i].score=student.branches[5].score;
						}
					}
					break;
				}
				else
				{
						for(int k=0;k<colleges.size();k++)
						{
							colleges.get(k).factors[i].score=0.0;
						}
						break;

				}

			}
		}
	}
	
	void generateLocationScores()
	{
		Factor[] factors=factorSettings.Factors;
		for(int i=0;i<factors.length;i++)
		{
			if(factors[i].getName().equals("Location")||factors[i].getName().equals("location"))
			{
				char ch=student.preferenceString.charAt(i);
				if(ch!='0')
				{
					collection = db.getCollection("locationscores");
					for(int j=0;j<colleges.size();j++)
					{	
						String query="{college_city:'"+colleges.get(j).college_city+"',student_city:'"+student.location+"'}";
				//		System.out.println("query="+query);
						BasicDBObject obj=(BasicDBObject)JSON.parse(query);
						DBObject tmp=collection.findOne(obj);
				//		System.out.println("tmp="+tmp);
						colleges.get(j).factors[i].score=(double)tmp.get("score");
					}
					break;
				}
				else
				{
					for(int k=0;k<colleges.size();k++)
					{
						colleges.get(k).factors[i].score=0.0;
					}
					break;
				}
					
			}
			
		}
	}
	
	void getStudents()
	{
		collection = db.getCollection("students");
		DBCursor cursor=collection.find();
		while(cursor.hasNext())
		{
			DBObject record=cursor.next();
			int rank=(int)record.get("rank");
			String location=(String)record.get("location");
			String preferenceString=(String)record.get("preferences");
			BasicDBList branchlist=(BasicDBList)record.get("branches");
			Branch[] branches=new Branch[branchlist.size()];
			for(int i=0;i<branches.length;i++)
			{
				branches[i]=new Branch();
				DBObject tmp=(DBObject) branchlist.get(i);
				branches[i].set_flg=(boolean)tmp.get("set_flg");
				branches[i].score=(double)tmp.get("score");
			}
			int fee_limit=(int)record.get("fee_limit");
			student.rank=rank;
			student.location=location;
			student.preferenceString=preferenceString;
			student.branches=branches;
			student.fee_limit=fee_limit;
			generateCollegeListForCurrentStudent();
		}
	}
	
	public static void main(String[] args)
	{
		CreateCollegeListsForStudents createCollegeListsForStudents=new CreateCollegeListsForStudents();
		createCollegeListsForStudents.run();
	}
}
