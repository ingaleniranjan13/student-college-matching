package Location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;

public class CreatePairs {
	BufferedReader br;
	FileReader reader;
	FileWriter writer;
	CreatePairs()
	{
		try {
			reader=new FileReader("/home/niranjan/Eclipse-Workspace/CreateData/src/Location/cities.txt");
			writer=new FileWriter("/home/niranjan/Eclipse-Workspace/CreateData/src/Location/locations.txt");
			br=new BufferedReader(reader);
			String str="";
			LinkedList<String> cities=new LinkedList<String>();
			while((str=br.readLine())!=null)
			{
				if(!cities.contains(str))
					cities.add(str);
			}
			for(int i=0;i<cities.size();i++)
			{
				for(int j=0;j<cities.size();j++)
				{
					writer.write(cities.get(i)+" "+cities.get(j)+"\n");
				}
			}
			reader.close();
			writer.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new CreatePairs();
	}
}
