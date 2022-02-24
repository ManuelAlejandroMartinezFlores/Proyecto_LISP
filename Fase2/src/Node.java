/**
 * 
 * @author Manuel Alejandro Martínez Flores
 * @author Mario Puente
 * @author Pedro Marroquín
 * 
 * Node.
 * Representa nodo de árbol de expresiones.
 * Puede ser Atom o SExpression
 *
 */

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Node {

	
	protected String data;
	
	
	protected ArrayList<Node> tokens;
	
	/**
	 * Instancia el arreglo de Nodos
	 */
	public Node() {
		tokens = new ArrayList<Node>();
	}
	
	/**
	 * Indica si es lista
	 * @return true - lista / false - no es lista
	 */
	public abstract boolean isList();
	
	/**
	 * Evalua el nodo
	 * @return nodo evaluado
	 */
	public abstract Node evaluate();
	
	/**
	 * Evalua el nodo con variables extra
	 * @param vars_extra
	 * @return nodo evaluado
	 */
	public abstract Node evaluate(HashMap<String, Node> vars_extra);
	
	/**
	 * Indica contenido del nodo
	 * @return contenido
	 */
	public String getData() {
		return data;
	}
	
	/**
	 * Indica arreglo de tokens Node
	 * @return tokens
	 */
	public ArrayList<Node> getTokens(){
		return tokens;
	}
	
	/**
	 * Copia el Nodo
	 * @return nodo nuevo
	 */
	public abstract Node copy();
	
	/**
	 * Indica la expresión del nodo
	 * @return
	 */
	public String getSE() {
		return data;
	}

	/**
	 * Sustituye variables
	 * @param vars_extra
	 */
	protected abstract void susVars(HashMap<String, Node> vars_extra);
	
	 
}
