package Location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collections;
import java.util.LinkedList;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

class Location  implements Comparable<Location>
{
	String colleges_city;
	String student_city;
	int distance;
	double score;
	public int compareTo(Location l){
		return this.distance-l.distance;
	}
}

public class CalculateLocationScores 
{
	LinkedList<Location> locations;
	FileReader reader;
	BufferedReader br;
	MongoClient mongoClient;
	DB db;
	DBCollection collection;
	CalculateLocationScores(String file)
	{
		try {
			mongoClient=new MongoClient();
			db=mongoClient.getDB("Match");
			collection = db.getCollection("locationscores");			
			locations=new LinkedList<Location>();
			reader=new FileReader(file);
			br=new BufferedReader(reader);
			String str="";
			while((str=br.readLine())!=null)
			{
				String[] tmp=str.split(" ");
				Location l=new Location();
				l.colleges_city=tmp[0];
				l.student_city=tmp[1];
				l.distance=Integer.parseInt(tmp[2]);
				locations.add(l);
			}
			Collections.sort(locations);
			double reference=(double)((locations.get((locations.size()/2))).distance);
			double max=-1;
			for(int i=0;i<locations.size();i++){
				Location tmp=locations.get(i);
				if(tmp.distance==0)
					tmp.score=100;
				else{
					tmp.score=100*reference/tmp.distance;
					if(tmp.score>max)
						max=tmp.score;
				}
			}
			max=max/100;
			for(int i=0;i<locations.size();i++){
				Location tmp=locations.get(i);
				if(tmp.distance!=0)
					tmp.score=(tmp.score/max);
				String json="{"+"college_city:"+"'"+tmp.colleges_city+"'"+","+"student_city:"+"'"+tmp.student_city+"'"+","+"score:"+tmp.score*2+"}";
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
		new CalculateLocationScores("/home/niranjan/Eclipse-Workspace/CreateData/src/Location/distances.txt");
	}
	
}
