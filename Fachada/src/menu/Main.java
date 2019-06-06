package menu;

import java.util.ArrayList;

import menu.jdbc.FachadaJDBC;

public class Main {

	/**
	 * M�todo inicial.
	 * 
	 * @param args Par�metros de entrada, no se usan :P
	 */
	public static void main(String[] args) {

		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add("a");
		//parameters.add("ivan.pinilla@gmail.com");

		FachadaJDBC facade = new FachadaJDBC("oracle");
		facade.getSelect("select * from userscore where username like '%?%'", parameters);
	}

}
