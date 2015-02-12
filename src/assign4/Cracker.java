package assign4;

import java.security.*;

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
	
	//We're in cracker mode
	public Cracker(byte[] hash, int passwordLength, int numThreads){
		
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
		ck = new Cracker(hash.getBytes(),Integer.parseInt(length),Integer.parseInt(threads));
	}

}
