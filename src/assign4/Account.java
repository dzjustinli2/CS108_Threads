package assign4;

import java.util.*;

public class Account implements Comparator<Account>, Comparable<Account>{
	
	private Integer id;
	private int balance;
	private int numTransactions;

	public Account(String id) {
		this(Integer.parseInt(id));
	}
	
	public Account(int id){
		this(new Integer(id));
	}
	
	public Account(Integer id){
		this.id = new Integer(id);
		balance = 1000;
		numTransactions = 0;
	}
	
	public int getID(){
		return id;
	}
	
	public String toString(){
		return "acct:" + id + " bal:" + balance + " trans:" + numTransactions; 
	}
	
	//Overriding the compareTo method
	public int compareTo(Account ac){
		Integer thisID = new Integer(this.getID());
		Integer thatID = new Integer(ac.getID());
		return thisID.compareTo(thatID);
	}

	//Overriding the compare method
	public int compare(Account ac1, Account ac2){
		return ac1.getID() - ac2.getID();
	}

}
