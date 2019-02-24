      
		public class Main {
        
			public static void main(String[] args) throws Exception {
			
				long startTime = System.nanoTime(); 
				
				StudentCollegeMapper obj=new StudentCollegeMapper();
				
				obj.connectToDatabase();
				
				obj.importColleges();
				
				obj.createPrefferenceLists();
				
				obj.allocateCollege();
				
				System.out.println("\nWe are Done!\n");
				
				long estimatedTime = System.nanoTime() - startTime;
				
				System.out.println("\nTime requires:"+estimatedTime+"nano seconds");
				
			}

        }

