
import java.sql.*;

public class PostgreSample {
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
