package worldgen;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WorldData;
import org.jetbrains.annotations.NotNull;

public class AtheriaLevelData extends DerivedLevelData {
    private final ServerLevelData wrapped;
    private final GameRules gameRules;

    private long dayTime;

    public AtheriaLevelData(ServerLevel level, WorldData worldData, ServerLevelData overworldData, long dayTime) {
        super(worldData, overworldData);
        this.wrapped = overworldData;
        this.gameRules = new GameRules();
        this.dayTime = dayTime;
    }

    public long getOverworldDayTime() {
        return this.wrapped.getDayTime();
    }

    @Override
    public long getDayTime() {
        return this.dayTime;
    }

    @Override
    public void setDayTime(long time) {
        this.dayTime = time;
    }

    @Override
    public void setClearWeatherTime(int time) {
        this.wrapped.setClearWeatherTime(time);
    }

    @Override
    public void setRaining(boolean raining) {
        this.wrapped.setRaining(raining);
    }

    @Override
    public void setRainTime(int time) {
        this.wrapped.setRainTime(time);
    }

    @Override
    public void setThundering(boolean thundering) {
        this.wrapped.setThundering(thundering);
    }

    @Override
    public void setThunderTime(int time) {
        this.wrapped.setThunderTime(time);
    }

    @Override
    public @NotNull GameRules getGameRules() {
        return this.gameRules;
    }
}
