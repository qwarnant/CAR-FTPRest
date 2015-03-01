package fr.univ.lille1.service;

import fr.univ.lille1.http.HttpResponse;
import org.apache.commons.net.ftp.FTPClient;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;


@Path("/ftp")
public class FtpService {

    private static final String FTP_SERVER_IP = "127.0.0.1";
    private static final int FTP_SERVER_PORT = 1024;

    private static final String FTP_USER_ANONYMOUS = "anonymous";
    private static final String FTP_PASS_ANONYMOUS = "anonymous";

    private FTPClient client;

    public FtpService() {
        this.client = new FTPClient();
        try {
            this.client.connect(FTP_SERVER_IP, FTP_SERVER_PORT);

            // Anonymous login by default
            this.client.login(FTP_USER_ANONYMOUS, FTP_PASS_ANONYMOUS);
            this.client.setKeepAlive(true);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @POST
    @Path("/connect")
    @Produces("text/html")
    public Response connect(@FormParam("username") String username, @FormParam("password") String password) {
        System.out.println("start connect");
        try {
            // Try to connect the user
            boolean result = this.client.login(username, password);
            System.out.println("Result login : " + result);

            // If the connection failed, reconnect the user as anonymous
            if (!result) {
                return Response.status(HttpResponse.CODE_FORBIDDEN).entity(HttpResponse.MSG_FORBIDDEN).build();
            }

            return Response.status(HttpResponse.CODE_STATUS_OK).entity(HttpResponse.MSG_STATUS_OK).build();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("end connect");


        return null;
    }


    @POST
    @Path("/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/html")
    public Response postFile(@Context HttpServletRequest request) {

        try {
            System.out.println("" + request.getParts());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return Response.status(HttpResponse.CODE_STATUS_OK).entity("postFile").build();

    }


    @GET
    @Path("/file/{filename}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile(@PathParam("filename") String filename) {
        File file = new File(System.getProperty("java.io.tmpdir") + filename);

        try {
            OutputStream os = new FileOutputStream(file);

            System.out.println(file.getName());

            boolean status = this.client.retrieveFile(filename, os);
            os.flush();

            if (!status) {
                return Response.status(404).entity("File not found.").build();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Response response = Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"") //optional
                .build();

        System.out.println(file.length());
        
        // Delete the temp file
        file.delete();
        
        return response;
    }

    @DELETE
    @Path("/file/{filename}")
    @Produces("text/html")
    public Response deleteFile(@PathParam("filename") String filename) {

        try {

            boolean status = this.client.deleteFile(filename);

            if (!status) {
                return Response.status(404).entity("File not found.").build();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return Response.ok().build();
    }

    @DELETE
    @Path("/folder/{foldername}")
    @Produces("text/html")
    public Response removeFolder(@PathParam("foldername") String foldername) {

        try {

            boolean status = this.client.removeDirectory(foldername);

            if (!status) {
                return Response.status(404).entity("Folder not found.").build();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return Response.ok().build();
    }

    @GET
    @Produces("text/html")
    public Response listFolder() {
        System.out.println("Start list");
        String texte = "";

        //TODO

        System.out.println("End list : " + texte);

        return Response.status(HttpResponse.CODE_STATUS_OK).entity(texte).build();
    }
}
