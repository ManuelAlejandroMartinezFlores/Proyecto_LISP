/**
 * 
 * @author Manuel Alejandro Martínez Flores
 * @author Mario Puente
 * @author Pedro Marroquín
 * 
 * Vista.
 * Clase que interactua con el usuario
 * Utiliza la terminal para leer y mostrar texto.
 *
 */

import java.util.Scanner;

public class Vista {

	private Scanner scan;
	
	/**
	 * Inicializa scanner
	 */
	Vista(){
		scan = new Scanner(System.in);
	}
	
	/**
	 * Lee expresion de terminal
	 * @return expresion
	 */
	public String leerExpresion() {
		return scan.nextLine();
	}
	
	/**
	 * Muestra un mensaje en terminal
	 * @param msg a mostrar
	 */
	public void mostrarMensaje(String msg) {
		System.out.println(msg);
	}
	
	/**
	 * Muestra el encabezado del programa
	 */
	public void mostrarEncabezado() {
		String txt = "Intérprete LISP. Grupo 4. Algoritmos y estructuras de datos\n";
		txt += "Más información:\nhttps://github.com/ManuelAlejandroMartinezFlores/Proyecto_LISP\n";
		txt += "Ingrese \'Q\' para salir";
		mostrarMensaje(txt);
	}
}
