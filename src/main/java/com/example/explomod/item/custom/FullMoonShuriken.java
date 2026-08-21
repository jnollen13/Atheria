package com.example.explomod.item.custom;

import com.example.explomod.ExploMod;
import com.example.explomod.entity.custom.NinjaStar;
import com.example.explomod.utill.ModTags;
import com.example.explomod.utill.PropertiesDescription;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FullMoonShuriken extends VisualEnchantmentTooltipItem implements ProjectileItem {

    public FullMoonShuriken(Properties properties) {
        super(properties);
    }

    @Override
    protected String itemSubType() {
        return "";
    }

    @Override
    protected String itemType() {
        return "Shuriken";
    }

    @Override
    public List<String> materials() {
        return List.of("Moon Silver");
    }

    @Override
    protected List<PropertiesDescription> additionalProperties() {
        return List.of(new PropertiesDescription("Moon Silver", "Critical Hits will sometimes deal extra damage."));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.SNOWBALL_THROW,
                SoundSource.NEUTRAL,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!level.isClientSide) {
            NinjaStar shuriken = new NinjaStar(level, player, itemstack);
            shuriken.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.92F, 0.45F);
            if(EnchantmentHelper.hasTag(itemstack, ModTags.Enchantments.BOOMERANG)){
                shuriken.setDestroy(false);
            }
            level.addFreshEntity(shuriken);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        itemstack.consume(1, player);
        player.getCooldowns().addCooldown(ExploMod.FULL_MOON.asItem(), 5);
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public @NotNull Projectile asProjectile(@NotNull Level level, Position pos, @NotNull ItemStack stack, @NotNull Direction direction) {
        NinjaStar shuriken = new NinjaStar(level, pos.x(), pos.y(), pos.z(), stack);
        return shuriken;
    }
}
