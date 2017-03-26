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

    @Override
    public String toString() {
        return String.format("id = %d\tfirstname = %s\tlastname = %s\tage = %d\tmail = %s",
                getId(), getFirstname(), getLastname(), getAge(), getMail());
    }
}
