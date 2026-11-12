package com.studyquest.ui;

import com.studyquest.system.GameController;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public final class ProfilePanel implements Panel {
    private final GameController controller;
    private final Label detail = new Label();
    private final VBox form = UiSupport.form();
    private final StackPane view = new StackPane(form);

    public ProfilePanel(GameController controller, Router router) {
        this.controller = controller;
        detail.setId("profile-detail");
        detail.setWrapText(true);
        form.getChildren().addAll(UiSupport.title("Profil & skill"), detail,
                UiSupport.button("Kembali ke markas", "profile-back", router::backToHub));
    }
    @Override
    public Parent getView() {
        return view;
    }

    @Override
    public void refresh() {
        var character = controller.getState().getUser().getCharacter();
        var skill = character.getSkills().getFirst();
        detail.setText(character.getName() + " • " + character.getClassName() + "\nLevel " + character.getLevel()
                + "\nHP " + character.getHp() + "/" + character.getMaxHp()
                + "   MP " + character.getMp() + "/" + character.getMaxMp()
                + "\nATK " + character.getAttack() + "   INT " + character.getIntelligence()
                + "\nXP " + character.getXp() + "/" + character.xpToNextLevel()
                + "\n" + skill.getName() + " • " + skill.getMpCost() + " MP • pengali " + skill.getMultiplier());
    }
}
