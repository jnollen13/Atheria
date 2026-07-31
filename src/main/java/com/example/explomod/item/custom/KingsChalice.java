package com.example.explomod.item.custom;

import com.example.explomod.utill.ModTags;
import com.example.explomod.utill.PropertiesDescription;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class KingsChalice extends VisualEnchantmentTooltipItem {
    public KingsChalice(Properties properties) {
        super(properties);
    }

    @Override
    public List<String> materials() {
        return List.of("Aetherian Gold", "Ruby");
    }

    @Override
    protected String itemSubType() {
        return "";
    }

    @Override
    protected String itemType() {
        return "Flask";
    }

    @Override
    protected List<PropertiesDescription> additionalProperties() {
        return List.of(new PropertiesDescription("Aetherian Gold", "valuable to piglins"));
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack itemstack = super.getDefaultInstance();
        itemstack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
        return itemstack;
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using the Item before the action is complete.
     */
    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entityLiving) {
        Player player = entityLiving instanceof Player ? (Player)entityLiving : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, stack);
        }

        if (!level.isClientSide) {
            PotionContents potioncontents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            potioncontents.forEachEffect(p_330883_ -> {
                if (p_330883_.getEffect().value().isInstantenous()) {
                    p_330883_.getEffect().value().applyInstantenousEffect(player, player, entityLiving, p_330883_.getAmplifier(), 1.0);
                } else {
                    if(EnchantmentHelper.hasTag(stack, ModTags.Enchantments.DISTILL)){
                        float enchantmentLevel = EnchantmentHelper.modifyCrossbowChargingTime(stack, entityLiving, 1.0f);
                        p_330883_=new MobEffectInstance(p_330883_.getEffect(), p_330883_.getDuration(), (int) ((p_330883_.getAmplifier()+1)*enchantmentLevel));
                    }
                    entityLiving.addEffect(p_330883_);
                }
            });
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            if(!player.hasInfiniteMaterials()) {
                stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
                if(entityLiving.getMainHandItem()==stack) {
                    stack.hurtAndBreak(1, entityLiving, EquipmentSlot.MAINHAND);
                }else {
                    stack.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
                }
            }
        }
        entityLiving.gameEvent(GameEvent.DRINK);
        return stack;
    }
    /**
     * Called when this item is used when targeting a Block
     */
    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack itemstack = context.getItemInHand();
        PotionContents potioncontents = itemstack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        BlockState blockstate = level.getBlockState(blockpos);
        if (context.getClickedFace() != Direction.DOWN && blockstate.is(BlockTags.CONVERTABLE_TO_MUD) && potioncontents.is(Potions.WATER)) {
            level.playSound(null, blockpos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
            assert player != null;
            itemstack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            player.setItemInHand(context.getHand(), ItemUtils.createFilledResult(itemstack, player, itemstack));
            player.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
            if (!level.isClientSide) {
                ServerLevel serverlevel = (ServerLevel)level;

                for (int i = 0; i < 5; i++) {
                    serverlevel.sendParticles(
                            ParticleTypes.SPLASH,
                            (double)blockpos.getX() + level.random.nextDouble(),
                            blockpos.getY() + 1,
                            (double)blockpos.getZ() + level.random.nextDouble(),
                            1,
                            0.0,
                            0.0,
                            0.0,
                            1.0
                    );
                }
            }

            level.playSound(null, blockpos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PLACE, blockpos);
            level.setBlockAndUpdate(blockpos, Blocks.MUD.defaultBlockState());
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            if (context.getClickedFace() != Direction.DOWN && potioncontents==PotionContents.EMPTY) {
                return InteractionResult.SUCCESS;
            }else {
                return InteractionResult.PASS;
            }
        }
    }
    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 28;
    }
    /**
     * Returns the action that specifies what animation to play when the item is being used.
     */
    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }
    /**
     * Called to trigger the item's "innate" right click behavior.
     */
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if(player.getItemInHand(hand).getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)!=PotionContents.EMPTY) {
            return ItemUtils.startUsingInstantly(level, player, hand);
        }else{
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        PotionContents potioncontents = stack.get(DataComponents.POTION_CONTENTS);
        assert potioncontents != null;
        if(potioncontents.potion().isPresent()&&stack.get(DataComponents.POTION_CONTENTS)!=PotionContents.EMPTY) {
            tooltipComponents.add(Component.literal("contains: " + potioncontents.potion().get().getRegisteredName().toLowerCase()).withStyle(ChatFormatting.GREEN));
            if(EnchantmentHelper.hasTag(stack, ModTags.Enchantments.DISTILL)){
                assert Minecraft.getInstance().player != null;
                float bonus = EnchantmentHelper.modifyCrossbowChargingTime(stack, Minecraft.getInstance().player, 1.0f);
                tooltipComponents.add(Component.literal("("+bonus*100+"% ").withStyle(ChatFormatting.GREEN,ChatFormatting.ITALIC).append(Component.translatable("tooltip.explomod.potion_power").append(")").withStyle(ChatFormatting.GREEN, ChatFormatting.ITALIC)));
            }
            addPotionTooltip(potioncontents.getAllEffects(), tooltipComponents::add, 1.0F, context.tickRate());
        }else{
            tooltipComponents.add(Component.literal("empty"));
        }
        if(stack.isEnchanted()){
            tooltipComponents.add(Component.literal(" "));
        }
    }

    public static void addPotionTooltip(Iterable<MobEffectInstance> effects, Consumer<Component> tooltipAdder, float durationFactor, float ticksPerSecond) {
        List<Pair<Holder<Attribute>, AttributeModifier>> list = Lists.newArrayList();
        boolean flag = true;

        for (MobEffectInstance mobeffectinstance : effects) {
            flag = false;
            MutableComponent mutablecomponent = Component.translatable(mobeffectinstance.getDescriptionId());
            Holder<MobEffect> holder = mobeffectinstance.getEffect();
            holder.value().createModifiers(mobeffectinstance.getAmplifier(), (p_331556_, p_330860_) -> list.add(new Pair<>(p_331556_, p_330860_)));
            if (mobeffectinstance.getAmplifier() > 0) {
                mutablecomponent = Component.translatable(
                        "potion.withAmplifier", mutablecomponent, Component.translatable("potion.potency." + mobeffectinstance.getAmplifier())
                );
            }
            if (!mobeffectinstance.endsWithin(20)) {
                mutablecomponent = Component.translatable(
                        "potion.withDuration", mutablecomponent, MobEffectUtil.formatDuration(mobeffectinstance, durationFactor, ticksPerSecond)
                );
            }
            tooltipAdder.accept(mutablecomponent.withStyle(ChatFormatting.DARK_GREEN));
        }
        if (flag) {
            tooltipAdder.accept(Component.translatable("effect.none").withStyle(ChatFormatting.GRAY));
        }
        if (!list.isEmpty()) {
            tooltipAdder.accept(CommonComponents.EMPTY);
            tooltipAdder.accept(Component.translatable("potion.does").withStyle(ChatFormatting.GREEN));
            // Neo: Override handling of potion attribute tooltips to support IAttributeExtension
            net.neoforged.neoforge.common.util.AttributeUtil.addPotionTooltip(list, tooltipAdder);
            if (true) return;
            for (Pair<Holder<Attribute>, AttributeModifier> pair : list) {
                AttributeModifier attributemodifier = pair.getSecond();
                double d1 = attributemodifier.amount();
                double d0;
                if (attributemodifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        && attributemodifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                    d0 = attributemodifier.amount();
                } else {
                    d0 = attributemodifier.amount() * 100.0;
                }

                if (d1 > 0.0) {
                    tooltipAdder.accept(
                            Component.translatable(
                                            "attribute.modifier.plus." + attributemodifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(d0),
                                            Component.translatable(pair.getFirst().value().getDescriptionId())
                                    )
                                    .withStyle(ChatFormatting.BLUE)
                    );
                } else if (d1 < 0.0) {
                    d0 *= -1.0;
                    tooltipAdder.accept(
                            Component.translatable(
                                            "attribute.modifier.take." + attributemodifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(d0),
                                            Component.translatable(pair.getFirst().value().getDescriptionId())
                                    )
                                    .withStyle(ChatFormatting.RED)
                    );
                }
            }
        }
    }
}
