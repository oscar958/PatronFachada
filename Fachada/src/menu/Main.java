package menu;

import java.util.ArrayList;

import menu.jdbc.FachadaJDBC;

public class Main {

	public static void main(String[] args) {

		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add("a");
		

		FachadaJDBC facade = new FachadaJDBC("oracle");
		facade.getSelect("select * from user where username like '%?%'", parameters);
	}

}
