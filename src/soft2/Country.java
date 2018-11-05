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
public class Country {
    SimpleIntegerProperty countryId;
    SimpleStringProperty country;
    SimpleStringProperty createBy;
    SimpleStringProperty createDate;
    SimpleStringProperty lastUpdate;
    SimpleStringProperty lastUpdatedBy;
    
    public Country(int countryId,String country,String createBy,String lastUpdatedBy){
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.countryId = new SimpleIntegerProperty(countryId);
        this.country = new SimpleStringProperty(country);
        this.createBy = new SimpleStringProperty(createBy);
        this.lastUpdatedBy = new SimpleStringProperty(lastUpdatedBy);
        this.createDate = new SimpleStringProperty(dtf.format(now));
        this.lastUpdate = new SimpleStringProperty(dtf.format(now));
    }
    public int getCountryId(){
        return countryId.get();
    }
    public String getCountry(){
        return country.get();
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
