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
public class Address {
    SimpleIntegerProperty addId;
    SimpleStringProperty add;
    SimpleStringProperty add2;
    SimpleIntegerProperty cityId;
    SimpleStringProperty postal;
    SimpleStringProperty phone;
    SimpleStringProperty createBy;
    SimpleStringProperty createDate;
    SimpleStringProperty lastUpdate;
    SimpleStringProperty lastUpdatedBy;
    
    public Address(int addId, String add,String add2,int cityId, String postal, String phone,String createBy,String lastUpdatedBy){
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.addId = new SimpleIntegerProperty(addId);
        this.add = new SimpleStringProperty(add);
        this.add2 = new SimpleStringProperty(add2);
        this.cityId = new SimpleIntegerProperty(addId);
        this.postal = new SimpleStringProperty(postal);
        this.phone = new SimpleStringProperty(phone);
        this.createBy = new SimpleStringProperty(createBy);
        this.lastUpdatedBy = new SimpleStringProperty(lastUpdatedBy);
        this.createDate = new SimpleStringProperty(dtf.format(now));
        this.lastUpdate = new SimpleStringProperty(dtf.format(now));
    }
    public int getAddId(){
        return addId.get();
    }
    public String getAdd(){
        return add.get();
    }
    public String getAdd2(){
        return add2.get();
    }
    public int getCityId(){
        return cityId.get();
    }
    public String getPhone(){
        return phone.get();
    }
    public String getPostal(){
        return postal.get();
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
