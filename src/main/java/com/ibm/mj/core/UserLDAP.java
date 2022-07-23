package com.ibm.mj.core;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.ibm.mj.core.ceObject.UserTool;

@Path("/ldap")
public class UserLDAP {

	/**
	 * Get a student record by its unique id
	 *
	 * @param id
	 *            The id of the student record to retrieve
	 * @return
	 */
	@SuppressWarnings("unused")
	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMemberShip(@Context UriInfo uriInfo, @PathParam("name") String name) {
		final String methodName = "getMemberShip";
		
		try {
			UserTool user = new UserTool(); 
			if (user != null) {
				try {
					return Response.ok(user.getMemberShip(name)).build();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					return Response.status(Status.PRECONDITION_FAILED).build();
				}
			} else {
				return Response.status(Status.NOT_FOUND).build();
			}
		} finally {
			
		}
}
	
	
}
