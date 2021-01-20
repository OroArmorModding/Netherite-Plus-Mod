package com.oroarmor.netherite_plus.advancement.criterion;

import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class RiptideNetheriteTridentCriterion extends AbstractCriterion<RiptideNetheriteTridentCriterion.Conditions> {
    public static final Identifier id = id("riptide_netherite_trident");

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RiptideNetheriteTridentCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        return new RiptideNetheriteTridentCriterion.Conditions(extended);
    }

    public void trigger(ServerPlayerEntity player) {
        test(player, (conditions) -> {
            return conditions.matches(player);
        });
    }

    public static class Conditions extends AbstractCriterionConditions {

        public Conditions(EntityPredicate.Extended player) {
            super(id, player);
        }

        public boolean matches(ServerPlayerEntity player) {
            return true;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            return jsonObject;
        }

    }
}
