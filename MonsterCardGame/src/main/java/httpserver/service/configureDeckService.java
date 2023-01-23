package httpserver.service;



import httpserver.http.Method;
import httpserver.server.Request;
import httpserver.server.Response;
import httpserver.server.Service;
import httpserver.http.ContentType;
import httpserver.http.HttpStatus;
import DB.*;
import User.*;

import org.json.JSONArray;

import java.sql.SQLException;

public class ConfigureDeckService implements Service {
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.PUT) {
            String response;
            String token = request.getHeaderMap().getHeader("Authorization");
            JSONArray jsonArray = new JSONArray(request.getBody());
            if(jsonArray.length() != 4) {
                return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Amount of cards is invalid");
            }

            UserRepository rep = new UserRepository();
            CardRepository dck = new CardRepository();
            User player = rep.getUserInfoByToken(token);
            String card1 = jsonArray.getString(0);
            String card2 = jsonArray.getString(1);
            String card3 = jsonArray.getString(2);
            String card4 = jsonArray.getString(3);
            try {
                int result = dck.configureDeck(player, card1,card2,card3,card4);
                if(result == 0){
                    response = "Deck successfully configured!";
                } else if (result == 1){
                    response = "Invalid Card selected!";
                } else {
                    response = "Error configuring deck!";
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return new Response(HttpStatus.OK, ContentType.PLAIN_TEXT, response);
        } else if(request.getMethod() == Method.GET) {
            String token = request.getHeaderMap().getHeader("Authorization");
            String response;
            UserRepository rep = new UserRepository();
            CardRepository dck = new CardRepository();


            User player = rep.getUserInfoByToken(token);
            response = dck.showDeck(player);


            return new Response(HttpStatus.OK, ContentType.PLAIN_TEXT, response);
        } else {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.PLAIN_TEXT, "There was a problem showing your deck!");
        }

    }
}
