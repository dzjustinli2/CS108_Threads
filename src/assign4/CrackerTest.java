package assign4;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.*;

public class CrackerTest {
	
	private HashMap<String,String> genMap;
	
	private void setMapUp(){
		genMap = new HashMap<String,String>();
		genMap.put("a","86f7e437faa5a7fce15d1ddcb9eaeaea377667b8");
		genMap.put("fm","adeb6f2a18fe33af368d91b09587b68e3abcb9a7");
		genMap.put("a!","34800e15707fae815d7c90d49de44aca97e2d759");
		genMap.put("xyz","66b27417d37e024c46526c2f6d358a754fc552f3");
	}

	@Test
	public void testGenerator() {
		setMapUp();
		Cracker ck = new Cracker("",false);
		for(Map.Entry<String, String> accountEntry : genMap.entrySet()){
			String hash = ck.generateHash(accountEntry.getKey());
			assertEquals(hash,accountEntry.getValue());
        }
		
	}

}
