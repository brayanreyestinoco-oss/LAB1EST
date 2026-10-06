module cr.ac.una.est.lab1est {

    requires javafx.controls;
    requires javafx.fxml;

    opens cr.ac.una.est.lab1est.controller to javafx.fxml;

    exports cr.ac.una.est.lab1est;
    exports cr.ac.una.est.lab1est.controller;
    exports cr.ac.una.est.lab1est.model;
}