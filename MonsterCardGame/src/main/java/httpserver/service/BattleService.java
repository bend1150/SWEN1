package httpserver.service;

import httpserver.http.Method;
import httpserver.server.Request;
import httpserver.server.Response;
import httpserver.server.Service;
import httpserver.http.ContentType;
import httpserver.http.HttpStatus;
import DB.*;
import User.*;
import Battle.*;


import java.sql.SQLException;

public class BattleService implements Service {

    public Response handleRequest(Request request) {

        if (request.getMethod() == Method.POST) {
            String token1 = request.getHeaderMap().getHeader("Authorization1");
            String token2 = request.getHeaderMap().getHeader("Authorization2");
            String response = "";

            UserRepository usrep = new UserRepository();
            CardRepository cdrep = new CardRepository();
            BattleLogic bttle = new BattleLogic();

            User player1 = usrep.getUserInfoByToken(token1);
            User player2 = usrep.getUserInfoByToken(token2);

            try {
                player1 = cdrep.getDeckFromDB(player1);
                player2 = cdrep.getDeckFromDB(player2);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            int result = bttle.battle(player1,player2);

            if(result==1){
                response = player1.getUsername()+" has won!!!";
                try {
                    usrep.updateStats(player1,player2);         //ELO update
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (result==2) {
                response = player2.getUsername()+" has won!!!";
                try {
                    usrep.updateStats(player2,player1);         //ELO update
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (result==3){
                response = "Draw!!! No player has won/lose.";
                try {
                    usrep.updateGamesCount(player1,player2);       //only gamesCount++
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }





            return new Response(HttpStatus.OK, ContentType.PLAIN_TEXT, response);
        }
        return null;
    }

}
