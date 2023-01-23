package httpserver.service;



import User.User;
import httpserver.http.Method;
import httpserver.server.Request;
import httpserver.server.Response;
import httpserver.server.Service;
import httpserver.http.ContentType;
import httpserver.http.HttpStatus;
import DB.UserRepository;

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

                UserRepository user = new UserRepository();

                int result = user.registerUser(username, password);

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
        } else if(request.getMethod() == Method.GET){

            String parameterName = request.getPathParts().get(1);
            String token = request.getHeaderMap().getHeader("Authorization");
            UserRepository rep = new UserRepository();
            User player = rep.getUserInfoByToken(token);
            String response="";

            if(!parameterName.equals(player.getUsername())){
                return new Response(HttpStatus.UNAUTHORIZED, ContentType.PLAIN_TEXT, "Access refused!!!");
            }

            response += "Name: " + player.getName() + "\n"
            + "Bio: "+player.getBio() + "\n"
            + "Image: "+player.getImage() + "\n";


            return new Response(HttpStatus.OK, ContentType.PLAIN_TEXT, response);



        } else if(request.getMethod() == Method.PUT){
            String parameterName = request.getPathParts().get(1);
            String token = request.getHeaderMap().getHeader("Authorization");
            UserRepository rep = new UserRepository();
            User player = rep.getUserInfoByToken(token);

            String name = "";
            String bio = "";
            String image = "";

            JSONObject body = new JSONObject(request.getBody());
            name = body.get("Name").toString();
            bio = body.get("Bio").toString();
            image = body.get("Image").toString();



            if(!parameterName.equals(player.getUsername())){
                return new Response(HttpStatus.UNAUTHORIZED, ContentType.PLAIN_TEXT, "Access refused!!!");
            }

            try {
                rep.editUserData(player,name,bio,image);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            return new Response(HttpStatus.ACCEPTED, ContentType.PLAIN_TEXT, "You have successfully updated your profile!");
        }
        else {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.PLAIN_TEXT, "Invalid request method");
        }
    }
}
