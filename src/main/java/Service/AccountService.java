package Service;

import DAO.AccountDAO;
import Model.Account;

import java.util.List;

public class AccountService {
    private final AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public Account registerAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            return null; // Username is blank
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null; // Password is too short
        }
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            return null; // Username already exists
        }
        return accountDAO.insertAccount(account);
    }

    public Account login(Account account) {
        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount; // Login successful
        }
        return null; // Login failed
    }

    public Account getAccountById(int accountId) {
        return accountDAO.getAccountById(accountId);
    }

    public Account getAccountByUsername(String username) {
        return accountDAO.getAccountByUsername(username);
    }
}