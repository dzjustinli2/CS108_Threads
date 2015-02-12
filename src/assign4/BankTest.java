package assign4;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.ArrayList;

public class BankTest {
	
	@Test
	public void smallTest() {
		String file = "small.txt";
		int maxThreads = 10;
		for(int i = 1; i <= maxThreads; i++){
			Bank bn = new Bank(file,i);
			bn.open(false);
			ArrayList<Account> ar = bn.getAccounts();
			int size = ar.size();
			for(int j = 0; j < size; j++){
				Account acc = ar.get(j);
				assertEquals(acc.getTransactions(),1);
				if(acc.getID()%2 == 0){
					assertEquals(acc.getBalance(),999);
				}else{
					assertEquals(acc.getBalance(),1001);
				}
			}
		}
	}
	
	@Test
	public void medTest() {
		String file = "5k.txt";
		int maxThreads = 10;
		for(int i = 1; i <= maxThreads; i++){
			Bank bn = new Bank(file,i);
			bn.open(false);
			ArrayList<Account> ar = bn.getAccounts();
			int size = ar.size();
			for(int j = 0; j < size; j++){
				Account acc = ar.get(j);
				assertEquals(acc.getBalance(),1000);
			}
		}
	}
	
	@Test
	public void bigTest() {
		String file = "100k.txt";
		int maxThreads = 10;
		for(int i = 1; i <= maxThreads; i++){
			Bank bn = new Bank(file,i);
			bn.open(false);
			ArrayList<Account> ar = bn.getAccounts();
			int size = ar.size();
			for(int j = 0; j < size; j++){
				Account acc = ar.get(j);
				assertEquals(acc.getBalance(),1000);
			}
		}
	}

}
