import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by val on 13.02.2017.
 */
public class User {
    private int id;
    private String firstname;
    private String lastname;
    private int age;
    private String mail;

    public User() {}

    public int getId() {return id;}
    public String getFirstname() {return firstname;}
    public String getLastname() {return lastname;}
    public int getAge() {return age;}
    public String getMail() {return mail;}

    public void setId(int id) {this.id = id;}
    public void setFirstname(String firstname) {this.firstname = firstname;}
    public void setLastname(String lastname) {this.lastname = lastname;}
    public void setAge(int age) {this.age = age;}
    public void setMail(String mail) {this.mail = mail;}

    @Override
    public String toString() {
        return String.format("id = %d\tfirstname = %s\tlastname = %s\tage = %d\tmail = %s",
                getId(), getFirstname(), getLastname(), getAge(), getMail());
    }

    public static ArrayList<User> getUserList(Connection connection) {
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

    public static void printUser(Connection connection) {
        ArrayList<User> list = getUserList(connection);
        for (User user : list) {
            System.out.println(user);
        }
    }

    public static void addUser(Connection connection) {
        User user = readUserFromConsole();
        if (user == null)
            return;
        String request = " (firstname, lastname, age, mail) ";
        String querry = "insert into databaseweekly.user" + request + "values (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(querry)) {
            ps.setString(1, user.getFirstname());
            ps.setString(2, user.getLastname());
            ps.setInt(3, user.getAge());
            ps.setString(4, user.getMail());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Data recording to table User mistake");
        }
    }

    public static User readUserFromConsole() {
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

    public static boolean deleteUser(Connection connection, int number) {
        ArrayList<Meeting> list = Meeting.getMeetingList(connection);
        for (Meeting meeting: list) {
            if (meeting.getUserId() == number) {
                Meeting.deleteMeeting(connection, meeting.getId());
            }
        }
        String querry = "delete from databaseweekly.user where id=" + number;
        int sum = 0;
        try (Statement statement = connection.createStatement()) {
            sum = statement.executeUpdate(querry);
        }
        catch(SQLException e) {
            System.out.println("Data deleting from table User mistake");
        }
        return sum == 1;
    }
}
