package com.nitinr.livelink;

import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class ImageProcessing {

    public static String processSnapShot(Image image, int width, int height) {
        byte[] data;
        data = NV21toJPEG(getImageBytes(image),
                width, height);

        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    private static byte[] getImageBytes(Image image) {
        byte[] imageBytes = null;
        try {
            ByteBuffer byteBufferY = image.getPlanes()[0].getBuffer();
            ByteBuffer byteBufferU = image.getPlanes()[1].getBuffer();
            ByteBuffer byteBufferV = image.getPlanes()[2].getBuffer();

//            byte[] bytes = new byte[byteBufferY.remaining() + byteBufferU.remaining() + byteBufferV.remaining()];
//            byteBufferY.get(bytes, 0, byteBufferY.remaining());
//            byteBufferU.get(bytes, byteBufferY.remaining(), byteBufferV.remaining());
//            byteBufferV.get(bytes, byteBufferY.remaining() + byteBufferV.remaining(), byteBufferU.remaining());

            imageBytes = new byte[byteBufferY.remaining()];
            byteBufferY.get(imageBytes);

            image.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageBytes;
    }

    private static byte[] NV21toJPEG(byte[] nv21, int width, int height) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YuvImage yuv = new YuvImage(nv21, ImageFormat.NV21, width, height, null);
        yuv.compressToJpeg(new Rect(0, 0, width, height), 100, out);
        return out.toByteArray();
    }
}
