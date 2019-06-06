/**
 * 
 */
package menu.jdbc;

import java.util.ArrayList;

import menu.business.ProcesarConsulta;

public class FachadaJDBC {

	public String host = "localhost";
	public int port = 1521;
	public String database = "SYSTEM";
	public String databaseUser = "SYSTEM";
	public String databasePassword = "123456";

	private AdminConJDBC connection = new AdminConJDBC();

	public FachadaJDBC(String database) {

		try {
			if (null == database || database.isEmpty()) {
				System.out.println("Iniciar la BD");
			}
			if (database.toLowerCase().equals("oracle")) {
				if (!connection.getDriver("oracle.jdbc.OracleDriver")) {
					System.out.println("Driver no encontrado.");
				}
				if (!connection.connect(host, port, this.database, databaseUser, databasePassword)) {
					System.out.println("No se pudo conectar BD ");
				}
			} else {
				System.out.println("Cree la BD");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getSelect(String query, ArrayList<Object> params) {
		if (null != connection) {
			System.out.println(ProcesarConsulta.exec(connection, query, params));
		} else {
			System.out.println("Error con la conexión");
		}
	}

	public AdminConJDBC getConnection() {
		return connection;
	}

}
