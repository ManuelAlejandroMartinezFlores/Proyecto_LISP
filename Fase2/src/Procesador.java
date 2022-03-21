/**
 * 
 * @author Manuel Alejandro Martínez Flores
 * @author Mario Puente
 * @author Pedro Marroquín
 * 
 * Procesador.
 * Procesa una expresión de texto y la convierte en una estructura de árbol de Nodos
 *
 */

import java.util.ArrayList;

public class Procesador {
	
	/**
	 * Evalua la expresión de texto en formato LISP
	 * @param expresion
	 * @return resultado
	 */
	public static String evaluate(String expresion) {
		try {
			return procesarEX(expresion).evaluate().getData();
		} catch (Exception e) {
			return "ERROR";
		}
	}
	
	
	/**
	 * Revisa que la cantidad de párentesis sea correcta
	 * @param expresion
	 * @return true - correcto / false - incorrecto
	 */
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
	

	/**
	 * Convierte la expresión de texto en un árbol de Nodos
	 * @param expresion
	 * @return árbol de Nodos
	 * @throws Exception 
	 */
	public static Node procesarEX(String expresion) throws Exception{
		// Se supone que las expresiones cumplen con lo siguiente:
		// tienen esta forma: (defun factorial (n) (cond ((= n 0) 1) (T (* n (factorial (- n 1))))))
		// Luego de parentesis izq no hay espacio.
		// Antes de parentesis izq siempre hay espacio.
		// El algoritmo se basa en la existencia de espacios en la expresión.
		
		/**
		 * Ejemplo:(defun factorial (n) (cond ((= n 0) 1) (T (* n (factorial (- n 1))))))
		 * Primero se agrega al arreglo: [defun factorial]
		 * Luego se evalua y añade el subarbol [n] -> [defun factorial [n]]
		 * Luego se identifica la expresión (cond ((= n 0) 1) (T (* n (factorial (- n 1)))))
		 * Y se añade al subarbol [cond], para evaluar las subexpresiones ((= n 0) 1) y (T (* n (factorial (- n 1))))
		 * Para la primera se genera el subarbol [[= n 0] 1], que se añade al anterior -> [cond [[= n 0] 1]]
		 * En la segunda expresión se genera el subarbol [T [* n [factorial [- n 1]]]], que se añade al anterior ->
		 * [cond [[= n 0] 1] [T [* n [factorial [- n 1]]]]]. Ahora ese subarbol se añade al principal:
		 * [defun factorial [n] [cond [[= n 0] 1] [T [* n [factorial [- n 1]]]]]]
		 */
		
		if (!Procesador.revisarParen(expresion)) {
			throw new Exception("Parentesis");
		}
		// Maneja casos especiales como 'quote'
		expresion = casosEsp(expresion);
		expresion = quitarEspacios(expresion);
		
		ArrayList<Node> data = new ArrayList<Node>();
		
		int inicio = 0; // Inicio de expresión interna
		int cnt = 0; // Contador de tokens
		int open = 0; // Cantidad de parentesis abiertos
		
		// Elimina parentesis
		expresion = expresion.substring(1, expresion.length() - 1);
		expresion = quitarEspacios(expresion);
		// Separa en tokens por espacio
		String[] exp = expresion.split(" ");
		
		// Itera tokens
		for (String s : exp) {
			// Maneja casos especiales
			s = casosEsp(s);
			
			int temp_open = 0; // Contador de parentesis abiertos en el token
			
			// Se separa en array de char
			char[] temp = s.toCharArray();
			for (char c : temp) {
				if (Character.toString(c).equals("(")) {
					// Parentesis abiertos
					open++;
					temp_open++;
				}
				if (Character.toString(c).equals(")")) {
					// Parentesis cerrados
					open--;
				}
			}
			
			// Si el token inicia y termina con parentesis y todos los parentesis se encuentran cerrados, es una expresión
			// Ejemplo: (defun factorial (n) (cond ((= n 0) 1) (T (* n (factorial (- n 1))))))
			// El token (n) es una expresión.
			if (Character.toString(s.charAt(s.length()-1)).equals(")") && Character.toString(s.charAt(0)).equals("(") && open==0){
				// Se añade al arbol un subarbol con la expresión identificada
				data.add(procesarEX(s));
				
			// Si el token inicia con parentesis y la cantidad de parentesis abiertos totales es igual a la del token se indica
			// el token como inicio de expresión
			// Ejemplo: (defun factorial (n) (cond ((= n 0) 1) (T (* n (factorial (- n 1))))))
			// el token '(cond' indica el inicio de expresión
			} else if (Character.toString(s.charAt(0)).equals("(") && open==temp_open) {
				inicio = cnt;
				
			// Si el token termina con parentesis y no hay parentesis abiertos, se indica el fin de la expresión y 
			// se recolectan los tokens desde el inicio marcado
			// Ejemplo: (defun factorial (n) (cond ((= n 0) 1) (T (* n (factorial (- n 1))))))
			// el token '1)))))' se marca como el fin de la expresión y la expresión completa es:
			// (cond ((= n 0) 1) (T (* n (factorial (- n 1)))))
			} else if (Character.toString(s.charAt(s.length()-1)).equals(")") && open == 0) {
				String txt = "";
				for (int i=inicio; i<cnt; i++) {
					txt += exp[i] + " ";
				}
				txt += s;
				// Sea añade al arbol un subarbol con la expresion identificada
				data.add(procesarEX(txt));
				
			// Si no hay parentesis abiertos, el token es un atom y se agrega al arbol actual
			}  else if (open == 0 && !s.equals("")){
				// Se añade el Atom al arbol, el Atom no tiene ramas.
				data.add(new Atom(s));
			}
			
			cnt++;
		}
		return new SExpression(data);
	}
	
	/**
	 * Sustituye casos especiales en el texto
	 * @param exp
	 * @return texto modificado
	 */
	public static String casosEsp(String exp) {
		if (exp.equals("=")) {
			exp = "equal";
		} else if (Character.toString(exp.charAt(0)).equals("\'") && Character.toString(exp.charAt(1)).equals("(")) {
			exp = "(quote " + exp.substring(2);
		}
		return exp;
		
	}
	
	/**
	 * Elimina espacios innecesarios
	 * @param s expresion
	 * @return expresion sin espacias
	 */
	private static String quitarEspacios(String s) {
		while (Character.toString(s.charAt(0)).equals(" ")) {
			s = s.substring(1);
		}
		while (Character.toString(s.charAt(s.length()-1)).equals(" ")) {
			s = s.substring(0, s.length()-1);
		}
		
		int id = 0;
		while (id < s.length() - 1) {
			if (Character.toString(s.charAt(id)).equals(" ") && Character.toString(s.charAt(id + 1)).equals(" ")) {
				s = s.substring(0, id) + s.substring(id+1);
			} else {
				id++;
			}
		}
		return s;
	}
}
