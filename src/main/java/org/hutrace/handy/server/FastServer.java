package org.hutrace.handy.server;

import org.hutrace.handy.config.ConfigSetterLoader;
import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.exception.AppLoaderException;
import org.hutrace.handy.exception.HandyserveException;
import org.hutrace.handy.language.LanguageLoader;
import org.hutrace.handy.loader.Loader;
import org.hutrace.handy.mapping.MappingLoader;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * 程序入口类
 * <p>直接调用{@link #instance()}静态方法获取实例后调用{@link #run()}即可启动程序
 * <p>你可以再这之前调用{@link ConfigSetterLoader#setResource(String)}
 * 或者{@link ConfigSetterLoader#setDefaultSetter(org.hutrace.handy.config.ConfigSetter)}
 * 这两个静态方法的其中一个来设置配置信息，如果你都没有调用，FastServer会去加载application.xml或者application.json
 * @author hu trace
 *
 */
public final class FastServer {

	public static final String SERVER_NAME = "fastserver";
	public static final String SERVER_VERSION = "/5.0.1";
	
	private long startTime = System.currentTimeMillis();
	
	private FastServer() {}
	
	private void loader() throws AppLoaderException {
		loaderExecute(LanguageLoader.instance);
		loaderExecute(new ConfigSetterLoader());
		if(Configuration.scan() != null && Configuration.scan().length > 0) {
			loaderExecute(new MappingLoader());
		}
		if(Configuration.loaders() != null) {
			for(Loader loader : Configuration.loaders()) {
				loaderExecute(loader);
			}
		}
	}
	
	private void loaderExecute(Loader loader) throws AppLoaderException {
		loader.execute();
	}

	private void start() throws HandyserveException {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup woker = new NioEventLoopGroup();
		EventExecutorGroup businessGroup = new DefaultEventExecutorGroup(
				Configuration.threadPools(),
				new DefaultThreadFactory(Configuration.threadName()));
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		try {
			serverBootstrap.channel(NioServerSocketChannel.class)
					.group(boss, woker)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childHandler(new ChannelInitializer(businessGroup));
			ChannelFuture future = serverBootstrap.bind(Configuration.port()).sync();
			System.err.println("The Server Post Is " + Configuration.port() +
					"\r\nThe fastserver started in " + (System.currentTimeMillis() - startTime) + "ms\r\n");
			future.channel().closeFuture().sync();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			boss.shutdownGracefully();
			woker.shutdownGracefully();
		}
	}

	/**
	 * 开始启动程序
	 * @return 
	 * @throws HandyserveException
	 */
	public void run() {
		try {
			System.err.println("Welcome Useing " + SERVER_NAME + SERVER_VERSION);
			System.err.println("Your application version is : " + Configuration.appVersion());
			start();
		}catch (HandyserveException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取FastServer实例
	 * @return 
	 * @throws HandyserveException
	 */
	public static FastServer instance() throws HandyserveException {
		FastServer server = new FastServer();
		server.loader();
		return server;
	}

}
