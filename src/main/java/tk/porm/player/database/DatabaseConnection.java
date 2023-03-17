package tk.porm.player.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	public Connection connection;
	public DatabaseConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/porm", "root", "");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
