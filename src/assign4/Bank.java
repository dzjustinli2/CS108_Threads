package assign4;

import java.util.concurrent.*;
import java.util.*;
import java.io.*;

public class Bank{
	
	private HashMap<Integer,Account> accountMap;
	private ArrayBlockingQueue<Transaction> transactionQueue;
	String file;
	int numWorkers;

	public Bank(String file, int numWorkers){
		this.file = file;
		this.numWorkers = numWorkers;
		accountMap = new HashMap<Integer,Account>();
		transactionQueue = new ArrayBlockingQueue<Transaction>(numWorkers*3);
	}
	
	public void open(){
		startWorkers();
		readFile(file);
	}
	
	private void startWorkers(){
		
	}
	
	private void createAccount(String accountID){
		Integer id = new Integer(Integer.parseInt(accountID));
		if(!accountMap.containsKey(id)){
			Account ac = new Account(id);
			accountMap.put(id, ac);
		}
	}
	
	private Transaction createTransaction(String fromAcc, String toAcc, String ammount, boolean terminator){
		if(terminator){
			return new Transaction();
		}else{
			return new Transaction(fromAcc,toAcc,ammount);
		}
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
				Transaction tr = createTransaction(fromAcc,toAcc,ammount,false);
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
		bn.open();
	}

}
