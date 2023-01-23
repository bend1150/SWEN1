package httpserver.service;

import Card.Card;
import httpserver.http.Method;
import httpserver.server.Request;
import httpserver.server.Response;
import httpserver.server.Service;
import httpserver.http.ContentType;
import httpserver.http.HttpStatus;
import DB.*;
import User.*;

import java.sql.SQLException;
import java.util.List;

public class ShowCardsService implements Service {
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET) {
            String response="";
            String token = request.getHeaderMap().getHeader("Authorization");

            UserRepository rep = new UserRepository();
            CardRepository stck = new CardRepository();
            User player = rep.getUserInfoByToken(token);

            try {
                List<Card> myCard= stck.showStack(player);
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
