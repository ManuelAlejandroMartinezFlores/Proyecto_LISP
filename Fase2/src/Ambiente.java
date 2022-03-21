/**
 * 
 * @author Manuel Alejandro Martínez Flores
 * @author Mario Puente
 * @author Pedro Marroquín
 * 
 * Ambiente.
 * Clase que representa el ambiente de LISP
 * Contiene funciones y variables globales
 *
 */

import java.util.ArrayList;
import java.util.HashMap;

public class Ambiente {

	private static HashMap<String, Function> funcs = new HashMap<String, Function>();
	private static HashMap<String, Node> vars = new HashMap<String, Node>();
	
	/**
	 * Indica si es la firma de una función
	 * @param data a evaluar
	 * @return true - función / false - no es función
	 */
	public static boolean isFunc(String data) {
		return funcs.containsKey(data);
	}
	
	/**
	 * Indica la función de la firma dada
	 * @param data a evaluar
	 * @return función
	 */
	public static Function getFunc(String data) {
		return funcs.get(data);
	}
	
	/**
	 * Guarda función en el Ambiente
	 * @param tokens de la función (nombre (vars) (cuerpo))
	 */
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
	
	/**
	 * Guarda una función en el Ambiente
	 * @param func
	 * @param nombre
	 */
	public static void setFunc(Function func, String nombre) {
		funcs.put(nombre, func);
	}
	
	/**
	 * Guarda una variable en el Ambiente
	 * @param nombre
	 * @param node
	 */
	public static void setVar(String nombre, Node node) {
		vars.put(nombre, node);
	}
	
	/**
	 * Indica la variable indicada
	 * @param data nombre de variable
	 * @return valor de variable
	 */
	public static Node getVar(String data) {
		return vars.get(data);
	}
	
	/**
	 * Indica si la data corresponde a una variable
	 * @param data
	 * @return true - variable / false - no es variable
	 */
	public static boolean isVar(String data) {
		return vars.containsKey(data);
	}
	
}
