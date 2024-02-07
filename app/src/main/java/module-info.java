module agh.edu.pl.thumbnail.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    requires java.desktop;
    requires com.google.gson;
    requires com.hivemq.client.mqtt;
    requires io.reactivex.rxjava2;
    opens agh.edu.pl.thumbnail.app.services.requests to com.google.gson;
    opens agh.edu.pl.thumbnail.app.dtos to com.google.gson;
    opens agh.edu.pl.thumbnail.app.enums to com.google.gson;
    opens agh.edu.pl.thumbnail.app.models to com.google.gson;

    opens agh.edu.pl.thumbnail.app to javafx.fxml;
    exports agh.edu.pl.thumbnail.app;
    exports agh.edu.pl.thumbnail.app.controllers;
    opens agh.edu.pl.thumbnail.app.controllers to javafx.fxml;
}