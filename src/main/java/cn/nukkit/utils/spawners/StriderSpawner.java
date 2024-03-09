package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.passive.EntityStrider;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

/**
 * @author LT_Name
 */
public class StriderSpawner extends AbstractEntitySpawner {

    public StriderSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        if (Utils.rand(1, 3) != 1) {
            this.spawnTask.createEntity("Strider", pos.add(0.5, 1, 0.5));
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityStrider.NETWORK_ID;
    }
}
