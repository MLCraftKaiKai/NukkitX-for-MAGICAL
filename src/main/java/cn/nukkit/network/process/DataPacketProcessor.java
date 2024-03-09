package cn.nukkit.network.process;

import cn.nukkit.PlayerHandle;
import cn.nukkit.network.protocol.DataPacket;
import org.jetbrains.annotations.NotNull;

/**
 * A DataPacketProcessor is used to handle a specific type of DataPacket. <br/>
 * DataPacketProcessor must be <strong>thread-safe</strong>. <br/>
 * <hr/>
 * Why not interfaces? Hotspot C2 JIT cannot handle so many classes that impl the same interface, it makes the
 * performance lower.
 */
public abstract class DataPacketProcessor<T extends DataPacket> {

    public abstract void handle(@NotNull PlayerHandle playerHandle, @NotNull T pk);

    public abstract int getPacketId();

    public int getProtocol() {
        return -1;
    }
}
