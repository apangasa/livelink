package com.nitinr.livelink;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.ar.core.exceptions.NotYetAvailableException;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ViewRenderable viewRenderable;
    private boolean snapShotTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            Toast.makeText(this, "ARCore Scene Form not supported", Toast.LENGTH_LONG)
                    .show();
        }

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        assert arFragment != null;
        arFragment.getArSceneView().getPlaneRenderer().setEnabled(false);

        createRenderable();

        //arFragment.getArSceneView().getScene().addOnUpdateListener(this::onSceneUpdate);
        onSceneUpdate(null);
    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        String openGlVersionString =
                ((ActivityManager) Objects.requireNonNull(activity.getSystemService(Context.ACTIVITY_SERVICE)))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    private void createRenderable() {
        ViewRenderable.builder()
                .setView(this, R.layout.activity_renderable)
                .build()
                .thenAccept(this::addToScene);
    }

    private void addToScene(ViewRenderable viewRenderable) {
        this.viewRenderable = viewRenderable;

        Vector3 forward = arFragment.getArSceneView().getScene().getCamera().getForward();
        Vector3 worldPosition = arFragment.getArSceneView().getScene().getCamera().getWorldPosition();
        Vector3 position = Vector3.add(forward, worldPosition);

        Vector3 direction = Vector3.subtract(worldPosition, position);
        direction.y = position.y;

        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setWorldPosition(position);
        node.setLookDirection(direction);
        node.setParent(arFragment.getArSceneView().getScene());

        Node render = new Node();
        render.setParent(node);
        render.setRenderable(viewRenderable);
    }

    private byte[]  snapShot() {
        try {
            Image snapShot = Objects.requireNonNull(arFragment.getArSceneView().getArFrame()).acquireCameraImage();
            ByteBuffer byteBufferY = snapShot.getPlanes()[0].getBuffer();
            ByteBuffer byteBufferU = snapShot.getPlanes()[1].getBuffer();
            ByteBuffer byteBufferV = snapShot.getPlanes()[2].getBuffer();

//            byte[] bytes = new byte[byteBufferY.remaining() + byteBufferU.remaining() + byteBufferV.remaining()];
//            byteBufferY.get(bytes, 0, byteBufferY.remaining());
//            byteBufferU.get(bytes, byteBufferY.remaining(), byteBufferV.remaining());
//            byteBufferV.get(bytes, byteBufferY.remaining() + byteBufferV.remaining(), byteBufferU.remaining());

            byte[] bytes = new byte[byteBufferY.remaining()];
            byteBufferY.get(bytes);


            //String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
//            Log.d(TAG, "START");
//            Log.d(TAG, base64);
//            Log.d(TAG, "END");

//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            YuvImage yuv = new YuvImage(bytes, ImageFormat.YUV_420_888, snapShot.getWidth(), snapShot.getHeight(), null);
//            yuv.compressToJpeg(new Rect(0, 0, snapShot.getWidth(), snapShot.getHeight()), 100, out);
//
//            byte[] imageBytes = out.toByteArray();
//            Bitmap bitmapImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//            Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
//
//            assert viewRenderable != null;
//
//            ImageView iv = viewRenderable.getView().findViewById(R.id.imageView);
//            iv.setImageBitmap(bitmapImage);
//            iv.setImageResource(R.drawable.ic_launcher_foreground);
            snapShotTaken = true;
            snapShot.close();

            return bytes;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private byte[] NV21toJPEG(byte[] nv21, int width, int height) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YuvImage yuv = new YuvImage(nv21, ImageFormat.NV21, width, height, null);
        yuv.compressToJpeg(new Rect(0, 0, width, height), 100, out);
        return out.toByteArray();
    }

    public void WriteImageInformation(Image image, String path) {

        byte[] data = null;
        data = NV21toJPEG(snapShot(),
                image.getWidth(), image.getHeight());

        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        Log.d(TAG, "end");
//        BufferedOutputStream bos = null;
//        try {
//            bos = new BufferedOutputStream(new FileOutputStream(path));
//            bos.write(data);
//            bos.flush();
//            bos.close();
//
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
    }

    public static int[] convertYUV420_NV21toRGB8888(byte [] data, int width, int height) {
        int size = width*height;
        int offset = size;
        int[] pixels = new int[size];
        int u, v, y1, y2, y3, y4;

        for(int i=0, k=0; i < size; i+=2, k+=2) {
            y1 = data[i  ]&0xff;
            y2 = data[i+1]&0xff;
            y3 = data[width+i  ]&0xff;
            y4 = data[width+i+1]&0xff;

            u = data[offset+k  ]&0xff;
            v = data[offset+k+1]&0xff;
            u = u-128;
            v = v-128;

            pixels[i  ] = convertYUVtoRGB(y1, u, v);
            pixels[i+1] = convertYUVtoRGB(y2, u, v);
            pixels[width+i  ] = convertYUVtoRGB(y3, u, v);
            pixels[width+i+1] = convertYUVtoRGB(y4, u, v);

            if (i!=0 && (i+2)%width==0)
                i+=width;
        }

        return pixels;
    }

    private static int convertYUVtoRGB(int y, int u, int v) {
        int r,g,b;

        r = y + (int)(1.402f*v);
        g = y - (int)(0.344f*u +0.714f*v);
        b = y + (int)(1.772f*u);
        r = r>255? 255 : r<0 ? 0 : r;
        g = g>255? 255 : g<0 ? 0 : g;
        b = b>255? 255 : b<0 ? 0 : b;
        return 0xff000000 | (b<<16) | (g<<8) | r;
    }

    private void onSceneUpdate(FrameTime frameTime) {
//        if (!snapShotTaken) {
//            final Handler handler = new Handler(Looper.getMainLooper());
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    //snapShot();
//                    try {
//                        Image image = Objects.requireNonNull(arFragment.getArSceneView().getArFrame()).acquireCameraImage();
//                        String mPath = Environment.getExternalStorageDirectory().toString() + ".jpg";
//
//                        WriteImageInformation((Image) image, (String) mPath);
//                    } catch (NotYetAvailableException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, 5000);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        arFragment.getPlaneDiscoveryController().hide();
    }
}
