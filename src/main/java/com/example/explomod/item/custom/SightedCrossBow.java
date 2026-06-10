package item.custom;

import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jetbrains.annotations.NotNull;

public class SightedCrossBow extends CrossbowItem {
    public SightedCrossBow(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canPerformAction(@NotNull ItemStack stack, net.neoforged.neoforge.common.@NotNull ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_SPYGLASS_ACTIONS.contains(itemAbility);
    }
}
