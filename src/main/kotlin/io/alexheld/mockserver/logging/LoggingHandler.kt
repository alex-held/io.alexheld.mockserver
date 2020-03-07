import org.slf4j.*

/*
package io.alexheld.mockserver.logging

import io.netty.buffer.*
import io.netty.channel.*
import io.netty.handler.ssl.*
import org.slf4j.*
import java.net.*
import java.nio.charset.*

*/

/**
 * @author alex held
 *//*

object Character {
    val NEWLINE: String get() = System.getProperty("line.separator")
}

class LoggingHandler(private val logger: Logger) : ChannelDuplexHandler() {

    private val NEW_LINE = "\n"
    private val BYTE2HEX = arrayOfNulls<String>(256)
    private val HEXPADDING = arrayOfNulls<String>(16)
    private val BYTEPADDING = arrayOfNulls<String>(16)
    private val BYTE2CHAR = CharArray(256)


    init {
        var i = 0

        // Generate the lookup table for byte-to-hex-dump conversion
        i = 0
        while (i < 10) {
            BYTE2HEX[i] = " 0$i"
            i++
        }

        while (i < 16) {
            BYTE2HEX[i] = " 0" + ('a' + i - 10)
            i++
        }
        while (i < BYTE2HEX.size) {
            BYTE2HEX[i] = " " + Integer.toHexString(i)
            i++
        }

        // Generate the lookup table for hex dump paddings
        i = 0
        while (i < HEXPADDING.size) {
            val padding: Int = HEXPADDING.size - i
            val buf = StringBuilder(padding * 3)
            for (j in 0 until padding) {
                buf.append("   ")
            }
            HEXPADDING[i] = buf.toString()
            i++
        }

        // Generate the lookup table for byte dump paddings
        i = 0
        while (i < BYTEPADDING.size) {
            val padding: Int = BYTEPADDING.size - i
            val buf = StringBuilder(padding)
            for (j in 0 until padding) {
                buf.append(' ')
            }
            BYTEPADDING[i] = buf.toString()
            i++
        }

        // Generate the lookup table for byte-to-char conversion

        i = 0
        while (i < BYTE2CHAR.size) {
            if (i <= 0x1f || i >= 0x7f) {
                BYTE2CHAR[i] = '.'
            } else {
                BYTE2CHAR[i] = i.toChar()
            }
            i++
        }
    }

    fun addLoggingHandler(ctx: ChannelHandlerContext) {
        val pipeline = ctx.pipeline()
        if (pipeline[LoggingHandler::class.java] != null) {
            pipeline.remove(LoggingHandler::class.java)
        }
        val context = pipeline.context(SslHandler::class.java)
        if (context != null) {
            pipeline.addAfter(context.name(), "LoggingHandler#0", this)
        } else {
            pipeline.addFirst(this)
        }
    }

    private fun logMessage(ctx: ChannelHandlerContext, eventName: String, msg: Any) {
        logger.trace(format(ctx, formatMessage(eventName, msg)))
    }

    protected fun format(ctx: ChannelHandlerContext, message: String?): String? {
        var chStr = ctx.channel().toString() + ' ' + message
        if (logger.isTraceEnabled) {
            chStr += NEW_LINE.toString() + "channel: " + ctx.channel().id() + NEW_LINE + "current: " + ctx.name() + NEW_LINE + "pipeline: " + ctx.pipeline().names() + NEW_LINE
        }
        return chStr
    }

    private fun formatMessage(eventName: String, msg: Any): String? {
        return (if (msg is ByteBuf) {
            formatByteBuf(eventName, msg)
        } else if (msg is ByteBufHolder) {
            formatByteBufHolder(eventName, msg)
        } else {
            formatNonByteBuf(eventName, msg)
        })
    }


    private fun formatByteBuf(eventName: String, buf: ByteBuf): String? {
        val length = buf.readableBytes()
        val rows = length / 16 + (if (length % 15 == 0) 0 else 1) + 4
        val dump = StringBuilder(rows * 80 + eventName.length + 16)
        dump.append(eventName).append('(').append(length).append('B').append(')')
            .append(NEW_LINE)
            .append("         +-------------------------------------------------+")
            .append(Character.NEWLINE)
            .append("         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |")
            .append(Character.NEWLINE)
            .append("+--------+-------------------------------------------------+----------------+")
        val startIndex = buf.readerIndex()
        val endIndex = buf.writerIndex()
        var i: Int
        i = startIndex
        while (i < endIndex) {
            val relIdx = i - startIndex
            val relIdxMod16 = relIdx and 15
            if (relIdxMod16 == 0) {
                dump.append(Character.NEWLINE)
                dump.append(java.lang.Long.toHexString(relIdx.toLong() and 0xFFFFFFFFL or 0x100000000L))
                dump.setCharAt(dump.length - 9, '|')
                dump.append('|')
            }
            dump.append(BYTE2HEX[buf.getUnsignedByte(i).toInt()])
            if (relIdxMod16 == 15) {
                dump.append(" |")
                if (i > 15 && buf.readableBytes() > i) {
                    dump.append(buf.toString(i - 15, 16, StandardCharsets.UTF_8).replace("" + NEW_LINE.toRegex(), "/").replace("\r".toRegex(), "/"))
                } else {
                    for (j in i - 15..i) {
                        dump.append(BYTE2CHAR[buf.getUnsignedByte(j).toInt()])
                    }
                }
                dump.append('|')
            }
            i++
        }
        if (i - startIndex and 15 != 0) {
            val remainder = length and 15
            dump.append(HEXPADDING[remainder])
            dump.append(" |")
            for (j in i - remainder until i) {
                dump.append(BYTE2CHAR[buf.getUnsignedByte(j).toInt()])
            }
            dump.append(BYTEPADDING[remainder])
            dump.append('|')
        }
        dump.append(Character.NEWLINE).append("+--------+-------------------------------------------------+----------------+")
        return dump.toString()
    }

    private fun formatByteBufHolder(eventName: String, msg: ByteBufHolder): String? {
        return formatByteBuf(eventName, msg.content())
    }

    private fun formatNonByteBuf(eventName: String, msg: Any): String? {
        val msgAsString = msg.toString()
        return eventName + "(rel:" + msgAsString.length + ")" + ": " + msgAsString
    }


    @Throws(java.lang.Exception::class)
    override fun channelRegistered(ctx: ChannelHandlerContext?) {
        logger.trace(format(ctx!!, "REGISTERED"))
        super.channelRegistered(ctx)
    }

    @Throws(java.lang.Exception::class)
    override fun channelUnregistered(ctx: ChannelHandlerContext?) {
        logger.trace(format(ctx!!, "UNREGISTERED"))
        super.channelUnregistered(ctx)
    }

    @Throws(java.lang.Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext?) {
        logger.trace(format(ctx!!, "ACTIVE"))
        super.channelActive(ctx)
    }

    @Throws(java.lang.Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext?) {
        logger.trace(format(ctx!!, "INACTIVE"))
        super.channelInactive(ctx)
    }

    @Throws(java.lang.Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable) {
        logger.trace(format(ctx!!, "EXCEPTION: $cause"), cause)
        super.exceptionCaught(ctx, cause)
    }

    @Throws(java.lang.Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any) {
        if (evt is SslHandshakeCompletionEvent) {
            logger.trace(format(ctx!!, "SslHandshakeCompletionEvent: "), evt.cause())
        } else if (evt is java.lang.Exception) {
            logger.trace(format(ctx!!, "Exception: "), evt)
        } else {
            logger.trace(format(ctx!!, "USER_EVENT: $evt"))
        }
        super.userEventTriggered(ctx, evt)
    }

    @Throws(java.lang.Exception::class)
    override fun bind(ctx: ChannelHandlerContext?, localAddress: SocketAddress, promise: ChannelPromise?) {
        logger.trace(format(ctx!!, "BIND($localAddress)"))
        super.bind(ctx, localAddress, promise)
    }

    @Throws(java.lang.Exception::class)
    override fun connect(ctx: ChannelHandlerContext?, remoteAddress: SocketAddress, localAddress: SocketAddress, promise: ChannelPromise?) {
        logger.trace(format(ctx!!, "CONNECT($remoteAddress, $localAddress)"))
        super.connect(ctx, remoteAddress, localAddress, promise)
    }

    @Throws(java.lang.Exception::class)
    override fun disconnect(ctx: ChannelHandlerContext?, promise: ChannelPromise?) {
        logger.trace(format(ctx!!, "DISCONNECT()"))
        super.disconnect(ctx, promise)
    }

    @Throws(java.lang.Exception::class)
    override fun close(ctx: ChannelHandlerContext?, promise: ChannelPromise?) {
        logger.trace(format(ctx!!, "CLOSE()"))
        super.close(ctx, promise)
    }

    @Throws(java.lang.Exception::class)
    override fun deregister(ctx: ChannelHandlerContext?, promise: ChannelPromise?) {
        logger.trace(format(ctx!!, "DEREGISTER()"))
        super.deregister(ctx, promise)
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any?) {
        logMessage(ctx, "RECEIVED", msg!!)
        ctx.fireChannelRead(msg)
    }

    override fun write(ctx: ChannelHandlerContext, msg: Any?, promise: ChannelPromise?) {
        logMessage(ctx, "WRITE", msg!!)
        ctx.write(msg, promise)
    }

    override fun flush(ctx: ChannelHandlerContext) {
        logger.trace(format(ctx, "FLUSH"))
        ctx.flush()
    }

}
*/

class LoggingHandler(private val logger: Logger){

}
