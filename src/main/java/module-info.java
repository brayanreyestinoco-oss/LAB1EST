module cr.ac.una.est.lab1est {
    requires javafx.controls;
    requires javafx.fxml;


    opens cr.ac.una.est.lab1est to javafx.fxml;
    exports cr.ac.una.est.lab1est;
}