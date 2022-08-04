package indp.elvis.server;

import indp.elvis.server.handler.MyServerHandler;
import indp.elvis.server.handler.ScheduleTaskQueueHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloWorld {
    public static void main(String[] args) throws Exception {
        //创建两个线程组 boosGroup、workerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务端的启动对象，设置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //设置两个线程组boosGroup和workerGroup
            bootstrap.group(bossGroup, workerGroup)
                    /**
                     * 设置服务端通道实现类型
                     * NioSocketChannel： 异步非阻塞的客户端 TCP Socket 连接。
                     * NioServerSocketChannel： 异步非阻塞的服务器端 TCP Socket 连接。
                     * OioSocketChannel： 同步阻塞的客户端 TCP Socket 连接。
                     * OioServerSocketChannel： 同步阻塞的服务器端 TCP Socket 连接。
                     */
                    .channel(NioServerSocketChannel.class)
                    //设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //设置打开TCP keepalive超时验证
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //设置关闭Nagle算法, 以网络通信效率为代价降低网络延迟
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //使用匿名内部类的形式初始化通道对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //给pipeline管道设置处理器
                            /**
                             * 处理器Handler主要分为两种：
                             * ChannelInboundHandlerAdapter(入站处理器): 数据从底层java NIO Channel到Netty的Channel
                             * ChannelOutboundHandler(出站处理器): 通过Netty的Channel来操作底层的java NIO Channel
                             */
                            socketChannel.pipeline().addLast(new MyServerHandler()).addLast(new ScheduleTaskQueueHandler()).addLast(new ScheduleTaskQueueHandler());
                        }
                    });//给workerGroup的EventLoop对应的管道设置处理器
            log.info("java技术爱好者的服务端已经准备就绪...");
            //绑定端口号，启动服务端
            ChannelFuture channelFuture = bootstrap.bind(6666).sync();
            //添加监听器
            channelFuture.addListener(new ChannelFutureListener() {
                //使用匿名内部类，ChannelFutureListener接口
                //重写operationComplete方法
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    //判断是否操作成功
                    if (future.isSuccess()) {
                        log.info("连接成功");
                    } else {
                        log.info("连接失败");
                    }
                }
            });

            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
