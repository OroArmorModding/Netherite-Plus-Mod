package com.oroarmor.netherite_plus.advancement.criterion;

import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class RiptideNetheriteTridentCriterion extends SimpleCriterionTrigger<RiptideNetheriteTridentCriterion.Conditions> {
    public static final ResourceLocation id = id("riptide_netherite_trident");

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RiptideNetheriteTridentCriterion.Conditions createInstance(JsonObject jsonObject, EntityPredicate.Composite extended, DeserializationContext advancementEntityPredicateDeserializer) {
        return new RiptideNetheriteTridentCriterion.Conditions(extended);
    }

    public void trigger(ServerPlayer player) {
        trigger(player, (conditions) -> {
            return conditions.matches(player);
        });
    }

    public static class Conditions extends AbstractCriterionTriggerInstance {

        public Conditions(EntityPredicate.Composite player) {
            super(id, player);
        }

        public boolean matches(ServerPlayer player) {
            return true;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext predicateSerializer) {
            JsonObject jsonObject = super.serializeToJson(predicateSerializer);
            return jsonObject;
        }

    }
}
