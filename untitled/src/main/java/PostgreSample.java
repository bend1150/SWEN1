
import java.sql.*;

public class PostgreSample {

    private final String url = "jdbc:postgresql://localhost:5432/CardGame"
    static void DataHandler() throws SQLException {

        connection = DriverManager.getConnection();
    }

    //private final String url ="";

    //private final String user ="";

    //private final String password = "PROGRES_PASSWORD";

    //System.out.println(url+user+password);
    /*
    if (DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/mydb",
            "postgres", "")) {
        System.out.println("Successful Connection");
    }
    else
    {
        System.out.println("Connection failed");
    }
    */
    /*


    public void openConnection(String url, String user, String password) throws SQLException {
        Connection _connection = DriverManager.getConnection(url, user, password);
        var statement = _connection.createStatement();
        statement.execute("delete from PlaygroundPoints");

    }

    public static void executeSqlSample(){
        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/mydb",
                "postgres",
                "");
             PreparedStatement statement = connection.prepareStatement("""
        INSERT INTO playgroundpoints
        (objectid, anlname, spielplatzdetail)
        VALUES(?, ?,? );
        """ )
        ){
            statement.setInt(1, data.getObjectId() );
            statement.setString(2, data.getAnlName() );
            statement.setString(3, data.getSpielplatzDetail());
            statement.execute();
        }   catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

*/
}
