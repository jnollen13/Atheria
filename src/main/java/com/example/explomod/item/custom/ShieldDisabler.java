package item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ShieldDisabler extends Item {
    public ShieldDisabler(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canDisableShield(@NotNull ItemStack stack, @NotNull ItemStack shield, @NotNull LivingEntity entity, @NotNull LivingEntity attacker) {
        return true;
    }
}
