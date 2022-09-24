// Account.java

import static java.lang.String.format;

/*
 Simple, thread-safe Account class encapsulates
 a balance and a transaction count.
*/
public class Account {
	private int id;
	private int balance;
	private int transactions;


	public Account(int id, int balance) {
		this.id = id;
		this.balance = balance;
		transactions = 0;
	}

	/**
	 * Adds the specific amount of balance to an account.
	 * This balance may be any integer, varying from negative to positive.
	 * In negative case, the account balance becomes smaller.
	 * @param amount amount of money that has to be added
	 */
	public synchronized void changeBalance(int amount){
		balance += amount;
		transactions++;
	}
	/**
	 * Returns a string representation of the object.
	 */
	@Override
	public String toString() {
		return format("acct:%d bal:%d trans:%d", id, balance, transactions);
	}
}
