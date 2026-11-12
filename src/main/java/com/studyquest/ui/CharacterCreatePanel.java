package com.studyquest.ui;

import com.studyquest.character.Archer;
import com.studyquest.character.Character;
import com.studyquest.character.Mage;
import com.studyquest.character.Warrior;
import com.studyquest.system.GameController;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public final class CharacterCreatePanel implements Panel {
    private final TextField name = new TextField();
    private final Label status = new Label();
    private final Label preview = new Label();
    private final ToggleGroup classes = new ToggleGroup();
    private final VBox form = UiSupport.form();
    private final StackPane view = new StackPane(form);

    public CharacterCreatePanel(GameController controller, Router router) {
        name.setId("character-name");
        status.setId("character-status");
        preview.setId("character-preview");
        form.setMaxWidth(900);
        HBox choices = new HBox(24);
        for (String job : new String[]{"Warrior", "Mage", "Archer"}) {
            RadioButton choice = new RadioButton(job);
            choice.setId("class-" + job);
            choice.setUserData(job);
            choice.setToggleGroup(classes);
            choices.getChildren().add(choice);
        }
        classes.selectedToggleProperty().addListener((observable, old, selected) -> updatePreview());
        form.getChildren().addAll(UiSupport.title("Pilih teman petualanganmu"), choices, preview,
                UiSupport.field("Nama karakter (2–20 karakter)", name), status,
                UiSupport.button("Mulai petualangan", "character-submit", () -> {
                    if (classes.getSelectedToggle() == null) {
                        status.setText("Pilih class karakter.");
                        return;
                    }
                    String job = classes.getSelectedToggle().getUserData().toString();
                    String characterName = name.getText();
                    UiSupport.run(form, status, () -> controller.createCharacter(job, characterName), () -> router.show("S04"));
                }), UiSupport.button("Keluar akun", "character-logout", () -> {
                    UiSupport.run(form, status, controller::logout, () -> router.show("S01"));
                }));
        preview.setWrapText(true);
        status.setWrapText(true);
        classes.getToggles().getFirst().setSelected(true);
    }

    private void updatePreview() {
        if (classes.getSelectedToggle() == null) {
            return;
        }
        Character character = switch (classes.getSelectedToggle().getUserData().toString()) {
            case "Mage" -> new Mage("Pratinjau");
            case "Archer" -> new Archer("Pratinjau");
            default -> new Warrior("Pratinjau");
        };
        var skill = character.getSkills().getFirst();
        preview.setText("HP " + character.getHp() + "   ATK " + character.getAttack() + "   INT " + character.getIntelligence()
                + "   MP " + character.getMp() + "\n" + skill.getName() + ": " + skill.getMpCost() + " MP, pengali " + skill.getMultiplier());
    }
    @Override
    public Parent getView() {
        return view;
    }

    @Override
    public void refresh() {
        name.clear();
        status.setText("");
        UiSupport.focus(name);
    }
}
