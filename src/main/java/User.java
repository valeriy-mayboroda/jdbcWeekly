import java.sql.*;
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

    public void setId(int id) {this.id = id;}
    public int getId() {return id;}

    public void setFirstname(String firstname) {this.firstname = firstname;}
    public String getFirstname() {return firstname;}

    public void setLastname(String lastname) {this.lastname = lastname;}
    public String getLastname() {return lastname;}

    public void setAge(int age) {this.age = age;}
    public int getAge() {return age;}

    public void setMail(String mail) {this.mail = mail;}
    public String getMail() {return mail;}

    public void save(Connection connection) {
        if (this == null)
            return;
        String request = " (firstname, lastname, age, mail) ";
        String querry = "insert into databaseweekly.user" + request + "values (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(querry)) {
            ps.setString(1, getFirstname());
            ps.setString(2, getLastname());
            ps.setInt(3, getAge());
            ps.setString(4, getMail());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Data recording to table User mistake");
        }
    }

    public boolean delete(Connection connection) {
        String querry = "delete from databaseweekly.user where id=" + getId();
        int sum = 0;
        try (Statement statement = connection.createStatement()) {
            sum = statement.executeUpdate(querry);
        }
        catch(SQLException e) {
            System.out.println("Data deleting from table User mistake");
        }
        return sum == 1;
    }

    @Override
    public String toString() {
        return String.format("id = %d\tfirstname = %s\tlastname = %s\tage = %d\tmail = %s",
                getId(), getFirstname(), getLastname(), getAge(), getMail());
    }
}
