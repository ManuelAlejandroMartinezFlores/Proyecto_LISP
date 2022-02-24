/**
 * 
 * @author Manuel Alejandro Martínez Flores
 * @author Mario Puente
 * @author Pedro Marroquín
 * 
 * Function.
 * Representa una función de LISP
 * Tiene un arreglo de variables y una SExpression como cuerpo
 *
 */

import java.util.ArrayList;
import java.util.HashMap;

public class Function {
	
	private Node expresion;
	private ArrayList<String> nombre_vars;
	
	/**
	 * Constructor
	 * @param se Expresion LISP cuerpo
	 * @param n_vars nombre de las variables de argumento
	 */
	public Function(Node se, ArrayList<String> n_vars) {
		expresion = se;
		nombre_vars = n_vars;
	}

	/**
	 * Evalua el cuerpo de la función con ciertos paramétros
	 * @param tokens párametros de la reunión
	 * @param vars Variables a sustituir
	 * @return
	 */
	public Node evaluar(ArrayList<Node> tokens, HashMap<String, Node> vars) {
		if (vars == null) {
			vars = new HashMap<String, Node>();
		}
		for (int i=0; i<nombre_vars.size(); i++) {
			vars.put(nombre_vars.get(i), tokens.get(i).evaluate(vars));
		}
		
		return expresion.copy().evaluate(vars);
	}
	
	/**
	 * Indica la expresión lisp como texto
	 * @return expresión lisp
	 */
	public String getSE() {
		return expresion.getSE();
	}
	
	/**
	 * Indica las variables de la función
	 * @return variables
	 */
	public String getVars() {
		String txt = "";
		for (String n : nombre_vars) {
			txt += n + " ";
		}
		return txt;
	}

}
