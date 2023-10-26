package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Account;
import model.Login;

public class AccountDAO {
    // データベース接続に使用する情報
    private final String JDBC_URL = "jdbc:mysql://172.16.0.178:3306/Asubaka";
    private final String DB_USER = "sa";
    private final String DB_PASS = "";


	public Account findByLogin(Login login) {
        Account account = null;

        // MySQLのJDBCドライバをロード
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        // データベースへ接続
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

            // SELECT文を準備
            String sql = "SELECT NAME, PASS, MAIL, OBJECTIVE, REWARD, DAY, COUNT, DATE FROM ACCOUNT WHERE NAME = ? AND PASS = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, login.getName());
            pStmt.setString(2, login.getPass());

            // SELECTを実行し、結果表を取得
            ResultSet rs = pStmt.executeQuery();

            // 一致したユーザーが存在した場合
            // そのユーザーを表すAccountインスタンスを生成
            if (rs.next()) {
                // 結果表からデータを取得
                String name = rs.getString("NAME");
                String pass = rs.getString("PASS");
                String mail = rs.getString("MAIL");
                String objective = rs.getString("OBJECTIVE");
                String reward = rs.getString("REWARD");
                int day = rs.getInt("DAY");
                int count =rs.getInt("COUNT");
                Object date = rs.getDate("DATE"); // 日付情報を取得
                account = new Account(name, pass, mail, objective, reward, day, count, date);
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        // 見つかったユーザーまたはnullを返す
        return null;
    } 

	public void update(Account account) {
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	        return;
	    }

	    try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
	        String sql = "UPDATE ACCOUNT SET DAY = ?, DATE = ? WHERE NAME = ?";
	        PreparedStatement pStmt = conn.prepareStatement(sql);
	        pStmt.setInt(1, account.getDay());
	        pStmt.setObject(2, account.getDate()); // java.sql.Dateを使用して日付を設定
	        pStmt.setString(3, account.getName());

	        pStmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
}
