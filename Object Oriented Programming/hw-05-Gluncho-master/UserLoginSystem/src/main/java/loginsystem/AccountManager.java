package loginsystem;

import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    Map<String,String> accounts;
    /**
     * Constructs a new object.
     */
    public AccountManager() {
        accounts = new HashMap<>();
        accounts.put("Patrick", "1234");
        accounts.put("Molly","FloPup");
    }

    public boolean isValidAccount(String username, String password){
        return password.equals(accounts.get(username));
    }

    public boolean containsUser(String username){
        return accounts.containsKey(username);
    }
    public void addAccount(String username, String password){
        accounts.put(username, password);
    }
}
