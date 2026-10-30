package com.studyquest.character;

import com.studyquest.battle.Monster;
import java.io.Serializable;

public interface Skill extends Serializable {
    String getName();
    int getMpCost();
    int use(Character user, Monster target);
}
