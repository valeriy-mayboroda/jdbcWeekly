import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by val on 23.01.2017.
 */
public class Main {

    private static final String URL = "jdbc:mysql://localhost:3306/databaseweekly?useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static void main (String[] args) throws SQLException, IOException {

        DBConnection db = new DBConnection();
        Connection connection = db.getConnection(URL, USERNAME, PASSWORD);

        BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
        int number = Integer.parseInt(reader.readLine());
        if (number == 1) {
            Meeting.print(connection);
        }
        else if (number == 2) {
        Meeting.add(connection);
        }
        else if (number == 3) {
            System.out.print("Удалить встречу № ");
            int id = Integer.parseInt(reader.readLine());
            Meeting.delete(connection, id);
        }
        reader.close();
        connection.close();
    }
}
