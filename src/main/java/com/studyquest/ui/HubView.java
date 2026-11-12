package com.studyquest.ui;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import com.studyquest.system.GameController;
import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public final class HubView implements Panel {
    private static final double SPEED = 190;
    private static final double PLAYER_SIZE = 32;
    private static final double INTERACTION_RADIUS = 140;
    private final GameController controller;
    private final Router router;
    private final Stage stage;
    private final VBox view = new VBox(12);
    private final Pane room = new Pane();
    private final Label hud = new Label();
    private final Label player = new Label("P");
    private final Label interaction = new Label("Dekati objek lalu tekan E, atau klik objek langsung.");
    private final Set<KeyCode> keys = new HashSet<>();
    private boolean active;
    private long previousFrame;
    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (previousFrame != 0) {
                double elapsedSeconds = (now - previousFrame) / 1_000_000_000.0;
                step(Math.min(elapsedSeconds, 0.05));
            }
            previousFrame = now;
        }
    };

    public HubView(GameController controller, Router router, Stage stage) {
        this.controller = controller;
        this.router = router;
        this.stage = stage;
        view.getStyleClass().add("hub");
        hud.setId("hub-hud");
        Button profile = UiSupport.button("Profil & skill", "hub-profile", () -> router.show("PROFILE"));
        Button save = UiSupport.button("Simpan", "hub-save", this::saveProgress);
        Button logout = UiSupport.button("Keluar akun", "hub-logout", this::logout);
        room.setId("hub-room");
        room.getStyleClass().add("room");
        room.setMinSize(1200, 480);
        room.setPrefSize(1200, 480);
        room.setMaxSize(1200, 480);
        room.setFocusTraversable(true);
        room.setAccessibleText("Markas. WASD atau panah untuk bergerak, E untuk interaksi.");
        room.setOnMousePressed(event -> {
            if (event.getTarget() == room) {
                room.requestFocus();
            }
        });
        addRoomObject("Papan Quest", "S05", 100, 65);
        addRoomObject("Peti", "S08", 500, 65);
        addRoomObject("Pedagang", "S09", 900, 65);
        addRoomObject("Gerbang Battle", "S10", 100, 330);
        addRoomObject("Rak Prestasi", "S11", 500, 330);
        addRoomObject("Meja Belajar", "S12", 900, 330);
        player.setId("hub-player");
        player.getStyleClass().add("player");
        player.setMinSize(PLAYER_SIZE, PLAYER_SIZE);
        player.setPrefSize(PLAYER_SIZE, PLAYER_SIZE);
        player.setMaxSize(PLAYER_SIZE, PLAYER_SIZE);
        player.setMouseTransparent(true);
        player.relocate(580, 220);
        room.getChildren().add(player);
        view.getChildren().addAll(UiSupport.title("Markas StudyQuest"), new HBox(22, hud, profile, save, logout), room,
                interaction,
                new Label("WASD / panah: berjalan • E: interaksi • Tab lalu Enter: akses objek tanpa berjalan"),
                new Label("Draf visual: P dan objek berlabel adalah placeholder, bukan aset pixel final. Panel S05–S12 belum diintegrasikan."));
        view.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        view.addEventFilter(KeyEvent.KEY_RELEASED, event -> keys.remove(event.getCode()));
        stage.focusedProperty().addListener((observable, wasFocused, focused) -> {
            if (!focused) {
                clearInput();
            }
        });
    }

    private void saveProgress() {
        clearInput();
        try {
            controller.saveNow();
            router.inform("Tersimpan", "Progres berhasil disimpan.");
        } catch (IOException exception) {
            router.inform("Gagal menyimpan", "Progres belum tersimpan. Coba lagi.");
        }
    }

    private void logout() {
        clearInput();
        try {
            controller.logout();
            router.show("S01");
        } catch (RuntimeException exception) {
            router.inform("Gagal keluar", exception.getMessage());
        }
    }

    private void handleKeyPressed(KeyEvent event) {
        if (!canMove()) {
            clearInput();
            return;
        }
        if (event.getCode() == KeyCode.E) {
            clearInput();
            Button nearestObject = findNearestObject();
            if (nearestObject != null) {
                openObject(nearestObject);
            }
            event.consume();
        } else if (isMovement(event.getCode())) {
            keys.add(event.getCode());
            event.consume();
        }
    }

    private void addRoomObject(String name, String destination, double x, double y) {
        Button button = UiSupport.button(name, "object-" + destination, () -> router.show(destination));
        button.setUserData(destination);
        button.relocate(x, y);
        button.setPrefSize(190, 70);
        button.getStyleClass().add("room-object");
        room.getChildren().add(button);
    }

    private void openObject(Button button) {
        clearInput();
        router.show(button.getUserData().toString());
    }

    private Button findNearestObject() {
        Button nearestObject = null;
        double shortestDistance = INTERACTION_RADIUS;
        for (Node node : room.getChildren()) {
            if (!(node instanceof Button button)) {
                continue;
            }
            Bounds bounds = node.getBoundsInParent();
            double distance = Math.hypot(player.getLayoutX() + PLAYER_SIZE / 2 - bounds.getCenterX(),
                    player.getLayoutY() + PLAYER_SIZE / 2 - bounds.getCenterY());
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearestObject = button;
            }
        }
        return nearestObject;
    }

    private boolean canMove() {
        if (!active || !stage.isFocused() || view.getScene() == null) {
            return false;
        }
        Node focusedNode = view.getScene().getFocusOwner();
        return !(focusedNode instanceof TextInputControl) && (focusedNode == room || focusedNode == null);
    }

    private static boolean isMovement(KeyCode key) {
        return switch (key) {
            case W, A, S, D, UP, LEFT, DOWN, RIGHT -> true;
            default -> false;
        };
    }

    private void step(double seconds) {
        if (!canMove()) {
            clearInput();
            return;
        }
        double horizontalDirection = (keys.contains(KeyCode.D) || keys.contains(KeyCode.RIGHT) ? 1 : 0)
                - (keys.contains(KeyCode.A) || keys.contains(KeyCode.LEFT) ? 1 : 0);
        double verticalDirection = (keys.contains(KeyCode.S) || keys.contains(KeyCode.DOWN) ? 1 : 0)
                - (keys.contains(KeyCode.W) || keys.contains(KeyCode.UP) ? 1 : 0);
        // Gerak empat arah, bukan diagonal yang lebih cepat.
        if (horizontalDirection != 0) {
            verticalDirection = 0;
        }
        move(horizontalDirection * SPEED * seconds, 0);
        move(0, verticalDirection * SPEED * seconds);
        Button nearestObject = findNearestObject();
        interaction.setText(nearestObject != null ? "E: buka " + nearestObject.getText()
                : "Dekati objek lalu tekan E, atau klik objek langsung.");
    }

    private void move(double horizontalDistance, double verticalDistance) {
        double previousX = player.getLayoutX();
        double previousY = player.getLayoutY();
        double nextX = Math.max(0, Math.min(room.getWidth() - PLAYER_SIZE, previousX + horizontalDistance));
        double nextY = Math.max(0, Math.min(room.getHeight() - PLAYER_SIZE, previousY + verticalDistance));
        player.relocate(nextX, nextY);
        for (Node node : room.getChildren()) {
            if (node instanceof Button && node.getBoundsInParent().intersects(player.getBoundsInParent())) {
                player.relocate(previousX, previousY);
                break;
            }
        }
    }

    public void clearInput() {
        keys.clear();
        previousFrame = 0;
    }

    public void setActive(boolean active) {
        this.active = active;
        clearInput();
        if (active) {
            timer.start();
            javafx.application.Platform.runLater(room::requestFocus);
        } else {
            timer.stop();
        }
    }

    @Override
    public Parent getView() {
        return view;
    }

    @Override
    public void refresh() {
        var character = controller.getState().getUser().getCharacter();
        hud.setText(character.getName() + " • Lv " + character.getLevel()
                + "\nHP " + character.getHp() + "/" + character.getMaxHp()
                + "   MP " + character.getMp() + "/" + character.getMaxMp()
                + "   XP " + character.getXp() + "/" + character.xpToNextLevel()
                + "   Gold " + character.getInventory().getGold());
    }
}
