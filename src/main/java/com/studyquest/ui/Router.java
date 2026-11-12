package com.studyquest.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.studyquest.system.GameController;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class Router {
    private final GameController controller;
    private final Stage stage;
    private final StackPane root;
    private final Map<String, Panel> panels = new HashMap<>();
    private String currentScreen;
    private final HubView hub;

    public Router(GameController controller, Stage stage, StackPane root) {
        this.controller = Objects.requireNonNull(controller);
        this.stage = Objects.requireNonNull(stage);
        this.root = Objects.requireNonNull(root);
        panels.put("S01", new LoginPanel(controller, this));
        panels.put("S02", new RegisterPanel(controller, this));
        panels.put("S03", new CharacterCreatePanel(controller, this));
        hub = new HubView(controller, this, stage);
        panels.put("S04", hub);
        panels.put("PROFILE", new ProfilePanel(controller, this));
    }

    public void registerPanel(String screenId, Panel panel) {
        panels.put(screenId, Objects.requireNonNull(panel));
    }

    /** Menjaga sesi dan karakter; layar belum diintegrasikan menampilkan pemberitahuan jujur. */
    public void show(String screenId) {
        String targetScreen = resolveAccessibleScreen(screenId);
        Panel panel = panels.get(targetScreen);
        if (panel == null) {
            inform("Panel belum tersedia", screenId + " belum diintegrasikan oleh pemilik modul.");
            return;
        }
        hub.setActive(false);
        root.getChildren().setAll(panel.getView());
        currentScreen = targetScreen;
        panel.refresh();
        hub.setActive(targetScreen.equals("S04"));
    }

    private String resolveAccessibleScreen(String screenId) {
        if (screenId.equals("S01") || screenId.equals("S02")) {
            return screenId;
        }
        if (controller.getState() == null) {
            return "S01";
        }
        if (controller.getState().getUser().getCharacter() == null) {
            return "S03";
        }
        return screenId;
    }

    /** Kembali mengaktifkan input markas setelah panel dilepas. */
    public void backToHub() {
        show("S04");
    }

    public void inform(String title, String message) {
        hub.setActive(false);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        try {
            alert.showAndWait();
        } finally {
            hub.setActive("S04".equals(currentScreen));
        }
    }

    public String getCurrentScreen() {
        return currentScreen;
    }
}
