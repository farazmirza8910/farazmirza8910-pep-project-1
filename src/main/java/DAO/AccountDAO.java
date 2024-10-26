package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    /**
     * Retrieve all accounts from the 'account' table.
     */
    public List<Account> getAllAccounts() {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Account account = new Account(rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return accounts;
    }

    /**
     * Insert a new account into the 'account' table.
     */
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account(username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generated_account_id = generatedKeys.getInt(1);
                    return new Account(generated_account_id,
                    account.getUsername(),
                    account.getPassword());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return null;
    }

    /**
     * Retrieve account by username and password for login verification.
     */
    public Account getAccountByCredentials(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return null;
    }

    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new Account(rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return null;
    }

    /**
     * Retrieve account by ID.
     * @param accountId the ID of the account
     * @return Account object if found, otherwise null
     */
    public Account getAccountById(int accountId) {
        String sql = "SELECT * FROM account WHERE account_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}