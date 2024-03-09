package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

/**
 * Created by Pub4Game on 28.01.2016.
 */
public class BlockHugeMushroomBrown extends BlockSolidMeta {

    public BlockHugeMushroomBrown() {
        this(0);
    }

    public BlockHugeMushroomBrown(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Brown Mushroom Block";
    }

    @Override
    public int getId() {
        return BROWN_MUSHROOM_BLOCK;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public double getHardness() {
        return 0.2;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item != null && item.hasEnchantment(Enchantment.ID_SILK_TOUCH)) {
            return new Item[]{this.toItem()};
        }
        return new Item[]{new ItemBlock(Block.get(BROWN_MUSHROOM), 0, Utils.rand() ? Utils.rand(0, 2) : 0)};
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.BROWN_BLOCK_COLOR;
    }
}
