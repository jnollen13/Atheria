package item.alchemy;

import com.example.explomod.ExploMod;
import com.example.explomod.effect.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;


public class Potions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, ExploMod.MODID);

    public static final Holder<Potion> ROCKET_POTION = POTIONS.register("rocket_potion", () -> new Potion(new MobEffectInstance(ModEffects.ROCKET, 1200)));

    public static void register(IEventBus eventBus)
}
