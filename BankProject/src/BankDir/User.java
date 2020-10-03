package BankDir;
import java.util.*;

public class User {
	Vector<String>userName=new Vector<String>();
	LinkedHashMap<String,String>credentials=new LinkedHashMap<String,String>();
	ArrayList<Integer> userId=new ArrayList<Integer>();
	LinkedHashMap<Integer,LinkedList<Integer>>cashBack=new LinkedHashMap<Integer,LinkedList<Integer>>();
	
	Integer count;
	int bal[]=new int[50];
	
	Scanner sc=new Scanner(System.in);
	Admin admin=new Admin();
	
	public User() {
		count=1;
		for(int i=0;i<bal.length;i++)
			bal[i]=0;
	}
	
	public void userCall() {
	
		System.out.println("1) Signup\n2) Login\n3) Exit User Mode");
		Integer Choice=sc.nextInt();
		switch(Choice) {
			case 1:
				signUp();
			break;
			case 2:
				login();
			break;
			case 3:
				System.out.println("Exitted Successfully");
				break;
			default:
				System.out.println("Invalid input");
			break;
		}		
		
	}
	
	public void AdminCall() {
		char ch;
		do {
			System.out.print("1) List of Account Holders\n2) Show offers\n");
			Integer Choice=sc.nextInt();
			switch(Choice) {
				case 1:
					admin.displayList(credentials,userId,userName);
				break;
				case 2:
					admin.offers(cashBack,userName);
				break;
				default:
					System.out.println("Invalid input");
				break;
			}System.out.println("PRESS 'y' to continue with the ADMIN or 'n' to LOGOUT(y/n)?");
			ch=sc.next().charAt(0);
		}while(ch=='y');
	}
	
	public void signUp() {
		
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
				credentials.put(email,pass);
				break;
			}
			else
				System.out.println("Password Does not match.. Please try again");
		}
		userName.add(username);
		userId.add(count);
		
		System.out.println("\nSignup Successfull for "+ username +"!!");
		System.out.println("\n\nREMEMBER YOUR USER-ID IS "+ count +"\n");
		count++;
		
		userCall();
	}
	
	public void login() {
		String email,pass;
		Integer id;
		System.out.println("Welcome to the Login Page!! Enter Your details...\n");
		sc.nextLine();
		while(true) {
			
			System.out.print("Enter email-id:");
			email=sc.nextLine();
			
			if(credentials.containsKey(email)) {
				System.out.print("Enter User-id:");
				id=sc.nextInt();
				
				sc.nextLine();
				if(userId.contains(id)) {
					
					System.out.print("Enter Password:");
					pass=sc.nextLine();
					if(pass.equals(credentials.get(email))) {
						System.out.println("\nLogin Successfull !!! Welcome\n");
						break;
					}

					else
						System.out.println("Password Incorrect");
				}
				else
					System.out.println("User-id Incorrect");
			
			}
			else {
				System.out.println("Email-id Incorrect.. Please try again!!");
				userCall();
				return;
			}
		}
		userOperations(id,email);
	}

	public void userOperations(Integer id,String email) {
		
		char ch;
		do {
			
			System.out.println("1) Add Money\n2) Transfer Money\n3) Check Balance\n4) Delete Current User\n5) Withdraw Money\n");
			Integer Choice=sc.nextInt();
			switch(Choice) {
				case 1:
					addMoney(id);
					break;
				case 2:
					transferMoney(id);
					break;
				case 3:
					checkBalance(id);
				break;
				case 4:
					deleteUser(id,email);
				return;
				case 5:
					withdrawMoney(id);
				break;
				default:
					System.out.println("Invalid input");
				break;
			}		
			System.out.println("PRESS 'y' to continue with the same user or 'n' to LOGOUT(y/n)?");
			ch=sc.next().charAt(0);
		}while(ch=='y');
	}
	public void addMoney(Integer id) {
		
		Integer inputBal;
		System.out.print("Enter Amount to be added:");
		inputBal=sc.nextInt();
		bal[id-1]+=inputBal;
		System.out.println("Successfully added Rs. "+inputBal);
	}
	public void checkBalance(Integer id) {
		System.out.println("Your current balance is: "+bal[id-1]);
	}
	public void transferMoney(Integer id) {
		LinkedList<Integer>temp=new LinkedList<Integer>();
		LinkedList<Integer>temp1=new LinkedList<Integer>();
		Integer sendBal,recieverId,reward=0;
		char ch;
		System.out.print("Enter id of the user to which you have to transfer money:");
		recieverId=sc.nextInt();
		if(userId.contains(recieverId)) {
			System.out.print("Enter amount to be transferred:");
			sendBal=sc.nextInt();
			if(sendBal<bal[id-1]) {				
				bal[id-1]-=sendBal;
				bal[recieverId-1]+=sendBal;
				System.out.println("Money transferred successfully to "+ userName.get(recieverId-1)+" with User-id "+recieverId+"\n");
				if(sendBal>500) {
					reward=admin.createOffer(sendBal);
					System.out.print("You have earned a SCRATCH CARD!! Press y to continue:");
					ch=sc.next().charAt(0);
					System.out.println("Congratulations! You have won Rs. "+reward+ " and have been successfully added to your account\n");
					bal[id-1]+=reward;
					temp1=cashBack.get(id);
					if(temp1!=null) {						
						temp=cashBack.get(id);
						temp.add(reward);
						System.out.println("Total cashback:"+temp);
						cashBack.put(id, temp);
					}
					else {
						temp.add(reward);
						cashBack.put(id, temp);
					}
					
					
				}
				
			}
			else
				System.out.println("Transfer amount cannot be more than Bank Balance!");
		}
		else
			System.out.println("The reciever Id does not exist");
	}
	public void withdrawMoney(Integer id) {
		
		Integer withdrawAmt;
		System.out.print("Enter amount to be withdrawn:");
		withdrawAmt=sc.nextInt();
		if(withdrawAmt<bal[id-1]) {			
			bal[id-1]-=withdrawAmt;
			System.out.println("Successfully withdrawn Rs. "+withdrawAmt);
		}
		else
			System.out.println("Sorry.. You don't have enough amount for this transaction");
	}
	public void deleteUser(Integer id, String email) {
		System.out.print("Are you sure you want to delete your account?(y/n):");
		char ch=sc.next().charAt(0);
		if(ch=='y') {
			credentials.remove(email);
			userName.remove(id-1);
			userId.remove(id-1);
			cashBack.remove(id);
			
			System.out.println("\nSuccessfully deleted the Current user");
		}
	}
}
