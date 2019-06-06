package menu.business;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import menu.jdbc.AdminConJDBC;

public class ProcesarConsulta {

	public static String exec(AdminConJDBC conn, String query, ArrayList<Object> params) {
		StringBuilder queryResult = new StringBuilder();

		if (conn != null) {
			try {
				PreparedStatement statement = conn.getConn().prepareStatement(transformParameters(query, params));
				ResultSet rs = statement.executeQuery();

				while (rs.next()) {
					queryResult.append(getResultsetDataByType(rs));
				}
				rs.close();
				statement.close();
				conn.getConn().close();

			} catch (SQLException e) {
				System.out.println("error consultando la información.");
				e.printStackTrace();
				return null;
			}
		}
		return queryResult.toString();
	}


	private static String getResultsetDataByType(ResultSet rs) {
		StringBuilder result = new StringBuilder();
		ResultSetMetaData metadata = null;
		try {
			metadata = rs.getMetaData();

			for (int i = 1; i <= metadata.getColumnCount(); i++) {

				switch (metadata.getColumnType(i)) {
				case Types.TIMESTAMP:
					if (null != rs.getTimestamp(i))
						result.append(rs.getTimestamp(i).toString()).append(", ");
					else
						result.append("[null]");
					break;
				case Types.DATE:
					if (null != rs.getDate(i))
						result.append(rs.getDate(i).toString()).append(", ");
					else
						result.append("[null]");
					break;
				case Types.INTEGER:
					Integer integer = rs.getInt(i);
					if (null != integer)
						result.append(String.valueOf(rs.getInt(i))).append(", ");
					break;
				case Types.VARCHAR:
					if (null != rs.getString(i))
						result.append(rs.getString(i).toString()).append(", ");
					else
						result.append("[null]");
					break;
				
				default:
					System.out.println("[Le faltó el tipo " + metadata.getColumnType(i) + "]");
					break;
				}
			}
		} catch (SQLException e) {
			System.out.println("Eror al analizar los tipos de datos devueltos.");
			e.printStackTrace();
		}
		result.append("\n");
		return result.toString();
	}

	
	private static String transformParameters(String query, ArrayList<Object> params) {
		String queryTemp = query;
		String s = "\\?";
		String s2 = "(?<=%)(.*)(?=%)";
		Pattern p = Pattern.compile(s);

		for (Object object : params) {
			Matcher m = p.matcher(queryTemp);
			if (object.getClass() == Integer.class) {
				queryTemp = m.replaceFirst(object.toString());
			} else if (object.getClass() == String.class) {
				if(!(query.contains("'%") && query.contains("%'"))) {
					queryTemp = m.replaceFirst("'" + object.toString() + "'");
				} else {
					queryTemp = m.replaceFirst(object.toString());
				}
			}
		}
		return queryTemp;
	}
}
