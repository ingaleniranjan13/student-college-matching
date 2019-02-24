import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
//import java.awt.event.WindowStateListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;


class tmpGUI extends JPanel implements Runnable{

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFrame frame;
	  
	  
	  public void run(){
		  
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		
			frame=new JFrame("Running");
			
			JPanel panel=new JPanel();
			JLabel label=new JLabel("Program is running ..... "+"\n"+"   Please Wait ....");
			panel.add(label);
			panel.setVisible(true);
			label.setVisible(true);
			frame.setSize(350, 120);
			frame.setVisible(true);
			frame.add(panel);
	  }
	  	
	  void start(){
		  run();
	  }
	  
	  	void close(){
	  		frame.dispose();
	  	}
	  
}


public class GUI extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel topPanel,bottomPanel;
	JTextField scount,ccount;
	JLabel l1,l2,nullLabel;
	JButton button;
	int students;
	int colleges;
	JFrame frame;
	
	GUI(){
	
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		setLayout(new GridLayout(2,0));
		
		nullLabel=new JLabel("                                                                               ");
		topPanel=new JPanel();
		add(topPanel);
		bottomPanel=new JPanel();
		add(bottomPanel);
		l1=new JLabel("  Number of students:  ");
		l2=new JLabel(" Number of colleges:   ");
		scount=new JTextField(10);
		scount.setEditable(true);
		ccount=new JTextField(10);
		ccount.setEditable(true);
		topPanel.add(nullLabel);
		topPanel.add(l1);
		topPanel.add(scount);
		topPanel.add(l2);
		topPanel.add(ccount);
		
		bottomPanel.add(nullLabel);
		button=new JButton("  submit  ");
		bottomPanel.add(button);
	
	
		button.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				students=Integer.parseInt(scount.getText());
				colleges=Integer.parseInt(ccount.getText());
				close(frame);
			}
			
		});
		
		
	}
	void close(JFrame frame){
		frame.dispose();
		
		tmpGUI tmp=new tmpGUI();
		
		tmp.start();
		
		CreateStudentData studentData=new CreateStudentData(students);
		CreateCollegeData collegeData=new CreateCollegeData(colleges);
		CreateLocationData locationData=new CreateLocationData();
		try {
			studentData.connectToDatabase();
			studentData.createStudentTable();
	//		studentData.rankGenerator();
			collegeData.connectToDatabase();
			collegeData.createCollegeTable();
			locationData.connectToDatabase();
			locationData.createLocationTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Data Generated Successfully !");
		terminate(tmp);
		
	}
	
	
	void terminate(tmpGUI tmp){
		tmp.close();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		JFrame frame=new JFrame("Terminate");
		frame.setVisible(true);
		JPanel panel=new JPanel();
		JLabel label=new JLabel("Data Generated Successfully");
		panel.add(label);
		frame.add(panel);
		frame.setSize(300, 100);
		frame.addWindowListener(new WindowListener(){

			public void windowClosing(WindowEvent e) {
				
				System.out.println("It's Working");
				System.exit(0);
				
			}

			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public static void main(String args[]){

		GUI obj=new GUI();
		
		obj.frame=new JFrame("GUI");
		obj.frame.setSize(400,200);
		obj.frame.add(obj);
		obj.frame.setVisible(true);
	}
	
}


