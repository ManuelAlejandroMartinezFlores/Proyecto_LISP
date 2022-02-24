import java.util.Scanner;

public class Vista {

	Scanner scan;
	
	Vista(){
		scan = new Scanner(System.in);
	}
	
	public String leerExpresion() {
		return scan.nextLine();
	}
	
	public void mostrarMensaje(String msg) {
		System.out.println(msg);
	}
	
	public void mostrarEncabezado() {
		String txt = "Intérprete LISP. Grupo 4. Algoritmos y estructuras de datos\n";
		txt += "Más información:\nhttps://github.com/ManuelAlejandroMartinezFlores/Proyecto_LISP\n";
		txt += "Ingrese \'Q\' para salir";
		mostrarMensaje(txt);
	}
}
