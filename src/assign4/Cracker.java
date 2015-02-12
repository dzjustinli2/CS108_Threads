package assign4;

import java.security.*;
import java.util.concurrent.*;
import java.util.*;
import java.io.*;
import java.lang.StringBuilder;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();	
	
	// possible test values:
	// a 86f7e437faa5a7fce15d1ddcb9eaeaea377667b8
	// fm adeb6f2a18fe33af368d91b09587b68e3abcb9a7
	// a! 34800e15707fae815d7c90d49de44aca97e2d759
	// xyz 66b27417d37e024c46526c2f6d358a754fc552f3	
	
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}
	
	private static String generator(String word){
		 try {
			 MessageDigest md = MessageDigest.getInstance("SHA");
			 String hash = getPrintableHash(word,md);
			 return hash;
		 }catch(Exception e) {
			 System.out.println("Exception computing digest on main thread");
			 e.printStackTrace();
		 }
		 return "Error generator hash";
	}
	
	private static String getPrintableHash(String word, MessageDigest md){
		return hexToString(md.digest(word.getBytes()));
	}
	
	/*-------------------------------*/
	
	private String generatedHash;
	
	//We're in generator mode
	public Cracker(String word,boolean print){
		if(word.length() == 0) return;
		generatedHash = generator(word);
		if(print) System.out.println(generatedHash);
	}
	
	public String generateHash(String word){
		generatedHash = generator(word);
		return generatedHash;
	}
	
	public String getGeneratedHash(){
		return generatedHash;
	}
	
	/*-------------------------------*/
	
	private int numThreads;
	private int passwordLength;
	private byte[] hash;
	private String password;
	private CountDownLatch found;
	private boolean foundPassword;
	MessageDigest md;
	
	//We're in cracker mode
	public Cracker(byte[] hash, int passwordLength, int numThreads){
		this.hash = hash;
		this.passwordLength = passwordLength;
		this.numThreads = numThreads;
		password = "";
		found = new CountDownLatch(1);
		foundPassword = false;
		try{
			md = MessageDigest.getInstance("SHA");
		}catch(Exception e) {
			System.out.println("Exception generating instance of message encryption algorithm");
			e.printStackTrace();
		}
	}

	private synchronized void setPassword(String password){
		this.password = password;
	}
	
	private synchronized void setFoundPassword(){
		this.foundPassword = true;
	}
	
	private synchronized boolean getFoundPassword(){
		return this.foundPassword;
	}
	
	public String findPassword(){
		int vocabSize = CHARS.length;
		int increment = vocabSize/numThreads;
		int remainder = vocabSize%numThreads;
		for(int i = 0; i < numThreads; i++){
			Worker wk;
			if(i == numThreads - 1){
				wk = new Worker(i*increment,(i+1)*increment-1+remainder);
			}else{
				wk = new Worker(i*increment,(i+1)*increment-1);
			}
			wk.start();
		}
		try{
			found.await();
		}catch(InterruptedException e){
			System.out.println("Main thread was interrupted before password was found");
			e.printStackTrace();
		}
		return password;
	}

	public class Worker extends Thread{
		//begin and end are inclusive
		private int begin;
		private int end;
		
		public Worker(int start, int finish){
			super();
			begin = start;
			end = finish;
		}
		
		public void run(){
			try{
				boolean val = false;
				for(int i = begin; i <= end; i++){
					Character ch = new Character(CHARS[i]);
					val = makeString(ch.toString());
					if(val) break;
				}
			}catch(Exception e){
				System.out.println("Exception in thread run loop");
				e.printStackTrace();
			}
		}
		
		private boolean makeString(String startingValue){
			if(getFoundPassword()) return false;
			if(startingValue.length() > passwordLength) return false;
			byte[] myHash = md.digest(startingValue.getBytes());
			if(myHash.equals(hash)){
				setPassword(startingValue);
				setFoundPassword();
				found.countDown();
				return true;
			}
			for(int i = 0; i < CHARS.length; i++){
				Character ch = new Character(CHARS[i]);
				String newValue = startingValue + ch.toString();
				if(makeString(newValue)) return true;
			}
			return false;
		}
		
	}
	
	public static void main(String[] args){
		Cracker ck;
		if(args.length == 1){
			ck = new Cracker(args[0],true);
			return;
		}
		if(args.length != 3) return;
		String hash = args[0];
		String length = args[1];
		String threads = args[2];
		ck = new Cracker(hexToArray(hash),Integer.parseInt(length),Integer.parseInt(threads));
		String password = ck.findPassword();
		System.out.println(password);
		System.out.println("all done");
	}

}
