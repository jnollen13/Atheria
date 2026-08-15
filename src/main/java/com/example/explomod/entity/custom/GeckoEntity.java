package com.example.explomod.entity.custom;

import com.example.explomod.Config;
import com.example.explomod.ExploMod;
import com.example.explomod.entity.ModEntities;
import com.example.explomod.registries.Spell;
import com.example.explomod.registries.SpellRegistries;
import com.example.explomod.spells.LevelingSpell;
import com.example.explomod.spells.Spells;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GeckoEntity extends Animal {
    private static final Logger log = LoggerFactory.getLogger(GeckoEntity.class);
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    protected boolean isWizard;
    public boolean setWizard =false;
    private int hatLossAnimationWait=0;
    private Spell learnedSpell;
    private int level;
    private int spellCooldown =0 ;
    private int despawnModifier = 0;
    private int thinkTimeOut = 0;
    private boolean canDespawn;
    private boolean isThinking = false;
    public static List<Spell> defaultCaveWizardCastableSpells = List.of(Spells.DASH,Spells.FIREBALL_SPELL,Spells.HEALTH_DRAIN,SpellRegistries.UNSUMMONING_SPELL,Spells.LIGHTNING_CUBE);

    public GeckoEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.isWizard = RandomSource.create().nextBoolean()&&RandomSource.create().nextBoolean()&&RandomSource.create().nextBoolean()&&RandomSource.create().nextBoolean()&&RandomSource.create().nextBoolean()&&RandomSource.create().nextBoolean();
        learnedSpell = defaultCaveWizardCastableSpells.get(RandomSource.create().nextInt(1, defaultCaveWizardCastableSpells.size()));
        this.canDespawn=true;
        if(learnedSpell instanceof LevelingSpell levelingSpell){
            this.level = RandomSource.create().nextIntBetweenInclusive(1, levelingSpell.getMaxLevel());
        }else{
            this.level=1;
        }
    }

    public void setCanTimeOutDespawn(boolean despawn){
        this.canDespawn = despawn;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if(this.isWizard){
            player.sendSystemMessage(this.caveWizardSpell().displayName(true));
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, stack -> stack.is(ExploMod.POPSICLE_FOOD.get()), false));

        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));

        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 7.2F));
        this.goalSelector.addGoal(7, new ThinkGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.1));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10, new MeleeAttackGoal(this, 0.1f, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 6d)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 0.1d)
                .add(Attributes.ATTACK_KNOCKBACK, 0)
                .add(Attributes.FOLLOW_RANGE, 24D);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ExploMod.POPSICLE_FOOD.get());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        return ModEntities.GECKO.get().create(level);
    }

    public int getSpellLevel(){
        if(this.learnedSpell instanceof LevelingSpell){
            return this.level;
        }else{
            return 1;
        }
    }

    public boolean canTimeOutDespawn() {
        return this.canDespawn;
    }

    public void setSpellLevel(int level){
        int plannedLevel = 1;
        if(this.learnedSpell instanceof LevelingSpell levelingSpell) {
            plannedLevel = Math.min(level, levelingSpell.getMaxLevel());
        }
        this.level=plannedLevel;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isWizard", isWizard);
        compound.putInt("hatLossAnimationWait", hatLossAnimationWait);
        compound.putString("spell", learnedSpell.name());
        compound.putInt("SpellLevel", getSpellLevel());
        compound.putInt("despawnModifier", despawnModifier);
        compound.putBoolean("canTimeOutDespawn", canTimeOutDespawn());
        compound.putInt("thinkCooldown", this.thinkTimeOut);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setWizard(compound.getBoolean("isWizard"));
        this.learnedSpell=SpellRegistries.SPELLS.getSpellFromName(compound.getString("spell"));
        this.hatLossAnimationWait=compound.getInt("hatLossAnimationWait");
        setSpellLevel(compound.getInt("SpellLevel"));
        setDespawnModifier(compound.getInt("despawnModifier"));
        setCanTimeOutDespawn(compound.getBoolean("canTimeOutDespawn"));
        setThinkTimeOut(compound.getInt("thinkCooldown"));

    }

    private void setupAnimationStates() {
        if(this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    public void setDespawnModifier(int despawnModifier) {
        this.despawnModifier = despawnModifier;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide()) {
            this.setupAnimationStates();
        }
        if(Config.SERVER.despawn_cave_lizards.get()&&this.tickCount+this.despawnModifier>=Config.SERVER.despawn_cave_lizards_ticks.get()&&this.canDespawn){
            this.remove(RemovalReason.DISCARDED);
        }
        if(hatLossAnimationWait==1){
            this.isWizard=false;
        } else if (hatLossAnimationWait>1) {
            hatLossAnimationWait-=1;
        }
        if(this.isWizard()){
            this.level().addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), 0, -0.1, 0);
        }
        if(thinkTimeOut>0){
            thinkTimeOut-=1;
        }
        this.wizardUtility();
    }

    public void think(ServerLevel serverLevel){
        if(isWizard) {
            this.spawnGolemIfNeeded(serverLevel, 5);
            if(Config.SERVER.cave_wizards_cast_spells.get()) {
                caveWizardSpell().DoSpell(serverLevel, this);
            }
        }
    }

    public Spell caveWizardSpell(){
        if(this.learnedSpell instanceof LevelingSpell levelingSpell){
            return levelingSpell.withLevel(this.level);
        }else {
            return this.learnedSpell;
        }
    }

    public void setThinkTimeOut(int thinkTimeOut) {
        this.thinkTimeOut = thinkTimeOut;
    }

    public void setWizard(boolean wizard) {
        this.isWizard = wizard;
        this.setWizard = wizard;
    }

    public boolean isWizard() {
        return this.isWizard;
    }

    protected void wizardUtility(){
        if(this.setWizard){
            this.isWizard=true;
        }
        if(this.isWizard()){
            if(this.spellCooldown>0){
                this.spellCooldown-=1;
            }
        }
    }

    public void spawnGolemIfNeeded(ServerLevel serverLevel, int minMobAmount) {
        if (this.wantsToSpawnGolem()) {
            AABB aabb = this.getBoundingBox().inflate(16.0F, 16.0F, 16.0F);
            List<GeckoEntity> list = serverLevel.getEntitiesOfClass(GeckoEntity.class, aabb);
            AtomicInteger numberPanicking = new AtomicInteger();
            List<GeckoEntity> transforming = List.of();
            list.forEach(geckoEntity -> {
                if(geckoEntity.isPanicking()){
                    numberPanicking.getAndIncrement();
                    transforming.add(geckoEntity);
                }
            });
            if(numberPanicking.get()>=minMobAmount){
                transforming.forEach(entity -> {
                    entity.remove(RemovalReason.DISCARDED);
                    GrassGolem golem = new GrassGolem(ModEntities.GRASS_GOLEM.get(), serverLevel);
                    golem.readAdditionalSaveData(entity.getPersistentData());
                    golem.addTag("summoned");
                    golem.setCanPickUpLoot(true);
                    golem.setHealth(20f);
                    golem.moveTo(entity.position());
                    golem.setCustomName(Component.translatable("mob_spawn_type.text.explomod.summoned").withStyle(ChatFormatting.BLUE));
                });
            }
        }

    }

    boolean isDigging() {
        return this.hasPose(Pose.DIGGING) || this.hasPose(Pose.EMERGING);
    }

    public boolean wantsToSpawnGolem() {
        return this.isPanicking()||this.isOnFire();
    }

    public void kickOffHat(){
        hatLossAnimationWait=100;
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if(this.isWizard) {
            if (spawnType == MobSpawnType.NATURAL) {
                this.setPose(Pose.EMERGING);
                this.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 2.0F, 1.0F);
            }
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    public boolean thinkTimeOutOver(){
        return thinkTimeOut<=0;
    }

    public void setThinking(boolean thinking) {
        this.isThinking = thinking;
    }

    public boolean isThinking() {
        return this.isThinking;
    }

    class ThinkGoal extends Goal {
        private final GeckoEntity mob;
        private int thinkTime;
        private final int startThinkTime;
        ThinkGoal(GeckoEntity mob) {
            super();
            this.mob=mob;
            this.startThinkTime=RandomSource.create().nextIntBetweenInclusive(1, 100);
            thinkTime=startThinkTime;
            this.mob.setThinking(true);
        }

        /**
         * @return if this goal can start
         */
        @Override
        public boolean canUse() {
            return mob.thinkTimeOutOver()&&RandomSource.create().nextBoolean()&&RandomSource.create().nextIntBetweenInclusive(1, 50)==2;
        }

        @Override
        public void tick() {
            super.tick();
            mob.think((ServerLevel) mob.level());
            mob.level().addParticle(ParticleTypes.FIREWORK, this.mob.getX(), this.mob.getY(), this.mob.getZ(), 0, 1, 0);
            thinkTime-=1;
        }

        @Override
        public boolean canContinueToUse() {
            return mob.thinkTimeOutOver()&&thinkTime>0;
        }

        @Override
        public void stop() {
            super.stop();
            thinkTimeOut=RandomSource.create().nextIntBetweenInclusive(10, 350);
            this.mob.setThinking(false);
        }
    }
}
