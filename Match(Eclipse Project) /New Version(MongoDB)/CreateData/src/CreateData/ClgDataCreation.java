package CreateData;
import java.util.*;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import java.io.File;


class Parameter
{
	String parameter_name;
	double score;
	
	public String toString()
	{
		return "{ factor:"+"'"+parameter_name+"'"+","+"score:"+score+"}";
	}
	
	static String getParameters(Parameter[] parameters) {
		String s="[";
		int i=0;
		for(i=0;i<parameters.length-1;i++) {
			s+=parameters[i].toString();
			s+=",";
		}
		s+=parameters[i].toString();
		s+="]";
		return s;
	}
}

class College
{
	int college_id;
	String college_name;
	String college_city;
	String branch;
	double total_score;
	int fees;
	Parameter []parameters;
	
	public String toString()
	{
		String s="{ college_name:"+"'"+college_name+"'"+","+
					"college_id:"+college_id+","+
					"college_city:"+"'"+college_city+"'"+","+
					"branch:"+"'"+branch+"',"+
					"factors:"+Parameter.getParameters(parameters)+","+
					"Fees:"+fees+
				"}";
		return s;
	}
	
}

public class ClgDataCreation
{
	int p;
	String param_name[];
	//int fees[]={30000,40000,60000,70000,100000};
	int fees[]={100000,100000,100000,100000,100000};
	/*ClgDataCreation(int p,String []param_name)
	{
		this.p=p;
		this.param_name=param_name;
	}*/
	
	public void createData()
	{
		
		try
		{
			String filename="/home/niranjan/Eclipse-Workspace/CreateData/src/CreateData/college_stats.csv";
			File file=new File(filename);
			String data;
			College []colleges=new College[606];
			MongoClient mongoclient=new MongoClient("localhost",27017);
			DB db=mongoclient.getDB("Match");
			DBCollection collection1=db.getCollection("factorsettings");
			DBCollection collection2=db.getCollection("colleges");

			DBCursor factorsettings_cursor=collection1.find();
			//while(factorsettings_cursor.hasNext())
			//{
			if(factorsettings_cursor.hasNext())
			{
				ArrayList<String> list=new ArrayList<String>();
				DBObject factorsettings=factorsettings_cursor.next();
				BasicDBList factorArrayObject=(BasicDBList)factorsettings.get("Factors");
				for(int i=0;i<factorArrayObject.size();i++)
				{
					BasicDBObject obj=(BasicDBObject) factorArrayObject.get(i);
					String name=(String)obj.get("name");
					list.add(name);
					
				 //BasicDBObject query = new BasicDBObject();
				 //query.put("location", college_city);
				//DBCursor student_cursor=collection2.find(query);
				}
				param_name=new String[list.size()];
				for(int k=0;k<list.size();k++)
				{
					String s=list.get(k);
					System.out.println("s="+s);
					param_name[k]=new String(s);
					
				}
				p=list.size();
				System.out.println("p:"+p);
			}
				Scanner sc=new Scanner(file);
				sc.nextLine();
				int i=0;
				while(sc.hasNext())
				{
					data=sc.nextLine();
					String college_row[]=data.split(",");
					colleges[i]=new College();
					colleges[i].college_id=Integer.parseInt(college_row[0]);
					colleges[i].college_name=college_row[1];
					colleges[i].college_city=college_row[3];
					colleges[i].branch=college_row[10];
					colleges[i].parameters=new Parameter[p];
					for(int j=0;j<4;j++)
					{
						if(j==2)
						{
							colleges[i].parameters[j]=new Parameter();
							colleges[i].parameters[j].parameter_name=param_name[j];
							colleges[i].parameters[j].score=0.0;
						}
						else
						{
							colleges[i].parameters[j]=new Parameter();
							colleges[i].parameters[j].parameter_name=param_name[j];
							colleges[i].parameters[j].score=Double.parseDouble(college_row[j+5]);
						}
					}
					int count=0;
					for(int j=4;j<p;j++)
					{
						if((count/4)==1)
							count=0;
						if(j==6)
						{
							colleges[i].parameters[j]=new Parameter();
							colleges[i].parameters[j].parameter_name=param_name[j];
							colleges[i].parameters[j].score=0.0;
						}
						else
						{
							colleges[i].parameters[j]=new Parameter();
							colleges[i].parameters[j].parameter_name=param_name[j];
							colleges[i].parameters[j].score=Double.parseDouble(college_row[5+count]);
							count++;
							
						}
					}
					
					Random random_number=new Random();
					int index=random_number.nextInt(5);
					colleges[i].fees=fees[index];
					System.out.println(colleges[i].toString());
					DBObject dbObject = (DBObject)JSON.parse(colleges[i].toString());			
					collection2.insert(dbObject);

					i++;
					
				
				}
				//System.out.println(colleges[i-1].toString());
					sc.close();
		}	
			
		catch(Exception e)
		{
			e.printStackTrace();
		}
}
	public static void main(String []args)
	{
			/*System.out.println("Enter number of parameters:");
			Scanner scanner=new Scanner(System.in);
			int p=scanner.nextInt();
			scanner.nextLine();
			String param_name[]=new String[p];
			for(int j=0;j<p;j++)
			{
				param_name[j]=scanner.nextLine();
			}*/
		
		ClgDataCreation c=new ClgDataCreation();
		c.createData();
		
	}
}