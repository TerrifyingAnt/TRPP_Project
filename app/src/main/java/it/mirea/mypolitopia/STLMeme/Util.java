package it.mirea.mypolitopia.STLMeme;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Package com.hc.opengl
 * Created by HuaChao on 2016/7/28.
 */
public class Util {
    /**
     *
     * @param a
     * @return
     */
    public static FloatBuffer floatToBuffer(float[] a) {
        // первый инициализующий буфер, длина массива * 4, потому что поплавка аккаунты для 4 байтов
        ByteBuffer bb = ByteBuffer.allocateDirect(a.length * 4);
        // Массив Сортировать по NATIORDER
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = bb.asFloatBuffer();
        buffer.put(a);
        buffer.position(0);
        return buffer;
    }

    /**
     *
     * @param bytes
     * @param offset
     * @return
     */
    public static int byte4ToInt(byte[] bytes, int offset) {
        int b3 = bytes[offset + 3] & 0xFF;
        int b2 = bytes[offset + 2] & 0xFF;
        int b1 = bytes[offset + 1] & 0xFF;
        int b0 = bytes[offset + 0] & 0xFF;
        return (b3 << 24) | (b2 << 16) | (b1 << 8) | b0;
    }

    /**
     *
     * @param bytes
     * @param offset
     * @return
     */
    public static short byte2ToShort(byte[] bytes, int offset) {
        int b1 = bytes[offset + 1] & 0xFF;
        int b0 = bytes[offset + 0] & 0xFF;
        return (short) ((b1 << 8) | b0);
    }

    /**
     *
     * @param bytes
     * @param offset
     * @return
     */
    public static float byte4ToFloat(byte[] bytes, int offset) {
        return Float.intBitsToFloat(byte4ToInt(bytes, offset));
    }

}