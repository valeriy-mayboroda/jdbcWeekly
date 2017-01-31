import java.io.BufferedReader;
import java.io.IOException;
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

    public Meeting (int id, String user, String name, java.sql.Date date, java.sql.Time time, String details) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.date = date;
        this.time = time;
        this.details = details;
    }

    public int getId() {
        return id;
    }
    public String getUser() {
        return user;
    }
    public String getName() {
        return name;
    }
    public Date getDate() {
        return date;
    }
    public Time getTime() {
        return time;
    }
    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return String.format("id = %d\tuser = %s\tname = %s\tdate = %s\ttime = %s\tdetails = %s",
                getId(), getUser(), getName(), getDate(), getTime(), getDetails());
    }

    public static ArrayList<Meeting> getMeetingList(Connection connection) throws SQLException {
        ArrayList<Meeting> result = new ArrayList();
        String querry = "select * from databaseweekly.meeting";
        PreparedStatement ps = connection.prepareStatement(querry);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Meeting meeting = new Meeting (rs.getInt("id"), rs.getString("user"), rs.getString("name"),
                    rs.getDate("date"), rs.getTime("time"), rs.getString("details"));
            result.add(meeting);
        }
        ps.close();
        return result;
    }

    public static void print (Connection connection) throws SQLException {
        ArrayList<Meeting> list = getMeetingList(connection);
        for (Meeting meeting : list) {
            System.out.println(meeting);
        }
    }

    public static void add (Connection connection) throws SQLException, IOException {
        BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
        System.out.print("user = ");
        String addUser = reader.readLine();
        System.out.print("name = ");
        String addName = reader.readLine();
        System.out.print("date (yyyy-mm-dd) = ");
        String date[] = reader.readLine().split("-");
        System.out.print("time (hh:mm:ss)= ");
        String time[] = reader.readLine().split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setLenient(false);
        calendar.setTimeInMillis(0);
        calendar.set(Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2]),
                Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
        long calImMillis = calendar.getTimeInMillis();
        java.sql.Date addDate = new java.sql.Date(calImMillis);
        java.sql.Time addTime = new java.sql.Time(calImMillis);
        System.out.print("details = ");
        String addDetails = reader.readLine();
        reader.close();

        ArrayList<Meeting> list = getMeetingList(connection);
        int addId = list.get(list.size() - 1).id + 1;
        String querry = "insert into databaseweekly.meeting values(?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(querry);
        ps.setInt(1, addId);
        ps.setString(2, addUser);
        ps.setString(3, addName);
        ps.setDate(4, addDate);
        ps.setTime(5, addTime);
        ps.setString(6, addDetails);
        ps.executeUpdate();
        ps.close();
    }

    public static boolean delete (Connection connection, int number) throws SQLException {
        String querry = "delete from databaseweekly.meeting where id=" + number;
        Statement statement = connection.createStatement();
        int sum = statement.executeUpdate(querry);
        statement.close();
        if (sum == 1)
            return true;
        return false;
    }
}
