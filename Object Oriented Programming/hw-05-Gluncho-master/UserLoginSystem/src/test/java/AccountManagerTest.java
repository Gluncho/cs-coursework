import junit.framework.TestCase;
import loginsystem.AccountManager;

public class AccountManagerTest extends TestCase {

    public void test1(){
        AccountManager manager = new AccountManager();
        assertTrue(manager.containsUser("Patrick"));
        assertTrue(manager.containsUser("Molly"));
    }

    public void testAddAccount(){
        AccountManager manager = new AccountManager();
        manager.addAccount("23", "34");
        manager.addAccount("12", "23");
        assertTrue(manager.isValidAccount("23", "34"));
        assertTrue(manager.isValidAccount("23", "34"));
        assertFalse(manager.isValidAccount("23", "23"));
        assertTrue(manager.containsUser("23"));
        assertTrue(manager.containsUser("12"));
        assertFalse(manager.containsUser("11"));
    }
}
