package httpserver.service;


import httpserver.http.Method;
import httpserver.server.Request;
import httpserver.server.Response;
import httpserver.server.Service;
import httpserver.http.ContentType;
import httpserver.http.HttpStatus;
import DB.*;
import User.*;

public class AcquirePackageService implements Service {
    public Response handleRequest(Request request) {

        if(!request.getPathParts().get(1).equals("packages")){       //getPathparts gets the path (transaction/packages)
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.PLAIN_TEXT, "Invalid request method");
        }

        if (request.getMethod() == Method.POST) {
            String token = request.getHeaderMap().getHeader("Authorization");
            String response;

            UserRepository user = new UserRepository();
            CardRepository pckg = new CardRepository();
            User player = user.getUserInfoByToken(token);



            int result = pckg.acquirePackage(player);
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
