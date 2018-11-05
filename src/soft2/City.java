/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soft2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Thompson
 */
public class City {
    SimpleIntegerProperty cityId;
    SimpleStringProperty city;
    SimpleIntegerProperty countryId;
    SimpleStringProperty createBy;
    SimpleStringProperty createDate;
    SimpleStringProperty lastUpdate;
    SimpleStringProperty lastUpdatedBy;
    
    public City(int cityId,String city,int countryId,String createBy,String lastUpdatedBy){
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.cityId = new SimpleIntegerProperty(cityId);
        this.city = new SimpleStringProperty(city);
        this.countryId = new SimpleIntegerProperty(countryId);
        this.createBy = new SimpleStringProperty(createBy);
        this.lastUpdatedBy = new SimpleStringProperty(lastUpdatedBy);
        this.createDate = new SimpleStringProperty(dtf.format(now));
        this.lastUpdate = new SimpleStringProperty(dtf.format(now));
    }
    public int getCityId(){
        return cityId.get();
    }
    public String getCity(){
        return city.get();
    }
    public int getCountryId(){
        return countryId.get();
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
