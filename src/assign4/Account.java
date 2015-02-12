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
	
	public int getTransactions(){
		return numTransactions;
	}
	
	public int getBalance(){
		return balance;
	}
	
	public synchronized void addAmmount(int ammount){
		numTransactions = numTransactions + 1;
		balance = balance + ammount;
	}
	
	@Override
	public String toString(){
		return "acct:" + id + " bal:" + balance + " trans:" + numTransactions; 
	}
	
	//Overriding the compareTo method
	@Override
	public int compareTo(Account ac){
		Integer thisID = new Integer(this.getID());
		Integer thatID = new Integer(ac.getID());
		return thisID.compareTo(thatID);
	}

	//Overriding the compare method
	@Override
	public int compare(Account ac1, Account ac2){
		return ac1.getID() - ac2.getID();
	}
	
	@Override
	public boolean equals(Object obj){
		// standard equals() technique 1
		if (obj == this) return true;
		// standard equals() technique 2
		// (null will be false)
		if (!(obj instanceof Account)) return false;
		Account other = (Account)obj;
		if(this.getID() == other.getID()) return true;
		return false;
	}
	
	@Override
	public int hashCode(){
		Integer hc = new Integer(this.getID());
		return hc.hashCode();
	}

}
