package cn.nukkit.level.biome.impl.beach;

import cn.nukkit.block.Block;
import cn.nukkit.level.biome.type.SandyBiome;
import cn.nukkit.level.generator.populator.impl.WaterIcePopulator;

public class ColdBeachBiome extends SandyBiome {
    public ColdBeachBiome() {
        this.addPopulator(new WaterIcePopulator());

        this.setBaseHeight(0f);
        this.setHeightVariation(0.025f);
    }

    @Override
    public int getCoverId(int x, int z) {
        return SNOW_LAYER << Block.DATA_BITS;
    }

    @Override
    public String getName() {
        return "Cold Beach";
    }

    @Override
    public boolean isFreezing() {
        return true;
    }

    @Override
    public boolean canRain() {
        return false;
    }
}
