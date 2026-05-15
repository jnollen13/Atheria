package item.custom;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ArmorMaterials extends net.minecraft.world.item.ArmorMaterials {
    public static final Holder<ArmorMaterial> PHANTOM;

    public static Holder<ArmorMaterial> bootstrap(Registry<ArmorMaterial> registry) {
        return PHANTOM;
    }

    private static Holder<ArmorMaterial> register(String name, EnumMap<ArmorItem.Type, Integer> defense, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace(name)));
        return register(name, defense, enchantmentValue, equipSound, toughness, knockbackResistance, repairIngredient, list);
    }

    private static Holder<ArmorMaterial> register(String name, EnumMap<ArmorItem.Type, Integer> defense, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngridient, List<ArmorMaterial.Layer> layers) {
        EnumMap<ArmorItem.Type, Integer> enummap = new EnumMap(ArmorItem.Type.class);

        for(ArmorItem.Type armoritem$type : ArmorItem.Type.values()) {
            enummap.put(armoritem$type, (Integer)defense.get(armoritem$type));
        }

        return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, ResourceLocation.withDefaultNamespace(name), new ArmorMaterial(enummap, enchantmentValue, equipSound, repairIngridient, layers, toughness, knockbackResistance));
    }

    static {
        PHANTOM = register("phantom", (EnumMap)Util.make(new EnumMap(ArmorItem.Type.class), (p_323380_) -> {
            p_323380_.put(ArmorItem.Type.BOOTS, 3);
            p_323380_.put(ArmorItem.Type.LEGGINGS, 6);
            p_323380_.put(ArmorItem.Type.CHESTPLATE, 8);
            p_323380_.put(ArmorItem.Type.HELMET, 3);
            p_323380_.put(ArmorItem.Type.BODY, 11);
        }), 10, SoundEvents.ARMOR_EQUIP_GENERIC, 2.0F, 0.0F, () -> Ingredient.of(new ItemLike[]{Items.PHANTOM_MEMBRANE}));
    }

}
