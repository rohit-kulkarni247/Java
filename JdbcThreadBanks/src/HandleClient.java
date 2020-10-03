
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class HandleClient implements Runnable {
	Socket client;
	char ch;
    Integer choice;
    String msg;
    Sample obj1=null;
    InputStream is;
    ObjectInputStream ois;
    OutputStream os ;
    ObjectOutputStream oos ;
    
    Statement st,st1,st2,st3;
    
    ResultSet rs;
    String query="";
    

    Scanner sc=new Scanner(System.in);
    
	
	public HandleClient(Socket socket) throws Exception {
		// TODO Auto-generated constructor stub
		is = socket.getInputStream();
        ois = new ObjectInputStream(is);
        os = socket.getOutputStream();
        oos = new ObjectOutputStream(os);
       
        st=Server.con.createStatement();
   	 	st1=Server.con.createStatement();
   	 	st2=Server.con.createStatement();
   	 	st3=Server.con.createStatement();
	
	}

	public void run() {
		try {
			 do {
		        	choice=(Integer)ois.readObject(); //1
		        	switch(choice) {   //user or admin or exit
		        	case 1: //user
		        		do {
		        			
		        			choice=(Integer)ois.readObject(); //2
		        			
		        			switch(choice) { //sign-up login or exit
		        			case 1: //sign-up
		        				obj1=(Sample)ois.readObject(); //3
		        				
//		        				credentials.put(obj1.email, obj1.password);
//		        				userName.add(obj1.name);
//		        				userId.add(obj1.Id);
		        				
		        				
		        				st.execute("insert into Users(username,email,password) "+"values( "+"'"+obj1.name+"'"+","+"'"+obj1.email+"'"+" , "+"'"+obj1.password+"'"+" )");
		        				rs=st.executeQuery("select user_id from users where email="+"'"+obj1.email+"'");
		        				rs.next();
		        				msg="Signup Successfull for "+ obj1.name + "!!\nREMEMBER YOUR USER-ID IS " + rs.getInt("user_id");
		        				oos.writeObject(msg); //4
		        			break;
		        			
		        			case 2: //login
		        				String email1,pass1;
		        				Integer id,flag=1;
		        				
		        			while(true) {
		        					
		        				id=(Integer)ois.readObject();
		        				query="select user_id from Users where user_id="+id;
		        				rs=st1.executeQuery(query);
		        				if(rs.next()) {
		        					oos.writeObject(flag); //5
		        					
		        				}
		        				
//		        				if(credentials.containsKey(email1)) {
//		        				}
		        				else {
		        					flag=-1;
		        					oos.writeObject(flag); //6
		        				}
		        				email1=(String)ois.readObject(); //7
		        				query="select email from Users where email="+"'"+email1+"'";
		        				ResultSet rs1=st1.executeQuery(query);
		        				if(rs1.next()) {
		        					oos.writeObject(flag); //8
		        					
		        				}
		        				
//		        				if(userId.contains(id)) {
//		        				}
		        				else {
		        					flag=-1;
		        					oos.writeObject(flag); //9
		        				}
		        				pass1=(String)ois.readObject(); //10
		        				query="select password from Users where password="+"'"+pass1+"'";
		        				ResultSet rs2=st1.executeQuery(query);
		        				if(rs2.next()) {
		        					msg="Login successful";
		        					oos.writeObject(msg); //11
		        					oos.writeObject(flag);
		        				}
//		        				if(pass1.equals(credentials.get(email1))) {
//		        					
//		        				}
		        				else {
		        					msg="Password incorrect";
		        					oos.writeObject(msg); //12
		        					flag=0;
		        					oos.writeObject(flag);
		        				}
		        				break;
		        			}
	        			do {
		        				choice=(Integer)ois.readObject(); //13
		        				switch(choice) {
		        				case 1: //add money
	        					Integer inputBal=(Integer)ois.readObject();
		        					query="select balance from users where user_id="+id;
		        					rs=st2.executeQuery(query);
		        					rs.next();
		        					Integer temp=rs.getInt("balance");
		        					temp+=inputBal;
		        					query="update users set balance="+temp+" where user_id="+id;
		        					st3.executeUpdate(query);
//		        					bal[id-1]+=inputBal;
		        					msg="Successfully added rps. "+inputBal;
		        					oos.writeObject(msg); //14
		        				break;
		        				
		        				case 2: //transfer money
		        					
									
									Integer sendBal,recieverId,amount=0,flag1=1;
									recieverId=(Integer)ois.readObject(); //id recieved 15
									query="select balance from Users where user_id="+recieverId;
			        				rs=st1.executeQuery(query);
//			        				rs.next();
			        				if(rs.next()) {
			        					oos.writeObject(flag1); //send flag=1 receiver id exists 16
			        					Integer temp4=rs.getInt("balance");
//									
										
										sendBal=(Integer)ois.readObject(); //amount to be transferred 17
										query="select balance from users where user_id="+id;
			        					rs=st2.executeQuery(query);
			        					rs.next();
			        					Integer temp3=rs.getInt("balance");
										if(sendBal<temp3) {
											flag=1;
											oos.writeObject(flag);
											temp3-=sendBal;
											query="update users set balance="+temp3+" where user_id="+id;
				        					st3.executeUpdate(query);
											temp4+=sendBal;
											query="update users set balance="+temp4+" where user_id="+recieverId;
				        					st3.executeUpdate(query);
				        					query="select username from users where user_id="+recieverId;
				        					rs=st3.executeQuery(query);
				        					rs.next();
											msg="Money transferred successfully to "+rs.getString("username")+" with User-id "+recieverId;
											oos.writeObject(msg); //acknowledgement of transfer 18
											
											if(sendBal>500) {
												flag=1;
												oos.writeObject(flag); //send flag=1 for scratch card 19
												msg="You have earned a scratch card.. Press any key to continue";
												oos.writeObject(msg); //scratch card msg 20
												amount=((int)(Math.random() * 10)+1);
												msg="Congratulations! You have won Rs. "+amount+ " and have been successfully added to your account";
												query="select balance from users where user_id="+id;
					        					rs=st2.executeQuery(query);
					        					rs.next();
					        					Integer temp5=rs.getInt("balance"); 
												temp5+=amount;
												query="update users set balance="+temp5+" where user_id="+id;
					        					st3.executeUpdate(query);
					        					
					        					query="insert into cashback values("+id+","+amount+")";
					        					st3.executeUpdate(query);
												
												
					        					oos.writeObject(msg); //cashback amount 21
											}
											else {
												flag=0;
												oos.writeObject(flag);
											}
												
											}
											else { //sendbal>temp3 cannot transfer amount
												amount=0;
												flag=0;
												oos.writeObject(flag); //22
											}
										}
			
									else {
										flag=0;
										oos.writeObject(flag);
										msg="Reciever id doesn't exist!";
										oos.writeObject(msg); //23
										
									}
								
		        				break;
		        				case 3: //check balance
		        					query="select balance from users where user_id="+id;
		        					rs=st.executeQuery(query);
		        					rs.next();
		        					Integer balance=rs.getInt("balance");
		        					msg="Your current balance is: "+balance;
		        					oos.writeObject(msg); //24
		        				break;
		        				
		        				case 4: //withdraw amount
		        					Integer withdrawAmt;
		        					withdrawAmt=(Integer)ois.readObject(); //25
		        					query="select balance from users where user_id="+id;
		        					rs=st2.executeQuery(query);
		        					rs.next();
		        					Integer temp2=rs.getInt("balance");
		        					if(withdrawAmt<temp2) {			
		        						temp2-=withdrawAmt;
		        						query="update users set balance="+temp2+" where user_id="+id;
			        					st3.executeUpdate(query);
		        						msg="Successfully withdrawn Rs. "+withdrawAmt;
		        						//oos.writeObject(msg); //26 a
		        					}
		        					else
		        						msg="Sorry.. You don't have enough amount for this transaction";
		        					oos.writeObject(msg); //26 b
		        				break;
		        				
		        				
		        					
		        				}
//		        			
		        				ch=(char)ois.readObject(); //27
		        			}while(ch=='y');
		        			break;
//		        			
		        		}
		        			
		        		ch=(char)ois.readObject(); //28
		        		}while(ch=='y');
		        	break;
		        	case 2://admin
		        		do {
		        			choice=(Integer)ois.readObject();
		        			switch(choice) {
		        			case 1: //list of account holders
		        				query="select * from users";
		        				String userdata="";
		        				ArrayList<String> str=new ArrayList<String>();
		        				rs=st3.executeQuery(query);
		        				while(rs.next()) {
 									userdata=rs.getInt(1)+"\t\t|\t\t"+rs.getString(2)+ "\t|\t"+rs.getString(3)+"\t\t|\t"+rs.getInt(5);
 									//System.out.println(userdata);
 									str.add(userdata);
		        				}
		        				oos.writeObject(str);
//		        				oos.writeObject(credentials);
//		        				oos.writeObject(userId);
//		        				oos.writeObject(userName);
		        			break;
		        			case 2: //show offers
		        			query="select * from cashback";
	        				String userdata1="";
	        				ArrayList<String> str1=new ArrayList<String>();
	        				rs=st3.executeQuery(query);
	        				while(rs.next()) {
									userdata1=rs.getInt(1)+"\t\t|\t\t"+rs.getInt(2);
									//System.out.println(userdata);
									str1.add(userdata1);
	        				}
	        				oos.writeObject(str1);
//		        				oos.writeObject(cashBack);
//		        				oos.writeObject(userName);
		        			break;
		        			}
		        		ch=(char)ois.readObject();	
		        		}while(ch=='y');
		        		break;
		        	case 3:
		        		String msgin="",msgout="";
						while(true) {
							msgin=(String)ois.readObject();
							if(!msgin.equals("end")) {
								System.out.println(msgin+"\n");
								msgout=sc.nextLine();
								oos.writeObject(msgout);
							}
							else
								break;
							
						}
					break;
		        	
		        }
		        	
		        	
		        	ch=(char)ois.readObject(); //29
		        }while(ch=='y');
		        
		        
		      
		        
		        
		        System.out.println("\nClosing sockets.");
		        
		        //ss.close();
		        //socket.close();
		}
		catch(Exception e) {
			System.out.println(e); 
	    }
	}
}
