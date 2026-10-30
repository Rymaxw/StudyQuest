package com.studyquest.item;

import com.studyquest.character.Character;

public interface Usable {
    boolean canUse(Character target);
    boolean use(Character target);
}
