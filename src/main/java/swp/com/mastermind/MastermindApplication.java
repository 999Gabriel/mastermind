package swp.com.mastermind;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class MastermindApplication extends Application {

    private static final Logger LOGGER = Logger.getLogger(MastermindApplication.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("mastermind.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to create log file", e);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        LOGGER.info("Starting Mastermind application");
        FXMLLoader fxmlLoader = new FXMLLoader(MastermindApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // Add the stylesheet
        scene.getStylesheets().add(MastermindApplication.class.getResource("modern-styles.css").toExternalForm());

        stage.setTitle("Mastermind");
        stage.setScene(scene);
        stage.show();
        LOGGER.info("Mastermind UI initialized and displayed");
    }

    public static void main(String[] args) {
        launch();
    }
}