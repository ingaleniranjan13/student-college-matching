package Fuzzy;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class WriteFactorsToDatabase {
	MongoClient mongoClient;
	DB db;
	DBCollection collection;
	public WriteFactorsToDatabase(Factor[] factors,int numberOfOptionalFactors) {
		// TODO Auto-generated constructor stub
		try
		{
			mongoClient=new MongoClient();
			db=mongoClient.getDB("Match");
			collection = db.getCollection("factorsettings");	
			String json="{"+
						"Number_of_Factors:"+factors.length+","+
						"Number_of_Optional_Factors:"+numberOfOptionalFactors+","+
						"Factors:"+getJSONArray(factors)+
						"}";
			DBObject dbObject = (DBObject)JSON.parse(json);			
			collection.insert(dbObject);
			mongoClient.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	String getJSONArray(Factor[] factors)
	{
		String returnvalue="";
		if(factors.length>0)
		{
			returnvalue="[";
			for(int i=0;i<factors.length-1;i++)
			{
				returnvalue+=(factors[i].toString()+",");
			}			
			returnvalue+=factors[factors.length-1].toString();
			returnvalue+="]";
		}
		return returnvalue;
	}
}
