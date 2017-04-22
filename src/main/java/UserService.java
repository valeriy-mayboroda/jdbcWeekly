import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
/**
 * Created by val on 07.04.2017.
 */
public class UserService {
    public void printUsers(Connection connection) {
        ArrayList<User> list = getUsersList(connection);
        for (User user : list) {
            System.out.println(user);
        }
    }
    public ArrayList<User> getUsersList(Connection connection) {
        ArrayList<User> result = new ArrayList();
        String querry = "select * from databaseweekly.user";
        try(PreparedStatement ps = connection.prepareStatement(querry)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFirstname(rs.getString("firstname"));
                user.setLastname(rs.getString("lastname"));
                user.setAge(rs.getInt("age"));
                user.setMail(rs.getString("mail"));
                result.add(user);
            }
        }
        catch (SQLException e) {
            System.out.println("Data reeding from table User mistake");
        }
        return result;
    }
    public void addUser(Connection connection) {
        User user = readUserFromConsole();
        user.save(connection);
    }
    public User readUserFromConsole() {
        User user = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            user = new User();
            System.out.print("firstname = ");
            user.setFirstname(reader.readLine());
            System.out.print("lastname = ");
            user.setLastname(reader.readLine());
            System.out.print("age = ");
            user.setAge(Integer.parseInt(reader.readLine()));
            System.out.print("mail = ");
            user.setMail(reader.readLine());
        }
        catch(Exception e) {
            System.out.println("Data reeding new User from console mistake");
        }
        return user;
    }
    public boolean deleteUser(Connection connection, int number) {
        ArrayList<User> users = getUsersList(connection);
        for (User user : users) {
            if (user.getId() == number) {
                for (Meeting meeting : user.getMeetings(connection)) {
                    meeting.delete(connection);
                }
                return user.delete(connection);
            }
        }
        return false;
    }
}
