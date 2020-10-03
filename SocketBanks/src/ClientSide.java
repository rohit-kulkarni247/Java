

import java.io.*;
import java.net.*;
import java.util.*;



public class ClientSide
{
	
	public static void main(String[] args) throws Exception
    {
    	 char ch;
    	 Integer Choice;
    	 Scanner sc=new Scanner(System.in);
     	 
     	 String msg;
    	 Integer id_user=1;
    	 
    	 Socket socket = new Socket("localhost", 7000);
         System.out.println("Connection established with the server\n");
         Sample obj=null;
         

       //Serialization
         OutputStream os = socket.getOutputStream();
         ObjectOutputStream oos = new ObjectOutputStream(os);
         InputStream is = socket.getInputStream();
         ObjectInputStream ois = new ObjectInputStream(is);
     
         BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

       
         System.out.println("--------------------------------------------------------\n|\tWelcome To the Bank Management System\t\t|\n--------------------------------------------------------\n");
         do {
        	 
			System.out.println("1)User\n2)Admin\n3)Chatbox\n4)Exit");
			
			Choice=sc.nextInt();
			oos.writeObject(Choice);  //1
			switch(Choice) {
				case 1: //user
					do {
							System.out.println("1) Signup\n2) Login\n3) Exit User Mode");
							Choice=sc.nextInt();
							oos.writeObject(Choice); //2
						switch(Choice) {
						case 1: //Sign-up
							String email,pass,username,confirm;
							System.out.println("Welcome to the Signup Page!!! Enter Your details...\n");
							sc.nextLine();
							while(true) {
								System.out.print("Enter Username(This will be your display name):");
								username=sc.nextLine();
								if(username.equals(""))
									System.out.println("Username cannot be empty");
								else
									break;
							}
							while(true) {
								System.out.print("Enter emailid:");
								email=sc.nextLine();
								if(email.equals(""))
									System.out.println("Email field cannot be empty");
								else
									break;
							}
							while(true) {
								System.out.print("Enter Password:");
								pass=sc.nextLine();
								if(pass.equals(""))
									System.out.println("Password cannot be empty");
								else
									break;
							}
							while(true) {
								System.out.print("Confirm Password:");
								confirm=sc.nextLine();
								if(pass.equals(confirm)) {
//									
									break;
								}
								else
									System.out.println("Password Does not match.. Please try again");
							}
							obj=new Sample(username,email,pass,id_user);
//							
							
							
							oos.writeObject(obj); //3
							id_user++;
							msg=(String)ois.readObject(); //4
							System.out.println("\n"+msg);
						break;
						
						case 2: //login
							String email1,pass1;
							Integer id,flag;
							System.out.println("Welcome to the Login Page!! Enter Your details...\n");
							sc.nextLine();
							while(true) {
								
								System.out.print("Enter email-id:");
								email1=sc.nextLine();
								oos.writeObject(email1); //5
								flag=(Integer)ois.readObject(); //6
								if(flag==1) {
									System.out.print("Enter User-id:");
									id=sc.nextInt();
									
									sc.nextLine();
									oos.writeObject(id); //7
									flag=(Integer)ois.readObject(); //8
									if(flag==1) {
										System.out.print("Enter Password:");
										pass1=sc.nextLine();
										oos.writeObject(pass1); //9
										msg=(String)ois.readObject(); //10
										System.out.println(msg);
										break;
									}
									else {
										System.out.println("User-id incorrect\n");
										break;
									}
								}
								else {
									System.out.println("Email incorrect\n");
									break;
								}
							}
							do {
								System.out.println("1) Add Money\n2) Transfer Money\n3) Check Balance\n4) Withdraw Money\n");
								Choice=sc.nextInt();
								oos.writeObject(Choice); //11
							switch(Choice) {
								case 1: //add money
									Integer inputBal;
									System.out.print("Enter Amount to be added:");
									inputBal=sc.nextInt();
									oos.writeObject(inputBal); //12
									msg=(String)ois.readObject(); //13
									System.out.println("\n"+msg);
								break;
								
								case 2: //transfer money
									
									Integer sendBal,recieverId,flag1;
									System.out.print("Enter id of the user to which you have to transfer money:");
									recieverId=sc.nextInt();
									oos.writeObject(recieverId); //send id 14
									flag1=(Integer)ois.readObject(); //15
									if(flag1==1) { //Receiver id exists
										System.out.print("Enter amount to be transferred:");
										sendBal=sc.nextInt();
										oos.writeObject(sendBal); //send amount 16
										msg=(String)ois.readObject(); //acknowledged 17
										System.out.println("\n"+msg);
										flag=(Integer)ois.readObject(); //recieve flag for scratch card 18
										if(flag==1) {
											msg=(String)ois.readObject(); //press any key msg 19
											System.out.println("\n"+msg);
											ch=sc.next().charAt(0);
											msg=(String)ois.readObject(); //cashback amount msg 20
											System.out.println("\n"+msg);
										}
									}
									else { //Reciever id does not exist
										msg=(String)ois.readObject(); //21
										System.out.println("\n"+msg);
									}
									break;
								
								case 3: //check balance
									msg=(String)ois.readObject(); //22
									System.out.println("\n"+msg);
								break;
								
								case 4: //withdraw money
									Integer withdrawAmt;
									System.out.print("Enter amount to be withdrawn:");
									withdrawAmt=sc.nextInt();
									oos.writeObject(withdrawAmt); //send withdrawl amount 23
									msg=(String)ois.readObject(); //acknowledgment 24
									System.out.println("\n"+msg);
									break;
								
							}
								System.out.println("\nDo you wish to continue with the Current User (y/n)?");
								ch=sc.next().charAt(0);
								oos.writeObject(ch);	 //25
							}while(ch=='y');
							break;
						
					}	
						System.out.println("\nDo you wish to continue with the User MENU (y/n)?");
						ch=sc.next().charAt(0);
						oos.writeObject(ch); //26
					}while(ch=='y');
				break;
				case 2: //admin
					do {
						System.out.print("1) List of Account Holders\n2) Show offers\n");
						Choice=sc.nextInt();
						oos.writeObject(Choice);
						switch(Choice) {
							case 1:
								//admin.displayList(credentials,userId,userName);
								System.out.println("User-id\t\tUsername\tEmail-Id");
								Vector<String>userName=new Vector<String>();
								LinkedHashMap<String,String>credentials=new LinkedHashMap<String,String>();
								ArrayList<Integer> userId=new ArrayList<Integer>();
								
								credentials=(LinkedHashMap<String,String>)ois.readObject();
								userId=(ArrayList<Integer>)ois.readObject();
								userName=(Vector<String>)ois.readObject();
								Iterator it = credentials.entrySet().iterator();
								Integer cnt=0;
								Enumeration<Integer> e = Collections.enumeration(userId);
								while (it.hasNext() && e.hasMoreElements()) { 
								    Map.Entry mapElement = (Map.Entry)it.next(); 
								    System.out.println(e.nextElement() +"\t\t"+userName.get(cnt)+"\t\t"+ mapElement.getKey()); 
								    cnt++;
								}
								break;
							case 2:
								//admin.offers(cashBack,userName);
								System.out.println("User-id\t\tUserName\tCashBack Earned");
								LinkedHashMap<Integer,LinkedList<Integer>>cashBack=new LinkedHashMap<Integer,LinkedList<Integer>>();
								Vector<String>userName1=new Vector<String>();
								
								cashBack=(LinkedHashMap<Integer,LinkedList<Integer>>)ois.readObject();
								userName1=(Vector<String>)ois.readObject();
								Iterator it1 = cashBack.entrySet().iterator();
								Integer cnt1=0;
								//Enumeration e = Collections.enumeration(userId);
								while (it1.hasNext()) { 
								    Map.Entry mapElement = (Map.Entry)it1.next();
								    Integer id=((Integer)mapElement.getKey())-1;
								    System.out.println(mapElement.getKey()+"\t\t"+userName1.get(id)+"\t\t"+mapElement.getValue()); 
								    cnt1++;
								}
								
							break;
							default:
								System.out.println("Invalid input");
							break;
						}System.out.println("PRESS 'y' to continue with the ADMIN or 'n' to LOGOUT(y/n)?");
						ch=sc.next().charAt(0);
						oos.writeObject(ch);
					}while(ch=='y');
				break;
				case 3:
					sc.nextLine();
					String msgin="",msgout="";
					System.out.println("Talk to server");
					msgout=sc.nextLine();
					while(true) {
						if(!msgout.equals("end")) {
							
							oos.writeObject(msgout);
							msgin=(String)ois.readObject();
							System.out.println(msgin+"\n");
							msgout=sc.nextLine();
						}
						else {
							oos.writeObject(msgout);
							break;
						}
					}
				break;
			}
				System.out.println("\nDo you wish to continue with the MAIN MENU (y/n)?");
				ch=sc.next().charAt(0);
				oos.writeObject(ch); //27
         	}while(ch=='y');
    	
    	
    	
    	
    	
    	
    	
    	



       
       

        System.out.println("\nClosing socket and terminating program.");
        socket.close();
    }
}