package indp.elvis.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * ChannelInboundHandlerAdapter处理器常用的事件有：
 * 注册事件 fireChannelRegistered。
 * 连接建立事件 fireChannelActive。
 * 读事件和读完成事件 fireChannelRead、fireChannelReadComplete。
 * 异常通知事件 fireExceptionCaught。
 * 用户自定义事件 fireUserEventTriggered。
 * Channel 可写状态变化事件 fireChannelWritabilityChanged。
 * 连接关闭事件 fireChannelInactive。
 */

@Slf4j
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送过来的消息
        ByteBuf byteBuf = (ByteBuf) msg;
        log.info("MyServerHandler收到客户端" + ctx.channel().remoteAddress() + "发送的消息：" + byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //发送消息给客户端
        log.info("channel 读取消息完毕...");
        ctx.writeAndFlush(Unpooled.copiedBuffer("服务端已收到消息，并给你发送一个问号?", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //发生异常，关闭通道
        ctx.close();
    }
}
