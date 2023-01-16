package httpserver.service;


import Card.Card;
import httpserver.http.Method;
import httpserver.server.Request;
import httpserver.server.Response;
import httpserver.server.Service;
import httpserver.http.ContentType;
import httpserver.http.HttpStatus;
import DB.*;
import Card.*;
import User.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class acquirePackageService implements Service {
    public Response handleRequest(Request request) {

        if(!request.getPathParts().get(1).equals("packages")){       //getPathpars gets the path (transaction/packages)
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.PLAIN_TEXT, "Invalid request method");
        }

        if (request.getMethod() == Method.POST) {
            String token = request.getHeaderMap().getHeader("Authorization");
            String response;
            User player = UserQuery.getUserInfoByToken(token);

            int result = CardQuery.acquirePackage(player);
            if (result == 0){
                response = "You have successfully bought a package";
            } else if (result==1){
                response ="You dont have enough coins!";
            }  else if (result == 2) {
                response = "There are no cards left in the store!";
            } else {
                response = "There was a problem buying a package!";
            }
            return new Response(HttpStatus.OK, ContentType.PLAIN_TEXT, response);
        }
        return new Response(HttpStatus.UNAUTHORIZED, ContentType.PLAIN_TEXT, "Invalid request method");
    }

}
