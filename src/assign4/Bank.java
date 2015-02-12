package assign4;

import java.util.*;
import java.io.*;

public class Bank {
	
	private HashMap<Integer,Account> accountMap;
	private ArrayList<Transaction> transactionArray;
	private int numWorkers;

	public Bank(String file, int numWorkers) {
		accountMap = new HashMap<Integer,Account>();
		this.numWorkers = numWorkers;
		readFile(file);
	}
	
	//Starts the bank processing the transactions
	public void open(){
		
	}
	
	private void readFile(String file){
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while(line != null){
				StringTokenizer cols = new StringTokenizer(line);
				String fromAcc = cols.nextToken();
				String toAcc = cols.nextToken();
				String ammount = cols.nextToken();
			}
			br.close();
		}catch(IOException e){
			System.out.println("Error reading in " + file);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String transactionFile = args[0];
		String numWorkers = args[1];
		Bank bn = new Bank(transactionFile,Integer.parseInt(numWorkers));
		bn.open();
	}

}
