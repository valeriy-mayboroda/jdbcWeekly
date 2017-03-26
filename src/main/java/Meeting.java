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

    public void setId(int id) {this.id = id;}
    public int getId() {return id;}

    public void setUser(String user) {this.user = user;}
    public String getUser() {return user;}

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

    @Override
    public String toString() {
        return String.format("id = %d\tuser = %s\tname = %s\tdate = %s\ttime = %s\tdetails = %s\tuserId = %d",
                getId(), getUser(), getName(), getDate(), getTime(), getDetails(), getUserId());
    }
}
