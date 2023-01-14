package DB;
import java.util.ArrayList;
import java.util.List;
import Card.*;
import User.*;
import java.sql.*;
import java.util.Random;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardQueryTest {

    @Test
    void buyPackage() throws SQLException {

        User user1 = new User("test","test123",15,100,83,4,1);
        assertEquals(0,DB.CardQuery.buyPackage(user1));

    }
}