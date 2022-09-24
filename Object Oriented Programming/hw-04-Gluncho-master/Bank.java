// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Bank {

	public class Worker extends Thread{
		@Override
		public void run() {
			while(true){
				try {
					Transaction currTransaction = queue.take();
					if(currTransaction == nullTrans) {
						queue.put(nullTrans);
						latch.countDown();
						break;
					}
					makeTransaction(currTransaction);
				} catch (InterruptedException e){e.printStackTrace();}
			}
		}
	}

	private void makeTransaction(Transaction currTransaction) {
		Account from = accounts.get(currTransaction.from);
		Account to = accounts.get(currTransaction.to);
		int amount = currTransaction.amount;
		from.changeBalance(-amount);
		to.changeBalance(amount);
	}

	public static final int ACCOUNTS = 20;	 // number of accounts
	public static final int INITIAL_BALANCE = 1000; // initial balance
	private final Transaction nullTrans = new Transaction(-1, 0, 0); // null transaction
	private List<Account> accounts;
	private BlockingQueue<Transaction> queue;
	private CountDownLatch latch;


	/**
	 * Constructs a new object.
	 */
	public Bank(int numWorkers) {
		super();
		queue = new ArrayBlockingQueue<>(Buffer.SIZE);
		latch = new CountDownLatch(numWorkers);
		accounts = new ArrayList<>();
		for(int i = 0; i < ACCOUNTS; i++){
			Account account = new Account(i, INITIAL_BALANCE);
			accounts.add(account);
		}
	}

	/*
         Reads transaction data (from/to/amt) from a file for processing.
         (provided code)
         */
	public void readFile(String file) {
			try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			// Use stream tokenizer to get successive words from file
			StreamTokenizer tokenizer = new StreamTokenizer(reader);
			
			while (true) {
				int read = tokenizer.nextToken();
				if (read == StreamTokenizer.TT_EOF) {
					queue.put(nullTrans);
					break;  // detect EOF
				}
				int from = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int to = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int amount = (int)tokenizer.nval;
				if(from == to) continue;
				queue.put(new Transaction(from, to, amount));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/*
	 Processes one file of transaction data
	 -fork off workers
	 -read file into the buffer
	 -wait for the workers to finish
	*/
	public void processFile(String file, int numWorkers) {
		for(int i = 0; i < numWorkers; i++){
			Worker worker = this.new Worker();
			worker.start();
		}
		readFile(file);
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		for(Account account : accounts) System.out.println(account);
	}

	public List<Account> getAccounts(){
		return accounts;
	}
	
	/*
	 Looks at commandline args and calls Bank processing.
	*/
	public static void main(String[] args) throws InterruptedException {
		// deal with command-lines args
		if (args.length == 0) {
			System.out.println("Args: transaction-file [num-workers [limit]]");
			return;
		}
		
		String file = args[0];
		
		int numWorkers = 1;
		if (args.length >= 2) {
			numWorkers = Integer.parseInt(args[1]);
		}
		
		Bank bank = new Bank(numWorkers);
		bank.processFile(file, numWorkers);
	}
}

