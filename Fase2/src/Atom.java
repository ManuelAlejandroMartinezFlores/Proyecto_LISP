/**
 * 
 * @author Manuel Alejandro Martínez Flores
 * @author Mario Puente
 * @author Pedro Marroquín
 * 
 * Atom.
 * Representa un Atom de LISP
 * Es indivisible y tiene un valor.
 *
 */

import java.util.HashMap;

public class Atom extends Node {

	

	private int tipo;
	
	/**
	 * Indica si es una lista
	 * Todo Atom no es una lista
	 */
	@Override
	public boolean isList() {
		return false;
	}
	
	/**
	 * Construye un Atom booleano
	 * @param b
	 */
	public Atom(boolean b) {
		if (b) {
			data = "T";
		} else {
			data = "NIL";
		}
		tokens.add(this);
		tipo = 0;
	}
	
	/**
	 * Construye un Atom de entero
	 * @param i
	 */
	public Atom(int i) {
		data = String.valueOf(i);
		tokens.add(this);
		tipo = 1;
	}
	
	/**
	 * Construye un Atom de String
	 * @param s
	 */
	public Atom(String s) {
		data = s;
		tokens.add(this);
		tipo = 2;
	}
	
	/**
	 * Construye un Atom
	 * @param data_
	 * @param tipo_ 0 - boolean / 1 - int / 2 - String
	 */
	public Atom(String data_, int tipo_) {
		data = data_;
		tipo = tipo_;
	}
	
	/**
	 * Evalua el valor del nodo
	 */
	public Node evaluate() {
		// Evalua si es variable golobal
		if (Ambiente.vars.containsKey(data)) {
			return Ambiente.vars.get(data);
		}
		return this;
	}

	/**
	 * Evalua el valor del nodo
	 * @param vars_extra 
	 */
	@Override
	public Node evaluate(HashMap<String, Node> vars_extra) {
		if (vars_extra != null && vars_extra.containsKey(data)) {
			return vars_extra.get(data);
		}
		return evaluate();
	}
	
	/**
	 * Genera una copia del nodo
	 */
	public Node copy() {
		return new Atom(data, tipo);
	}
	/**
	 * Sustituye variables en el nodo;
	 */
	public void susVars(HashMap<String, Node> vars_extra) {
		data = evaluate(vars_extra).getData();
	}

}
