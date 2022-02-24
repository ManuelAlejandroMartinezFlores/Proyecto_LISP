import java.util.ArrayList;
import java.util.HashMap;

public class Ambiente {

	public static HashMap<String, Function> funcs = new HashMap<String, Function>();
	public static HashMap<String, Node> vars = new HashMap<String, Node>();
	
	public static boolean isFunc(String data) {
		return funcs.containsKey(data);
	}
	
	public static Function getFunc(String data) {
		return funcs.get(data);
	}
	
	public static void setFunc(ArrayList<Node> tokens) {
		String nombre = tokens.remove(0).getData();
		Node n_vars = tokens.remove(0);
		ArrayList<String> nombre_vars = new ArrayList<String>();
		for (Node n : n_vars.getTokens()) {
			nombre_vars.add(n.getData());
		}
		Node se = tokens.get(0);
		Function func = new Function(se, nombre_vars);
		funcs.put(nombre, func);
	}
	
	public static void setFunc(Function func, String nombre) {
		funcs.put(nombre, func);
	}
	
	public static void setVar(String nombre, Node node) {
		vars.put(nombre, node);
	}
	
	public static Node getVar(String data) {
		return vars.get(data);
	}
	
}
