package com.example.explomod.advancement;

import com.example.explomod.ExploMod;
import com.example.explomod.registry.AtheriaRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class StatTrigger extends SimpleCriterionTrigger<StatTrigger.TriggerInstance> implements IStatTrigger {
    @Override
    public void trigger(ServerPlayer player) {
        this.trigger(player, (instance) -> instance.test(player));
    }

    public static void doTrigger(ServerPlayer player){
        AtheriaAdvancementTriggers.STAT.get().trigger(player);
    }

    @Override
    public @NotNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public static Criterion<TriggerInstance> usages(int count) {
        return AtheriaAdvancementTriggers.STAT.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), MinMaxBounds.Ints.exactly(count)));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemStack> item,
                                  MinMaxBounds.Ints score) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(StatTrigger.TriggerInstance::player),
                        ItemStack.SINGLE_ITEM_CODEC.optionalFieldOf("item").forGetter(StatTrigger.TriggerInstance::item),
                MinMaxBounds.Ints.CODEC.optionalFieldOf("usages", MinMaxBounds.Ints.ANY).forGetter(StatTrigger.TriggerInstance::score))
                .apply(codec, StatTrigger.TriggerInstance::new));

        public boolean test(ServerPlayer player) {
            if(this.item.isPresent()) {
                if(this.score.matches(player.getStats().getValue(Stats.ITEM_USED.get(this.item.get().getItem())))){
                    return true;
                }else{
                    int a = player.getStats().getValue(Stats.ITEM_USED.get(this.item.get().getItem()));
                    int j = 0;
                    boolean b = false;
                    for (int i = a; i > 0&&!this.score.matches(i); i--) {
                        j = i;
                    }
                    if(j>0){
                        b=true;
                    }
                    if(this.score.matches(a)){
                        b=true;
                    }
                    return b;
                }
            }else{
                return false;
            }
        }
    }

}