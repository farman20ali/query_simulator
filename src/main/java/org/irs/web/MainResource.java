package org.irs.web;

import jakarta.inject.Inject;
import jakarta.ws.rs.*; 
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import org.irs.dto.RequestDto;
import org.irs.dto.ResponseDto;
import org.irs.dto.UserDto;
import org.irs.service.LovService;
import org.irs.service.UserService;

import io.quarkus.logging.Log;
@Path("/irs")
public class MainResource {

  @Inject
  UserService userService;

  
  @Inject
  LovService lovService;


  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/user")
  public ResponseDto userService(RequestDto requestDto) {
    try {
      Log.info("RPIN : "+ requestDto);
      return userService.fetchData(requestDto);

    }catch (Exception e){
      ResponseDto responseDto=new ResponseDto();
      responseDto.setFailure(e.getMessage());
      return  responseDto;
    }

  }


  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/createUser")
  public Response createUser(UserDto userDto) {
    try {
      userService.createUser(userDto);
      return Response.status(Response.Status.CREATED).entity("User registered successfully").build();
  } catch (IllegalArgumentException e) {
      return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
  } catch (Exception e) {
    return Response.status(Response.Status.BAD_REQUEST).entity("FAILED").build();
  }

  }


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/lov/{fileName}")
  public List<Object> lovService(@PathParam("fileName") String fileName) {
    try {
      Log.info("RPIN : "+ fileName);
      return lovService.fetchData(fileName);

    }catch (Exception e){
       List<Object> results = new ArrayList<>();
      return  results;
    }

  }

}
