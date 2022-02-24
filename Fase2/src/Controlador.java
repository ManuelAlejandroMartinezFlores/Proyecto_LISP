
public class Controlador {

	public static void main(String[] args) {
		
		String exp = "";
		Vista vista = new Vista();
		
		vista.mostrarEncabezado();
		
		while (true) {
			vista.mostrarMensaje("\n*");
			exp = vista.leerExpresion();
			if (exp.equals("Q")) {
				break;
			}
			try {
				vista.mostrarMensaje(Procesador.evaluate(exp));
			} catch (Exception e) {
				vista.mostrarMensaje("Error");
			}
		} 
		
		vista.mostrarMensaje("\nCerrando...");
	
	}
	
}
