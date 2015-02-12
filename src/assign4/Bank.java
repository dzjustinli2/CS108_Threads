package assign4;

import java.util.concurrent.*;
import java.util.*;
import java.io.*;

public class Bank{
	
	private HashMap<Integer,Account> accountMap;
	private ArrayBlockingQueue<Transaction> transactionQueue;
	private ArrayList<Account> accounts;
	private CountDownLatch latch;
	String file;
	int numWorkers;
	
	public class Worker extends Thread{
		public void run(){
			try{
				Transaction tr = transactionQueue.take();
				while(!tr.isSentinal()){
					performTransaction(tr);
					tr = transactionQueue.take();
				}
				latch.countDown();
			}catch(Exception e){
				System.out.println("Exception in thread run loop");
				e.printStackTrace();
			}
		}
		
		private void performTransaction(Transaction tr){
			
		}
	}

	private void startWorkers(){
		for(int i = 0; i < numWorkers; i++){
			Worker wk = new Worker();
			wk.start();
		}
	}

	public Bank(String file, int numWorkers){
		this.file = file;
		this.numWorkers = numWorkers;
		accountMap = new HashMap<Integer,Account>();
		transactionQueue = new ArrayBlockingQueue<Transaction>(numWorkers*3);
		accounts = new ArrayList<Account>();
		latch = new CountDownLatch(numWorkers);
	}
	
	public void open(){
		startWorkers();
		readFile(file);
		try{
			latch.await();
		}catch(InterruptedException e){
			System.out.println("Main thread was interrupted before latch counted down");
			e.printStackTrace();
		}
		printAccounts();
	}
	
	private void printAccounts(){
		for(Map.Entry<Integer, Account> accountEntry : accountMap.entrySet()){
			accounts.add(accountEntry.getValue());
        }
		Collections.sort(accounts);
		int size = accounts.size();
		for(int i = 0; i < size; i++){
			System.out.println(accounts.get(i));
		}
	}
	
	private void createAccount(String accountID){
		Integer id = new Integer(Integer.parseInt(accountID));
		if(!accountMap.containsKey(id)){
			Account ac = new Account(id);
			accountMap.put(id, ac);
		}
	}
	
	private Transaction createTransaction(String fromAcc, String toAcc, String ammount, boolean sentinal){
		if(sentinal){
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
				try{
					transactionQueue.put(tr);
				}catch(Exception e){
					System.out.println("Exception while reading in transactions");
					e.printStackTrace();
				}
				line = br.readLine();
			}
			br.close();
			try{
				for(int i = 0; i < numWorkers; i++){
					Transaction tr = createTransaction(null,null,null,true);
					transactionQueue.put(tr);
				}
			}catch(Exception e){
				System.out.println("Exception while adding sentinal transactions");
				e.printStackTrace();
			}
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
