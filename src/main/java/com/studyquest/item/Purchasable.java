package com.studyquest.item;

import com.studyquest.character.Character;

public interface Purchasable {
    int getPrice();
    boolean canBePurchasedBy(Character c);
}
