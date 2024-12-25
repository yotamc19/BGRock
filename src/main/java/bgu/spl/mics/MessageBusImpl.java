package bgu.spl.mics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only one public method (in addition to getters which can be public solely for unit testing) may be added to this class
 * All other methods and members you add the class must be private.
 */
public class MessageBusImpl implements MessageBus {
	private static MessageBusImpl instance = null;
	private final ConcurrentHashMap<MicroService, BlockingQueue<Message>> microServiceQueues; // foreach MicroService, hold a queue of messages waiting to be handled
	private final ConcurrentHashMap<Class<? extends Event<?>>, ConcurrentLinkedQueue<MicroService>> eventSubscribers; // foreach event, hold the MicroServices which are subscribed to this event
	private final ConcurrentHashMap<Class<? extends Broadcast>, List<MicroService>> broadcastSubscribers; // foreach broadcast, hold the MicroServices which are subscribed to this broadcast
	private final ConcurrentHashMap<Event<?>, Future<?>> eventFutures; // foreach event, hold the Future of this event

	public MessageBusImpl() {
		instance = null;
		microServiceQueues = new ConcurrentHashMap<>();
		eventSubscribers = new ConcurrentHashMap<>();
		broadcastSubscribers = new ConcurrentHashMap<>();
		eventFutures = new ConcurrentHashMap<>();
	}

	public static synchronized MessageBusImpl getInstance() {
        if (instance == null) {
            instance = new MessageBusImpl();
        }
        return instance;
    }
	
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		eventSubscribers.putIfAbsent(type, new ConcurrentLinkedQueue<>());
		eventSubscribers.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		broadcastSubscribers.putIfAbsent(type, Collections.synchronizedList(new ArrayList<>()));
		broadcastSubscribers.get(type).add(m);
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		Future<T> f = (Future<T>)eventFutures.get(e);
		if (f != null) {
			f.resolve(result);
			eventFutures.remove(e);
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		List<MicroService> subs = broadcastSubscribers.get(b.getClass());
		if (subs != null) {
			for (MicroService m : subs) {
				BlockingQueue<Message> q = microServiceQueues.get(m);
				if (q != null) {
					q.add(b);
				} else {
					System.out.println("sendBroadcast - queue is null");
				}
			}
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		ConcurrentLinkedQueue<MicroService> subs = eventSubscribers.get(e.getClass());
		if (subs != null) {
			MicroService m = subs.poll();
			if (m != null) {
				subs.add(m);
				BlockingQueue<Message> q = microServiceQueues.get(m);
				// if (q != null) {
					q.add(e);
					Future<T> future = new Future<>();
					eventFutures.put(e, future);
					return future;
				// }
			}
		}
		return null;
	}

	@Override
	public void register(MicroService m) {
		microServiceQueues.putIfAbsent(m, new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(MicroService m) {
		microServiceQueues.remove(m);
		eventSubscribers.values().forEach(q -> q.remove(m));
		broadcastSubscribers.values().forEach(l -> l.remove(m));
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		BlockingQueue<Message> q = microServiceQueues.get(m);
		if (q == null) {
			throw new InterruptedException("This Micro Service is not registered");
		}
		return q.take();
	}
}
