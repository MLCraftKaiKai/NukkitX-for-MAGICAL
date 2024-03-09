package cn.nukkit.item;

public class ItemMagmaCream extends Item {

    public ItemMagmaCream() {
        this(0, 1);
    }

    public ItemMagmaCream(Integer meta) {
        this(meta, 1);
    }

    public ItemMagmaCream(Integer meta, int count) {
        super(MAGMA_CREAM, 0, count, "Magma Cream");
    }
}
