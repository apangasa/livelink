package com.nitinr.livelink;

import androidx.appcompat.app.AppCompatActivity;



import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


import com.google.ar.core.exceptions.NotYetAvailableException;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ViewRenderable viewRenderable;

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

        createRenderable(true);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                createRenderable(false);
            }
        }, 20000);

        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onSceneUpdate);
        onSceneUpdate(null);

        SttService sttService = SttService.getInstance(MainActivity.this);
        sttService.startSpeechToText();

        final Handler handler2 = new Handler(Looper.getMainLooper());
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                sttService.stopSpeechToText();
        }
        }, 5000);
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

    private void createRenderable(boolean isGif) {

        int layout = (isGif)? R.layout.activity_animation : R.layout.activity_renderable;

        ViewRenderable.builder()
                .setView(this, layout)
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

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               node.setParent(null);
            }
        }, 20000);
    }

    private void onSceneUpdate(FrameTime frameTime) {

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Image image = Objects.requireNonNull(arFragment.getArSceneView().getArFrame()).acquireCameraImage();

                    String base64 = ImageProcessing.processSnapShot(image, image.getWidth(), image.getHeight());

                    Log.d(TAG, "end");
                } catch (NotYetAvailableException e) {
                    e.printStackTrace();
                }
            }
        }, 5000);

    }

    @Override
    protected void onResume() {
        super.onResume();

        arFragment.getPlaneDiscoveryController().hide();
    }
}
