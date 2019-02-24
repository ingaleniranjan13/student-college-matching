package Fuzzy;

import java.io.BufferedReader;
import java.io.FileReader;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class InputFactorSettings {
	int numberOfFactors;
	int numberOfOptionalFactors;
	Factor[] factors;
	FileReader reader;
	BufferedReader br;
	InputFactorSettings()
	{
		try
		{
			reader=new FileReader("/home/niranjan/Eclipse-Workspace/Match/src/Fuzzy/factorsettings.txt");
			br=new BufferedReader(reader);
			String json="",tmp="";
			while((tmp=br.readLine())!=null)
				json+=tmp;
			DBObject dbObject = (DBObject)JSON.parse(json);
			numberOfFactors=(int)dbObject.get("Number_of_Factors");
			numberOfOptionalFactors=(int)dbObject.get("Number_of_Optional_Factors");
			BasicDBList factorArrayObject=(BasicDBList) dbObject.get("Factors");
			factors=new Factor[numberOfFactors];
			for(int i=0;i<factorArrayObject.size();i++)
			{
				BasicDBObject obj=(BasicDBObject) factorArrayObject.get(i);
				String name=(String)obj.get("Name");
				int defaultIndex=(int)obj.get("DefaultIndex");
				double defaultWeight=(double)obj.get("DefaultWeight");
				factors[i]=new Factor(name,defaultWeight,defaultWeight,defaultIndex,defaultIndex);
			}
			new WriteFactorsToDatabase(factors,numberOfOptionalFactors);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String[] args)
	{
		new InputFactorSettings();
	}
}
