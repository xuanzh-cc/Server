package com.zxcc.socket.core;

import com.zxcc.game.protobuf.ModuleProtocol.Header;
import com.zxcc.game.protobuf.ModuleProtocol.RequestMsg;
import com.zxcc.game.protobuf.ModuleProtocol.ResponseMsg;
import com.zxcc.socket.filter.session.SessionManagerFilter;
import com.zxcc.socket.model.Cmd;
import com.zxcc.socket.model.Result;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class HandlerDispatch 
{
	
	private static final Logger logger = LoggerFactory.getLogger(HandlerDispatch.class);
	private ExecutorService messageExecutor;
	/**服务是否运行标识*/
	private boolean running = false;
	
	@Autowired
	private CmdRegister cmdRegister;
	
	@PostConstruct
	public void init() {
			if(!running)
			{
				running = true;
				messageExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
				
			}
			
	}
	
	
	
	
	public void stop() {
		running = false;
	}


	public void addMessageQueue(ChannelHandlerContext ctx,RequestMsg msg) {
		MessageWorker messageWorker = new MessageWorker(ctx,msg);
		this.messageExecutor.execute(messageWorker);
	}
	
	
	private final class MessageWorker implements Runnable {
		private RequestMsg msg;
		private ChannelHandlerContext ctx;
		private MessageWorker(ChannelHandlerContext ctx,RequestMsg messageQueue) {
			this.msg = messageQueue;
			this.ctx =ctx;
		}

		public void run() {
			try {
				handMessageQueue();
			} finally {

			}
		}

		/**
		 * 处理消息队列
		 */
		private void handMessageQueue() {
			logger.debug("received : " + msg.toString());
			Header header = msg.getHeader();

//			logger.debug(header.toString());

			ResponseMsg.Builder builder = ResponseMsg.newBuilder().setHeader(
					Header.newBuilder().setCsn(header.getCsn())
							.setCmd(header.getCmd()).setMod(header.getMod()).setState(0));
			Cmd cmd = Cmd.valueOf(header.getCmd(), header.getMod());
			
		
			Object value = SessionManagerFilter.getIdentity(ctx.channel());
		
			if (value != null) {
				SessionManagerFilter.responseCache.put((Long) value, msg.getHeader().getCsn());
			}
			MethodInvoke methodInvoke = cmdRegister.getProcessor(cmd);
			Result result = methodInvoke.invoke(msg, ctx.channel());
			if(result.getBody() != null)
			{
				builder.setBody(result.getBody());
			}
			
			builder.setCode(result.getCode());
			logger.debug("send header : " + builder.build().toString());
			send(builder.build(), ctx.channel());
		}
	}
	
	public void send(ResponseMsg msg, Channel... channels)
	{
		if (channels.length != 0)
		{
			for (Channel channel : channels)
			{
				if (!channel.isActive()) {
					continue;
				}
				
				Object value = SessionManagerFilter.getIdentity(channel);
				
				if (value != null) {
					

					CsnGenerator csnGernerator = SessionManagerFilter.csnGenerators.get((Long) value);
					if (csnGernerator == null) {
						csnGernerator = new CsnGenerator();
						SessionManagerFilter.csnGenerators.put((Long) value, csnGernerator);
					}
					synchronized (csnGernerator) {
						List<ResponseMsg> list = SessionManagerFilter.pushCache.get((Long) value);
						Header.Builder hbuild = Header.newBuilder(msg.getHeader()).setSsn(csnGernerator.next());
						msg = ResponseMsg.newBuilder(msg).setHeader(hbuild).build();
						if (list == null) {
							list = new ArrayList<ResponseMsg>();
							list.add(msg);
							SessionManagerFilter.pushCache.put((Long) value, list);
						} else {
							if (!list.contains(msg)) {

								if (list.size() > 10) {
									list.remove(0);
								}
								list.add(msg);
							}
						}
						
				}
				
			}
				
			channel.writeAndFlush(msg);
		}
			
		}
	}
	
	@PreDestroy
	public void destroy()
	{
		messageExecutor.shutdown();
	}
}
