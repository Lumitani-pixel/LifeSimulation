module net.normalv.lifesimulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires Logger;

    opens net.normalv.lifesimulation to javafx.fxml;
    exports net.normalv.lifesimulation;
    exports net.normalv.lifesimulation.controller;
    opens net.normalv.lifesimulation.controller to javafx.fxml;
    exports net.normalv.lifesimulation.world;
    opens net.normalv.lifesimulation.world to javafx.fxml;
    exports net.normalv.lifesimulation.world.water;
    opens net.normalv.lifesimulation.world.water to javafx.fxml;
}