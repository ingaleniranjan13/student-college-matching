import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

      

	class tmpGUI extends Thread{

		
		  JFrame frame;
		  
    	  public void run(){
    	
    			try {
    				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
    		
    			frame=new JFrame("Running");
    			frame.setVisible(true);
    			JPanel panel=new JPanel();
    			panel.add(new JLabel("                                                       "));
    			JLabel label=new JLabel("         Program is running .....        ");
    			panel.add(label);
    			panel.add(new JLabel("           Please wait .....         "));
    			frame.add(panel);
    			frame.setSize(350, 120);
    			
    		}
    	  	
    	  	void close(){
    	  		frame.dispose();
    	  	}
    	  
      }



		public class Main {
        
			public static void main(String[] args) {
			
				long startTime = System.nanoTime(); 
				
	//			tmpGUI tmp=new tmpGUI();
				
	//			tmp.start();
				
				StudentCollegeMapper obj=new StudentCollegeMapper();
			
	
				try {
					obj.connectToDatabase();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				obj.rankGenerator();
				
				obj.importColleges();
				
				obj.createPrefferenceLists();
				
				obj.allocateCollege();
				
				
				System.out.println("\nWe are Done!\n");
				
				long estimatedTime = System.nanoTime() - startTime;
				
				String str="\nTime requires:"+estimatedTime+" nano seconds";
				
				obj.terminate(str);
				
				System.out.println(str);
				
		//		obj.terminate(str,tmp);
				
				
			}

        }

