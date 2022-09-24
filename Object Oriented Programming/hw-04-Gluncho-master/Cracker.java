// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import java.security.*;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	private static MessageDigest messageDigest;
	public class Worker extends Thread{
		private final int leftIndex;
		private final int rightIndex;
		private MessageDigest digest;
		private final byte[] hash;
		public Worker(int leftIndex, int rightIndex, byte[] hash) {
			this.leftIndex = leftIndex;
			this.rightIndex = rightIndex;
			this.hash = hash;
			try {
				digest = MessageDigest.getInstance("SHA");
			} catch (NoSuchAlgorithmException ignored) {}
		}

		@Override
		public void run() {
			tryAllPasswords();
			latch.countDown();
		}

		private void tryAllPasswords() {
			StringBuilder builder = new StringBuilder("");
			for(int i = leftIndex; i <= rightIndex; i++){
				builder.append(CHARS[i]);
				recTryAllPasswords(builder);
				builder.deleteCharAt(builder.length() - 1);
			}
		}

		private void recTryAllPasswords(StringBuilder password) {
			if(password.length() > maxLength) return;
			if(hasRequiredHash(password)) System.out.println(password);
			for(char ch : CHARS){
				password.append(ch);
				recTryAllPasswords(password);
				password.deleteCharAt(password.length() - 1);
			}
		}

		private boolean hasRequiredHash(StringBuilder password) {
			byte[] bytes = password.toString().getBytes();
			digest.update(bytes);
			return Arrays.equals(digest.digest(), hash);
		}

	}
	private final CountDownLatch latch;
	private final int maxLength;
	private final int numWorkers;

	/**
	 * Constructs a new object.
	 */
	public Cracker(int maxLength, int numWorkers) {
		this.maxLength = maxLength;
		this.numWorkers = numWorkers;
		latch = new CountDownLatch(numWorkers);
	}


	private static void initMessageDigest(){
		try{
			messageDigest = MessageDigest.getInstance("SHA");
		}catch(NoSuchAlgorithmException e){e.printStackTrace();}
	}
	public void crack(String hash){
		byte[] bytes = hexToArray(hash);
		int part = CHARS.length / numWorkers;
		for(int i = 0; i < CHARS.length; i += part){
			Worker worker;
			if(i + part > CHARS.length) worker = new Worker(i, CHARS.length - 1, bytes);
			else worker = new Worker(i, i + part - 1, bytes);
			worker.start();
		}
		try{
			latch.await();
		}catch(InterruptedException ignored){}
	}


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

	public static String getHash(String password){
		if(messageDigest == null) initMessageDigest();
		messageDigest.update(password.getBytes());
		return hexToString(messageDigest.digest());
	}
	
	
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Args: target length [workers]");
			return;
		}else if(args.length == 1){
			String password = args[0];
			String hash = getHash(password);
			System.out.println(hash);
			return;
		}
		// args: targ len [num]
		String targ = args[0];
		int len = Integer.parseInt(args[1]);
		int num = 1;
		if (args.length>2) {
			num = Integer.parseInt(args[2]);
		}
		Cracker cracker = new Cracker(len, num);
		// a! 34800e15707fae815d7c90d49de44aca97e2d759
		// xyz 66b27417d37e024c46526c2f6d358a754fc552f3

		// YOUR CODE HERE
		cracker.crack(targ);
	}
}
