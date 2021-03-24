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
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.ByteArrayOutputStream;
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

        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onSceneUpdate);
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

    private void snapShot() {
        try {
            Image snapShot = Objects.requireNonNull(arFragment.getArSceneView().getArFrame()).acquireCameraImage();
            ByteBuffer byteBufferY = snapShot.getPlanes()[0].getBuffer();
            ByteBuffer byteBufferU = snapShot.getPlanes()[1].getBuffer();
            ByteBuffer byteBufferV = snapShot.getPlanes()[2].getBuffer();

            byte[] bytes = new byte[byteBufferY.remaining() + byteBufferU.remaining() + byteBufferV.remaining()];
            byteBufferY.get(bytes, 0, byteBufferY.remaining());
            byteBufferU.get(bytes, byteBufferY.remaining(), byteBufferV.remaining());
            byteBufferV.get(bytes, byteBufferY.remaining() + byteBufferV.remaining(), byteBufferU.remaining());


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
//            //iv.setImageBitmap(bitmapImage);
//            iv.setImageResource(R.drawable.ic_launcher_foreground);
            snapShotTaken = true;
            snapShot.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSceneUpdate(FrameTime frameTime) {
        if (!snapShotTaken) {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    snapShot();
                }
            }, 5000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        arFragment.getPlaneDiscoveryController().hide();
    }
}
