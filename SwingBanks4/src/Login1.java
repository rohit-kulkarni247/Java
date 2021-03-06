import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class LoginFrame extends JFrame {

    Container container = getContentPane();
    JLabel userLabel = new JLabel("ID");
    JLabel email = new JLabel("Email");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JLabel label=new JLabel("");
    JTextField userTextField = new JTextField();
    JTextField emailField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JButton resetButton = new JButton("RESET");
    JCheckBox showPassword = new JCheckBox("Show Password");
    
  //socket connection
    static ObjectOutputStream clientOutputStream;
	static ObjectInputStream clientInputStream ;
	static Socket socketConnection ;
	

    LoginFrame() {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
//        getContentPane().setBackground(new Color(47, 79, 79));
        userTextField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
               String value = userTextField.getText();
               int l = value.length();
               if ((ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') || ke.getKeyChar()==8) {
            	   userTextField.setEditable(true);
                  label.setText("");
               } else {
            	  userTextField.setEditable(false);
                  label.setText("* Enter only numeric digits(0-9)");
               }
            }
         });
        EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					socketConnection = new Socket("127.0.0.1", 11111);
					clientOutputStream = new ObjectOutputStream(socketConnection.getOutputStream());
					clientInputStream = new ObjectInputStream(socketConnection.getInputStream());
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
    }

    public void setLayoutManager() {
        container.setLayout(null);
        
    }

    public void setLocationAndSize() {
        userLabel.setBounds(50, 150, 100, 30);
        label.setBounds(50, 180, 200, 20);
        email.setBounds(50, 220, 100, 30);
        emailField.setBounds(150, 220, 150, 30);
        passwordLabel.setBounds(50, 290, 100, 30);
        userTextField.setBounds(150, 150, 150, 30);
        passwordField.setBounds(150, 220+70, 150, 30);
        showPassword.setBounds(150, 250+70, 150, 30);
        loginButton.setBounds(50, 300+110, 100, 30);
        resetButton.setBounds(200, 300+110, 100, 30);
        JLabel lblNewJgoodiesTitle = new JLabel("Please Login");
		lblNewJgoodiesTitle.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 30));
		lblNewJgoodiesTitle.setBounds(80, 22, 572, 66);
		add(lblNewJgoodiesTitle);

    }

    public void addComponentsToContainer() {
        container.add(userLabel);
        container.add(label);
        container.add(email);
        container.add(emailField);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(loginButton);
        container.add(resetButton);
    }

    public void addActionEvent() {
        loginButton.addActionListener(new SubmitLogin());
        resetButton.addActionListener(new ResetButton());
        showPassword.addActionListener(new Show());
    }

    
    
    public class SubmitLogin implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			try {
				clientOutputStream.writeObject("Login");
				
				String id=userTextField.getText();
				String email=emailField.getText();
				String password=passwordField.getText();
				
				clientOutputStream.writeObject(id);
				clientOutputStream.writeObject(email);
				clientOutputStream.writeObject(password);
				
				String f;
				try {
					f = (String) clientInputStream.readObject();
					if(f.equals("1")) {
						JOptionPane.showMessageDialog(null, "Login Successfull!!");
						new UserFunctions(id);
							
					}else {
						
						JOptionPane.showMessageDialog(null, "Ooops!! Something went wrong!!");
						setVisible(false);
						new Login1();
						
					}
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
    
    public class ResetButton implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			 userTextField.setText("");
	         passwordField.setText("");
		}
	}
    
    public class Show implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
		}
	}
    
    

}
public class Login1 {
	  
	Login1(){
		LoginFrame frame = new LoginFrame();
        frame.setTitle("Login Form");
        frame.setVisible(true);
        frame.setBounds(450, 150, 370, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);        
	}
    public static void main(String[] a) {
        
    	new Login1();
    }
    
    
    
    
}
