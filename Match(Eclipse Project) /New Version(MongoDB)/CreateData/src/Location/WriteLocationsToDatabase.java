package Location;

//This file is not required

import java.io.BufferedReader;
import java.io.FileReader;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class WriteLocationsToDatabase {
	BufferedReader br;
	FileReader reader;
	MongoClient mongoClient;
	DB db;
	DBCollection collection;
	WriteLocationsToDatabase()
	{
		try {
			mongoClient=new MongoClient();
			db=mongoClient.getDB("Match");
			collection = db.getCollection("locationscores");
			reader=new FileReader("/home/niranjan/Eclipse-Workspace/CreateData/src/Location/locationscores.txt");
			br=new BufferedReader(reader);
			String str="";
			while((str=br.readLine())!=null)
			{
				String[] tmp=str.split(" ");
				String json="{"+"college_city:"+"'"+tmp[0]+"'"+","+"student_city:"+"'"+tmp[1]+"'"+","+"score:"+tmp[2]+"}";
				DBObject dbObject = (DBObject)JSON.parse(json);
				collection.insert(dbObject);
			}
			mongoClient.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args)
	{
		new WriteLocationsToDatabase();
	}
}
