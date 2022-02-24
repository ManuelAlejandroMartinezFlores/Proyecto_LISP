/**
 * 
 * @author Manuel Alejandro Martínez Flores
 * @author Mario Puente
 * @author Pedro Marroquín
 * 
 * Archivos.
 * Lee los archivos .txt que representan archivos .lisp
 * Utilizado por (load filename.txt)
 *
 */

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class Archivos {
	
	/**
	 * Lee y evalua expresiones en un archivo
	 * @param data nombre de archivo
	 * @throws IOException
	 */
	public static void leer(String data) throws IOException {		
		BufferedReader reader = new BufferedReader(new FileReader(data));
        String row;
        while ((row = reader.readLine()) != null){
        	Procesador.evaluate(row);
        }
        reader.close();
	}
}
