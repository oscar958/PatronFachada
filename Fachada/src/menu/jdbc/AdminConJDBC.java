/**
 * 
 */
package menu.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Usa par�metros para establecer una conexi�n con una base de datos.
 * 
 * @author IPinilla
 *
 */
public class AdminConJDBC {

	private String dbDriver = "jdbc:postgresql://";
	private Connection conn = null;

	/**
	 * Obtiene el driver adecuado, si no lo encuentra, genera una excepci�n.
	 * 
	 * @param driverName Nombre edl driver de conexi�n a base de datos.
	 * @return Devuelve <code><b>true</b></code> si encontr� un driver adecuado.
	 */
	public boolean getDriver(String driverName) {
		try {
			Class.forName(driverName);
			System.out.println("Driver registrado.");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver no encontrado.");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Conecta a la base de datos con los par�metros ingresados.
	 * 
	 * @param host       Nombre del servidor.
	 * @param port       Puerto de conexi�n al servidor.
	 * @param database   Nombre de la base de datos a la que se va a conectar.
	 * @param dbUser     Nombre del usuario de conexi�n a la base de datos.
	 * @param dbPassword Clave del usuario de conexi�n a la base de datos.
	 * @return Devuelve <code><b>true</b></code> si pudo conectarse a la base de
	 *         datos.
	 */
	public boolean connect(String host, int port, String database, String dbUser, String dbPassword) {

		try {
			conn = DriverManager.getConnection(dbDriver + host + ":" + String.valueOf(port) + "/" + database, dbUser,
					dbPassword);
		} catch (SQLException e) {
			System.out.println("Conexi�n fall�:");
			e.printStackTrace();
			return false;
		}

		if (conn != null) {
			System.out.println("Conexi�n establecida.");
			return true;
		} else {
			System.out.println("Coneci�n fall�.");
		}

		return false;
	}

	/**
	 * Devuelve el objeto de conexi�n a la base ed datos
	 * 
	 * @return Objeto de conexi�n.
	 */
	public Connection getConn() {
		return conn;
	}
}
