package com.studyquest.item;

import com.studyquest.character.Character;

public interface Usable {
    boolean use(Character target);
    boolean canUse(Character target);
}
