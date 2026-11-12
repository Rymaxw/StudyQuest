package com.studyquest.ui;

import com.studyquest.system.GameController;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public final class RegisterPanel implements Panel {
    private final TextField username = new TextField();
    private final PasswordField password = new PasswordField();
    private final PasswordField confirm = new PasswordField();
    private final Label status = new Label();
    private final VBox form = UiSupport.form();
    private final StackPane view = new StackPane(form);

    public RegisterPanel(GameController controller, Router router) {
        username.setId("register-username");
        password.setId("register-password");
        confirm.setId("register-confirm");
        status.setId("register-status");
        status.setWrapText(true);
        form.getChildren().addAll(UiSupport.title("Daftar akun"),
                new Label("Username: 3–20 huruf, angka, atau _. Password: minimal 6 karakter."),
                UiSupport.field("Username", username), UiSupport.field("Password", password),
                UiSupport.field("Konfirmasi password", confirm), status,
                UiSupport.button("Buat akun", "register-submit", () -> {
                    if (!password.getText().equals(confirm.getText())) {
                        status.setText("Konfirmasi password belum sama.");
                        return;
                    }
                    String enteredUsername = username.getText();
                    String enteredPassword = password.getText();
                    UiSupport.run(form, status, () -> controller.register(enteredUsername, enteredPassword), () -> {
                        UiSupport.clearPasswords(form);
                        router.show("S03");
                    });
                }), UiSupport.button("Kembali ke login", "register-back", () -> router.show("S01")));
        form.getChildren().stream().filter(Label.class::isInstance).map(Label.class::cast)
                .forEach(label -> label.setWrapText(true));
    }
    @Override
    public Parent getView() {
        return view;
    }

    @Override
    public void refresh() {
        UiSupport.clearPasswords(form);
        status.setText("");
        UiSupport.focus(username);
    }
}
