module swp.com.mastermind {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens swp.com.mastermind to javafx.fxml;
    exports swp.com.mastermind;
}