package com.example.explomod.entity.npc;

import com.example.explomod.entity.custom.GrassGolem;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class GrassGolemAttackGoal extends MeleeAttackGoal {
    private final GrassGolem zombie;
    private int raiseArmTicks;

    public GrassGolemAttackGoal(GrassGolem grassGolem, double speedModifier, boolean followingTargetEvenIfNotSeen) {
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
