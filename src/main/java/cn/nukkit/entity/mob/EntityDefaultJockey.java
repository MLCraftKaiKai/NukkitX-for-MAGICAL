package cn.nukkit.entity.mob;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.passive.EntityChicken;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.SetEntityLinkPacket;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * @author Nissining
 */
public class EntityDefaultJockey extends EntityWalkingMob {
    /**
     * 支持其他的敌对型生物
     */
    @Getter
    @Setter
    private EntityWalkingMob mob;

    public EntityDefaultJockey(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getKillExperience() {
        return 0;
    }

    @Override
    public int getNetworkId() {
        // 支持其他坐骑但不局限于小鸡
        boolean jockey = namedTag.contains("JockeyId");
        if (jockey) {
            return namedTag.getInt("JockeyId");
        }
        return EntityChicken.NETWORK_ID;
    }

    @Override
    public float getHeight() {
        return 0.8F;
    }

    @Override
    public float getWidth() {
        return 0.6F;
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public double getSpeed() {
        return 1.5D;
    }

    @Override
    public String getName() {
        return "DefaultJockey";
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        setMob(new EntityZombie(this.getChunk(), Entity.getDefaultNBT(this)));
        Optional.ofNullable(getMob())
                .ifPresent(mob -> {
                    mob.setBaby(true);
                    mob.spawnToAll();
                    this.mountEntity(mob);
                });
    }

    @Override
    public boolean mountEntity(Entity entity) {
        broadcastLinkPacket(entity, SetEntityLinkPacket.TYPE_RIDE);

        // Add variables to entity
        entity.riding = this;
        entity.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, true);
        passengers.add(entity);

        entity.setSeatPosition(new Vector3f(0, getHeight() * 0.5f));
        updatePassengerPosition(entity);
        return true;
    }

    @Override
    public boolean dismountEntity(Entity entity) {
        broadcastLinkPacket(entity, SetEntityLinkPacket.TYPE_REMOVE);
        // Refurbish the entity
        entity.riding = null;
        entity.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, false);
        passengers.remove(entity);

        entity.setSeatPosition(new Vector3f());
        updatePassengerPosition(entity);

        // Avoid issues with anti fly
        entity.resetFallDistance();
        return true;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        boolean b = super.onUpdate(currentTick);
        if (b) {
            Optional.ofNullable(getMob())
                    .ifPresent(mob -> {
                        if (!mob.isAlive()) {
                            setMob(null);
                            followTarget = null;
                        }
                    });
        }
        return b;
    }

    @Override
    public void attackEntity(Entity player) {

    }
}
