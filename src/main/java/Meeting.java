import java.sql.*;
/**
 * Created by val on 26.01.2017.
 */
public class Meeting {
    private int id;
    private String name;
    private java.sql.Date date;
    private java.sql.Time time;
    private String details;
    private int userId;

    public Meeting() {}

    public void setId(int id) {this.id = id;}
    public int getId() {return id;}

    public void setName(String name) {this.name = name;}
    public String getName() {return name;}

    public void setDate(java.sql.Date date) {this.date = date;}
    public Date getDate() {return date;}

    public void setTime(java.sql.Time time) {this.time = time;}
    public Time getTime() {return time;}

    public void setDetails(String details) {this.details = details;}
    public String getDetails() {return details;}

    public void setUserId(int userId) {this.userId = userId;}
    public int getUserId() {return userId;}

    public void save(Connection connection) {
        if (this == null)
            return;
        String request = " (name, date, time, details, userId) ";
        String querry = "insert into databaseweekly.meeting" + request + "values (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(querry)) {
            ps.setString(1, getName());
            ps.setDate(2, getDate());
            ps.setTime(3, getTime());
            ps.setString(4, getDetails());
            ps.setInt(5, getUserId());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Data recording to table Meeting mistake");
        }
    }

    public boolean delete(Connection connection) {
        String querry = "delete from databaseweekly.meeting where id=" + getId();
        int sum = 0;
        try (Statement statement = connection.createStatement()) {
            sum = statement.executeUpdate(querry);
        }
        catch(SQLException e) {
            System.out.println("Data deleting from table Meeting mistake");
        }
        return sum == 1;
    }

        @Override
    public String toString() {
        return String.format("id = %d\tname = %s\tdate = %s\ttime = %s\tdetails = %s\tuserId = %d",
                getId(), getName(), getDate(), getTime(), getDetails(), getUserId());
    }
}
