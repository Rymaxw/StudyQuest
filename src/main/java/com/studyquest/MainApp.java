package com.studyquest;

import java.io.IOException;
import java.nio.file.Path;
import com.studyquest.system.FileDataRepository;
import com.studyquest.system.GameController;
import com.studyquest.ui.Router;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        GameController controller = new GameController(new FileDataRepository(
                Path.of(System.getProperty("user.home"), ".studyquest", "saves")));
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(java.util.Objects.requireNonNull(
                MainApp.class.getResource("/styles/theme.css")).toExternalForm());
        Router router = new Router(controller, stage, root);
        stage.setScene(scene);
        stage.setTitle("StudyQuest");
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            boolean accountOperationInProgress = root.lookupAll(".paper").stream()
                    .anyMatch(javafx.scene.Node::isDisabled);
            if (accountOperationInProgress) {
                event.consume();
                return;
            }
            try {
                controller.saveNow();
            } catch (IOException exception) {
                event.consume();
                router.inform("Gagal menyimpan", "Progres belum tersimpan. Jendela tetap terbuka; coba lagi.");
            }
        });
        router.show("S01");
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
