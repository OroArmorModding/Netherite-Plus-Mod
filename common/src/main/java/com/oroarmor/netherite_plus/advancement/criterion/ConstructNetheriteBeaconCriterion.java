package com.oroarmor.netherite_plus.advancement.criterion;

import com.google.gson.JsonObject;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class ConstructNetheriteBeaconCriterion extends AbstractCriterion<ConstructNetheriteBeaconCriterion.Conditions> {
    private static final Identifier ID = id("construct_netherite_beacon");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public ConstructNetheriteBeaconCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        NumberRange.IntRange intRange = NumberRange.IntRange.fromJson(jsonObject.get("level"));
        return new ConstructNetheriteBeaconCriterion.Conditions(extended, intRange);
    }

    public void trigger(ServerPlayerEntity player, NetheriteBeaconBlockEntity beacon) {
        test(player, (conditions) -> {
            return conditions.matches(beacon);
        });
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final NumberRange.IntRange level;

        public Conditions(EntityPredicate.Extended player, NumberRange.IntRange level) {
            super(ConstructNetheriteBeaconCriterion.ID, player);
            this.level = level;
        }

        public static ConstructNetheriteBeaconCriterion.Conditions level(NumberRange.IntRange level) {
            return new ConstructNetheriteBeaconCriterion.Conditions(EntityPredicate.Extended.EMPTY, level);
        }

        public boolean matches(NetheriteBeaconBlockEntity beacon) {
            return level.test(beacon.getBeaconLevel());
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("level", level.toJson());
            return jsonObject;
        }
    }
}
