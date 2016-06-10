/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ssolanki
 */
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.services.drive.model.File;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.net.URI;
import java.net.URISyntaxException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class FileUploadTestNGTest {
    
    //Steps to get the data CLIENT_ID and REDIRECT_URI
    
    private static final String CLIENT_ID = "408798892807-ti8kejlgp881d7pgdti8skova6i0qf2d.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "p4PHHkhfGIw7UYoO1gB901um";
    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
    private static GoogleDriveBasic gd;
    
    //Enter the file path you would like to upload on google drive
    //private static final String file_to_upload="C:\\Users\\Tushar\\Dropbox\\misc\\office\\googledriveupload\\misc\\document.txt";
    
    private static final String file_to_upload="src\\test\\resources\\document.txt";
    
    private static final String image_to_upload="src\\test\\resources\\Rainbow.jpg";
    
    @BeforeClass
    public void setUp() {
        // code that will be invoked before this test starts
    }
    
    @Test
    public static void main(String[] args) throws IOException, URISyntaxException{
		// To allow our application to connect with our google drive
		// we must first authorize opening a url in the browser, giving
		// to allow and copy and paste the authorization code in the application
		gd=new GoogleDriveBasic(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
		
		System.out.println("Opening the following URL in your browser");
		System.out.println(" " + gd.getURL());
		Desktop d=Desktop.getDesktop();
                d.browse(new URI(gd.getURL()));
		
                System.out.println("Enter the authorization code");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                
                String code = br.readLine();
                 try {
		gd.setCode(code);
		
		// Upload a text file to google drive
		// The document.txt is located in the same folder of the project
		// In the first arg the path to the file is places
		// In the second the file name in google drive
		String fileID=gd.uploadTextFile(file_to_upload,"document.txt");
                
		System.out.println("I uploaded file. FILE ID : "+fileID);
		
		// Download the contents of the text file previously uploaded
		String content=gd.downloadTextFile(fileID);
		System.out.println("Content:"+content);
		
                String imagefileID=gd.uploadImageFile(image_to_upload,"image.jpg");
                
                System.out.println("\n\n\nIMAGE Uploaded Successfully. Image File ID :"+imagefileID);
		
		System.out.println("Press enter to list file in Google drive");
                    InputStreamReader inputStreamReader = new InputStreamReader(System.in);
		
		System.out.println("Conducting consultation...");
		// Query files currently in our google drive
		List<File> result = gd.retrieveAllFiles();
		Iterator<File> iter = result.iterator();
		while (iter.hasNext()){
			System.out.println(iter.next().getTitle());
		}
		System.out.println("Files found"+result.size());
                }catch(TokenResponseException e){
                    
                    org.testng.Assert.fail("Testcase failed");
                    System.out.println(e.getMessage());
                }

    }
    
}
