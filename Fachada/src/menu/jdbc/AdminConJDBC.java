
package menu.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class AdminConJDBC {
	
	private String dbDriver = "jdbc:oracle:thin//";
	private Connection connected = null;
	
	public boolean getDriver(String driverName) {
		try {
			Class.forName(driverName);
			System.out.println("Driver registrado");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver no encontrado");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean connect(String host, int port, String database, String dbUser, String dbPassword) {
		try {
			connected = DriverManager.getConnection(dbDriver + host + ":" + String.valueOf(port) + "/" + database, dbUser,
					dbPassword);
		} catch (SQLException e) {
			System.out.println("Conexión falló:");
			e.printStackTrace();
			return false;
		}
		if (connected != null) {
			System.out.println("Conexión establecida.");
			return true;
		} else {
			System.out.println("Conexión fallida.");
		}
		return false;
	}
	public Connection getConn() {
		return conn;
	}
}
