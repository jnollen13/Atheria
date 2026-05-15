package com.example.explomod.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.ticks.LevelTickAccess;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Level extends net.minecraft.world.level.Level {
    public static final ResourceKey<net.minecraft.world.level.Level> DARK;
    static {
    DARK = ResourceKey.create(Registries.DIMENSION, ResourceLocation.withDefaultNamespace("dark"));
    }

    /**
     * @param blockPos
     * @param blockState
     * @param blockState1
     * @param i
     */
    @Override
    public void sendBlockUpdated(BlockPos blockPos, BlockState blockState, BlockState blockState1, int i) {

    }

    /**
     * @param player
     * @param v
     * @param v1
     * @param v2
     * @param holder
     * @param soundSource
     * @param v3
     * @param v4
     * @param l
     */
    @Override
    public void playSeededSound(@Nullable Player player, double v, double v1, double v2, Holder<SoundEvent> holder, SoundSource soundSource, float v3, float v4, long l) {

    }

    /**
     * @param player
     * @param entity
     * @param holder
     * @param soundSource
     * @param v
     * @param v1
     * @param l
     */
    @Override
    public void playSeededSound(@Nullable Player player, Entity entity, Holder<SoundEvent> holder, SoundSource soundSource, float v, float v1, long l) {

    }

    /**
     * @return
     */
    @Override
    public String gatherChunkSourceStats() {
        return "";
    }

    /**
     * @param i
     * @return
     */
    @Override
    public @Nullable Entity getEntity(int i) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public TickRateManager tickRateManager() {
        return null;
    }

    /**
     * @param mapId
     * @return
     */
    @Override
    public @Nullable MapItemSavedData getMapData(MapId mapId) {
        return null;
    }

    /**
     * @param mapId
     * @param mapItemSavedData
     */
    @Override
    public void setMapData(MapId mapId, MapItemSavedData mapItemSavedData) {

    }

    /**
     * @return
     */
    @Override
    public MapId getFreeMapId() {
        return null;
    }

    /**
     * @param i
     * @param blockPos
     * @param i1
     */
    @Override
    public void destroyBlockProgress(int i, BlockPos blockPos, int i1) {

    }

    /**
     * @return
     */
    @Override
    public Scoreboard getScoreboard() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public RecipeManager getRecipeManager() {
        return null;
    }

    /**
     * @return
     */
    @Override
    protected LevelEntityGetter<Entity> getEntities() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public PotionBrewing potionBrewing() {
        return null;
    }

    /**
     * @param v
     */
    @Override
    public void setDayTimeFraction(float v) {

    }

    /**
     * @return
     */
    @Override
    public float getDayTimeFraction() {
        return 0;
    }

    /**
     * @return
     */
    @Override
    public float getDayTimePerTick() {
        return 0;
    }

    /**
     * @param v
     */
    @Override
    public void setDayTimePerTick(float v) {

    }

    /**
     * @return
     */
    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public LevelTickAccess<Fluid> getFluidTicks() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public ChunkSource getChunkSource() {
        return null;
    }

    /**
     * @param player
     * @param i
     * @param blockPos
     * @param i1
     */
    @Override
    public void levelEvent(@Nullable Player player, int i, BlockPos blockPos, int i1) {

    }

    /**
     * @param holder
     * @param vec3
     * @param context
     */
    @Override
    public void gameEvent(Holder<GameEvent> holder, Vec3 vec3, GameEvent.Context context) {

    }

    /**
     * @param direction
     * @param b
     * @return
     */
    @Override
    public float getShade(Direction direction, boolean b) {
        return 0;
    }

    /**
     * @return
     */
    @Override
    public List<? extends Player> players() {
        return List.of();
    }

    /**
     * @param i
     * @param i1
     * @param i2
     * @return
     */
    @Override
    public Holder<Biome> getUncachedNoiseBiome(int i, int i1, int i2) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public FeatureFlagSet enabledFeatures() {
        return null;
    }
}
