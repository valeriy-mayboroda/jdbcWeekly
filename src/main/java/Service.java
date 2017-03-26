import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by val on 25.03.2017.
 */
public class Service {
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

    public static void printMeetings(Connection connection, int... number) {
        ArrayList<Meeting> list = getMeetingsList(connection);
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < number.length; i ++) {
            numbers.add(number[i]);
        }
        if (numbers.size() == 0) {
            for (Meeting meeting : list)
                System.out.println(meeting);
        }
        else {
            for (int i : numbers) {
                for (Meeting meeting : list) {
                    if (meeting.getUserId() == i)
                        System.out.println(meeting);
                }
            }
        }
    }
    public static ArrayList<Meeting> getMeetingsList(Connection connection) {
        ArrayList<Meeting> result = new ArrayList();
        String querry = "select * from databaseweekly.meeting";
        try(PreparedStatement ps = connection.prepareStatement(querry)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Meeting meeting = new Meeting();
                meeting.setId(rs.getInt("id"));
                meeting.setUser(rs.getString("user"));
                meeting.setName(rs.getString("name"));
                meeting.setDate(rs.getDate("date"));
                meeting.setTime(rs.getTime("time"));
                meeting.setDetails(rs.getString("details"));
                meeting.setUserId(rs.getInt("userId"));
                result.add(meeting);
            }
        }
        catch (SQLException e) {
            System.out.println("Data reeding from table Meeting mistake");
        }
        return result;
    }

    public void addUser(Connection connection) {
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

    public void addMeting(Connection connection) {
        Meeting meeting = readMeetingFromConsole();
        if (meeting == null)
            return;
        String request = " (user, name, date, time, details, userId) ";
        String querry = "insert into databaseweekly.meeting" + request + "values (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(querry)) {
            ps.setString(1, meeting.getUser());
            ps.setString(2, meeting.getName());
            ps.setDate(3, meeting.getDate());
            ps.setTime(4, meeting.getTime());
            ps.setString(5, meeting.getDetails());
            ps.setInt(6, meeting.getUserId());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Data recording to table Meeting mistake");
        }
    }
    public Meeting readMeetingFromConsole() {
        Meeting meeting = null;
        try (BufferedReader reader = new BufferedReader (new InputStreamReader(System.in))) {
            meeting = new Meeting();
            System.out.print("user = ");
            meeting.setUser(reader.readLine());
            System.out.print("name = ");
            meeting.setName(reader.readLine());

            System.out.print("date (yyyy-mm-dd) = ");
            String date[] = reader.readLine().split("-");
            System.out.print("time (hh:mm:ss)= ");
            String time[] = reader.readLine().split(":");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            calendar.setLenient(false);
            calendar.setTimeInMillis(0);
            calendar.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]),
                    Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
            long calImMillis = calendar.getTimeInMillis();
            meeting.setDate(new java.sql.Date(calImMillis));
            meeting.setTime(new java.sql.Time(calImMillis));
            System.out.print("details = ");
            meeting.setDetails(reader.readLine());
            System.out.print("userId = ");
            meeting.setUserId(Integer.parseInt(reader.readLine()));
        }
        catch(Exception e) {
            System.out.println("Data reeding new Meeting from console mistake");
        }
        return meeting;
    }

    public boolean deleteMeeting(Connection connection, int number) {
        String querry = "delete from databaseweekly.meeting where id=" + number;
        int sum = 0;
        try (Statement statement = connection.createStatement()) {
            sum = statement.executeUpdate(querry);
        }
        catch(SQLException e) {
            System.out.println("Data deleting from table Meeting mistake");
        }
        return sum == 1;
    }

    public boolean deleteUser(Connection connection, int number) {
        ArrayList<Meeting> list = getMeetingsList(connection);
        for (Meeting meeting: list) {
            if (meeting.getUserId() == number) {
                deleteMeeting(connection, meeting.getId());
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
