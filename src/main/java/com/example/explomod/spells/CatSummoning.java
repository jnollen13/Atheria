package com.example.explomod.spells;

import com.example.explomod.effect.ModEffects;
import com.example.explomod.registries.Spell;
import com.example.explomod.stats.AtheriaStats;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CatSummoning extends Spell {

    public CatSummoning(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, float manaUsage, int minimum) {
        super(name, saveTo, cooldownDuration, manaUsage, minimum);
    }

    public CatSummoning(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, float manaUsage) {
        super(name, saveTo, cooldownDuration, manaUsage);
    }

    @Override
    public void DoSpell(Level level, LivingEntity entity) {
        if(entity instanceof Player player){
            player.awardStat(AtheriaStats.SPELLS_CAST.get(), 1);
        }
        if(!level.isClientSide){
            Cat cat = new Cat(EntityType.CAT, level);
            cat.teleportTo(entity.getX(), entity.getY(), entity.getZ());
            cat.setOwnerUUID(entity.getUUID());
            cat.tryToTeleportToOwner();
            cat.setBaby(true);
            cat.setBodyArmorItem(new ItemStack(Items.IRON_CHESTPLATE));
            entity.addEffect(new MobEffectInstance(ModEffects.COOLDOWN, 50000, 9, false, true, false, new MobEffectInstance(MobEffects.GLOWING)));
            if(entity instanceof Player player){
                player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 9800);
                player.getCooldowns().addCooldown(player.getOffhandItem().getItem(), 5000);
                player.getCooldowns().addCooldown(player.getItemBySlot(EquipmentSlot.CHEST).getItem(), 500);
                player.getCooldowns().addCooldown(player.getItemBySlot(EquipmentSlot.HEAD).getItem(), 500);
                player.getCooldowns().addCooldown(player.getItemBySlot(EquipmentSlot.BODY).getItem(), 500);
                player.getCooldowns().addCooldown(player.getItemBySlot(EquipmentSlot.FEET).getItem(), 500);
                player.getCooldowns().addCooldown(player.getItemBySlot(EquipmentSlot.LEGS).getItem(), 500);
                player.getCooldowns().addCooldown(Items.DIAMOND_SWORD, 250);
                player.getCooldowns().addCooldown(Items.NETHERITE_SWORD, 1000);
                player.getCooldowns().addCooldown(Items.GOLDEN_SWORD, 20);
                player.getCooldowns().addCooldown(Items.IRON_SWORD, 50);
                player.getCooldowns().addCooldown(Items.BOW, 500);
                player.getCooldowns().addCooldown(Items.CROSSBOW, 500);
                player.getCooldowns().addCooldown(Items.SNOWBALL, 500);
                player.getCooldowns().addCooldown(Items.ENDER_PEARL, 500);
                player.getCooldowns().addCooldown(Items.COBBLESTONE, 500);
                player.getCooldowns().addCooldown(Items.DIRT, 500);
                player.getCooldowns().addCooldown(Items.COOKED_BEEF, 500);
                player.getCooldowns().addCooldown(Items.WIND_CHARGE, 500);
                player.getCooldowns().addCooldown(Items.MACE, 500);
                player.getCooldowns().addCooldown(Items.EGG, 500);
                player.getCooldowns().addCooldown(Items.FISHING_ROD, 500);
                player.getCooldowns().addCooldown(Items.COOKED_SALMON, 20);
                level.addParticle(ParticleTypes.HAPPY_VILLAGER, true, player.getX(), player.getY()+1, player.getZ(), 0, 0.1, 0);
            }
        }
    }
}
