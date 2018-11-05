/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soft2;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Thompson
 */
public class Appointment {
    SimpleIntegerProperty appointmentId;
    SimpleIntegerProperty customerId;
    SimpleStringProperty title;
    SimpleStringProperty description;
    SimpleStringProperty location;
    SimpleStringProperty contact;
    SimpleStringProperty url;
    Timestamp start;
    Timestamp end;
    SimpleStringProperty createDate;
    SimpleStringProperty createdBy;
    SimpleStringProperty lastUpdate;
    SimpleStringProperty lastUpdateBy;
    
    public Appointment(int appointmentId, int customerId, String title, String description, String location, String contact, String url, Timestamp start, Timestamp end,String createdBy,String lastUpdateBy){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.location = new SimpleStringProperty(location);
        this.contact = new SimpleStringProperty(contact);
        this.url = new SimpleStringProperty(url);
        this.start = start;
        this.end = end;
        this.createdBy = new SimpleStringProperty(createdBy);
        this.lastUpdateBy = new SimpleStringProperty(lastUpdateBy);
        this.createDate = new SimpleStringProperty(dtf.format(now));
        this.lastUpdate = new SimpleStringProperty(dtf.format(now));
        
    }
    public int getAppId(){
        return appointmentId.get();
    }
    public int getCustId(){
        return customerId.get();
    }
    public String getTitle(){
        return title.get();
    }
    public String getDescription(){
        return description.get();
    }
    public String getLocation(){
        return location.get();
    }
    public String getContact(){
        return contact.get();
    }
    public String getUrl(){
        return url.get();
    }
    public Timestamp getStart(){
        return start;
    }
    public Timestamp getEnd(){
        return end;
    }
    public String getCreatedBy(){
        return createdBy.get();
    }
    public String getLastUpdateBy(){
        return lastUpdateBy.get();
    }
    public String getCreateDate(){
        return createDate.get();
    }
    public String getLastUpdate(){
        return lastUpdate.get();
    }
}
