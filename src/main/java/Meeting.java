import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by val on 26.01.2017.
 */
public class Meeting {
    private int id;
    private String user;
    private String name;
    private java.sql.Date date;
    private java.sql.Time time;
    private String details;
    private int userId;

    public Meeting() {}

    public int getId() {return id;}
    public String getUser() {return user;}
    public String getName() {return name;}
    public Date getDate() {return date;}
    public Time getTime() {return time;}
    public String getDetails() {return details;}
    public int getUserId() {return userId;}

    public void setId(int id) {this.id = id;}
    public void setUser(String user) {this.user = user;}
    public void setName(String name) {this.name = name;}
    public void setDate(java.sql.Date date) {this.date = date;}
    public void setTime(java.sql.Time time) {this.time = time;}
    public void setDetails(String details) {this.details = details;}
    public void setUserId(int userId) {this.userId = userId;}

    @Override
    public String toString() {
        return String.format("id = %d\tuser = %s\tname = %s\tdate = %s\ttime = %s\tdetails = %s\tuserId = %d",
                getId(), getUser(), getName(), getDate(), getTime(), getDetails(), getUserId());
    }

    public static ArrayList<Meeting> getMeetingList(Connection connection) {
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

    public static void printMeeting(Connection connection, int... number) {
        ArrayList<Meeting> list = getMeetingList(connection);
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

    public static void addMeting(Connection connection) {
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

    public static Meeting readMeetingFromConsole() {
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

    public static boolean deleteMeeting(Connection connection, int number) {
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
}
