package indp.elvis.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送过来的消息
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("received " + ctx.channel().remoteAddress() + "message: " + byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //send to client
        ctx.writeAndFlush(Unpooled.copiedBuffer("server received message, and send you a", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //exception
        System.out.println("channel closed: " + ctx.channel().remoteAddress());
        ctx.close();
    }

    public static void main(String[] args) {
        for (byte a : "EOF".getBytes(CharsetUtil.UTF_8))
            System.out.println(a);
    }
}
