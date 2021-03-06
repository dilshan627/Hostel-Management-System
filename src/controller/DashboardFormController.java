package controller;

import bo.BOFactory;
import bo.BOType;
import bo.custom.impl.DashboardBoImpl;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

public class DashboardFormController {
    public AnchorPane context;
    public AnchorPane slideContext;
    public Label lblTime;
    public Label lblDate;
    public Label lblStudent;
    public Label lblRegister;

    private final DashboardBoImpl dashboardBo = BOFactory.getInstance().getBO(BOType.DASHBOARD);

    public void initialize() {
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(currentTime.getHour() + ":" +
                    currentTime.getMinute() + ":" +
                    currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        try {
            String s = dashboardBo.allCount();
            String s1 = dashboardBo.allRegCount();
            lblStudent.setText(s);
            lblRegister.setText(s1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void homeOnAction(ActionEvent actionEvent) throws IOException {
        util.navigation.navigate(context, "dashboard");
    }

    public void studentOnAction(ActionEvent actionEvent) throws IOException {
        util.navigation.navigate(slideContext, "student");
    }

    public void roomOnAction(ActionEvent actionEvent) throws IOException {
        util.navigation.navigate(slideContext, "room");
    }

    public void logoutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)) {
            util.navigation.navigate(context, "login");
        }
    }

    public void accountOnAction(ActionEvent actionEvent) throws IOException {
        util.navigation.navigate(slideContext, "account");
    }

    public void registrationOnAction(ActionEvent actionEvent) throws IOException {
        util.navigation.navigate(slideContext, "registration");
    }

    public void detailsOnAction(ActionEvent actionEvent) throws IOException {
        util.navigation.navigate(slideContext, "details");
    }
}
