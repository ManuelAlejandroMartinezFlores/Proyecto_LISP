import java.util.ArrayList;
import java.util.HashMap;

public class Function {
	
	private Node expresion;
	private ArrayList<String> nombre_vars;
	
	
	public Function(Node se, ArrayList<String> n_vars) {
		expresion = se;
		nombre_vars = n_vars;
	}

	public Node evaluar(ArrayList<Node> tokens, HashMap<String, Node> vars) {
		if (vars == null) {
			vars = new HashMap<String, Node>();
		}
		for (int i=0; i<nombre_vars.size(); i++) {
			vars.put(nombre_vars.get(i), tokens.get(i).evaluate(vars));
		}
		
		return expresion.copy().evaluate(vars);
	}
	
	public String getSE() {
		return expresion.getSE();
	}
	
	public String getVars() {
		String txt = "";
		for (String n : nombre_vars) {
			txt += n + " ";
		}
		return txt;
	}
	
	public void print0() {
		System.out.println(expresion.getTokens().get(0).getData());
	}
}