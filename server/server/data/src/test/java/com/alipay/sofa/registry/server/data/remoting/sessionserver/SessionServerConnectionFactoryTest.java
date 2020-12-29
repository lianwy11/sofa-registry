package com.alipay.sofa.registry.server.data.remoting.sessionserver;

import com.alipay.remoting.Connection;
import com.alipay.sofa.registry.common.model.ProcessId;
import com.alipay.sofa.registry.remoting.bolt.BoltChannel;
import com.alipay.sofa.registry.server.shared.env.ServerEnv;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.junit.Assert;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;

public class SessionServerConnectionFactoryTest {

    @Test
    public void testConnection() throws Exception {
        SessionServerConnectionFactory factory = new SessionServerConnectionFactory();
        final String IP1 = "66.66.66.66";
        final String IP2 = "66.66.66.65";

        MockBlotChannel channel1 = new MockBlotChannel(IP1, 5550);
        factory.registerSession(ServerEnv.PROCESS_ID, channel1);

        Assert.assertTrue(factory.containsConnection(ServerEnv.PROCESS_ID));
        Assert.assertEquals(1, factory.getSessionConnections().size());
        Assert.assertEquals(1, factory.getProcessIds().size());
        Assert.assertTrue(factory.getSessonConnectionMap().containsKey(IP1));


        MockBlotChannel channel2 = new MockBlotChannel(IP1, 5551);
        factory.registerSession(ServerEnv.PROCESS_ID, channel2);

        Assert.assertTrue(factory.containsConnection(ServerEnv.PROCESS_ID));
        Assert.assertEquals(1, factory.getSessionConnections().size());
        Assert.assertEquals(1, factory.getProcessIds().size());
        Assert.assertTrue(factory.getSessonConnectionMap().containsKey(IP1));


        ProcessId processId2 = ServerEnv.createProcessId();
        MockBlotChannel channel3 = new MockBlotChannel(IP2, 5551);
        factory.registerSession(processId2, channel3);

        Assert.assertTrue(factory.containsConnection(ServerEnv.PROCESS_ID));
        Assert.assertTrue(factory.containsConnection(processId2));
        Assert.assertEquals(2, factory.getProcessIds().size());
        Assert.assertTrue(factory.getSessonConnectionMap().containsKey(IP1));
        Assert.assertTrue(factory.getSessonConnectionMap().containsKey(IP2));

        channel1.connected = false;
        factory.sessionDisconnected(channel1);

        Assert.assertTrue(factory.containsConnection(ServerEnv.PROCESS_ID));
        Assert.assertTrue(factory.containsConnection(processId2));
        Assert.assertEquals(2, factory.getProcessIds().size());
        Assert.assertTrue(factory.getSessonConnectionMap().containsKey(IP1));
        Assert.assertTrue(factory.getSessonConnectionMap().containsKey(IP2));
        Assert.assertEquals(1, factory.getAllSessonConnections().get(IP1).size());
        Assert.assertEquals(1, factory.getAllSessonConnections().get(IP2).size());


        factory.sessionDisconnected(channel3);

        Assert.assertTrue(factory.containsConnection(ServerEnv.PROCESS_ID));
        Assert.assertTrue(!factory.containsConnection(processId2));
        Assert.assertEquals(1, factory.getProcessIds().size());
        Assert.assertTrue(factory.getSessonConnectionMap().containsKey(IP1));
        Assert.assertTrue(!factory.getSessonConnectionMap().containsKey(IP2));
        Assert.assertEquals(1, factory.getAllSessonConnections().get(IP1).size());
        Assert.assertEquals(0, factory.getAllSessonConnections().get(IP2).size());
    }

    private static final class MockBlotChannel extends  BoltChannel{
        final InetSocketAddress remote;
        final InetSocketAddress local = new InetSocketAddress(ServerEnv.IP, 9602);
        boolean connected = true;
        final Connection conn = new Connection(new MockNettyChannel());

        MockBlotChannel(String remoteAddress, int remotePort){
            this.remote = new InetSocketAddress(remoteAddress, remotePort);
        }
        @Override
        public InetSocketAddress getRemoteAddress() {
            return remote;
        }

        @Override
        public InetSocketAddress getLocalAddress() {
            return local;
        }

        @Override
        public boolean isConnected() {
            return connected;
        }

        public Connection getConnection() {
            return conn;
        }
    }

    private static final class MockNettyChannel implements   io.netty.channel.Channel{

        @Override
        public ChannelId id() {
            return null;
        }

        @Override
        public EventLoop eventLoop() {
            return null;
        }

        @Override
        public Channel parent() {
            return null;
        }

        @Override
        public ChannelConfig config() {
            return null;
        }

        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public boolean isRegistered() {
            return false;
        }

        @Override
        public boolean isActive() {
            return false;
        }

        @Override
        public ChannelMetadata metadata() {
            return null;
        }

        @Override
        public SocketAddress localAddress() {
            return null;
        }

        @Override
        public SocketAddress remoteAddress() {
            return null;
        }

        @Override
        public ChannelFuture closeFuture() {
            return null;
        }

        @Override
        public boolean isWritable() {
            return false;
        }

        @Override
        public long bytesBeforeUnwritable() {
            return 0;
        }

        @Override
        public long bytesBeforeWritable() {
            return 0;
        }

        @Override
        public Unsafe unsafe() {
            return null;
        }

        @Override
        public ChannelPipeline pipeline() {
            return null;
        }

        @Override
        public ByteBufAllocator alloc() {
            return null;
        }

        @Override
        public ChannelFuture bind(SocketAddress localAddress) {
            return null;
        }

        @Override
        public ChannelFuture connect(SocketAddress remoteAddress) {
            return null;
        }

        @Override
        public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
            return null;
        }

        @Override
        public ChannelFuture disconnect() {
            return null;
        }

        @Override
        public ChannelFuture close() {
            return null;
        }

        @Override
        public ChannelFuture deregister() {
            return null;
        }

        @Override
        public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
            return null;
        }

        @Override
        public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
            return null;
        }

        @Override
        public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
            return null;
        }

        @Override
        public ChannelFuture disconnect(ChannelPromise promise) {
            return null;
        }

        @Override
        public ChannelFuture close(ChannelPromise promise) {
            return null;
        }

        @Override
        public ChannelFuture deregister(ChannelPromise promise) {
            return null;
        }

        @Override
        public Channel read() {
            return null;
        }

        @Override
        public ChannelFuture write(Object msg) {
            return null;
        }

        @Override
        public ChannelFuture write(Object msg, ChannelPromise promise) {
            return null;
        }

        @Override
        public Channel flush() {
            return null;
        }

        @Override
        public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
            return null;
        }

        @Override
        public ChannelFuture writeAndFlush(Object msg) {
            return null;
        }

        @Override
        public ChannelPromise newPromise() {
            return null;
        }

        @Override
        public ChannelProgressivePromise newProgressivePromise() {
            return null;
        }

        @Override
        public ChannelFuture newSucceededFuture() {
            return null;
        }

        @Override
        public ChannelFuture newFailedFuture(Throwable cause) {
            return null;
        }

        @Override
        public ChannelPromise voidPromise() {
            return null;
        }

        @Override
        public <T> Attribute<T> attr(AttributeKey<T> key) {
            return new Attribute(){

                @Override
                public AttributeKey key() {
                    return null;
                }

                @Override
                public Object get() {
                    return null;
                }

                @Override
                public void set(Object value) {

                }

                @Override
                public Object getAndSet(Object value) {
                    return null;
                }

                @Override
                public Object setIfAbsent(Object value) {
                    return null;
                }

                @Override
                public Object getAndRemove() {
                    return null;
                }

                @Override
                public boolean compareAndSet(Object oldValue, Object newValue) {
                    return false;
                }

                @Override
                public void remove() {

                }
            };
        }

        @Override
        public <T> boolean hasAttr(AttributeKey<T> key) {
            return false;
        }

        @Override
        public int compareTo(Channel o) {
            return 0;
        }
    }


}
