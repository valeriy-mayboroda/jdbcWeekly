import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
/**
 * Created by val on 07.04.2017.
 */
public class MeetingService {
    public void printMeetings(Connection connection, int... number) {
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
    public ArrayList<Meeting> getMeetingsList(Connection connection) {
        ArrayList<Meeting> result = new ArrayList();
        String querry = "select * from databaseweekly.meeting";
        try(PreparedStatement ps = connection.prepareStatement(querry)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Meeting meeting = new Meeting();
                meeting.setId(rs.getInt("id"));
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
    public void addMeting(Connection connection) {
        Meeting meeting = readMeetingFromConsole();
        meeting.save(connection);
    }
    public Meeting readMeetingFromConsole() {
        Meeting meeting = null;
        try (BufferedReader reader = new BufferedReader (new InputStreamReader(System.in))) {
            meeting = new Meeting();
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
        ArrayList<Meeting> list = getMeetingsList(connection);
        for (Meeting meeting : list) {
            if (meeting.getId() == number)
                return meeting.delete(connection);
        }
        return false;
    }
}
