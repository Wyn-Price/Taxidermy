package com.wynprice.taxidermy.network;

import com.wynprice.taxidermy.DataHandler;
import com.wynprice.taxidermy.DataHeader;
import com.wynprice.taxidermy.GuiTaxidermyBlock;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

public class S6SendHeaders implements IMessage {

    private DataHandler<?> handler;
    private List<DataHeader> headers;

    public S6SendHeaders() {
    }

    public S6SendHeaders(DataHandler<?> handler, List<DataHeader> headers) {
        this.handler = handler;
        this.headers = headers;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.handler = DataHandler.read(buf);
        this.headers = new ArrayList<>();
        int size = buf.readShort();
        for (int i = 0; i < size; i++) {
            this.headers.add(DataHeader.readFromBuf(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        DataHandler.write(buf, this.handler);
        buf.writeShort(this.headers.size());
        for (DataHeader header : this.headers) {
            DataHeader.writeToBuf(header, buf);
        }
    }

    public static class Handler implements IMessageHandler<S6SendHeaders, IMessage> {

        @Override
        public IMessage onMessage(S6SendHeaders message, MessageContext ctx) {
            if(Minecraft.getMinecraft().currentScreen instanceof GuiTaxidermyBlock) {
                ((GuiTaxidermyBlock) Minecraft.getMinecraft().currentScreen).setList(message.handler, message.headers);
            }
            return null;
        }
    }
}