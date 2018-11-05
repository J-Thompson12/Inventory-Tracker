/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soft2;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 *
 * @author Thompson
 */
public class RemTask extends TimerTask {

    String title;
    Date startTime;
    Date endTime;
    int id;

    public RemTask(String title, Date startTime, Date endTime, int id) {
        this.startTime = startTime;
        this.title = title;
        this.endTime = endTime;
        this.id = id;
    }

    public void run() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(title);
                alert.setHeaderText("Appointment at" + " " + startTime + " to " + endTime);
                alert.showAndWait();
            }
        });

    }
    
    public int getId(){
        return id;
    }
}
