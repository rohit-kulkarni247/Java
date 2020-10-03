package BankDir;
import java.util.*;
import java.lang.Math; 

public class Admin {
	public void displayList(HashMap<String,String>credentials, ArrayList<Integer> userId, Vector<String> username) {
		System.out.println("User-id\t\tUsername\tEmail-Id");
		Iterator it = credentials.entrySet().iterator();
		Integer cnt=0;
		Enumeration<Integer> e = Collections.enumeration(userId);
		while (it.hasNext() && e.hasMoreElements()) { 
		    Map.Entry mapElement = (Map.Entry)it.next(); 
		    System.out.println(e.nextElement() +"\t\t"+username.get(cnt)+"\t\t"+ mapElement.getKey()); 
		    cnt++;
		}
	}
	
	public int createOffer(Integer sendBal) {
		Integer amount = 0;
		if(sendBal>500) {
			amount=((int)(Math.random() * 10)+1);
		}
		else
			amount=0;
		return amount;
	}
	public void offers(LinkedHashMap<Integer, LinkedList<Integer>> cashBack,Vector<String>userName) {
		System.out.println("User-id\t\tUserName\tCashBack Earned");
		Iterator it = cashBack.entrySet().iterator();
		Integer cnt=0;
		//Enumeration e = Collections.enumeration(userId);
		while (it.hasNext()) { 
		    Map.Entry mapElement = (Map.Entry)it.next();
		    Integer id=((Integer)mapElement.getKey())-1;
		    System.out.println(mapElement.getKey()+"\t\t"+userName.get(id)+"\t\t"+mapElement.getValue()); 
		    cnt++;
		}
	}
}


