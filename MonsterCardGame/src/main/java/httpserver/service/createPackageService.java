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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class createPackageService implements Service{

    public Response handleRequest(Request request) {

        if (request.getMethod() == Method.POST) {
            JSONArray jsonArray = new JSONArray(request.getBody());
            List<Card> cards = new ArrayList<Card>();

            for (int i = 0; i < 5 ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String cardid = jsonObject.getString("Id");
                String name = jsonObject.getString("Name").toUpperCase();
                float damage = (float) jsonObject.getDouble("Damage");
                String elementType = "NORMAL";

                if(name.contains("FIRE")){
                    elementType ="FIRE";
                }
                if(name.contains("WATER")){
                    elementType ="WATER";
                }
                if(name.contains("ICE")){
                    elementType ="ICE";
                }


                if(name.toUpperCase().contains("SPELL")) {
                    SpellCard spellcard = new SpellCard(name,elementType,damage,0,cardid);
                    cards.add(spellcard);
                } else {
                    MonsterCard monsterCard = new MonsterCard(name,elementType,damage,0,cardid);
                    cards.add(monsterCard);
                }

            int status = DB.CardQuery.createPackage(cards);


            }
        }

        return null;
    }


}
