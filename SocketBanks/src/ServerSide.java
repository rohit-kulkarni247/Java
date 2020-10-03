

import java.io.*;
import java.net.*;
import java.util.*;



public class ServerSide 
{
    public static void main(String[] args) throws Exception
     {
    	Vector<String>userName=new Vector<String>();
    	LinkedHashMap<String,String>credentials=new LinkedHashMap<String,String>();
    	ArrayList<Integer> userId=new ArrayList<Integer>();
    	LinkedHashMap<Integer,LinkedList<Integer>>cashBack=new LinkedHashMap<Integer,LinkedList<Integer>>();
    	 

    	int bal[]=new int[50];
    	for(int i=0;i<bal.length;i++)
			bal[i]=0;
    	
    	
    	char ch;
        Integer choice;
        String msg;
        Sample obj1=null;
    	
    	ServerSocket ss = new ServerSocket(7000);
        System.out.println("ServerSocket awaiting connections...");
        Socket socket = ss.accept();
        System.out.println("Connection from " + socket+"\n");

        //Deserialization
        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        
        Scanner sc=new Scanner(System.in);
        
        
        do {
        	choice=(Integer)ois.readObject(); //1
        	switch(choice) {   //user or admin or exit
        	case 1: //user
        		do {
        			
        			choice=(Integer)ois.readObject(); //2
        			
        			switch(choice) { //sign-up login or exit
        			case 1: //sign-up
        				obj1=(Sample)ois.readObject(); //3
        				
        				credentials.put(obj1.email, obj1.password);
        				userName.add(obj1.name);
        				userId.add(obj1.Id);
        				msg="Signup Successfull for "+ obj1.name + "!!\nREMEMBER YOUR USER-ID IS " + obj1.Id;
        				oos.writeObject(msg); //4
        				break;
        			
        			case 2: //login
        				String email1,pass1;
        				Integer id,flag=1;
        			while(true) {
        					
        				email1=(String)ois.readObject();
        				if(credentials.containsKey(email1)) {
        					oos.writeObject(flag); //5
        				}
        				else {
        					flag=-1;
        					oos.writeObject(flag); //6
        				}
        				id=(Integer)ois.readObject(); //7
        				if(userId.contains(id)) {
        					oos.writeObject(flag); //8
        				}
        				else {
        					flag=-1;
        					oos.writeObject(flag); //9
        				}
        				pass1=(String)ois.readObject(); //10
        				if(pass1.equals(credentials.get(email1))) {
        					msg="Login successful";
        					oos.writeObject(msg); //11
        				}
        				else {
        					msg="Password incorrect";
        					oos.writeObject(msg); //12
        				}
        				break;
        			}
        			do {
        				choice=(Integer)ois.readObject(); //13
        				switch(choice) {
        				case 1: //add money
        					Integer inputBal=(Integer)ois.readObject();
        					bal[id-1]+=inputBal;
        					msg="Successfully added rps. "+inputBal;
        					oos.writeObject(msg); //14
        				break;
        				
        				case 2:
        					LinkedList<Integer>temp=new LinkedList<Integer>();
							LinkedList<Integer>temp1=new LinkedList<Integer>();
							Integer sendBal,recieverId,amount=0,flag1=1;
							recieverId=(Integer)ois.readObject(); //id recieved 15
							if(userId.contains(recieverId)) {
								oos.writeObject(flag1); //send flag=1 reciever id exists 16
								
								sendBal=(Integer)ois.readObject(); //amount to be transferred 17
								if(sendBal<bal[id-1]) {				
									bal[id-1]-=sendBal;
									bal[recieverId-1]+=sendBal;
									msg="Money transferred successfully to "+userName.get(recieverId-1)+" with User-id "+recieverId;
									oos.writeObject(msg); //acknowledgement of transfer 18
									
									if(sendBal>500) {
										flag=1;
										oos.writeObject(flag); //send flag=1 for scratch card 19
										msg="You have earned a scratch card.. Press any key to continue";
										oos.writeObject(msg); //scratch card msg 20
										amount=((int)(Math.random() * 10)+1);
										msg="Congratulations! You have won Rs. "+amount+ " and have been successfully added to your account";
										 
										bal[id-1]+=amount;
										temp1=cashBack.get(id);
										if(temp1!=null) {						
											temp=cashBack.get(id);
											temp.add(amount);
											
											cashBack.put(id, temp);
										}
										else {
											temp.add(amount);
											cashBack.put(id, temp);
										}
										oos.writeObject(msg); //cashback amount 21
									}
									else {
										amount=0;
										flag=0;
										oos.writeObject(flag); //22
									}
								}
							}
							else {
								msg="Reciever id doesn't exist!";
								oos.writeObject(msg); //23
								
							}
						
        				break;
        				case 3:
        					msg="Your current balance is: "+bal[id-1];
        					oos.writeObject(msg); //24
        				break;
        				
        				case 4:
        					Integer withdrawAmt;
        					withdrawAmt=(Integer)ois.readObject(); //25
        					if(withdrawAmt<bal[id-1]) {			
        						bal[id-1]-=withdrawAmt;
        						msg="Successfully withdrawn Rs. "+withdrawAmt;
        						//oos.writeObject(msg); //26 a
        					}
        					else
        						msg="Sorry.. You don't have enough amount for this transaction";
        					oos.writeObject(msg); //26 b
        				break;
        				
        				
        					
        				}
        			
        				ch=(char)ois.readObject(); //27
        			}while(ch=='y');
        			break;
        			
        		}
        			
        		ch=(char)ois.readObject(); //28
        		}while(ch=='y');
        	break;
        	case 2://admin
        		do {
        			choice=(Integer)ois.readObject();
        			switch(choice) {
        			case 1:
        				oos.writeObject(credentials);
        				oos.writeObject(userId);
        				oos.writeObject(userName);
        			break;
        			case 2:
        				oos.writeObject(cashBack);
        				oos.writeObject(userName);
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
        
        ss.close();
        socket.close();
    }
}