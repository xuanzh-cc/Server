package com.zxcc.socket.filter.session;

import com.google.common.eventbus.EventBus;
import com.zxcc.game.protobuf.ModuleProtocol.ResponseMsg;
import com.zxcc.socket.core.CsnGenerator;
import com.zxcc.socket.core.HandlerDispatch;
import com.zxcc.socket.event.ChannelCloseEvent;
import com.zxcc.socket.model.Session;
import com.zxcc.socket.util.DelayedElement;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.net.SocketAddress;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

/**
 * 会话管理过滤器，作为{@link SessionManager}的过滤器实现方式，用于管理的会话创建与关闭
 * 
 * @author LJJ
 */
@Sharable
public class SessionManagerFilter extends ChannelInboundHandlerAdapter implements SessionManager,ChannelOutboundHandler,
		ApplicationContextAware
{

	private static final Logger logger = LoggerFactory.getLogger(SessionManagerFilter.class);

	// /** 当前的事件监听器 */
	// private Map<Type, SessionListener> listeners = new HashMap<Type,
	// SessionListener>();

	@Autowired
	private EventBus eventBus;
	/** 已经鉴别用户身份的会话，Key:用户身份标识，Value:{@link Channel} */
	private Map<Object, Channel> identities = new ConcurrentHashMap<Object, Channel>();

	/** 匿名会话，Key:{@link Channel#id()}，Value:{@link Channel} */
	private Map<String, Channel> anonymous = new ConcurrentHashMap<String, Channel>();

	/** 已经关闭的已鉴权会话，Key:用户身份标识，Value:{@link Channel} */
	private Map<Object, Channel> closeds = new ConcurrentHashMap<Object, Channel>();

	/** */
	private static Map<String, Session> sessions = new ConcurrentHashMap<>();

	/** 被踢下线的会话属性 */
	private static final AttributeKey<Boolean> ATT_KICKED = AttributeKey.valueOf("kicked");

	/** CHANNEL_ID标识 */
	private static final AttributeKey<String> CHANNEL_ID = AttributeKey.valueOf("CHANNEL_ID");
	/** 原因标识 */
	private static final AttributeKey<Integer> ATT_CAUSE = AttributeKey.valueOf("cause");

	/** 延迟删除队列 */
	private DelayQueue<DelayedElement<Object>> removeQueue;

	/** 当前的通信控制器 */
	@Autowired
	private HandlerDispatch handler;

	/** 延迟时间(单位:秒) */
	private int delayTimes = 30;

	/** 开启调试模式 */
	private boolean debug;
	
	
	/**
	 * 接收消息缓存
	 */
	public static ConcurrentHashMap<Long, Integer> responseCache = new ConcurrentHashMap<Long, Integer>();
	/**
	 * 推送消息缓存
	 */
	public static ConcurrentHashMap<Long, List<ResponseMsg>> pushCache = new ConcurrentHashMap<Long, List<ResponseMsg>>();

	/**
	 * 序列号生成缓存器
	 */
	public static ConcurrentHashMap<Long, CsnGenerator> csnGenerators = new ConcurrentHashMap<Long, CsnGenerator>();


	@Override
	public void putIdentity(Object identity, Channel channel)
	{
		Attribute<String> channelId = channel.attr(CHANNEL_ID);
		String id = channelId.get();
		if (!sessions.containsKey(id))
		{
			Session session = new Session();
			session.setId((Long) identity);
			sessions.put(id, session);
		}
		onIdentified(channel);
		// attIdentity.set(identity);
	}

	
	@Override
	public void cleanCache(long owner) {
		pushCache.remove(owner);
		responseCache.remove(owner);
		csnGenerators.remove(owner);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
	{
		try
		{

			Channel channel = ctx.channel();
			Attribute<String> channelId = channel.attr(CHANNEL_ID);
			String id = channelId.get();
			Session session = sessions.get(id);
			//TODO 加拦截
			if(session == null)
			{
				ctx.write(msg, promise);
				return;
			}
			Object obj = session.getId();
			Attribute<Boolean> attProcess = channel.attr(ATT_PROCEED);
			Boolean bool = attProcess.get();

			if (obj != null && (bool == null || !bool))
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("发现未处理的用户身份标识[{}]", obj);
				}
//				onIdentified(channel);
				attProcess.set(true);
			}
			ctx.write( msg, promise);
		}
		catch (Exception e)
		{
			logger.error("", e);
			throw e;
		}
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
	{
		Channel channel = ctx.channel();
		onSessionClosed(channel);
		Attribute<String> channelId = channel.attr(CHANNEL_ID);
		String id = channelId.get();
		Session session = sessions.get(id);
		if (logger.isDebugEnabled())
		{

		
			if (session == null)
			{
				logger.debug("用户会话[{}]关闭移除",id);
				return;
			}
			Object obj = session.getId();
			if (obj != null)
			{
				logger.debug("用户[{}]会话[{}]关闭移除", obj, id);
			}
			else
			{
				logger.debug("会话[{}]关闭移除", obj, id);
			}
		}
		sessions.remove(id);
		super.handlerRemoved(ctx);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception
	{
		Channel channel = ctx.channel();
		Attribute<String> channelId = channel.attr(CHANNEL_ID);
		String id = channelId.get();

		if ( (id == null ))
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("发现未处理的用户身份标识[{}]", id);
			}
//			onIdentified(channel);
			channelId.set(UUID.randomUUID().toString());
			
		}
		onSessionOpened(channel);
		if (logger.isDebugEnabled())
		{
			logger.debug("会话[{}]创建添加", id);
		}
		super.handlerAdded(ctx);
	}

	// SessionManager 的逻辑代码

	/** 延迟移除处理线程代码 */
	private class DelayRunner implements Runnable
	{
		@Override
		public void run()
		{
			while (true)
			{
				try
				{
					DelayedElement<Object> e = removeQueue.take();
					Object identity = e.getContent();
					Channel closed = closeds.remove(identity);
					if (closed != null)
					{
						Integer cause = closed.attr(ATT_CAUSE).get();
						if (cause == null)
						{
							cause = 0;
						}

						eventBus.post(ChannelCloseEvent.valueOf(identity, cause, closed));
						// ChannelId id = closed.id();

					}
				}
				catch (InterruptedException e)
				{
					logger.error("会话延迟移除处理线程被非法打断", e);
				}
			}
		}
	}

	@PostConstruct
	public void initialize() throws Exception
	{
		if (!isDebug())
		{
			// handler = this.applicationContext.getBean(handlerName,
			// Handler.class);
			Assert.notNull(handler, "通信控制器不能为空，请通过setHandlerName(String)设置正确的通信控制器名");
		}

		if (hasDelayTimes())
		{
			removeQueue = new DelayQueue<DelayedElement<Object>>();
			Thread thread = new Thread(new DelayRunner(), "会话延迟移除处理");
			thread.setDaemon(true);
			thread.start();
		}
	}

	@Override
	public void send(ResponseMsg request, Object... ids)
	{
		if (ids == null || ids.length == 0)
		{
			return;
		}
		// 获取对应的会话
		List<Channel> channels = new ArrayList<Channel>(ids.length);
		for (Object id : ids)
		{
			Channel session = identities.get(id);
			if (session == null)
			{
				continue;
			}
			channels.add(session);
		}

		// 发送信息
		if (channels.isEmpty())
		{
			return;
		}
		logger.debug(request.toString());
		handler.send(request, channels.toArray(new Channel[0]));
	}

	@Override
	public void send(ResponseMsg request, Channel... channels)
	{
		if (channels == null)
		{
			return;
		}
		handler.send(request, channels);
	}

	@Override
	public void sendAll(ResponseMsg request)
	{
		sendAllIdentified(request);
		sendAllAnonymous(request);
	}

	@Override
	public void sendAllIdentified(ResponseMsg request)
	{
		handler.send(request, identities.values().toArray(new Channel[0]));
	}

	@Override
	public void sendAllAnonymous(ResponseMsg request)
	{
		handler.send(request, anonymous.values().toArray(new Channel[0]));
	}

	@Override
	public boolean isOnline(Object... ids)
	{
		for (Object id : ids)
		{
			if (!identities.containsKey(id))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public Collection<Object> getOnlineIdentities()
	{
		HashSet<Object> result = new HashSet<Object>();
		result.addAll(identities.keySet());
		return result;
	}

	@Override
	public Collection<?> kick(int cause, Object... ids)
	{
		HashSet<Object> result = new HashSet<Object>();
		for (Object id : ids)
		{
			Channel channel = identities.get(id);
			if (channel == null)
			{
				continue;
			}

			// 设置会话状态
			Attribute<Boolean> attKicked = channel.attr(ATT_KICKED);
			Attribute<Integer> attCause = channel.attr(ATT_CAUSE);
			attKicked.set(true);
			attCause.set(cause);
			// ATT_CAUSE.setValue(channel, cause);
			if (cause < 0)
			{
				Attribute<Boolean> attIgnoreEvent = channel.attr(ATT_IGNORE_EVENT);
				attIgnoreEvent.set(true);
			}
			// 先移除被踢的会话再关闭会话，这是为了避免sessionClosed被异步触发产生的未知性
			remove(channel);
			channel.close();
			channel.disconnect();
			

			result.add(id);
		}
		return result;
	}

	@Override
	public Collection<?> kickAll(int cause)
	{
		HashSet<Object> result = new HashSet<Object>();
		for (Entry<Object, Channel> entry : identities.entrySet())
		{
			Object id = entry.getKey();
			Channel channel = entry.getValue();
			// 设置会话状态

			Attribute<Boolean> attKicked = channel.attr(ATT_KICKED);
			Attribute<Integer> attCause = channel.attr(ATT_CAUSE);
			attKicked.set(true);
			attCause.set(cause);
			if (cause < 0)
			{
				Attribute<Boolean> attIgnoreEvent = channel.attr(ATT_IGNORE_EVENT);
				attIgnoreEvent.set(true);
			}
			// 先移除被踢的会话再关闭会话，这是为了避免sessionClosed被异步触发产生的未知性

			remove(channel);
			channel.close();
			// 发出会话关闭事件
			if (cause >= 0)
			{
				eventBus.post(ChannelCloseEvent.valueOf(id, cause, channel));
			}

			result.add(id);
		}
		return result;
	}

	@Override
	public Channel getSession(Object id)
	{
		Channel result = identities.get(id);

		if (result != null)
		{
			return result;
		}
		result = closeds.get(id);
		return result;
	}

	@Override
	public Collection<Channel> getAnonymous()
	{
		return anonymous.values();
	}

	@Override
	public void copyAttributes(Channel src, Channel dest)
	{
	}

	@Override
	public int count(boolean includeAnonymous)
	{
		int result = 0;
		result += identities.size();
		if (includeAnonymous)
		{
			result += anonymous.size();
		}
		return result;
	}

	// 监听的方法

	/**
	 * 响应会话创建
	 * 
	 * @param session
	 *            新创建的会话实例
	 */
	void onSessionOpened(Channel session)
	{
		add(session);
	}

	/**
	 * 响应会话关闭
	 * 
	 * @param channel
	 *            被关闭的会话实例
	 */
	void onSessionClosed(Channel channel)
	{

		Object identity = getIdentity(channel);
		remove(channel);
		if (identity == null)
		{
			return;
		}

		if (hasDelayTimes())
		{
			// 在有延时设置的情况下，将已经鉴权的会话放入延迟队列中
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, delayTimes);
			DelayedElement<Object> e = new DelayedElement<Object>(identity, calendar.getTime());
			removeQueue.put(e);

			closeds.put(identity, channel);
		}
		else
		{
			Attribute<Integer> attProcess = channel.attr(ATT_CAUSE);
			Integer cause = attProcess.get();
			if (cause == null)
			{
				cause = 0;
			}
			if(cause >= 0)
			{
				eventBus.post(ChannelCloseEvent.valueOf(identity, cause, channel));
			}
			

		}
	}

	/**
	 * 响应用户验证
	 * 
	 * @param channel
	 */
	void onIdentified(Channel channel)
	{
		Object identity = getIdentity(channel);
		if (identity == null)
		{
			return;
		}
		Attribute<String> channelId = channel.attr(CHANNEL_ID);
		String id = channelId.get();
		anonymous.remove(id);
		identities.put(identity, channel);

		// 处理有延迟移除情况下的会话内容复制
		if (hasDelayTimes())
		{
			Channel prev = closeds.remove(identity);
			if (prev != null)
			{
				// 复制会话属性
				copyAttributes(prev, channel);
				// fireReplacedEvent(identity, channel, prev);
				return;
			}
		}
		// fireIdentifiedEvent(identity, channel);
	}

	// 内部方法
	//
	// /** 会话替换事件 */
	// private void fireReplacedEvent(Object identity, Channel current, Channel
	// replaced) {
	// SessionListener listener = listeners.get(Type.REPLACED);
	// if (listener == null) {
	// return;
	// }
	// // if (!ATT_IGNORE_EVENT.getValue(current, false)) {
	// // listener.onEvent(new SessionReplacedEvent(SessionEventCause.NORMAL,
	// identity, current,
	// // replaced));
	// // }
	// }

	// /** 发出完成身份验证事件 */
	// private void fireIdentifiedEvent(Object identity, Channel session) {
	// SessionListener listener = listeners.get(Type.IDENTIFIED);
	// if (listener == null) {
	// return;
	// }
	// // if (!ATT_IGNORE_EVENT.getValue(session, false)) {
	// // listener.onEvent(new SessionEvent(SessionEventCause.NORMAL,
	// Type.IDENTIFIED, identity,
	// // session));
	// // }
	// }

	// /** 发出关闭事件 */
	// private void fireClosedEvent(Object identity, int cause, Channel session)
	// {
	// SessionListener listener = listeners.get(Type.CLOSED);
	// if (listener == null) {
	// return;
	// }
	// // if (!ATT_IGNORE_EVENT.getValue(session, false)) {
	// // listener.onEvent(new SessionEvent(cause, Type.CLOSED, identity,
	// session));
	// // }
	// }

	private void add(Channel channel)
	{
		Attribute<String> channelId = channel.attr(CHANNEL_ID);
		String id = channelId.get();
		anonymous.put(id, channel);
	}

	private void remove(Channel channel)
	{
		
		Attribute<String> channelId = channel.attr(CHANNEL_ID);
		String id = channelId.get();
		anonymous.remove(id);
		Object identity = getIdentity(channel);
		if (identity != null)
		{
			if (identities.get(identity) == channel)
			{
				identities.remove(identity);
			}

		}

	}

	public static Object getIdentity(Channel channel)
	{
		
		Attribute<String> channelId = channel.attr(CHANNEL_ID);
		String id = channelId.get();
		Session session = sessions.get(id);
		if (session == null)
		{
			return null;
		}
		Object obj = session.getId();
		return obj;
	}

	/**
	 * 检查是否有延迟时间设置
	 * 
	 * @return
	 */
	private boolean hasDelayTimes()
	{
		if (delayTimes > 0)
		{
			return true;
		}
		return false;
	}

	public boolean isDebug()
	{
		return debug;
	}

	public void setDebug(boolean debug)
	{
		this.debug = debug;
	}

	

	/**
	 * 设置会话真实移除的延迟时间
	 * 
	 * @param delayTimes
	 *            延迟时间(单位:秒)
	 */
	public void setDelayTimes(int delayTimes)
	{
		if (delayTimes <= 0)
		{
			throw new IllegalArgumentException("延迟时间[" + delayTimes + "]不能小于等于0");
		}
		this.delayTimes = delayTimes;
	}

	// public void setListeners(Map<Type, SessionListener> listeners) {
	// this.listeners = listeners;
	// }

	@SuppressWarnings("unused")
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;
	}

	
	@Override
	public int getMaxCSn(long owner) {
		Integer tmp = responseCache.get(owner);
		return tmp == null ? 0 : tmp;
	}
	
	
	
	Comparator<? super ResponseMsg> cmp = new Comparator<ResponseMsg>() {

		@Override
		public int compare(ResponseMsg o1, ResponseMsg o2) {
			if (o1.getHeader().getSsn() > o2.getHeader().getSsn()) {
				return 1;
			} else if (o1.getHeader().getSsn() < o2.getHeader().getSsn()) {
				return -1;
			}
			return 0;
		}

	};
	
	@Override
	public List<ResponseMsg> pushNextsMessage(long owner, int ssn) {

		List<ResponseMsg> result = new ArrayList<ResponseMsg>();
		List<ResponseMsg> tmp = pushCache.get(owner);
		

		if (tmp != null) {
			for (ResponseMsg message : tmp) {
				if (message == null) {
					continue;
				}
				if (message.getHeader().getSsn() > ssn) {
					result.add(message);
				}
			}
		}

		Collections.sort(result, cmp);
		return result;
	}


	 /**
     * Calls {@link ChannelHandlerContext#bind(SocketAddress, ChannelPromise)} to forward
     * to the next {@link ChannelOutboundHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress,
            ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }

    /**
     * Calls {@link ChannelHandlerContext#connect(SocketAddress, SocketAddress, ChannelPromise)} to forward
     * to the next {@link ChannelOutboundHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress,
            SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }

    /**
     * Calls {@link ChannelHandlerContext#disconnect(ChannelPromise)} to forward
     * to the next {@link ChannelOutboundHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise)
            throws Exception {
        ctx.disconnect(promise);
    }

    /**
     * Calls {@link ChannelHandlerContext#close(ChannelPromise)} to forward
     * to the next {@link ChannelOutboundHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise)
            throws Exception {
        ctx.close(promise);
    }

    /**
     * Calls {@link ChannelHandlerContext#close(ChannelPromise)} to forward
     * to the next {@link ChannelOutboundHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }

    /**
     * Calls {@link ChannelHandlerContext#read()} to forward
     * to the next {@link ChannelOutboundHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }

    

    /**
     * Calls {@link ChannelHandlerContext#flush()} to forward
     * to the next {@link ChannelOutboundHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

}
