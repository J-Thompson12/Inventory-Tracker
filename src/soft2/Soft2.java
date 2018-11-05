/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soft2;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 *
 * @author Thompson
 */
public class Soft2 extends Application {

    TextField userTxt = new TextField();
    PasswordField pwTxt = new PasswordField();
    Scene main, login, newUser, addEventScene, addCustScene, editCustScene, reportScene;
    String lang = null;
    String loc = null;
    String loginLog = null;
    PrintStream out;
    String user = null;
    ObservableList<String> custList = FXCollections.observableArrayList();
    private ObservableList<ObservableList> data;
    TableView<ObservableList> cal = new TableView();
    ZoneId zone;
    boolean addPressed;
    ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(4);
    public  static ArrayList<RemTask> remList = new ArrayList<>();
    TableView<ObservableList> report = new TableView();
    private ObservableList<ObservableList> reportData;

    @Override
    public void start(Stage primaryStage) {
        // Login Screen-------------------------------------------------------------------------------------------------
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        grid.add(userTxt, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        grid.add(pwTxt, 1, 2);
        Parent root;

        Button loginBtn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(loginBtn);
        grid.add(hbBtn, 1, 4);
        final Text loginError = new Text();
        loginError.setFill(Color.RED);
        grid.add(loginError, 1, 5);

        Label location = new Label("Select Location");
        grid.add(location, 0, 6);
        final ComboBox locationBox = new ComboBox();
        locationBox.getItems().addAll(
                "US/Arizona",
                "America/New_York",
                "Europe/London"
        );
        grid.add(locationBox, 1, 6);

        Label language = new Label("Select Language");
        grid.add(language, 0, 7);
        final ComboBox langBox = new ComboBox();
        langBox.getItems().addAll(
                "English",
                "Spanish"
        );
        grid.add(langBox, 1, 7);
        loc = ("US/Arizona");
        lang = "English";
        langBox.setPromptText("English");
        locationBox.setPromptText("US/Arizona");
        locationBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String oldLoc, String newLoc) {
                loc = newLoc;
            }
        });
        langBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String oldLang, String newLang) {
                lang = newLang;
            }
        });

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        // check if creds are valid or not
        loginBtn.setOnAction((ActionEvent event)
                -> {
            if (isValidCreds()) {
                setRem();
                buildTable();
                buildData("SELECT appointmentId,title, description,location,contact,url,start,end from appointment where Year(start) = Year(CURRENT_TIMESTAMP) \n"
                    + " AND Month(start) = Month(CURRENT_TIMESTAMP)");
                primaryStage.setScene(main);
                user = userTxt.getText();
                try {
                    Date date = new Date();
                    switch (loc) {
                        case "US/Arizona":
                            df.setTimeZone(TimeZone.getTimeZone("US/Arizona"));
                            break;
                        case "America/New_York":
                            df.setTimeZone(TimeZone.getTimeZone("America/New_York"));
                            break;
                        case "Europe/London":
                            df.setTimeZone(TimeZone.getTimeZone("Europe/London"));
                            break;
                        default:
                            break;
                    }
                    out = new PrintStream(new FileOutputStream("output.txt", true));
                    out.println(userTxt.getText() + " " + df.format(date));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Soft2.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.setOut(out);
            } else {
                if (lang.equals("Spanish")) {
                    loginError.setText("usuario o contraseña invalido");
                } else if (lang.equals("English")) {
                    loginError.setText("Invalid username or password");
                }
            }
        });

        login = new Scene(grid, 400, 350);

        primaryStage.setTitle("Schedule");
        primaryStage.setScene(login);
        primaryStage.show();

        //Main screen------------------------------------------------------------------------------------------
        BorderPane bp = new BorderPane();

        ToggleGroup radioGroup = new ToggleGroup();
        RadioButton monthToggle = new RadioButton("Month");
        monthToggle.setToggleGroup(radioGroup);
        monthToggle.setSelected(true);
        RadioButton weekToggle = new RadioButton("Week           ");
        weekToggle.setToggleGroup(radioGroup);
        HBox radioBtns = new HBox();
        radioBtns.setPadding(new Insets(15, 15, 15, 15));
        radioBtns.setTranslateX(350);

        radioGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
            if (radioGroup.getSelectedToggle() != null) {
                if (weekToggle.isSelected()) {
                    buildData("SELECT appointmentId,title, description,location,contact,url,start,end from appointment where start between now() and adddate(now(),7)");
                } else if (monthToggle.isSelected()) {
                    buildData("SELECT appointmentId,title, description,location,contact,url,start,end from appointment where Year(start) = Year(CURRENT_TIMESTAMP) \n"
                    + " AND Month(start) = Month(CURRENT_TIMESTAMP)");
                }

            }
        });

        cal.setTranslateY(-50);
        cal.setMaxHeight(400);
        cal.setMaxWidth(800);
        cal.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox calBtns = new HBox();
        calBtns.setPadding(new Insets(15, 15, 15, 250));
        calBtns.setTranslateY(-70);
        calBtns.setSpacing(15);
        Button addEvent = new Button("Add Event");
        addEvent.setPadding(new Insets(10, 15, 10, 15));
        Button editEvent = new Button("Edit Event");
        editEvent.setPadding(new Insets(10, 15, 10, 15));
        Button delEvent = new Button("Delete Event");
        delEvent.setPadding(new Insets(10, 15, 10, 15));
        Button reports = new Button("Reports");
        reports.setPadding(new Insets(10, 15, 10, 15));

        //go to new event screen
        addEvent.setOnAction((ActionEvent event) -> {
            addPressed = true;
            primaryStage.setScene(addEventScene);
            primaryStage.setTitle("Add New Event");
        });
        //delete selected event
        delEvent.setOnAction((ActionEvent event) -> {
            if (cal.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                if (lang.equals("Spanish")) {
                    alert.setHeaderText("Seleccione un artículo");
                } else if (lang.equals("English")) {
                    alert.setHeaderText("Please Select an Item");
                }
                alert.showAndWait();  
                
            } else {
                String id = cal.getSelectionModel().getSelectedItem().toString();
                String[] id2 = id.split(",");
                String id3 = id2[0];
                id3 = id3.substring(1);
                updateQuery("DELETE from appointment where appointmentId = " + id3);
                updateQuery("DELETE from reminder where appointmentId = " + id3);
                int id4 = Integer.parseInt(id3);
                RemTask rem = lookupRem(id4);
                if(rem != null){
                    rem.cancel();
                }
                
                if (radioGroup.getSelectedToggle() != null) {
                    if (weekToggle.isSelected()) {
                        buildData("SELECT appointmentId,title, description,location,contact,url,start,end from appointment where start between now() and adddate(now(),7)");
                    } else if (monthToggle.isSelected()) {
                        buildData("SELECT appointmentId,title, description,location,contact,url,start,end from appointment where Year(start) = Year(CURRENT_TIMESTAMP) \n"
                    + " AND Month(start) = Month(CURRENT_TIMESTAMP)");
                    }

                }

            }
        });
        reports.setOnAction((ActionEvent event) -> {
            primaryStage.setScene(reportScene);
            buildReport("SELECT MONTH(start) as month, COUNT(DISTINCT description) as total from appointment GROUP BY MONTH(start) ORDER BY MONTH(start)");

        });

        bp.setTop(radioBtns);
        bp.setCenter(cal);
        bp.setBottom(calBtns);
        calBtns.getChildren().addAll(addEvent, editEvent, delEvent, reports);
        radioBtns.getChildren().addAll(weekToggle, monthToggle);
        main = new Scene(bp, 900, 600);

        //Event Scene-------------------------------------------------------------------------------------------------------
        GridPane addEventGrid = new GridPane();
        addEventGrid.setHgap(25);
        addEventGrid.setVgap(30);

        Label custLabel = new Label("Customer");
        addEventGrid.add(custLabel, 1, 1);
        Label titleLabel = new Label("Title");
        addEventGrid.add(titleLabel, 1, 2);
        Label descLabel = new Label("Description");
        addEventGrid.add(descLabel, 1, 3);
        Label locLabel = new Label("Location");
        addEventGrid.add(locLabel, 1, 4);
        Label conLabel = new Label("Contact");
        addEventGrid.add(conLabel, 1, 5);
        Label urlLabel = new Label("URL");
        addEventGrid.add(urlLabel, 1, 6);
        Label startLabel = new Label("Start Date");
        addEventGrid.add(startLabel, 1, 7);
        Label endLabel = new Label("End Date ");
        addEventGrid.add(endLabel, 3, 7);
        endLabel.setTranslateX(-80);
        Label stLabel = new Label("Start Time");
        addEventGrid.add(stLabel, 1, 8);
        Label etLabel = new Label("End Time");
        addEventGrid.add(etLabel, 3, 8);
        etLabel.setTranslateX(-80);

        ComboBox custBox = new ComboBox();
        addEventGrid.add(custBox, 2, 1);
        custBox.setMaxWidth(170);
        fillCombo();
        custBox.setItems(custList);

        TextField titleTxt = new TextField();
        addEventGrid.add(titleTxt, 2, 2);
        TextArea descTxt = new TextArea();
        descTxt.setPromptText("Type of appointment");
        descTxt.setMaxHeight(100);
        descTxt.setMaxWidth(200);
        addEventGrid.add(descTxt, 2, 3);
        TextField locTxt = new TextField();
        addEventGrid.add(locTxt, 2, 4);
        TextField conTxt = new TextField();
        addEventGrid.add(conTxt, 2, 5);
        TextField urlTxt = new TextField();
        addEventGrid.add(urlTxt, 2, 6);
        TextField startTimeTxt = new TextField();
        startTimeTxt.setPromptText("HH:mm");
        addEventGrid.add(startTimeTxt, 2, 8);
        startTimeTxt.setMaxWidth(125);
        TextField endTimeTxt = new TextField();
        endTimeTxt.setPromptText("HH:mm");
        addEventGrid.add(endTimeTxt, 3, 8);
        endTimeTxt.setMaxWidth(125);

        DatePicker startDate = new DatePicker();
        addEventGrid.add(startDate, 2, 7);
        startDate.setMaxWidth(125);
        DatePicker endDate = new DatePicker();
        addEventGrid.add(endDate, 3, 7);
        endDate.setMaxWidth(125);

        Button addEventSave = new Button("Save");
        addEventGrid.add(addEventSave, 3, 9);
        addEventSave.setTranslateX(-90);
        addEventSave.setMaxWidth(80);
        Button addEventCancel = new Button("Cancel");
        addEventGrid.add(addEventCancel, 3, 9);
        addEventCancel.setMaxWidth(80);
        Button addCustBtn = new Button("Add");
        addCustBtn.setMaxWidth(60);
        Button editCustBtn = new Button("Edit");
        editCustBtn.setMaxWidth(60);
        Button delCustBtn = new Button("Delete");
        delCustBtn.setMaxWidth(60);

        HBox custHBox = new HBox();
        custHBox.setSpacing(5);
        custHBox.setTranslateX(-40);
        addEventGrid.add(custHBox, 3, 1);

        //go to new customer screen
        addCustBtn.setOnAction((ActionEvent event) -> {
            primaryStage.setScene(addCustScene);
            primaryStage.setTitle("Add New Customer");
        });
        //return to main screen
        addEventCancel.setOnAction((ActionEvent Event) -> {
            primaryStage.setScene(main);
            primaryStage.setTitle("Schedular");
            titleTxt.clear();
            descTxt.clear();
            locTxt.clear();
            conTxt.clear();
            urlTxt.clear();
            startTimeTxt.clear();
            endTimeTxt.clear();
            startDate.setValue(null);
            endDate.setValue(null);

        });
        //edit selected customer
        editCustBtn.setOnAction((ActionEvent event) -> {
            primaryStage.setScene(editCustScene);
        });
        //delete selected customer
        delCustBtn.setOnAction((ActionEvent event) -> {
            updateQuery("DELETE from customer WHERE customerName = '" + custBox.getValue().toString() + "'");
            for (int i = 0; i < custList.size(); i++) {
                String name = custList.get(i);
                if (name.equals(custBox.getValue().toString())) {
                    custList.remove(i);
                }
            }
        });
        
        addEventSave.setOnAction((ActionEvent event) -> {
            if (custBox.getValue() == null || titleTxt.getText().equals("") || descTxt.getText().equals("") || locTxt.getText().equals("")
                    || conTxt.getText().equals("") || urlTxt.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                if (lang.equals("Spanish")) {
                    alert.setHeaderText("Falta un campo");
                } else if (lang.equals("English")) {
                    alert.setHeaderText("You are missing a field");
                }
                alert.showAndWait();

            } else {
                if (addPressed == true) {

                    int appointId = getLastId("appointment");
                    try {
                        LocalDate sdate = startDate.getValue();
                        LocalDate edate = endDate.getValue();
                        String startDT = sdate + " " + startTimeTxt.getText();
                        String endDT = edate + " " + endTimeTxt.getText();
                        zone = ZoneId.of(loc);
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");
                        LocalDateTime ldtStart = LocalDateTime.parse(startDT, format);
                        LocalDateTime locStart = ldtStart;
                        ZonedDateTime zdtStart = ldtStart.atZone(zone);
                        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of("UTC"));
                        ldtStart = utcStart.toLocalDateTime();
                        Timestamp startsqlts = Timestamp.valueOf(ldtStart);
                        LocalDateTime ldtEnd = LocalDateTime.parse(endDT, format);
                        LocalDateTime locEnd = ldtEnd;
                        ZonedDateTime zdtEnd = ldtEnd.atZone(zone);
                        ZonedDateTime utcEnd = zdtEnd.withZoneSameInstant(ZoneId.of("UTC"));
                        ldtEnd = utcEnd.toLocalDateTime();
                        Timestamp endsqlts = Timestamp.valueOf(ldtEnd);
                        Appointment appoint = new Appointment(appointId, getId(custBox.getValue().toString()), titleTxt.getText(), descTxt.getText(),
                                locTxt.getText(), conTxt.getText(), urlTxt.getText(), startsqlts, endsqlts, user, user);
                        updateQuery("INSERT INTO appointment VALUES(" + appoint.getAppId() + "," + appoint.getCustId() + ",'" + appoint.getTitle() + "',"
                                + "'" + appoint.getDescription() + "','" + appoint.getLocation() + "','" + appoint.getContact() + "','"
                                + appoint.getUrl() + "','" + appoint.getStart() + "','" + appoint.getEnd() + "','" + appoint.getCreateDate() + "','"
                                + appoint.getCreatedBy() + "','" + appoint.getLastUpdate() + "','" + appoint.getLastUpdateBy() + "')");
                        int remId = getLastId("reminder");
                        LocalDateTime remTime = ldtStart.minusMinutes(15);
                        System.out.println(remTime);
                        Reminder rem = new Reminder(remId, remTime, 15, 1, appoint.getAppId(), user, "remCol");
                        updateQuery("INSERT INTO reminder VALUES(" + rem.getRemId() + ",'" + rem.getRemDate() + "'," + rem.getSnoozeIncrement() + "," + rem.getSnoozeIncrementTypeId()
                                + "," + rem.getAppointmentId() + ",'" + rem.getCreatedBy() + "','" + rem.getCreatedDate() + "','" + rem.getRemindercol() + "')");
                        Timer timer = new Timer();
                        Timestamp startStamp= Timestamp.valueOf(locStart);
                        Timestamp endStamp= Timestamp.valueOf(locEnd);
                        RemTask task = new RemTask(titleTxt.getText(), startStamp, endStamp, remId);
                        ZonedDateTime utcCur = remTime.atZone(ZoneId.of("UTC"));
                        ZonedDateTime newCur = utcCur.withZoneSameInstant(ZoneId.of(loc));
                        remTime = newCur.toLocalDateTime();
                        Timestamp curTime = Timestamp.valueOf(remTime);
                        timer.schedule(task, curTime);
                        remList.add(task);
                        ObservableList<String> row = FXCollections.observableArrayList();
                        if (radioGroup.getSelectedToggle() != null) {
                            if (weekToggle.isSelected()) {
                                buildData("SELECT appointmentId,title, description,location,contact,url,start,end from appointment where start between now() and adddate(now(),7)");
                            } else if (monthToggle.isSelected()) {
                                buildData("SELECT appointmentId,title, description,location,contact,url,start,end from appointment where Year(start) = Year(CURRENT_TIMESTAMP) \n"
                    + " AND Month(start) = Month(CURRENT_TIMESTAMP)");
                            }

                        }
                        

                        titleTxt.clear();
                        descTxt.clear();
                        locTxt.clear();
                        conTxt.clear();
                        urlTxt.clear();
                        startTimeTxt.clear();
                        endTimeTxt.clear();
                        startDate.setValue(null);
                        endDate.setValue(null);

                        primaryStage.setScene(main);
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Input Error");
                        if (lang.equals("Spanish")) {
                            alert.setHeaderText("Eso no es un tiempo válido");
                        } else if (lang.equals("English")) {
                            alert.setHeaderText("That is not a valid time");
                        }

                        alert.showAndWait();
                    }
                } else if (addPressed == false) {
                    try {
                        LocalDate sdate = startDate.getValue();
                        LocalDate edate = endDate.getValue();
                        String startDT = sdate + " " + startTimeTxt.getText();
                        String endDT = edate + " " + endTimeTxt.getText();
                        zone = ZoneId.of(loc);
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");
                        LocalDateTime ldtStart = LocalDateTime.parse(startDT, format);
                        LocalDateTime locStart = ldtStart;
                        ZonedDateTime zdtStart = ldtStart.atZone(zone);
                        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of("UTC"));
                        ldtStart = utcStart.toLocalDateTime();
                        Timestamp startsqlts = Timestamp.valueOf(ldtStart);
                        LocalDateTime ldtEnd = LocalDateTime.parse(endDT, format);
                        LocalDateTime locEnd = ldtEnd;
                        ZonedDateTime zdtEnd = ldtEnd.atZone(zone);
                        ZonedDateTime utcEnd = zdtEnd.withZoneSameInstant(ZoneId.of("UTC"));
                        ldtEnd = utcEnd.toLocalDateTime();
                        Timestamp endsqlts = Timestamp.valueOf(ldtEnd);
                        LocalDateTime remTime = ldtStart.minusMinutes(15);
                        String id = cal.getSelectionModel().getSelectedItem().toString();
                        String[] id2 = id.split(",");
                        String id3 = id2[0];
                        id3 = id3.substring(1);
                        updateQuery("update appointment a left outer join reminder r on a.appointmentId = r.appointmentId set "
                                + "a.customerId = " + getId(custBox.getValue().toString()) + ","
                                + "a.title = '" + titleTxt.getText() + "',"
                                + "a.description = '" + descTxt.getText() + "',"
                                + "a.location = '" + locTxt.getText() + "',"
                                + "a.contact = '" + conTxt.getText() + "',"
                                + "a.url = '" + urlTxt.getText() + "',"
                                + "a.start = '" + startsqlts + "',"
                                + "a.end = '" + endsqlts + "',"
                                + "r.reminderDate = '" + remTime + "'"
                                + "where a.appointmentId = " + id3);
                        //Delete and create new reminder task
                        Connection conn = null;
                        Statement st = null;
                        ResultSet rs = null;
                        int id4 = 0;
                        try {
                            conn = getConnection();
                            st = conn.createStatement();
                            rs = st.executeQuery("SELECT reminder.reminderId from reminder left outer join appointment on "
                                    + "reminder.appointmentId = appointment.appointmentId where appointment.appointmentId = " + id3);
                            while (rs.next()) {
                                String idS = rs.getString(1);
                                id4 = Integer.parseInt(idS);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(Soft2.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            closeST(st);
                            closeConn(conn);
                            closeRS(rs);

                        }
                        RemTask oldRem = lookupRem(id4);
                        if(oldRem != null){
                            oldRem.cancel();
                        }
                        
                        Timer timer = new Timer();
                        Timestamp startStamp= Timestamp.valueOf(locStart);
                        Timestamp endStamp= Timestamp.valueOf(locEnd);
                        RemTask task = new RemTask(titleTxt.getText(), startStamp, endStamp, id4);
                        ZonedDateTime utcCur = remTime.atZone(ZoneId.of("UTC"));
                        ZonedDateTime newCur = utcCur.withZoneSameInstant(ZoneId.of(loc));
                        remTime = newCur.toLocalDateTime();
                        Timestamp curTime = Timestamp.valueOf(remTime);
                        timer.schedule(task, curTime);
                        if (radioGroup.getSelectedToggle() != null) {
                            if (weekToggle.isSelected()) {
                                buildData("SELECT appointmentId,title, description,location,contact,url,start,end from appointment where start between now() and adddate(now(),7)");
                            } else if (monthToggle.isSelected()) {
                                buildData("SELECT appointmentId,title, description,location,contact,url,start,end from appointment where Year(start) = Year(CURRENT_TIMESTAMP) \n"
                    + " AND Month(start) = Month(CURRENT_TIMESTAMP)");
                            }

                        }
                        titleTxt.clear();
                        descTxt.clear();
                        locTxt.clear();
                        conTxt.clear();
                        urlTxt.clear();
                        startTimeTxt.clear();
                        endTimeTxt.clear();
                        startDate.setValue(null);
                        endDate.setValue(null);

                        primaryStage.setScene(main);
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Input Error");
                        if (lang.equals("Spanish")) {
                            alert.setHeaderText("Eso no es un tiempo válido");
                        } else if (lang.equals("English")) {
                            alert.setHeaderText("That is not a valid time");
                        }

                        alert.showAndWait();
                    }
                }
            }
        });

        editEvent.setOnAction((ActionEvent event) -> {
            addPressed = false;
            if (cal.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                if (lang.equals("Spanish")) {
                    alert.setHeaderText("Seleccione un artículo");
                } else if (lang.equals("English")) {
                    alert.setHeaderText("Please Select an Item");
                }

                alert.showAndWait();
            } else {
                Connection conn = null;
                Statement st = null;
                ResultSet rs = null;
                String id = cal.getSelectionModel().getSelectedItem().toString();
                String[] id2 = id.split(",");
                String id3 = id2[0];
                id3 = id3.substring(1);
                try {
                    conn = getConnection();
                    st = conn.createStatement();
                    rs = st.executeQuery("SELECT a.title, a.description,a.location,a.contact,a.url,a.start,a.end, c.customerName "
                            + "from appointment a left outer join customer c on a.customerId = c.customerId where a.appointmentId = " + id3);
                    while (rs.next()) {
                        titleTxt.setText(rs.getString("title"));
                        descTxt.setText(rs.getString("description"));
                        locTxt.setText(rs.getString("location"));
                        conTxt.setText(rs.getString("contact"));
                        urlTxt.setText(rs.getString("url"));
                        String sDate = rs.getString("start").substring(0, rs.getString("start").length() - 5);
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        LocalDateTime ldtStart = LocalDateTime.parse(sDate, format);
                        ZonedDateTime zdtStart = ldtStart.atZone(ZoneId.of("UTC"));
                        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of(loc));
                        LocalDate ldStart = utcStart.toLocalDateTime().toLocalDate();
                        LocalTime ltStart = utcStart.toLocalTime();
                        String eDate = rs.getString("end").substring(0, rs.getString("end").length() - 5);
                        LocalDateTime ldtend = LocalDateTime.parse(eDate, format);
                        ZonedDateTime zdtend = ldtend.atZone(ZoneId.of("UTC"));
                        ZonedDateTime utcend = zdtend.withZoneSameInstant(ZoneId.of(loc));
                        LocalDate ldend = utcStart.toLocalDateTime().toLocalDate();
                        LocalTime ltEnd = utcend.toLocalTime();
                        startDate.setValue(ldStart);
                        endDate.setValue(ldend);
                        startTimeTxt.setText(ltStart.toString());
                        endTimeTxt.setText(ltEnd.toString());
                        custBox.setValue(rs.getString("customerName"));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Soft2.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    closeST(st);
                    closeConn(conn);
                    closeRS(rs);

                }
                primaryStage.setScene(addEventScene);
            }
        });

        custHBox.getChildren().addAll(addCustBtn, editCustBtn, delCustBtn);
        addEventScene = new Scene(addEventGrid, 500, 600);

        //Add Customer Scene----------------------------------------------------------------------------------------------
        GridPane addCustGrid = new GridPane();
        addCustGrid.setHgap(25);
        addCustGrid.setVgap(30);

        Label nameLabel = new Label("Name");
        addCustGrid.add(nameLabel, 1, 1);
        Label addLabel = new Label("Address");
        addCustGrid.add(addLabel, 1, 2);
        Label add2Label = new Label("Address 2");
        addCustGrid.add(add2Label, 1, 3);
        Label cityLabel = new Label("City");
        addCustGrid.add(cityLabel, 1, 4);
        Label countryLabel = new Label("Country");
        addCustGrid.add(countryLabel, 1, 5);
        Label postalLabel = new Label("Postal Code");
        addCustGrid.add(postalLabel, 1, 6);
        Label phoneLabel = new Label("Phone");
        addCustGrid.add(phoneLabel, 1, 7);

        TextField nameTxt = new TextField();
        addCustGrid.add(nameTxt, 2, 1);
        TextField addTxt = new TextField();
        addCustGrid.add(addTxt, 2, 2);
        TextField add2Txt = new TextField();
        addCustGrid.add(add2Txt, 2, 3);
        TextField cityTxt = new TextField();
        addCustGrid.add(cityTxt, 2, 4);
        TextField countryTxt = new TextField();
        addCustGrid.add(countryTxt, 2, 5);
        TextField postalTxt = new TextField();
        addCustGrid.add(postalTxt, 2, 6);
        TextField phoneTxt = new TextField();
        addCustGrid.add(phoneTxt, 2, 7);

        Button saveCust = new Button("Save");
        addCustGrid.add(saveCust, 2, 8);
        saveCust.setMaxWidth(80);
        Button cancelCust = new Button("Cancel");
        addCustGrid.add(cancelCust, 2, 8);
        cancelCust.setMaxWidth(80);
        cancelCust.setTranslateX(90);

        saveCust.setOnAction((ActionEvent event) -> {
            if (nameTxt.getText().equals("") || addTxt.getText().equals("") || cityTxt.getText().equals("") || countryTxt.getText().equals("")
                    || postalTxt.getText().equals("") || phoneTxt.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                if (lang.equals("Spanish")) {
                    alert.setHeaderText("Falta un campo");
                } else if (lang.equals("English")) {
                    alert.setHeaderText("You are missing a field");
                }

                alert.showAndWait();

            } else if (add2Txt.getText().equals("")) {
                add2Txt.setText("0");

            } else {
                String nameMatch = "";
                Connection conn = null;
                Statement st = null;
                ResultSet rs = null;
                try {
                    conn = getConnection();
                    st = conn.createStatement();
                    rs = st.executeQuery("SELECT customerName from customer WHERE customerName = '" + nameTxt.getText() + "';");
                    if (rs.next()) {
                        nameMatch = rs.getString(1);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Soft2.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    closeST(st);
                    closeConn(conn);
                    closeRS(rs);

                }
                if (nameMatch.equals(nameTxt.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Duplicate Customer");
                    if (lang.equals("Spanish")) {
                        alert.setHeaderText("Ese cliente ya existe");
                    } else if (lang.equals("English")) {
                        alert.setHeaderText("That customer already exists");
                    }

                    alert.showAndWait();
                } else {
                    int countId = -1;
                    try {
                        conn = getConnection();
                        st = conn.createStatement();
                        rs = st.executeQuery("SELECT countryId from country WHERE country = '" + countryTxt.getText() + "';");
                        if (rs.next()) {
                            countId = rs.getInt(1);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Soft2.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        closeST(st);
                        closeConn(conn);
                        closeRS(rs);

                    }

                    if (countId == -1) {
                        countId = getLastId("country");
                        Country country = new Country(countId, countryTxt.getText(), user, user);
                        updateQuery("INSERT INTO country VALUES("
                                + country.getCountryId() + "," + "'" + country.getCountry() + "'" + "," + "'" + country.getCreateDate() + "'" + ","
                                + "'" + country.getCreateBy() + "'" + "," + "'" + country.getLastUpdate() + "'" + "," + "'" + country.getLastUpdatedBy() + "'" + ");");

                    }

                    int ctyId = -1;
                    try {
                        conn = getConnection();
                        st = conn.createStatement();
                        rs = st.executeQuery("SELECT cityId from city WHERE city = '" + cityTxt.getText() + "';");
                        if (rs.next()) {
                            ctyId = rs.getInt(1);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Soft2.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        closeST(st);
                        closeConn(conn);
                        closeRS(rs);

                    }

                    if (ctyId == -1) {
                        ctyId = getLastId("city");
                        City city = new City(ctyId, cityTxt.getText(), countId, user, user);
                        updateQuery("INSERT INTO city VALUES("
                                + city.getCityId() + "," + "'" + city.getCity() + "'" + "," + city.getCountryId() + "," + "'" + city.getCreateDate() + "'" + ","
                                + "'" + city.getCreateBy() + "'" + "," + "'" + city.getLastUpdate() + "'" + "," + "'" + city.getLastUpdatedBy() + "'" + ");");

                    }

                    int lastAddId = getLastId("address");
                    Address add = new Address(lastAddId, addTxt.getText(), add2Txt.getText(), ctyId, postalTxt.getText(), phoneTxt.getText(), user, user);
                    updateQuery("INSERT INTO address VALUES(" + lastAddId + "," + "'" + add.getAdd() + "'," + "'" + add.getAdd2() + "',"
                            + "'" + ctyId + "','" + add.getPostal() + "','" + add.getPhone() + "','" + add.getCreateDate() + "'" + ","
                            + "'" + add.getCreateBy() + "'" + "," + "'" + add.getLastUpdate() + "'" + "," + "'" + add.getLastUpdatedBy() + "'" + ");");
                    int custId = getLastId("customer");
                    Customer cust = new Customer(custId, nameTxt.getText(), lastAddId, user, user);
                    int id = cust.getCustId();
                    String name = cust.getName();
                    int addId = cust.getAddId();
                    int active = cust.getActive();
                    String createBy = cust.getCreateBy();
                    String createDate = cust.getCreateDate();
                    String lastUpdate = cust.getLastUpdate();
                    String lastUpdatedBy = cust.getLastUpdatedBy();
                    updateQuery("INSERT INTO customer (customerId,customerName,addressId,active,createDate,createdBy,lastUpdate,lastUpdateBy)"
                            + "VALUES(" + id + "," + "'" + name + "'" + "," + addId + "," + active + "," + "'" + createDate + "'" + ","
                            + "'" + createBy + "'" + "," + "'" + lastUpdate + "'" + "," + "'" + lastUpdatedBy + "'" + ");");

                    nameTxt.clear();
                    cityTxt.clear();
                    countryTxt.clear();
                    addTxt.clear();
                    add2Txt.clear();
                    postalTxt.clear();
                    phoneTxt.clear();
                    custList.add(name);
                    primaryStage.setScene(addEventScene);
                }

            }
        });

        cancelCust.setOnAction((ActionEvent event) -> {
            nameTxt.clear();
            cityTxt.clear();
            countryTxt.clear();
            addTxt.clear();
            add2Txt.clear();
            postalTxt.clear();
            phoneTxt.clear();
            primaryStage.setScene(addEventScene);
        });

        addCustScene = new Scene(addCustGrid, 400, 500);

        //Edit Customer Scene----------------------------------------------------------------------------------------------
        GridPane editCustGrid = new GridPane();
        editCustGrid.setHgap(25);
        editCustGrid.setVgap(30);

        Label nameLabelEdit = new Label("Name");
        editCustGrid.add(nameLabelEdit, 1, 1);
        Label addLabelEdit = new Label("Address");
        editCustGrid.add(addLabelEdit, 1, 2);
        Label add2LabelEdit = new Label("Address 2");
        editCustGrid.add(add2LabelEdit, 1, 3);
        Label cityLabelEdit = new Label("City");
        editCustGrid.add(cityLabelEdit, 1, 4);
        Label countryLabelEdit = new Label("Country");
        editCustGrid.add(countryLabelEdit, 1, 5);
        Label postalLabelEdit = new Label("Postal Code");
        editCustGrid.add(postalLabelEdit, 1, 6);
        Label phoneLabelEdit = new Label("Phone");
        editCustGrid.add(phoneLabelEdit, 1, 7);

        TextField nameTxtEdit = new TextField();
        editCustGrid.add(nameTxtEdit, 2, 1);
        TextField addTxtEdit = new TextField();
        editCustGrid.add(addTxtEdit, 2, 2);
        TextField add2TxtEdit = new TextField();
        editCustGrid.add(add2TxtEdit, 2, 3);
        TextField cityTxtEdit = new TextField();
        editCustGrid.add(cityTxtEdit, 2, 4);
        TextField countryTxtEdit = new TextField();
        editCustGrid.add(countryTxtEdit, 2, 5);
        TextField postalTxtEdit = new TextField();
        editCustGrid.add(postalTxtEdit, 2, 6);
        TextField phoneTxtEdit = new TextField();
        editCustGrid.add(phoneTxtEdit, 2, 7);

        Button saveCustEdit = new Button("Save");
        editCustGrid.add(saveCustEdit, 2, 8);
        saveCustEdit.setMaxWidth(80);
        Button cancelCustEdit = new Button("Cancel");
        editCustGrid.add(cancelCustEdit, 2, 8);
        cancelCustEdit.setMaxWidth(80);
        cancelCustEdit.setTranslateX(90);

        editCustBtn.setOnAction((ActionEvent event) -> {
            String custName = custBox.getValue().toString();
            Connection conn = null;
            Statement st = null;
            ResultSet rs = null;
            try {
                conn = getConnection();
                st = conn.createStatement();
                rs = st.executeQuery("SELECT customer.customerName, address.address,address.address2,city.city,country.country,address.postalCode,address.phone from customer "
                        + "left outer join address on (customer.addressId = address.addressId)"
                        + "left outer join city on (address.cityId = city.cityId)"
                        + "left outer join country on (city.countryId = country.countryId)"
                        + "where customer.customerName = '" + custName + "'");
                if (rs.next()) {
                    nameTxtEdit.setText(rs.getString("customerName"));
                    addTxtEdit.setText(rs.getString("address"));
                    add2TxtEdit.setText(rs.getString("address2"));
                    cityTxtEdit.setText(rs.getString("city"));
                    countryTxtEdit.setText(rs.getString("country"));
                    postalTxtEdit.setText(rs.getString("postalCode"));
                    phoneTxtEdit.setText(rs.getString("phone"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Soft2.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeST(st);
                closeConn(conn);
                closeRS(rs);
            }
            primaryStage.setScene(editCustScene);

        });

        saveCustEdit.setOnAction((ActionEvent event) -> {
            if (nameTxtEdit.getText().equals("") || addTxtEdit.getText().equals("") || cityTxtEdit.getText().equals("") || countryTxtEdit.getText().equals("")
                    || postalTxtEdit.getText().equals("") || phoneTxtEdit.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                if (lang.equals("Spanish")) {
                    alert.setHeaderText("Falta un campo");
                } else if (lang.equals("English")) {
                    alert.setHeaderText("You are missing a field");
                }

                alert.showAndWait();

            } else if (add2TxtEdit.equals("")) {
                add2TxtEdit.setText("  ");

            } else {

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

                //update customer
                updateQuery("UPDATE country,city,address,customer SET "
                        + "customer.customerName='" + nameTxtEdit.getText() + "',"
                        + "customer.lastUpdate='" + dtf.format(now) + "',"
                        + "customer.lastUpdateBy='" + user + "',"
                        + "address.address='" + addTxtEdit.getText() + "',"
                        + "address.address2='" + add2TxtEdit.getText() + "',"
                        + "address.postalCode='" + postalTxtEdit.getText() + "',"
                        + "address.phone='" + phoneTxtEdit.getText() + "',"
                        + "address.lastUpdate='" + dtf.format(now) + "',"
                        + "address.lastUpdateBy='" + user + "',"
                        + "city.city='" + cityTxtEdit.getText() + "',"
                        + "city.lastUpdate='" + dtf.format(now) + "',"
                        + "city.lastUpdateBy='" + user + "',"
                        + "country.country='" + countryTxtEdit.getText() + "',"
                        + "country.lastUpdate='" + dtf.format(now) + "',"
                        + "country.lastUpdateBy='" + user + "'"
                        + "WHERE country.countryId = city.countryId and "
                        + "city.cityId = address.cityId and "
                        + "address.addressId = customer.addressId and "
                        + "customer.customerName = '" + custBox.getValue().toString() + "'");

                for (int i = 0; i < custList.size(); i++) {
                    String name = custList.get(i);
                    if (name == custBox.getValue().toString()) {
                        custList.remove(i);
                        custList.add(nameTxtEdit.getText());
                    }
                }

            }
            primaryStage.setScene(addEventScene);
        });

        cancelCust.setOnAction((ActionEvent event) -> {
            primaryStage.setScene(addEventScene);
        });

        editCustScene = new Scene(editCustGrid, 400, 500);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                int i = 0;
                /*while(!remList.isEmpty()){
                    
                    RemTask rem = lookupRem(i);
                    if (rem != null) {
                        rem.cancel();
                        i++;
                }
                }*/

                System.exit(0);
            }
        });

        //Reports Scene----------------------------------------------------------------------------------------------
        BorderPane reportPane = new BorderPane();
        reportScene = new Scene(reportPane, 400, 550);
        ToggleGroup radioGroup2 = new ToggleGroup();
        RadioButton typeToggle = new RadioButton("# of appointment types by month");
        typeToggle.setToggleGroup(radioGroup2);
        typeToggle.setSelected(true);
        RadioButton scheduleToggle = new RadioButton("Consultant Schedule");
        scheduleToggle.setToggleGroup(radioGroup2);
        RadioButton numAppToggle = new RadioButton("# Appointments each month");
        numAppToggle.setToggleGroup(radioGroup2);
        VBox radioBtns2 = new VBox();
        radioBtns2.setSpacing(10);
        radioBtns2.setPadding(new Insets(10, 10, 10, 10));
        radioBtns2.setTranslateX(50);
        
        
        report.setMaxWidth(350);
        report.setMaxHeight(350);
        report.setTranslateY(-15);
        report.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        Button exitBtn = new Button("Exit");
        exitBtn.setPadding(new Insets(10,15,10,15));
        exitBtn.setTranslateX(300);
        exitBtn.setTranslateY(-25);
        
        radioGroup2.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
            if (radioGroup.getSelectedToggle() != null) {
                if (typeToggle.isSelected()) {
                    report.getColumns().clear();
                    data.clear();
                    buildReport("SELECT MONTH(start) as month, COUNT(DISTINCT description) as total from appointment GROUP BY MONTH(start) ORDER BY MONTH(start)");
                } else if (scheduleToggle.isSelected()) {
                    report.getColumns().clear();
                    data.clear();
                    buildReport("SELECT createdBy, title, start from appointment where start >= curDate()");
                    
                }
                else if(numAppToggle.isSelected()) {
                    report.getColumns().clear();
                    data.clear();
                    buildReport("SELECT MONTH(start) as month, count(*) as total from appointment GROUP BY MONTH(start)");
                }

            }
        });
        
        exitBtn.setOnAction((ActionEvent event) -> {
            primaryStage.setScene(main);
            if (radioGroup.getSelectedToggle() != null) {
                    if (weekToggle.isSelected()) {
                        buildData("SELECT appointmentId,title, description,location,contact,url,start,end from appointment where start between now() and adddate(now(),7)");
                    } else if (monthToggle.isSelected()) {
                        buildData("SELECT appointmentId,title, description,location,contact,url,start,end from appointment where Year(start) = Year(CURRENT_TIMESTAMP) \n"
                    + " AND Month(start) = Month(CURRENT_TIMESTAMP)");
                    }

                }
        });
        
        radioBtns2.getChildren().addAll(typeToggle, scheduleToggle, numAppToggle);
        reportPane.setTop(radioBtns2);
        reportPane.setCenter(report);
        reportPane.setBottom(exitBtn);

    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }

    /**
     *
     * @return
     */
    public Connection getConnection() {
        Connection conn = null;
        String driver = "com.mysql.jdbc.Driver";
        String db = "U04aeq";
        String url = "jdbc:mysql://52.206.157.109/" + db;
        String user = "U04aeq";
        String pass = "53688186091";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);
            return conn;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Soft2.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            if (lang.equals("Spanish")) {
                alert.setHeaderText("No está conectado a la base de datos");
            } else if (lang.equals("English")) {
                alert.setHeaderText("You are not connected to the Database");
            }

            alert.showAndWait();
            return null;
        }
    }

    private boolean isValidCreds() {
        boolean letIn = false;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * from user WHERE userName = " + "'" + userTxt.getText() + "'" + "and password = " + "'" + pwTxt.getText() + "'");
            while (rs.next()) {
                if (rs.getString("userName") != null && rs.getString("password") != null) {
                    letIn = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Soft2.class.getName()).log(Level.SEVERE, null, ex);
            letIn = false;
        } finally {
            closeST(st);
            closeConn(con);
            closeRS(rs);

        }
        return letIn;
    }

    private int getLastId(String table) {
        int id = 0;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT " + table + "Id from " + table);
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Soft2.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeST(st);
            closeConn(conn);
            closeRS(rs);

        }
        return id + 1;
    }

    private void updateQuery(String query) {
        Connection conn = null;
        Statement st = null;
        try {
            conn = getConnection();
            st = conn.createStatement();
            int result = st.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(Soft2.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeST(st);
            closeConn(conn);

        }
    }

    private static void closeRS(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
        }
    }

    private static void closeST(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
        }
    }

    private static void closeConn(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
        }
    }

    public void fillCombo() {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT customerName from customer");

            while (rs.next()) {
                custList.add(rs.getString("customerName"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Soft2.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConn(conn);
            closeST(st);
            closeRS(rs);
        }
    }

    public int getId(String name) {
        int id = 0;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT customerId from customer WHERE customerName = '" + name + "'");
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Soft2.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeST(st);
            closeConn(conn);
            closeRS(rs);

        }
        return id;
    }

    public void buildData(String query) {
        Connection c = null;
        ResultSet rs = null;
        data = FXCollections.observableArrayList();
        try {
            c = getConnection();
            String SQL = query;
            rs = c.createStatement().executeQuery(SQL);

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if (i == 7) {
                        String s = rs.getString(7);
                        String sDate = s.substring(0, s.length() - 5);
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        LocalDateTime ldtStart = LocalDateTime.parse(sDate, format);
                        ZonedDateTime zdtStart = ldtStart.atZone(ZoneId.of("UTC"));
                        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of(loc));
                        ldtStart = utcStart.toLocalDateTime();
                        Timestamp startsqlts = Timestamp.valueOf(ldtStart);
                        row.add(startsqlts.toString());
                    } else if (i == 8) {
                        String s = rs.getString(8);
                        String eDate = s.substring(0, s.length() - 5);
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        LocalDateTime ldtEnd = LocalDateTime.parse(eDate, format);
                        ZonedDateTime zdtEnd = ldtEnd.atZone(ZoneId.of("UTC"));
                        ZonedDateTime utcEnd = zdtEnd.withZoneSameInstant(ZoneId.of(loc));
                        ldtEnd = utcEnd.toLocalDateTime();
                        Timestamp endsqlts = Timestamp.valueOf(ldtEnd);
                        row.add(endsqlts.toString());
                    } else {
                        row.add(rs.getString(i));
                    }
                }
                data.add(row);
            }
            cal.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        } finally {
            closeConn(c);
            closeRS(rs);
        }

    }

    public void buildTable() {
        Connection c = null;
        ResultSet rs = null;
        try {
            c = getConnection();
            String SQL = "SELECT appointmentId,title, description,location,contact,url,start,end from appointment";
            rs = c.createStatement().executeQuery(SQL);
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                cal.getColumns().addAll(col);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        } finally {
            closeConn(c);
            closeRS(rs);
        }

    }
    public void setRem(){
        Connection c = null;
        ResultSet rs = null;
        data = FXCollections.observableArrayList();
        try {
            c = getConnection();
            String SQL = "SELECT reminder.reminderDate, appointment.title, appointment.start,appointment.end, reminder.reminderId from reminder left outer join appointment on appointment.appointmentId = reminder.appointmentId";
            rs = c.createStatement().executeQuery(SQL);

            while (rs.next()) {
                Timestamp remsqlts = null;
                Timestamp startsqlts = null;
                Timestamp endsqlts = null;
                String title = null;
                int id = 0;
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if(i == 1)
                            {
                                String s = rs.getString(1);
                                String sDate = s.substring(0, s.length() - 5);
                                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                LocalDateTime ldtRem = LocalDateTime.parse(sDate, format);
                                ZonedDateTime zdtRem = ldtRem.atZone(ZoneId.of("UTC"));
                                ZonedDateTime utcRem = zdtRem.withZoneSameInstant(ZoneId.of(loc));
                                ldtRem = utcRem.toLocalDateTime();
                                remsqlts = Timestamp.valueOf(ldtRem);
                             
                            }
                    else if(i == 3)
                            {
                                String s = rs.getString(3);
                                String sDate = s.substring(0, s.length() - 5);
                                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                LocalDateTime ldtStart = LocalDateTime.parse(sDate, format);
                                ZonedDateTime zdtStart = ldtStart.atZone(ZoneId.of("UTC"));
                                ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of(loc));
                                ldtStart = utcStart.toLocalDateTime();
                                startsqlts = Timestamp.valueOf(ldtStart);
                               
                            }
                    else if(i == 4)
                            {
                                String s = rs.getString(4);
                                String eDate = s.substring(0, s.length() - 5);
                                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                LocalDateTime ldtEnd = LocalDateTime.parse(eDate, format);
                                ZonedDateTime zdtEnd = ldtEnd.atZone(ZoneId.of("UTC"));
                                ZonedDateTime utcEnd = zdtEnd.withZoneSameInstant(ZoneId.of(loc));
                                ldtEnd = utcEnd.toLocalDateTime();
                                endsqlts = Timestamp.valueOf(ldtEnd);        
                                
                            }
                    else if(i == 5){
                            id = Integer.parseInt(rs.getString(5));
                            
                    }
                    else{
                            title = rs.getString(i);
                           
                    }
                }
                LocalDateTime localtDateAndTime = LocalDateTime.now();
                ZonedDateTime utcCur = localtDateAndTime.atZone(ZoneId.systemDefault());
                ZonedDateTime newCur = utcCur.withZoneSameInstant(ZoneId.of(loc));
                localtDateAndTime = newCur.toLocalDateTime();
                Timestamp curTime = Timestamp.valueOf(localtDateAndTime);
                if (curTime.getTime() < startsqlts.getTime()) {
                    
                    Timer timer = new Timer();
                    RemTask rem = new RemTask(title, startsqlts, endsqlts, id);
                    timer.schedule(rem, remsqlts);
                    remList.add(rem);
                }
                if(curTime.getTime() > startsqlts.getTime()){
                    updateQuery("DELETE FROM reminder WHERE reminderId = " + id);
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        } finally {
            closeConn(c);
            closeRS(rs);
        }
    }
        public static RemTask lookupRem(int id){
        for(int i = 0; i < remList.size(); i++)
       {
           RemTask rem = remList.get(i);
           int prodId = rem.getId();
           if(prodId == id)
           {
               return rem;    
           } 
       }
        return null;
        
    }
        public void buildReport(String query) {
        Connection c = null;
        ResultSet rs = null;
        reportData = FXCollections.observableArrayList();
        try {
            c = getConnection();
            String SQL = query;
            rs = c.createStatement().executeQuery(SQL);
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                report.getColumns().addAll(col);
            }
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if (i == 3) {
                        String s = rs.getString(3);
                        String sDate = s.substring(0, s.length() - 5);
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        LocalDateTime ldtStart = LocalDateTime.parse(sDate, format);
                        ZonedDateTime zdtStart = ldtStart.atZone(ZoneId.of("UTC"));
                        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of(loc));
                        ldtStart = utcStart.toLocalDateTime();
                        Timestamp startsqlts = Timestamp.valueOf(ldtStart);
                        row.add(startsqlts.toString());
                    } else {
                        row.add(rs.getString(i));
                    }
                }
                reportData.add(row);
            }
            report.setItems(reportData);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        } finally {
            closeConn(c);
            closeRS(rs);
        }

    }
}
