/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soft2;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleIntegerProperty;


/**
 *
 * @author Thompson
 */
public class user {

    Connection conn = null;
    String driver = "com.mysql.jdbc.Driver";
    String db = "U04aeq";
    String url = "jdbc:mysql://52.206.157.109/" + db;
    String user = "U04aeq";
    String pass = "53688186091";
    SimpleIntegerProperty userId;
    String userName;
    String password;
    SimpleIntegerProperty active;
    String createBy;
    String createDate;
    String lastUpdate;
    String lastUpdatedBy;

    public user(int userId, String userName, String password, String createBy) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.userId = new SimpleIntegerProperty(userId);
        this.userName = userName;
        this.password = password;
        this.active = new SimpleIntegerProperty(1);
        this.createBy = createBy;
        this.createDate = dtf.format(now);
        this.lastUpdate = dtf.format(now);
        this.lastUpdatedBy = createBy;
    }
    

}
