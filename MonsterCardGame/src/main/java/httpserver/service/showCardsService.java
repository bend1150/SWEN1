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

public class showCardsService implements Service {
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET) {
            String response="";
            String token = request.getHeaderMap().getHeader("Authorization");
            User player = UserQuery.getUserInfoByToken(token);

            try {
                List<Card> myCard= CardQuery.showStack(player);
                for (Card card: myCard) {
                    response += "Name: " + card.getName() + "("+card.getDamage()+")"+"\n";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.PLAIN_TEXT, "Error while show card");
            }
            return new Response(HttpStatus.OK, ContentType.PLAIN_TEXT, response);
        }

        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.PLAIN_TEXT, "Error while show card");

    }

}
