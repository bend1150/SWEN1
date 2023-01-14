package httpserver.service;



import httpserver.http.Method;
import httpserver.server.Request;
import httpserver.server.Response;
import httpserver.server.Service;
import httpserver.http.ContentType;
import httpserver.http.HttpStatus;
import DB.UserQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;


public class UserService implements Service {
    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.POST) {
            String username = "";
            String password = "";
            String response = "";
            try {
                JSONObject body = new JSONObject(request.getBody());
                username = body.get("Username").toString();
                password = body.get("Password").toString();

              int result = DB.UserQuery.registerUser(username, password);

                if (result == 0) {
                    response = "User created successfully";
                } else if (result == 1) {
                    response = "Failed: User already exists";
                } else {
                    response = "Error when creating user";
                }
            } catch (JSONException e) {
                response = "Error parsing JSON in request body";
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return new Response(HttpStatus.CREATED, ContentType.PLAIN_TEXT, response);
        } else {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.PLAIN_TEXT, "Invalid request method");
        }
    }


}
