/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBufAllocator
 *  io.netty.channel.Channel
 *  io.netty.channel.Channel$Unsafe
 *  io.netty.channel.ChannelConfig
 *  io.netty.channel.ChannelFuture
 *  io.netty.channel.ChannelId
 *  io.netty.channel.ChannelMetadata
 *  io.netty.channel.ChannelOutboundInvoker
 *  io.netty.channel.ChannelPipeline
 *  io.netty.channel.ChannelProgressivePromise
 *  io.netty.channel.ChannelPromise
 *  io.netty.channel.EventLoop
 *  io.netty.util.Attribute
 *  io.netty.util.AttributeKey
 */
package com.trolmastercard.sexmod.world;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import java.net.SocketAddress;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;

public class FakeNetwork extends NetworkManager {
    public FakeNetwork(EnumPacketDirection enumPacketDirection) {
        super(enumPacketDirection);
    }

    public Channel channel() {
        return new Channel(){
            public ChannelId id() {
                return null;
            }

            public EventLoop eventLoop() {
                return null;
            }

            public Channel parent() {
                return null;
            }

            public ChannelConfig config() {
                return null;
            }

            public boolean isOpen() {
                return false;
            }

            public boolean isRegistered() {
                return false;
            }

            public boolean isActive() {
                return false;
            }

            public ChannelMetadata metadata() {
                return null;
            }

            public SocketAddress localAddress() {
                return null;
            }

            public SocketAddress remoteAddress() {
                return null;
            }

            public ChannelFuture closeFuture() {
                return null;
            }

            public boolean isWritable() {
                return false;
            }

            public long bytesBeforeUnwritable() {
                return 0L;
            }

            public long bytesBeforeWritable() {
                return 0L;
            }

            public Channel.Unsafe unsafe() {
                return null;
            }

            public ChannelPipeline pipeline() {
                return null;
            }

            public ByteBufAllocator alloc() {
                return null;
            }

            public ChannelPromise newPromise() {
                return null;
            }

            public ChannelProgressivePromise newProgressivePromise() {
                return null;
            }

            public ChannelFuture newSucceededFuture() {
                return null;
            }

            public ChannelFuture newFailedFuture(Throwable throwable) {
                return null;
            }

            public ChannelPromise voidPromise() {
                return null;
            }

            public ChannelFuture bind(SocketAddress socketAddress) {
                return null;
            }

            public ChannelFuture connect(SocketAddress socketAddress) {
                return null;
            }

            public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress2) {
                return null;
            }

            public ChannelFuture disconnect() {
                return null;
            }

            public ChannelFuture close() {
                return null;
            }

            public ChannelFuture deregister() {
                return null;
            }

            public ChannelFuture bind(SocketAddress socketAddress, ChannelPromise channelPromise) {
                return null;
            }

            public ChannelFuture connect(SocketAddress socketAddress, ChannelPromise channelPromise) {
                return null;
            }

            public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) {
                return null;
            }

            public ChannelFuture disconnect(ChannelPromise channelPromise) {
                return null;
            }

            public ChannelFuture close(ChannelPromise channelPromise) {
                return null;
            }

            public ChannelFuture deregister(ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public Channel read() {
                return null;
            }

            public ChannelFuture write(Object object) {
                return null;
            }

            public ChannelFuture write(Object object, ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public Channel flush() {
                return null;
            }

            public ChannelFuture writeAndFlush(Object object, ChannelPromise channelPromise) {
                return null;
            }

            public ChannelFuture writeAndFlush(Object object) {
                return null;
            }

            public <T> Attribute<T> attr(AttributeKey<T> attributeKey) {
                return new Attribute<T>(){
                    public T setIfAbsent(T t) {
                        return null;
                    }

                    public T getAndSet(T t) {
                        return null;
                    }

                    public AttributeKey<T> key() {
                        return null;
                    }

                    public T getAndRemove() {
                        return null;
                    }

                    public void remove() {
                    }

                    public T get() {
                        return null;
                    }

                    public boolean compareAndSet(T t, T t2) {
                        return false;
                    }

                    public void set(T t) {
                    }
                };
            }

            public <T> boolean hasAttr(AttributeKey<T> attributeKey) {
                return false;
            }

            @Override
            public int compareTo(Channel channel) {
                return 0;
            }

            ////io_netty_channel_ChannelOutboundInvoker_flush
            //@Override
            //public ChannelOutboundInvoker io_netty_channel_ChannelOutboundInvoker_flush() {
            //    return this.io_netty_channel_Channel_flush();
            //}

            //// io_netty_channel_ChannelOutboundInvoker_read
            //public ChannelOutboundInvoker io_netty_channel_ChannelOutboundInvoker_read() {
            //    return this.io_netty_channel_Channel_read();
            //}

            //public int compareTo(Object object) {
            //    return this.a((Channel)object);
            //}
        };
    }
}

