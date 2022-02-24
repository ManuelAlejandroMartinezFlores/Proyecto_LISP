
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class Archivos {

	public static void leer(String data) throws IOException {		
		BufferedReader reader = new BufferedReader(new FileReader(data));
        String row;
        while ((row = reader.readLine()) != null){
        	Procesador.evaluate(row);
        }
        reader.close();
	}
}
