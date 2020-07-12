package com.quicktutorialz.nio.endpoints.v1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.quicktutorialz.nio.entities.Users;
import com.mawashi.nio.annotations.Api;
import com.mawashi.nio.utils.Action;
import com.mawashi.nio.utils.Endpoints;
import com.quicktutorialz.nio.daos.v1.ToDoDao;
import com.quicktutorialz.nio.daos.v1.ToDoDaoImpl;
import com.quicktutorialz.nio.entities.ResponseDto;
import com.quicktutorialz.nio.entities.ToDo;
import com.quicktutorialz.nio.entities.ToDoDto;


public class ToDoEndpoints extends Endpoints{
	ToDoDao toDoDao = ToDoDaoImpl.getInstance();
	
	@Api(path = "/api/v1/create", method = "POST", consumes = "application/json", produces = "application/json")
    Action createToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        ToDoDto input = (ToDoDto) getDataFromJsonBodyRequest(request, ToDoDto.class);
        ToDo output = toDoDao.create(input);
        toJsonResponse(request, response, new ResponseDto(200, output));
    };
	
    @Api(path = "/api/v1/read/{id}", method = "GET", produces = "application/json")
    Action readToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        String id = getPathVariables(request).get("id");
        Optional<ToDo> output = toDoDao.read(id);
        toJsonResponse(request, response, new ResponseDto(200, output.isPresent() ? output.get() : "todo not found"));
    };

    @Api(path = "/api/v1/read", method = "GET", produces = "application/json")
    Action readAllToDos = (HttpServletRequest request, HttpServletResponse response) -> {
    	Connection conexion=null; 
    	java.util.LinkedList<Users> usuarios=new java.util.LinkedList<Users>();
    	try {
	          Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	          String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
	            "databaseName=editorialweb;user=sa;password=123;";
	          conexion = DriverManager.getConnection(connectionUrl);
	        } catch (SQLException e) {
	            System.out.println("SQL Exception: "+ e.toString());
	        } catch (ClassNotFoundException cE) {
	            System.out.println("Class Not Found Exception: "+ cE.toString());
	        }
	        String sql = "select name, email, password, id from dbo.users";
	        try (PreparedStatement cmd = conexion.prepareStatement(sql)) {
	                ResultSet rs = cmd.executeQuery();
	                
	                while(rs.next())
	                {
	                	String name = rs.getString(1);
	                	String email = rs.getString(2);
	                	String password = rs.getString(3);
	                	Integer estado = 1; //rs.getInt(4);
	                	Integer id = rs.getInt(4);
	                    Users temp = new Users(name, email, password, estado, id);
	                    usuarios.add(temp);
	                }
	                conexion.close();
	                toJsonResponse(request, response, new ResponseDto(200, usuarios));
	                //return new ResponseEntity<LinkedList<Users>>(usuarios, HttpStatus.OK);
	            }
	        catch(Exception ex)
	        {
	        	toJsonResponse(request, response, new ResponseDto(200, ""));
	        	//return new ResponseEntity<LinkedList<Users>>(HttpStatus.NO_CONTENT);
	        }
    	
    	//List<ToDo> output = toDoDao.readAll();
        //toJsonResponse(request, response, new ResponseDto(200, output));

    };
	
    @Api(path = "/api/v1/update", method = "POST", consumes = "application/json", produces = "application/json")
    Action updateToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        ToDo input = (ToDo) getDataFromJsonBodyRequest(request, ToDo.class);
        Optional<ToDo> output = toDoDao.update(input);
        toJsonResponse(request, response, new ResponseDto(200, output.isPresent() ? output.get() : "todo not updated"));
    };

    @Api(path = "/api/v1/delete/{id}", method = "GET", produces = "application/json")
    Action deleteToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        String id = getPathVariables(request).get("id");
        toJsonResponse(request, response, new ResponseDto(200, toDoDao.delete(id) ? "todo deleted" : "todo not found"));
    };
	
	public ToDoEndpoints() {
		setEndpoint( "/api/v1/create", createToDo);
		setEndpoint( "/api/v1/read/{id}", readToDo);
		setEndpoint( "/api/v1/read", readAllToDos);
		setEndpoint( "/api/v1/update", updateToDo);
		setEndpoint( "/api/v1/delete/{id}", deleteToDo);
	}
}
