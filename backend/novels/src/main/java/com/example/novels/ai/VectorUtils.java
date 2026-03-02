package com.example.novels.ai;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class VectorUtils {
    // float[] => byte[]
    public static byte[] toBytes(float[] v) {
        ByteBuffer bb = ByteBuffer.allocate(v.length * 4).order(ByteOrder.LITTLE_ENDIAN);
        for (float f : v)
            bb.putFloat(f);
        return bb.array();
    }

    // byte[] => float[]
    public static float[] toFloats(byte[] bytes, int dim) {
        if (bytes == null)
            throw new IllegalArgumentException("bytes is null");
        if (bytes.length != dim * 4) {
            throw new IllegalArgumentException(
                    "Invalid bytes length. expected=" + (dim * 4) + ", actual=" + bytes.length);
        }

        float[] v = new float[dim];
        ByteBuffer bb = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < dim; i++) {
            v[i] = bb.getFloat();
        }
        return v;
    }
}
