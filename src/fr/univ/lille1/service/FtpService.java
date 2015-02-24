package fr.univ.lille1.service;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.net.SocketException;

@Path("/ftp")
public class FtpService {

	private FTPClient ftpClient = new FTPClient();

    public FtpService() {
        ftpClient = new FTPClient();
		try {
			ftpClient.connect("127.0.0.1", 1024);
			ftpClient.login("johndoe", "foo");
			ftpClient.setKeepAlive(true);

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@GET
	@Produces("text/html")
	public String list() {
		String texte = "";
		try {

			for(FTPFile file : ftpClient.listFiles()){
				texte += file.getName() + "<br>";
			}
			
		} catch (IOException e) {
			texte = e.getMessage();
		}
		return texte;
	}

	// @GET
	// @Path("/book/{isbn}")
	// public String getBook( @PathParam("isbn") String isbn ) {
	// return "Book: "+isbn;
	// }
	//
	// @GET
	// @Path("{var: .*}/stuff")
	// public String getStuff( @PathParam("var") String stuff ) {
	// return "Stuff: "+stuff;
	// }
}
