package com.studyquest.ui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

final class UiSupport {
    private UiSupport() {}

    static Label title(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("title");
        label.setWrapText(true);
        return label;
    }

    static VBox field(String text, TextField input) {
        Label label = new Label(text);
        label.setLabelFor(input);
        input.setAccessibleText(text);
        input.setMaxWidth(Double.MAX_VALUE);
        return new VBox(6, label, input);
    }

    static VBox form() {
        VBox form = new VBox(14);
        form.setMaxWidth(480);
        form.getStyleClass().add("paper");
        return form;
    }

    static Button button(String text, String id, Runnable action) {
        Button button = new Button(text);
        button.setId(id);
        button.setOnAction(event -> action.run());
        return button;
    }

    static void run(VBox form, Label status, Runnable operation, Runnable success) {
        form.setDisable(true);
        status.setText("Memproses...");
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                operation.run();
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            form.setDisable(false);
            status.setText("");
            success.run();
        });
        task.setOnFailed(event -> {
            form.setDisable(false);
            status.setText(task.getException().getMessage());
        });
        Thread thread = new Thread(task, "studyquest-account");
        thread.setDaemon(true);
        thread.start();
    }

    static void clearPasswords(VBox form) {
        form.lookupAll(".password-field").forEach(node -> ((PasswordField) node).clear());
    }

    static void focus(TextField field) {
        Platform.runLater(field::requestFocus);
    }
}
