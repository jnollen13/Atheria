package com.example.explomod.spells;

import com.example.explomod.registries.LevelScaling;
import com.example.explomod.registries.Spell;
import com.example.explomod.stats.AtheriaStats;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LevelingSpell extends Spell {
    private final LevelScaling scaling;
    private final int maxLevel;
    private int level;
    public LevelingSpell(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, int manaUsage, int minimum, LevelScaling scaling, int maxLevel) {
        super(name, saveTo, cooldownDuration, manaUsage, minimum);
        this.scaling=scaling;
        this.maxLevel=maxLevel;
    }

    public LevelingSpell(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, float manaUsage, int minimum, LevelScaling scaling, int maxLevel) {
        super(name, saveTo, cooldownDuration, manaUsage, minimum);
        this.scaling=scaling;
        this.maxLevel=maxLevel;
    }

    public LevelingSpell(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, int manaUsage, LevelScaling scaling, int maxLevel) {
        super(name, saveTo, cooldownDuration, manaUsage);
        this.scaling=scaling;
        this.maxLevel=maxLevel;
    }

    public LevelingSpell(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, float manaUsage, LevelScaling scaling, int maxLevel) {
        super(name, saveTo, cooldownDuration, manaUsage);
        this.scaling=scaling;
        this.maxLevel=maxLevel;
    }

    public LevelingSpell(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, LevelScaling scaling, int maxLevel) {
        super(name, saveTo, cooldownDuration);
        this.scaling=scaling;
        this.maxLevel=maxLevel;
    }

    public LevelingSpell(@Nullable String name, @NotNull Item saveTo, LevelScaling scaling, int maxLevel) {
        super(name, saveTo);
        this.scaling=scaling;
        this.maxLevel=maxLevel;
    }

    protected LevelScaling getScaling() {
        return scaling;
    }

    /**
     * gets the level of the spell
     */
    public int getLevel(){
        return level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public LevelingSpell getSpell() {
        return this;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Integer> getPossibleLevels(){
        List<Integer> levelList = new java.util.ArrayList<>(List.of(1));
        for (int i = 1; i < this.maxLevel+1; i++) {
            levelList.add(i);
        }
        return levelList;
    }

    @Override
    public void DoSpell(Level level, LivingEntity entity) {
        if(entity instanceof Player player){
            player.awardStat(AtheriaStats.SPELLS_CAST.get(), 1);
        }
    }

    @Override
    public float manaUsage() {
        return manaLeveling(super.manaUsage());
    }

    /**
     * increase of mana per level of spell. could be done with scaling preset methods or something else.
     * @param baseManaUsage the amount of mana used for a level one spell
     */
    public float manaLeveling(float baseManaUsage) {
        return applyFloatLevelingAdd(baseManaUsage);
    }

    @Override
    public Component displayName(boolean formated) {
        if(!formated) {
            return translated();
        }else{
            return Component.translatable(descriptionId()).append(" ").append(Component.translatable("enchantment.level."+level)).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED);
        }
    }

    @Override
    public int getCooldown() {
        return cooldownLeveling(super.getCooldown());
    }

    public int cooldownLeveling(int baseCooldown){
        return applyIntLevelingCustomLinear(baseCooldown, 1);
    }

        protected int applyIntLeveling(int leveling) {
            return leveling*(level-1);
        }

        protected int applyIntLevelingAdd(int base) {
            return base + applyIntLeveling(getScaling().getInt());
        }

        protected int applyIntLevelingCustomLinear(int base, int perLevelAfterFirst){
            return base + (level-1)*perLevelAfterFirst;
        }

        protected int applyIntLevelingMultiply(int base) {
            return base * applyIntLeveling(getScaling().getInt());
        }

        protected int applyIntLevelingAddMultiplied(int base) {
            return base + (applyIntLeveling(getScaling().getInt()) * base);
        }

        protected int applyIntLevelingSubtract(int base) {
            return base - applyIntLeveling(getScaling().getInt());
        }

        protected int applyIntLevelingDivide(int base) {
            return base / applyIntLeveling(getScaling().getInt());
        }

        protected float applyFloatLeveling(float leveling) {
            return leveling*(level-1);
        }

        protected float applyFloatLevelingAdd(float base) {
            return base + applyFloatLeveling(scaling.getFloat());
        }

        protected float applyFloatLevelingMultiply(float base) {
            return base * applyFloatLeveling(scaling.getFloat());
        }

        protected float applyFloatLevelingAddMultiplied(float base) {
            return base + (applyFloatLeveling(scaling.getFloat()) * base);
        }

        protected float applyFloatLevelingSubtract(float base) {
            return base - applyFloatLeveling(scaling.getFloat());
        }

        protected float applyFloatLevelingDivide(float base) {
            return base / applyFloatLeveling(scaling.getFloat());
        }

        protected double applyDoubleLevelingAdd(double base) {
            return base + scaling.getDouble();
        }

        protected double applyDoubleLevelingMultiply(double base) {
            return base * scaling.getDouble();
        }

        protected double applyDoubleLevelingAddMultiplied(double base) {
            return base + (scaling.getDouble() * base);
        }

        protected double applyDoubleLevelingSubtract(double base) {
            return base - scaling.getDouble();
        }

        protected double applyDoubleLevelingDivide(double base) {
            return base / scaling.getDouble();
        }

        protected double applyDoubleLevelingAddDivided(double base) {
            return base + (base/scaling.getDouble());
        }
}