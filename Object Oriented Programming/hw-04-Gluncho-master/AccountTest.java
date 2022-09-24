import junit.framework.TestCase;

public class AccountTest extends TestCase {
    public void test1(){
        Account acc = new Account(0,1000);
        acc.changeBalance(-5);
        assertEquals("acct:0 bal:995 trans:1", acc.toString());
    }

    public void test2(){
        Account a1 = new Account(0,20);
        Account a2 = new Account(1, 100);
        Account a3 = new Account(2, 20);
        a1.changeBalance(-10);
        a2.changeBalance(10);
        assertEquals("acct:0 bal:10 trans:1", a1.toString());
        assertEquals("acct:1 bal:110 trans:1", a2.toString());
        assertEquals("acct:2 bal:20 trans:0", a3.toString());
    }
}
