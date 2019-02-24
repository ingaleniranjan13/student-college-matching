import java.io.BufferedReader;
//import java.io.FileNotFoundException;
import java.io.FileReader;
//import java.util.Scanner;

public class tma {
	
	int counter[];
	FileReader reader;
	int boys[][];
	int girls[][];
	String list[];
	int matches[];
	int n;
	boolean completed;
   // Scanner sc;
	boolean arr[];
	tma(){
	/*	sc=new Scanner(System.in);
		System.out.println("Input Size of Problem:");
		n=sc.nextInt();
		*/
		try {
			FileReader r=new FileReader("/home/niranjan/eclipse/MyPrograms/src/boys.txt");
			BufferedReader b=new BufferedReader(r);
			n=0;
			while((b.readLine())!=null)
				n++;
			b.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		boys=new int[n][n];
		girls=new int[n][n];
		counter=new int[n];
		matches=new int[n];
		for(int i=0;i<n;i++)
			counter[i]=0;
	}
    void input(){
		try {
			reader=new FileReader("/home/niranjan/eclipse/MyPrograms/src/boys.txt");
			BufferedReader br=new BufferedReader(reader);
			String str="";
			int index=0;
			while((str=br.readLine())!=null){
				list=str.split(" ");
				for(int i=1;i<list.length;i++)
					boys[index][i-1]=Integer.parseInt(list[i])-1;
				index++;
			}
			
			reader=new FileReader("/home/niranjan/eclipse/MyPrograms/src/girls.txt");
			br=new BufferedReader(reader);
			index=0;
			while((str=br.readLine())!=null){
				list=str.split(" ");
				matches[index]=Integer.parseInt(list[list.length-1])-1;
				for(int i=1;i<list.length;i++)
					girls[index][Integer.parseInt(list[i])-1]=i-1;
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<n;i++){
			System.out.println("Boy"+(i+1));
			for(int j=0;j<n;j++){
				System.out.println(boys[i][j]);
			}
		}
		for(int i=0;i<n;i++){
			System.out.println("Girl"+(i+1));
			for(int j=0;j<n;j++){
				System.out.println(girls[i][j]);
			}
		}
	}
	
	void runMatch(){
		
		arr=new boolean[n];
		while(!completed){
			for(int i=0;i<arr.length;i++)
				arr[i]=false;
			for(int i=0;i<n;i++){
				int b=i;
				int g=boys[b][counter[b]];
				arr[g]=true;
				if( girls[g][b] <= girls[g][matches[g]])
					matches[g]=i;
				else {
					counter[b]++;
				}
			}
			int itr=0;
			for(itr=0;itr<arr.length;itr++){
				if(arr[itr]==false)
					break;
			}
			if(itr==arr.length)
				completed=true;
	
		}
		System.out.println("Matches Are:");
		for(int i=0;i<n;i++){
			System.out.println("Girl "+(i+1)+" matched to Boy "+(matches[i]+1));
		}
	}
	
	public static void main(String args[]){
		tma obj=new tma();
		obj.input();
		obj.runMatch();
	}
}
/*import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class tma {
	
	int counter[];
	FileReader reader;
	int boys[][];
	int girls[][];
	String list[];
	int matches[];
	int n;
	boolean completed;
    Scanner sc;
	boolean arr[];
	tma(){
		sc=new Scanner(System.in);
		System.out.println("Input Size of Problem:");
		n=sc.nextInt();
		boys=new int[n][n];
		girls=new int[n][n];
		counter=new int[n];
		matches=new int[n];
		for(int i=0;i<n;i++)
			counter[i]=0;
	}
    void input(){
		try {
			reader=new FileReader("/home/niranjan/eclipse/MyPrograms/src/boys.txt");
			BufferedReader br=new BufferedReader(reader);
			String str="";
			int index=0;
			while((str=br.readLine())!=null){
				list=str.split(" ");
				for(int i=1;i<list.length;i++)
					boys[index][i-1]=Integer.parseInt(list[i])-1;
				index++;
			}
			
			reader=new FileReader("/home/niranjan/eclipse/MyPrograms/src/girls.txt");
			br=new BufferedReader(reader);
			index=0;
			while((str=br.readLine())!=null){
				list=str.split(" ");
				matches[index]=Integer.parseInt(list[list.length-1])-1;
				for(int i=1;i<list.length;i++)
					girls[index][Integer.parseInt(list[i])-1]=i-1;
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<n;i++){
			System.out.println("Boy"+(i+1));
			for(int j=0;j<n;j++){
				System.out.println(boys[i][j]);
			}
		}
		for(int i=0;i<n;i++){
			System.out.println("Girl"+(i+1));
			for(int j=0;j<n;j++){
				System.out.println(girls[i][j]);
			}
		}
	}
	
	void runMatch(){
		
		arr=new boolean[n];
		while(!completed){
			for(int i=0;i<arr.length;i++)
				arr[i]=false;
			for(int i=0;i<n;i++){
				int b=i;
				int g=boys[b][counter[b]];
				arr[g]=true;
				if( girls[g][b] <= girls[g][matches[g]])
					matches[g]=i;
				else {
					counter[b]++;
				}
			}
			int itr=0;
			for(itr=0;itr<arr.length;itr++){
				if(arr[itr]==false)
					break;
			}
			if(itr==arr.length)
				completed=true;
	
		}
		System.out.println("Matches Are:");
		for(int i=0;i<n;i++){
			System.out.println("Girl "+(i+1)+" matched to Boy "+(matches[i]+1));
		}
	}
	
	public static void main(String args[]){
		tma obj=new tma();
		obj.input();
		obj.runMatch();
	}
}
*/