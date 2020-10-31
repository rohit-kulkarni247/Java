// Java program to implement 
// a Simple Registration Form 
// using Java Swing 

import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator; 

class MyFrame1 
	extends JFrame 
	{ 

	// Components of the Form 
	private Container c; 
	private JLabel title; 
	private JLabel name,unit,enterdata; 
	private JTextField tname,tname1,tunit,tdate,tenterdata; 
	private JLabel email; 
	private JTextField temail;
	private JLabel pass; 
	private JPasswordField tpass;
	private JLabel mno; 
	private JTextField tmno; 
	private JLabel gender; 
	private JRadioButton male; 
	private JRadioButton female; 
	private ButtonGroup gengp; 
	private JLabel dob; 
	private JComboBox date; 
	private JComboBox month; 
	private JComboBox year; 
	private JLabel add; 
	private JTextArea tadd; 
	private JCheckBox term; 
	private JButton refresh,users,cashback; 
	
	private JButton reset; 
	private JTextArea tout; 
	private JLabel res; 
	private JTextArea resadd; 

	

	//socket connection
    static ObjectOutputStream oos;
	static ObjectInputStream ois ;
	static Socket socket ;	
	
	// constructor, to initialize the components 
	// with default values. 
	public MyFrame1() 
	{ 
		
	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					System.out.println("Hiiiii");
					socket = new Socket("127.0.0.1", 11111);
					oos = new ObjectOutputStream(socket.getOutputStream());
					ois = new ObjectInputStream(socket.getInputStream());
					
					oos.writeObject("Admin");
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		
		setTitle("Welcome Admin"); 
		setBounds(300, 90, 900, 600+300); 
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
		setResizable(false); 

	
		setLayout(null); 

		
		users = new JButton("List of Users"); 
		users.setFont(new Font("Arial", Font.PLAIN, 15)); 
		users.setSize(300, 50); 
		users.setLocation(90, 80); 
		users.addActionListener(new Regi()); 
		add(users); 
		
		cashback = new JButton("CashBack details"); 
		cashback.setFont(new Font("Arial", Font.PLAIN, 15)); 
		cashback.setSize(300, 50); 
		cashback.setLocation(460, 80); 
//		setunit.addActionListener(new Regi());
		cashback.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					oos.writeObject("cashback");
					String display=(String)ois.readObject();
					tout.setText(display);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
				
			add(cashback); 

		tout = new JTextArea(); 
		tout.setFont(new Font("Arial", Font.PLAIN, 15)); 
		tout.setSize(620, 400+100); 
		tout.setLocation(120, 200); 
		tout.setLineWrap(true); 
		tout.setEditable(false); 
		add(tout); 
//		
		
		refresh = new JButton("Refresh"); 
		refresh.setFont(new Font("Arial", Font.PLAIN, 15)); 
		refresh.setSize(120, 50); 
		refresh.setLocation(370, 720); 
		refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	setVisible(false);
            	new AdminLogin();
            	
            }
        });
		add(refresh);
		
		
		setVisible(true); 
	} 

	// method actionPerformed() 
	// to get the action performed 
	// by the user and act accordingly 
	 public class Regi implements ActionListener{
			public void actionPerformed(ActionEvent ev) {
				try {
					oos.writeObject("user details");
					//ArrayList<String> str=new ArrayList<String>();
					//str=(ArrayList)ois.readObject();
					
					String display=(String)ois.readObject();
					//Iterator it=str.iterator();
						//while(it.hasNext()) {
							//display+=it.toString();
							//tout.setText(tout.getText()+it.next()+" ");
						//}
					tout.setText(display);	
					
				} catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	 
	
	 
	 public class whnpby implements ActionListener{
			public void actionPerformed(ActionEvent ev) {
				try {
					oos.writeObject("whnpby");
					String settext="";
					ArrayList<String> arr=new ArrayList<String>();
					
					arr=(ArrayList)ois.readObject();
//					System.out.println(arr.size());
					if(arr.size()==0) {
						JOptionPane.showMessageDialog(null, "Everyone had payed bill Successfully!!");
					}else {
						Iterator it=arr.iterator();
						while(it.hasNext()){
							settext+=it.next()+"\n";;
						}
						tout.setText(settext);
					}
					
					JOptionPane.showMessageDialog(null, "Please do refresh before doing next task!!");
				} catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	
	 
} 

// Driver Code 
public class AdminLogin extends Thread{ 
	public static String id;
	AdminLogin(){
		new MyFrame1(); 		
	}
	public static void main(String[] args) throws Exception 
	{ 
		new AdminLogin();
	} 
	
} 