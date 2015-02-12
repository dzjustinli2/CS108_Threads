package assign4;

import java.util.concurrent.*;
import java.util.*;
import java.io.*;

public class Bank{
	
	private HashMap<Integer,Account> accountMap;
	private ArrayBlockingQueue<Transaction> transactionQueue;
	int numWorkers;

	public Bank(String file, int numWorkers){
		this.numWorkers = numWorkers;
		accountMap = new HashMap<Integer,Account>();
		transactionQueue = new ArrayBlockingQueue<Transaction>(numWorkers*3);
		readFile(file);
	}
	
	private void createAccount(String accountID){
		
	}
	
	private Transaction createTransaction(String fromAcc, String toAcc, String ammount){
		return null;
	}
	
	private void readFile(String file){
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while(line != null){
				//System.out.println(line);
				StringTokenizer cols = new StringTokenizer(line);
				String fromAcc = cols.nextToken();
				String toAcc = cols.nextToken();
				String ammount = cols.nextToken();
				createAccount(fromAcc);
				createAccount(toAcc);
				Transaction tr = createTransaction(fromAcc,toAcc,ammount);
				//transactionQueue.put(tr);
				line = br.readLine();
			}
			br.close();
		}catch(IOException e){
			System.out.println("Error reading in " + file);
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		String transactionFile = args[0];
		String numWorkers = args[1];
		Bank bn = new Bank(transactionFile,Integer.parseInt(numWorkers));
	}

}
