package fr.univ.lille1.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class exceptionTest extends WebApplicationException {


	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = 7900472270198173520L;

	public exceptionTest( final String message ) {
		super(
			Response
				.status( Status.CONFLICT )
				.entity( "MESSAGE EXCEPTION HERE: " + message )
				.build()
		);
	
	}
}
