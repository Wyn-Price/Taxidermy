package com.wynprice.tabulataxidermy.network;

import com.wynprice.tabulataxidermy.TTBlockEntity;
import com.wynprice.tabulataxidermy.TabulaTaxidermy;
import io.netty.buffer.ByteBuf;
import net.dumbcode.dumblibrary.server.network.WorldModificationsMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.vecmath.Vector3f;

public class C3SetBlockProperties implements IMessage {

    private BlockPos blockPos;
    private Vector3f position;
    private Vector3f rotation;
    private float scale;

    public C3SetBlockProperties() {
    }

    public C3SetBlockProperties(BlockPos blockPos, Vector3f position, Vector3f rotation, float scale) {
        this.blockPos = blockPos;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.blockPos = BlockPos.fromLong(buf.readLong());
        this.position = new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
        this.rotation = new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
        this.scale = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.blockPos.toLong());

        buf.writeFloat(this.position.x);
        buf.writeFloat(this.position.y);
        buf.writeFloat(this.position.z);

        buf.writeFloat(this.rotation.x);
        buf.writeFloat(this.rotation.y);
        buf.writeFloat(this.rotation.z);

        buf.writeFloat(this.scale);
    }

    public static class Handler extends WorldModificationsMessageHandler<C3SetBlockProperties, C3SetBlockProperties>  {

        @Override
        protected void handleMessage(C3SetBlockProperties message, MessageContext ctx, World world, EntityPlayer player) {
            TileEntity entity = world.getTileEntity(message.blockPos);
            if(entity instanceof TTBlockEntity) {
                ((TTBlockEntity) entity).setTranslation(message.position);
                ((TTBlockEntity) entity).setRotation(message.rotation);
                ((TTBlockEntity) entity).setScale(message.scale);
                entity.markDirty();
            }
            TabulaTaxidermy.NETWORK.sendToDimension(new S4SyncBlockProperties(message.blockPos, message.position, message.rotation, message.scale), world.provider.getDimension());
        }
    }
}