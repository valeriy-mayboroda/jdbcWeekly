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
        System.out.println("1 - Вывод списка всех пользователей");
        System.out.println("2 - Вывод списка дел, встреч всех пользователей");
        System.out.println("3 - Вывод списка дел, встреч пользователя по id пользователя");
        System.out.println("4 - Добавить нового пользователя");
        System.out.println("5 - Добавить новое дело, встречу");
        System.out.println("6 - Удалить дело, встречу по id дела, встречи");
        System.out.println("7 - Удалить пользователя по id пользователя с удалением всех его дел, встреч");

        int number = Integer.parseInt(reader.readLine());
        MeetingService ms = new MeetingService();
        UserService us = new UserService();
        if (number == 1) {
            us.printUsers(connection);
        }
        else if (number == 2) {
            ms.printMeetings(connection);
        }
        else if (number == 3) {
            System.out.print("Вывести список дел, встреч пользователя № ");
            ms.printMeetings(connection, Integer.parseInt(reader.readLine()));
        }
        else if (number == 4) {
            us.addUser(connection);
        }
        else if (number == 5) {
            ms.addMeting(connection);
        }
        else if (number == 6) {
            System.out.print("Удалить дело, встречу № ");
            int id = Integer.parseInt(reader.readLine());
            ms.deleteMeeting(connection, id);
        }
        else if (number == 7) {
            System.out.print("Удалить пользователя № (с удалением его всех дел, встреч) ");
            int id = Integer.parseInt(reader.readLine());
            us.deleteUser(connection, id);
        }
        reader.close();
        connection.close();
    }
}
