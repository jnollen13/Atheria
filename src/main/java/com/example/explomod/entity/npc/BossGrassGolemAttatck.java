package com.example.explomod.entity.npc;

import com.example.explomod.entity.custom.BossGrassGolem;
import com.example.explomod.entity.custom.GrassGolem;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class BossGrassGolemAttatck extends MeleeAttackGoal {
    private final BossGrassGolem zombie;
    private int raiseArmTicks;

    public BossGrassGolemAttatck(BossGrassGolem grassGolem, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(grassGolem, speedModifier, followingTargetEvenIfNotSeen);
        this.zombie = grassGolem;
    }

    public void start() {
        super.start();
        this.raiseArmTicks = 0;
    }

    public void stop() {
        super.stop();
        this.zombie.setAggressive(false);
    }

    public void tick() {
        super.tick();
        ++this.raiseArmTicks;
        if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
            this.zombie.setAggressive(true);
        } else {
            this.zombie.setAggressive(false);
        }

    }
}
