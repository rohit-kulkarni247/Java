package BankDir;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		User user=new User();
		char ch;
		System.out.println("--------------------------------------------------------\n|\tWelcome To the Bank Management System\t\t|\n--------------------------------------------------------\n");
		do {
			
			System.out.println("1)User\n2)Admin\n3)Exit");
			Scanner sc=new Scanner(System.in);
			Integer Choice=sc.nextInt();
			switch(Choice) {
				case 1:
					user.userCall();
				break;
				case 2:
					user.AdminCall();
				break;
				case 3:	
					System.out.println("Exitted successfully");
				return;
				default:
					System.out.println("Invalid input");
				break;

			}
			System.out.println("Do you wish to continue with the MAIN MENU (y/n)?");
			ch=sc.next().charAt(0);
		}while(ch=='y');
	}

}
