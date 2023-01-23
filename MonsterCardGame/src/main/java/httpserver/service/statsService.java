package httpserver.service;

import DB.UserRepository;
import httpserver.http.ContentType;
import httpserver.http.HttpStatus;
import httpserver.http.Method;
import httpserver.server.Request;
import httpserver.server.Response;
import httpserver.server.Service;

import java.sql.SQLException;

public class StatsService implements Service {
    public Response handleRequest(Request request) {

        if (request.getMethod() == Method.GET) {
            String top=String.format("| %-5s | %-15s | %-10s | %-10s |\n", "elo", "username", "gamesCount", "winRatio"+"\n"+"----------------------------------------------------------------------");

            String response = "";

            try {
                response = UserRepository.showScoreBoard();
            } catch (SQLException e) {
                e.printStackTrace();
                return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.PLAIN_TEXT, "Error while show Scoreboard");
            }
            return new Response(HttpStatus.OK, ContentType.PLAIN_TEXT, top+response);
        }

        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.PLAIN_TEXT, "Error while show Scoreboard");
    }
}
