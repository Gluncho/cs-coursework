import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class BankTest extends TestCase {

    private void checkAccountBalancesEqual(Bank bank1, Bank bank2) {
        List<Account> actual = bank1.getAccounts();
        List<Account> expected = bank2.getAccounts();
        assertEquals(Bank.ACCOUNTS, expected.size());
        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < actual.size(); i++){
            assertEquals(expected.get(i).toString(), actual.get(i).toString());
        }
    }

    public void testSmall(){
        Bank small = new Bank(100);
        Bank singleThread = new Bank(1);
        small.processFile("small.txt", 100);
        singleThread.processFile("small.txt", 1);
        checkAccountBalancesEqual(small, singleThread);
    }

    public void testMedium(){
        Bank medium = new Bank(20);
        Bank singleThread = new Bank(1);
        medium.processFile("5k.txt", 20);
        singleThread.processFile("5k.txt", 1);
        checkAccountBalancesEqual(medium, singleThread);
    }

    public void testLarge(){
        Bank large = new Bank(500);
        Bank singleThread = new Bank(3);
        large.processFile("100k.txt", 500);
        singleThread.processFile("100k.txt", 3);
        checkAccountBalancesEqual(large, singleThread);
    }

    public void testMain(){
        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                Bank.main(new String[]{"5k.txt", "200"});
            }
        });
    }

    public void testMainEmpty() throws InterruptedException {
        OutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os));
        Bank.main(new String[]{});
        String out = os.toString();
        out = out.substring(0, out.length() - 2); // remove the end-line
        assertEquals("Args: transaction-file [num-workers [limit]]", out);
    }

}
