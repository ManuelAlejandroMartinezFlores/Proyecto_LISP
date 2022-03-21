/**
 * 
 * @author Manuel Alejandro Martínez Flores
 * @author Mario Puente
 * @author Pedro Marroquín
 * 
 * SExpression.
 * Expresion de LISP, extiende Node
 * Contiene otros Nodos
 *
 */

import java.util.ArrayList;
import java.util.HashMap;

public class SExpression extends Node {

	/**
	 * Construye la expresión
	 * @param tokens_
	 */
	public SExpression(ArrayList<Node> tokens_) {
		tokens = new ArrayList<Node>(tokens_);
		data = "("; 
		for (Node n : tokens) {
			data += n.getData() + " ";
		}
		data = data.substring(0, data.length()-1);
		data += ")";
	}

	/**
	 * Indica si es lista
	 */
	@Override
	public boolean isList() {
		return true;
	}
	
	/**
	 * Evalua la expresión.
	 * Evalua el árbol de Nodos en un algoritmo Depth First
	 */
	@Override
	public Node evaluate() {
		// Suma
		if (tokens.get(0).getData().equals("+")) {
			int resultado = 0;
			for (int i=1; i<tokens.size(); i++) {
				resultado += Integer.valueOf(tokens.get(i).evaluate().getData());
			}
			return new Atom(resultado);
		// Multiplicacion
		} else if (tokens.get(0).getData().equals("*")) {

			int resultado = 1;
			for (int i=1; i<tokens.size(); i++) {
				resultado *= Integer.valueOf(tokens.get(i).evaluate().getData());
			}
			return new Atom(resultado);
		// Resta
		} else if (tokens.get(0).getData().equals("-")) {
			int resultado = Integer.valueOf(tokens.get(1).evaluate().getData());
			for (int i=2; i<tokens.size(); i++) {
				resultado -= Integer.valueOf(tokens.get(i).evaluate().getData());  
			}
			return new Atom(resultado);
		// División
		} else if (tokens.get(0).getData().equals("/")) {
			int resultado = Integer.valueOf(tokens.get(1).evaluate().getData());
			for (int i=2; i<tokens.size(); i++) {
				resultado /= Integer.valueOf(tokens.get(i).evaluate().getData());
			}
			return new Atom(resultado);
		// Defun - define una función
		} else if (tokens.get(0).getData().equals("defun")){
			tokens.remove(0);
			Ambiente.setFunc(tokens);
			return new Atom(true);
		// Quote - detiene la evaluación de la expresion
		} else if (tokens.get(0).getData().equals("quote")) {
			tokens.remove(0);
			return new SExpression(tokens);
		// Setq - define variables
		} else if (tokens.get(0).getData().equals("setq")) {
			tokens.remove(0);
			for (int i=0; i<tokens.size()/2; i++) {
				Ambiente.setVar(tokens.get(2*i).getData(), tokens.get(2*i +1).evaluate());
			}
			return new Atom(true);
		// Equal - evalua la igualdad del primer y segundo token
		} else if (tokens.get(0).getData().equals("equal")) {
			if (tokens.get(1).evaluate().getData().equals(tokens.get(2).evaluate().getData())) {
				return new Atom(true);
			} else {
				return new Atom(false);
			}
		// > - evalua si el primer token es mayor al segundo
		} else if (tokens.get(0).getData().equals(">")) {
			try {
				if (Integer.valueOf(tokens.get(1).evaluate().getData()) > Integer.valueOf(tokens.get(2).evaluate().getData())) {
					return new Atom(true);
				} else {
					return new Atom(false);
				}
			} catch (Exception e) {
				if (tokens.get(1).evaluate().getData().compareTo(tokens.get(2).evaluate().getData()) > 0) {
					return new Atom(true);
				} else {
					return new Atom(false);
				}
			}
		// < - evalua si el primer token es menor al segundo
		} else if (tokens.get(0).getData().equals("<")) {
			try {
				
				if (Integer.valueOf(tokens.get(1).evaluate().getData()) < Integer.valueOf(tokens.get(2).evaluate().getData())) {
					return new Atom(true);
				} else {
					return new Atom(false);
				}
			} catch (Exception e) {
				if (tokens.get(1).evaluate().getData().compareTo(tokens.get(2).evaluate().getData()) < 0) {
					return new Atom(true);
				} else {
					return new Atom(false);
				}
			}
		// atom - indica si el primer token es un atom	
		} else if (tokens.get(0).getData().equals("atom")) {
			if (tokens.size() == 2 && !tokens.get(1).evaluate().isList()) {
				return new Atom(true);
			} else {
				return new Atom(false);
			}
		// list - genera una lista con el predicado
		} else if (tokens.get(0).getData().equals("list")) {
			tokens.remove(0);
			ArrayList<Node> temp = new ArrayList<Node>();
			for (Node n : tokens) {
				temp.add(n.evaluate());
			}
			return new SExpression(temp);
		// cond - evalua los siguientes tokens (condicional predicado) y regresa el predicado si el condicional
		} else if (tokens.get(0).getData().equals("cond")) {
			tokens.remove(0);
			for (Node n : tokens) {
				if (n.getTokens().get(0).evaluate().getData().equals("T")) {
					return n.getTokens().get(1).evaluate();
				}
			}
			return new Atom(false);
		// load - carga un archivo .txt
		} else if (tokens.get(0).getData().equals("load")) {
			try {
				Archivos.leer(tokens.get(1).getData());
				return new Atom(true);
			} catch (Exception e) {
				return new Atom(false);
			}
		// evalua la funcion float
		} else if (tokens.get(0).getData().equals("float")) {
			return tokens.get(1).evaluateFloat();
		// revisa si es una función y la evalua
		} else if (Ambiente.isFunc(tokens.get(0).getData())){
			Function func = Ambiente.getFunc(tokens.get(0).getData());
			tokens.remove(0);
			return func.evaluar(tokens, null);
		// evalua como lista
		} else {
			tokens.add(0, new Atom("list"));
			return evaluateFloat();
		}
	}

	@Override
	public Node evaluate(HashMap<String, Node> vars_extra) {
		if (vars_extra == null) {
			return evaluate();
		}
		
		// Sustituye variables en la expresión
		for (int i=0; i<tokens.size(); i++) {
			Node n =tokens.get(i);
			n.susVars(vars_extra);
			if (vars_extra.containsKey(n.getData())) {
				n = vars_extra.get(tokens.get(i).getData());
				tokens.set(i, n);
			}
		}
		
		return evaluate();
	}
	
	
	/**
	 * Indica la expresión
	 */
	public String getSE() {
		String txt = "("; 
		for (Node n : tokens) {
			txt += n.getData() + " ";
		}
		txt = txt.substring(0, txt.length()-1);
		txt += ")";
		return txt;
	}
	
	/**
	 * Genera una copia de la expresión
	 */
	public Node copy() {
		ArrayList<Node> temp = new ArrayList<Node>();
		for (Node n : tokens) {
			temp.add(n.copy());
		}
		return new SExpression(temp);
	}
	
	/**
	 * Sustituye variables en la expresión
	 */
	public void susVars(HashMap<String, Node> vars_extra) {
		if (!tokens.get(0).getData().equals("setq")) {
			ArrayList<Node> temp = new ArrayList<Node>();
			for (Node n : tokens) {
				n.susVars(vars_extra);
				temp.add(n);
			}
			tokens = temp;
		}
	}

	
	public Node evaluateFloat() {
		if (tokens.get(0).getData().equals("+")) {
			float resultado = 0;
			for (int i=1; i<tokens.size(); i++) {
				resultado += Float.valueOf(tokens.get(i).evaluate().getData());
			}
			return new Atom(resultado);
		// Multiplicacion
		} else if (tokens.get(0).getData().equals("*")) {

			float resultado = 1;
			for (int i=1; i<tokens.size(); i++) {
				resultado *= Float.valueOf(tokens.get(i).evaluate().getData());
			}
			return new Atom(resultado);
		// Resta
		} else if (tokens.get(0).getData().equals("-")) {
			float resultado = Float.valueOf(tokens.get(1).evaluate().getData());
			for (int i=2; i<tokens.size(); i++) {
				resultado -= Float.valueOf(tokens.get(i).evaluate().getData());  
			}
			return new Atom(resultado);
		// División
		} else if (tokens.get(0).getData().equals("/")) {
			float resultado = Float.valueOf(tokens.get(1).evaluate().getData());
			for (int i=2; i<tokens.size(); i++) {
				resultado /= Float.valueOf(tokens.get(i).evaluate().getData());
			}
			return new Atom(resultado);
		// Defun - define una función
		} else if (tokens.get(0).getData().equals("defun")){
			tokens.remove(0);
			Ambiente.setFunc(tokens);
			return new Atom(true);
		// Quote - detiene la evaluación de la expresion
		} else if (tokens.get(0).getData().equals("quote")) {
			tokens.remove(0);
			return new SExpression(tokens);
		// Setq - define variables
		} else if (tokens.get(0).getData().equals("setq")) {
			tokens.remove(0);
			for (int i=0; i<tokens.size()/2; i++) {
				Ambiente.setVar(tokens.get(2*i).getData(), tokens.get(2*i +1).evaluate());
			}
			return new Atom(true);
		// Equal - evalua la igualdad del primer y segundo token
		} else if (tokens.get(0).getData().equals("equal")) {
			if (tokens.get(1).evaluate().getData().equals(tokens.get(2).evaluate().getData())) {
				return new Atom(true);
			} else {
				return new Atom(false);
			}
		// > - evalua si el primer token es mayor al segundo
		} else if (tokens.get(0).getData().equals(">")) {
			try {
				if (Float.valueOf(tokens.get(1).evaluate().getData()) > Float.valueOf(tokens.get(2).evaluate().getData())) {
					return new Atom(true);
				} else {
					return new Atom(false);
				}
			} catch (Exception e) {
				if (tokens.get(1).evaluate().getData().compareTo(tokens.get(2).evaluate().getData()) > 0) {
					return new Atom(true);
				} else {
					return new Atom(false);
				}
			}
		// < - evalua si el primer token es menor al segundo
		} else if (tokens.get(0).getData().equals("<")) {
			try {
				
				if (Float.valueOf(tokens.get(1).evaluate().getData()) < Float.valueOf(tokens.get(2).evaluate().getData())) {
					return new Atom(true);
				} else {
					return new Atom(false);
				}
			} catch (Exception e) {
				if (tokens.get(1).evaluate().getData().compareTo(tokens.get(2).evaluate().getData()) < 0) {
					return new Atom(true);
				} else {
					return new Atom(false);
				}
			}
		// atom - indica si el primer token es un atom	
		} else if (tokens.get(0).getData().equals("atom")) {
			if (tokens.size() == 2 && !tokens.get(1).evaluate().isList()) {
				return new Atom(true);
			} else {
				return new Atom(false);
			}
		// list - genera una lista con el predicado
		} else if (tokens.get(0).getData().equals("list")) {
			tokens.remove(0);
			ArrayList<Node> temp = new ArrayList<Node>();
			for (Node n : tokens) {
				temp.add(n.evaluate());
			}
			return new SExpression(temp);
		// cond - evalua los siguientes tokens (condicional predicado) y regresa el predicado si el condicional
		} else if (tokens.get(0).getData().equals("cond")) {
			tokens.remove(0);
			for (Node n : tokens) {
				if (n.getTokens().get(0).evaluate().getData().equals("T")) {
					return n.getTokens().get(1).evaluate();
				}
			}
			return new Atom(false);
		// load - carga un archivo .txt
		} else if (tokens.get(0).getData().equals("load")) {
			try {
				Archivos.leer(tokens.get(1).getData());
				return new Atom(true);
			} catch (Exception e) {
				return new Atom(false);
			}
		// evalua la funcion float
		} else if (tokens.get(0).getData().equals("float")) {
			return tokens.get(1).evaluateFloat();
		// revisa si es una función y la evalua
		} else if (Ambiente.isFunc(tokens.get(0).getData())){
			Function func = Ambiente.getFunc(tokens.get(0).getData());
			tokens.remove(0);
			return func.evaluar(tokens, null);
		// evalua como lista
		} else {
			tokens.add(0, new Atom("list"));
			return evaluateFloat();
		}
	}
}
