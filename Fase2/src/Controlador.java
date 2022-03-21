/**
 * 
 * @author Manuel Alejandro Martínez Flores
 * @author Mario Puente
 * @author Pedro Marroquín
 * 
 * Controlador.
 * Clase que contiene el método main.
 * Debe ser ejecutada para correr el intérprete
 *
 */
public class Controlador {

	public static void main(String[] args) {
		
		String exp = "";
		Vista vista = new Vista();
		
		vista.mostrarEncabezado();
		
		while (true) {
			vista.mostrarMensaje("\n*");
			exp = vista.leerExpresion();
			// 'Q' cierrra el programa
			if (exp.equals("Q")) {
				break;
			}
			
			vista.mostrarMensaje(Procesador.evaluate(exp));
		} 
		
		vista.mostrarMensaje("\nCerrando...");
	
	}
	
}
