package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import lombok.ToString;

@ToString
public class NetworkChunkPublisherUpdatePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.NETWORK_CHUNK_PUBLISHER_UPDATE_PACKET;

    public BlockVector3 position;
    public int radius;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.position = this.getSignedBlockPosition();
        this.radius = (int) this.getUnsignedVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putSignedBlockPosition(position);
        this.putUnsignedVarInt(radius);
        if (this.protocol >= ProtocolInfo.v1_19_20) {
            this.putInt(0); // Saved chunks
        }
    }
}
