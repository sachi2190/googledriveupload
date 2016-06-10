/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ssolanki
 */
import com.google.api.services.drive.model.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
//https://gist.github.com/jesusnoseq/4362854

public class GoogleDriveTest {
  // Pasos para conseguir los datos CLIENT_ID y REDIRECT_URI en
	// https://developers.google.com/drive/quickstart-java#step_1_enable_the_drive_api
	private static final String CLIENT_ID = "408798892807-ti8kejlgp881d7pgdti8skova6i0qf2d.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "p4PHHkhfGIw7UYoO1gB901um";
	private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
	private static GoogleDriveBasic gd;
	
	public static void main(String[] args) throws IOException{
		// Para permitir a nuestra aplicacion conectarse con nuestro google drive
		// primero tenemos que autorizarla abriendo una url en el navegador, darle
		// a permitir y copiar y pegar el codigo de autorizacion en la aplicacion
		gd=new GoogleDriveBasic(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
		
		System.out.println("Open the following URL in your browser");
		System.out.println(" " + gd.getURL());
		System.out.println("Enter the authorization code");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine();
		gd.setCode(code);
		
		// Upload a text file to google drive
		// The document.txt is located in the same folder of the project
		// In the first arg the path to the file is places
		// In the second the file name in google drive
		String fileID=gd.uploadTextFile("C:\\Users\\Tushar\\Dropbox\\misc\\office\\googledriveupload\\misc\\document.txt","document.txt");
		System.out.println("I uploaded file. FILE ID : "+fileID);
		
		// Descarga el contenido del fichero de texto anteriormente subido
		String contenido=gd.downloadTextFile(fileID);
		System.out.println("Content:"+contenido);
		
		System.out.println("Press enter to list file in Google drive");
		new InputStreamReader(System.in);
		
		System.out.println("Conducting consultation...");
		// Consulta los ficheros que hay actualmente en nuestro google drive
		List<File> result = gd.retrieveAllFiles();
		Iterator<File> iter = result.iterator();
		while (iter.hasNext()){
			System.out.println(iter.next().getTitle());
		}
		System.out.println("Files found"+result.size());
        }
}