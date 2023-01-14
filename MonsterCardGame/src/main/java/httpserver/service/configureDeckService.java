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

public class configureDeckService implements Service {
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.PUT) {
            String response;
            String token = request.getHeaderMap().getHeader("Authorization");
            JSONArray jsonArray = new JSONArray(request.getBody());
            if(jsonArray.length() != 4) {
                return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Amount of cards is invalid");
            }

            User player = DB.UserQuery.getUserInfoByToken(token);
            String card1 = jsonArray.getString(0);
            String card2 = jsonArray.getString(1);
            String card3 = jsonArray.getString(2);
            String card4 = jsonArray.getString(3);


            try {
                int result = CardQuery.configureDeck(player, card1,card2,card3,card4);
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
        } else {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.PLAIN_TEXT, "Invalid request method");
        }
        
    }
}
