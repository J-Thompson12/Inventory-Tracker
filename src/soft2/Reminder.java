/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soft2;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Thompson
 */
public class Reminder {
    SimpleIntegerProperty remId;
    LocalDateTime remDate;
    SimpleIntegerProperty snoozeIncrement;
    SimpleIntegerProperty snoozeIncrementTypeId;
    SimpleIntegerProperty appointmentId;
    SimpleStringProperty createdBy;
    SimpleStringProperty createdDate;
    SimpleStringProperty remindercol;
    
    public Reminder(int remId,LocalDateTime remDate,int snoozeIncrement, int snoozeIncrementTypeId, int appointmentId, String createdBy, String remindercol){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.remId = new SimpleIntegerProperty(remId);
        this.remDate = remDate;
        this.snoozeIncrement = new SimpleIntegerProperty(snoozeIncrement);
        this.snoozeIncrementTypeId = new SimpleIntegerProperty(snoozeIncrementTypeId);
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.createdBy = new SimpleStringProperty(createdBy);
        this.createdDate = new SimpleStringProperty(dtf.format(now));
        this.remindercol = new SimpleStringProperty(remindercol);
    }
    
    public int getRemId(){
        return remId.get();
    }
    public LocalDateTime getRemDate(){
        return remDate;
    }
    public int getSnoozeIncrement(){
        return snoozeIncrement.get();
    }
    public int getSnoozeIncrementTypeId(){
        return snoozeIncrementTypeId.get();
    }
    public int getAppointmentId(){
        return appointmentId.get();
    }
    public String getCreatedBy(){
        return createdBy.get();
    }
    public String getCreatedDate(){
        return createdDate.get();
    }
    public String getRemindercol(){
        return remindercol.get();
    }
}
