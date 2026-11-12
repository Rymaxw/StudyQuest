package com.studyquest.ui;

import com.studyquest.system.GameController;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public final class LoginPanel implements Panel {
    private final TextField username = new TextField();
    private final PasswordField password = new PasswordField();
    private final Label status = new Label();
    private final VBox form = UiSupport.form();
    private final StackPane view = new StackPane(form);

    public LoginPanel(GameController controller, Router router) {
        username.setId("login-username");
        password.setId("login-password");
        status.setId("login-status");
        status.setWrapText(true);
        form.getChildren().addAll(UiSupport.title("StudyQuest"), new Label("Masuk ke jurnal petualanganmu."),
                UiSupport.field("Username", username), UiSupport.field("Password", password), status,
                UiSupport.button("Masuk", "login-submit", () -> {
                    String enteredUsername = username.getText();
                    String enteredPassword = password.getText();
                    UiSupport.run(form, status, () -> controller.login(enteredUsername, enteredPassword), () -> {
                        password.clear();
                        router.show(controller.getState().getUser().getCharacter() == null ? "S03" : "S04");
                        if (controller.getState().isRecoveredFromBackup()) {
                            router.inform("Data dipulihkan", "Data utama tidak dapat dibaca. Cadangan berhasil dimuat.");
                        }
                    });
                }),
                UiSupport.button("Daftar akun", "login-register", () -> router.show("S02")));
        view.setAlignment(Pos.CENTER);
        password.setOnAction(event -> ((javafx.scene.control.Button) form.lookup("#login-submit")).fire());
    }

    @Override
    public Parent getView() {
        return view;
    }

    @Override
    public void refresh() {
        password.clear();
        status.setText("");
        UiSupport.focus(username);
    }
}
