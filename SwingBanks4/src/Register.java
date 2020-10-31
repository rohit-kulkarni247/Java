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

class MyFrame extends JFrame { 

	// Components of the Form 
	private Container c; 
	private JLabel title; 
	private JLabel name; 
	private JTextField tname; 
	private JLabel email; 
	private JTextField temail;
	private JLabel pass,conpass; 
	private JPasswordField tpass,tconpass;
	
	 
	
	//private JCheckBox term; 
	private JButton sub; 
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
	public MyFrame() 
	{ 
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					
					socket = new Socket("127.0.0.1", 11111);
					oos = new ObjectOutputStream(socket.getOutputStream());
					ois = new ObjectInputStream(socket.getInputStream());
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		
		setTitle("Registration Form"); 
		setBounds(300, 90, 900, 600+100); 
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
		setResizable(false); 

		c = getContentPane(); 
		c.setLayout(null); 

		title = new JLabel("Registration Form"); 
		title.setFont(new Font("Arial", Font.PLAIN, 30)); 
		title.setSize(300, 30); 
		title.setLocation(300, 30); 
		c.add(title); 

		name = new JLabel("Username"); 
		name.setFont(new Font("Arial", Font.PLAIN, 20)); 
		name.setSize(100, 20); 
		name.setLocation(100, 100); 
		c.add(name); 

		tname = new JTextField(); 
		tname.setFont(new Font("Arial", Font.PLAIN, 15)); 
		tname.setSize(190, 20); 
		tname.setLocation(200, 100); 
		c.add(tname); 

		email = new JLabel("Email-ID"); 
		email.setFont(new Font("Arial", Font.PLAIN, 20)); 
		email.setSize(100, 20); 
		email.setLocation(100, 200); 
		c.add(email);
		
		temail = new JTextField(); 
		temail.setFont(new Font("Arial", Font.PLAIN, 15)); 
		temail.setSize(190, 20); 
		temail.setLocation(200, 200); 
		c.add(temail);
		
		pass = new JLabel("Password"); 
		pass.setFont(new Font("Arial", Font.PLAIN, 20)); 
		pass.setSize(100, 20); 
		pass.setLocation(100, 300); 
		c.add(pass);
		
		tpass = new JPasswordField(); 
		tpass.setFont(new Font("Arial", Font.PLAIN, 15)); 
		tpass.setSize(190, 20); 
		tpass.setLocation(200, 300); 
		c.add(tpass);
		
		conpass = new JLabel("Confirm"); 
		conpass.setFont(new Font("Arial", Font.PLAIN, 20)); 
		conpass.setSize(100, 20); 
		conpass.setLocation(100, 400); 
		c.add(conpass);
		
		tconpass = new JPasswordField(); 
		tconpass.setFont(new Font("Arial", Font.PLAIN, 15)); 
		tconpass.setSize(190, 20); 
		tconpass.setLocation(200, 400); 
		c.add(tconpass);
		


		sub = new JButton("Signup"); 
		sub.setFont(new Font("Arial", Font.PLAIN, 15)); 
		sub.setSize(100, 20); 
		sub.setLocation(150, 500+50); 
		sub.addActionListener(new Regi()); 
		c.add(sub); 

		reset = new JButton("Reset"); 
		reset.setFont(new Font("Arial", Font.PLAIN, 15)); 
		reset.setSize(100, 20); 
		reset.setLocation(270, 500+50); 
		reset.addActionListener(new Res()); 
		c.add(reset); 

		tout = new JTextArea(); 
		tout.setFont(new Font("Arial", Font.PLAIN, 15)); 
		tout.setSize(300, 400); 
		tout.setLocation(500, 100); 
		tout.setLineWrap(true); 
		tout.setEditable(false); 
		c.add(tout); 

		setVisible(true); 
	} 

	// method actionPerformed() 
	// to get the action performed 
	// by the user and act accordingly 
	 public class Regi implements ActionListener{
			public void actionPerformed(ActionEvent ev) {
				try {
					oos.writeObject("Register");
						String mail,pass,confirm1; 
						String data = tname.getText();
						mail=temail.getText();
						pass=tpass.getText();
						confirm1=tconpass.getText();
						if(pass.equals(confirm1)) {
							
						
						tout.setText("Username="+data+"\n"+"Password="+pass+"\n" ); 
						tout.setEditable(false); 
						if(	mail.length()>=3 && pass.length()>=3) {
						oos.writeObject(data);
						oos.writeObject(mail);
						oos.writeObject(pass);
						
						
						String reply=(String)ois.readObject();
						
						if(!reply.equals("-1")){
							Register.id=reply;
							JOptionPane.showMessageDialog(null, "Registration Successful!!\nPlease remember this id="+reply);
							new Login1();
							setVisible(false);
						}
						else {
							JOptionPane.showMessageDialog(null, "Oops!!! Something went wrong!!");
							new Register();
							setVisible(false);
						}
					}
						else {
							JOptionPane.showMessageDialog(null, "All fields are mandatory");
							setVisible(false);
							new Register();
						}
						
				}     
						else {
							JOptionPane.showMessageDialog(null, "Password fields does not match!! Please try again");
							setVisible(false);
							new Register();
						}
//					
					
					
					
					
					
					
				} catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	 public class Res implements ActionListener{
			public void actionPerformed(ActionEvent ev) {
				String def = ""; 
				tname.setText(def); 
				 
				res.setText(def); 
				tout.setText(def); 
//				term.setSelected(false); 
				 
				resadd.setText(def); 
			}
		}
	 
} 

// Driver Code 
public class Register extends Thread{ 
	public static String id;
	Register(){
		new MyFrame(); 		
	}
	public static void main(String[] args) throws Exception 
	{ 
		new Register();
	} 
	
} 


