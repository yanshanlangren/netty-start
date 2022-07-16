package indp.elvis.server.handler;

import io.netty.channel.ChannelOutboundHandlerAdapter;

/**
 * ChannelOutboundHandler处理器常用的事件有：
 * 端口绑定 bind。
 * 连接服务端 connect。
 * 写事件 write。
 * 刷新时间 flush。
 * 读事件 read。
 * 主动断开连接 disconnect。
 * 关闭 channel 事件 close。
 */
public class MyChannelOutboundHandler extends ChannelOutboundHandlerAdapter {
}
