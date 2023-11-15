package me.faln.chaoticenchants.rarity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.lucko.helper.item.ItemStackBuilder;

import java.util.List;

@AllArgsConstructor @Getter @Builder
public final class Rarity {

    private final String id;
    private final String displayName;
    private final int cost;
    private final double incinerateFactor;
    private final String color;
    private final ItemStackBuilder identifiedItem;
    private final String identifiedName;
    private final List<String> identifiedLore;
    private final ItemStackBuilder unidentifiedItem;
    private final List<String> unidentifiedLore;

}
