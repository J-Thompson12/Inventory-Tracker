/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soft2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Thompson
 */
public class Customer {
    SimpleIntegerProperty custId;
    SimpleStringProperty name;
    SimpleIntegerProperty addId;
    SimpleIntegerProperty active;
    SimpleStringProperty createBy;
    SimpleStringProperty createDate;
    SimpleStringProperty lastUpdate;
    SimpleStringProperty lastUpdatedBy;
    
    public Customer(int custId,String name,int addId,String createBy,String lastUpdatedBy){
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.custId = new SimpleIntegerProperty(custId);
        this.name = new SimpleStringProperty(name);
        this.addId = new SimpleIntegerProperty(addId);
        this.active = new SimpleIntegerProperty(1);
        this.createBy = new SimpleStringProperty(createBy);
        this.lastUpdatedBy = new SimpleStringProperty(lastUpdatedBy);
        this.createDate = new SimpleStringProperty(dtf.format(now));
        this.lastUpdate = new SimpleStringProperty(dtf.format(now));
    }
    public int getCustId(){
        return custId.get();
    }
    public String getName(){
        return name.get();
    }
    public int getAddId(){
        return addId.get();
    }
    public int getActive(){
        return active.get();
    }
    public String getCreateBy(){
        return createBy.get();
    }
    public String getLastUpdatedBy(){
        return lastUpdatedBy.get();
    }
    public String getCreateDate(){
        return createDate.get();
    }
    public String getLastUpdate(){
        return lastUpdate.get();
    }
    
    
}
