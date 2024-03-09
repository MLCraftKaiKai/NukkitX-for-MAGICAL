package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.List;

/**
 * Created on 2015/12/26 by xtypr.
 * Package cn.nukkit.entity in project Nukkit .
 */
public class EntityXPOrb extends Entity {

    public static final int NETWORK_ID = 69;

    /**
     * Split sizes used for dropping experience orbs
     */
    public static final int[] ORB_SPLIT_SIZES = {2477, 1237, 617, 307, 149, 73, 37, 17, 7, 3, 1}; // This is indexed biggest to smallest so that we can return as soon as we found the biggest value
    public Player closestPlayer = null;
    private int pickupDelay;
    private int exp;

    public EntityXPOrb(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    /**
     * Returns the largest size of normal XP orb that will be spawned for the specified amount of XP. Used to split XP
     * up into multiple orbs when an amount of XP is dropped.
     */
    public static int getMaxOrbSize(int amount) {
        for (int split : ORB_SPLIT_SIZES) {
            if (amount >= split) {
                return split;
            }
        }

        return 1;
    }

    /**
     * Splits the specified amount of XP into an array of acceptable XP orb sizes.
     */
    public static List<Integer> splitIntoOrbSizes(int amount) {
        List<Integer> result = new IntArrayList();

        while (amount > 0) {
            int size = getMaxOrbSize(amount);
            result.add(size);
            amount -= size;
        }

        return result;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.1f;
    }

    @Override
    public float getLength() {
        return 0.1f;
    }

    @Override
    public float getHeight() {
        return 0.1f;
    }

    @Override
    protected float getGravity() {
        return 0.04f;
    }

    @Override
    protected float getDrag() {
        return 0.02f;
    }

    @Override
    public boolean canCollide() {
        return false;
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        setMaxHealth(5);

        if (namedTag.contains("Health")) {
            this.setHealth(namedTag.getShort("Health"));
        } else {
            this.setHealth(5);
        }

        if (namedTag.contains("Age")) {
            this.age = namedTag.getShort("Age");
        }

        if (namedTag.contains("PickupDelay")) {
            this.pickupDelay = namedTag.getShort("PickupDelay");
        }

        if (namedTag.contains("Value")) {
            this.exp = namedTag.getShort("Value");
        }

        if (this.exp <= 0) {
            this.exp = 1;
        }

        this.dataProperties.putInt(DATA_EXPERIENCE_VALUE, this.exp);
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        return (source.getCause() == DamageCause.VOID ||
                source.getCause() == DamageCause.FIRE_TICK ||
                (source.getCause() == DamageCause.ENTITY_EXPLOSION ||
                        source.getCause() == DamageCause.BLOCK_EXPLOSION) &&
                        !this.isInsideOfWater()) && super.attack(source);
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        int tickDiff = currentTick - this.lastUpdate;
        if (tickDiff <= 0 && !this.justCreated) {
            return true;
        }
        this.lastUpdate = currentTick;

        if (this.age > 6000) {
            this.close();
            return false;
        }

        boolean hasUpdate = entityBaseTick(tickDiff);
        if (this.isAlive()) {
            if (this.pickupDelay > 0) {
                this.pickupDelay -= tickDiff;
                if (this.pickupDelay < 0) {
                    this.pickupDelay = 0;
                }
            }/* else {
                Entity[] e = this.level.getCollidingEntities(this.boundingBox, this);
                for (Entity entity : e) {
                    if (entity instanceof Player) {
                        if (((Player) entity).pickupEntity(this, false)) {
                            return true;
                        }
                    }
                }
            }*/

            if (!this.isOnGround()) {
                this.motionY -= this.getGravity();
            }

            /*if (this.checkObstruction(this.x, this.y, this.z)) {
                hasUpdate = true;
            }*/

            if (this.closestPlayer == null || this.closestPlayer.distanceSquared(this) > 64.0D) {
                for (Player p : this.getViewers().values()) {
                    if (p == this.closestPlayer) continue; // Current closestPlayer is null or too far away
                    if (!p.isSpectator() && p.distanceSquared(this) <= 64) {
                        this.closestPlayer = p;
                        break;
                    }
                }
            }

            if (this.closestPlayer != null && (this.closestPlayer.isSpectator() || !this.closestPlayer.canPickupXP())) {
                this.closestPlayer = null;
            }

            if (this.closestPlayer != null) {
                double dX = (this.closestPlayer.x - this.x) / 8.0D;
                double dY = (this.closestPlayer.y + (double) this.closestPlayer.getEyeHeight() / 2.0D - this.y) / 8.0D;
                double dZ = (this.closestPlayer.z - this.z) / 8.0D;
                double d = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
                double diff = 1.0D - d;

                if (diff > 0.0D) {
                    diff = diff * diff;
                    this.motionX += dX / d * diff * 0.1D;
                    this.motionY += dY / d * diff * 0.1D;
                    this.motionZ += dZ / d * diff * 0.1D;
                }
            }

            this.move(this.motionX, this.motionY, this.motionZ);

            double friction = 1d - this.getDrag();

            if (this.onGround && (Math.abs(this.motionX) > 0.00001 || Math.abs(this.motionZ) > 0.00001)) {
                friction = this.getLevel().getBlock(this.temporalVector.setComponents((int) Math.floor(this.x), (int) Math.floor(this.y - 1), (int) Math.floor(this.z) - 1)).getFrictionFactor() * friction;
            }

            this.motionX *= friction;
            this.motionY *= 1 - this.getDrag();
            this.motionZ *= friction;

            if (this.onGround) {
                this.motionY *= -0.5;
            }

            this.updateMovement();
        }

        return hasUpdate || !this.onGround || Math.abs(this.motionX) > 0.00001 || Math.abs(this.motionY) > 0.00001 || Math.abs(this.motionZ) > 0.00001;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putShort("Health", (int) getHealth());
        this.namedTag.putShort("Age", age);
        this.namedTag.putShort("PickupDelay", pickupDelay);
        this.namedTag.putShort("Value", exp);
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        if (exp <= 0) {
            throw new IllegalArgumentException("XP amount must be greater than 0, got " + exp);
        }
        this.exp = exp;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    public int getPickupDelay() {
        return pickupDelay;
    }

    public void setPickupDelay(int pickupDelay) {
        this.pickupDelay = pickupDelay;
    }
}