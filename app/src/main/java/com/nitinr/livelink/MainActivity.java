package com.nitinr.livelink;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Config;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ModelRenderable andyRenderable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            Toast.makeText(this, "ARCore Scene Form not supported", Toast.LENGTH_LONG)
                    .show();
        } else {
            Toast.makeText(this, "ARCore Scene Form supported!", Toast.LENGTH_LONG)
                    .show();
        }

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        arFragment.getArSceneView().getPlaneRenderer().setEnabled(false);

        //arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> createRenderable(hitResult.createAnchor()));

        createRenderable();

        //createSession();
    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
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
                .thenAccept(viewRenderable -> {
                    addToScene(viewRenderable);
                });
    }

    private void addToScene(ViewRenderable viewRenderable) {
//        //Session session = arFragment.getArSceneView().getSession();
//        float[] pos = { 0, 0, -1 };
//        float[] rotation = { 0, 0, 0, 1 };
//        //Anchor anchor =  session.createAnchor(new Pose(pos, rotation));
//
//        //assert session != null;
////        Anchor anchor = session.createAnchor(Objects.requireNonNull(arFragment.getArSceneView()
////                .getArFrame()).getCamera().getDisplayOrientedPose()
////                .compose(Pose.makeTranslation(0, 0, -1f))
////                .extractTranslation());
//        AnchorNode anchorNode = new AnchorNode(anchor);
//        anchorNode.setRenderable(viewRenderable);
//        anchorNode.setParent(arFragment.getArSceneView().getScene());
//
//        arFragment.getArSceneView().getScene().addChild(anchorNode);

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

    @Override
    protected void onResume() {
        super.onResume();

        arFragment.getPlaneDiscoveryController().hide();
    }
}
