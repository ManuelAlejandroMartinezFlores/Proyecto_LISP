import java.util.ArrayList;

public class Procesador {
	
	public static String evaluate(String expresion) {
		return procesarEX(expresion).evaluate().getData();
	}

	public static boolean revisarParen(String expresion) {
		char[] data = expresion.toCharArray();
		int abiertos = 0;
		for (char c : data) {
			if (Character.toString(c).equals("(")) {
				abiertos++;
			} else if (Character.toString(c).equals(")")) {
				abiertos--;
			}
			if (abiertos < 0) {
				return false;
			}
		}
		return abiertos == 0;
	}
	
	public static Node procesarEX(String expresion){
		assert(Procesador.revisarParen(expresion));
		expresion = casosEsp(expresion);
		ArrayList<Node> data = new ArrayList<Node>();
		int inicio = 0;
		int cnt = 0;
		int open = 0;
		expresion = expresion.substring(1, expresion.length() - 1);
		String[] exp = expresion.split(" ");
		for (String s : exp) {
			s = casosEsp(s);
			int temp_open = 0;
			char[] temp = s.toCharArray();
			for (char c : temp) {
				if (Character.toString(c).equals("(")) {
					open++;
					temp_open++;
				}
				if (Character.toString(c).equals(")")) {
					open--;
				}
			}
			
			if (Character.toString(s.charAt(s.length()-1)).equals(")") && Character.toString(s.charAt(0)).equals("(")){
				data.add(procesarEX(s));
			} else if (Character.toString(s.charAt(0)).equals("(") && open==temp_open) {
				inicio = cnt;
			} else if (Character.toString(s.charAt(s.length()-1)).equals(")") && open == 0) {
				String txt = "";
				for (int i=inicio; i<cnt; i++) {
					txt += exp[i] + " ";
				}
				txt += s;
				data.add(procesarEX(txt));
			}  else if (open == 0){
				data.add(new Atom(s));
			}
			cnt++;
		}
		return new SExpression(data);
	}
	
	public static String casosEsp(String exp) {
		if (exp.equals("=")) {
			exp = "equal";
		} else if (Character.toString(exp.charAt(0)).equals("\'") && Character.toString(exp.charAt(1)).equals("(")) {
			exp = "(quote " + exp.substring(2);
		}
		return exp;
		
	}
}