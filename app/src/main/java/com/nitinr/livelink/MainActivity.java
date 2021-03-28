package com.nitinr.livelink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.ar.core.exceptions.NotYetAvailableException;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ViewRenderable profileRenderable; // Profile AR element

    private TransformableNode animNode;
    private TransformableNode profileNode;

    enum ArStatus { // Which AR type element to render
        PROFILE,
        ANIMATION_LOADING,
        ANIMATION_LINKED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            Toast.makeText(this, "ARCore Scene Form not supported", Toast.LENGTH_LONG)
                    .show();
        }

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        // disables plane calibration animation
        arFragment.getArSceneView().getPlaneRenderer().setEnabled(false);
        arFragment.getPlaneDiscoveryController().hide();
        arFragment.getPlaneDiscoveryController().setInstructionView(null);

        startService(new Intent(this, VoiceService.class));

        VoiceService voiceService = new VoiceService();
        voiceService.setQueryListener(new VoiceService.QueryListener() {
            @Override
            public void onQueryCaptured(String query) {
                switch (query) {
                    case "detect":
                        createRenderable(ArStatus.ANIMATION_LOADING, null);

                        if (animNode != null) {
                            animNode.setParent(null);
                        }

                        snapShot();
                        break;
                    case "link":
                        if (profileNode != null) {
                            linkWithUser();
                            createRenderable(ArStatus.ANIMATION_LINKED, null);
                        }
                        break;
                    case "off":
                        if (animNode != null && profileNode != null) {
                            animNode.setParent(null);
                            profileNode.setParent(null);
                        }
                        break;
                }

            }

            @Override
            public void onDataLoaded(JSONObject data) {
                if (data != null) {
                    createRenderable(ArStatus.PROFILE, data);
                }
            }
        });
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

    private void createRenderable(ArStatus status, JSONObject data) {

        int layout = (status == ArStatus.ANIMATION_LOADING || status == ArStatus.ANIMATION_LINKED)?
                R.layout.activity_animation : R.layout.activity_renderable;

        ViewRenderable.builder()
                .setView(this, layout)
                .build()
                .thenAccept(viewRenderable -> {
                    addToScene(viewRenderable, status, data);
                });
    }


    private void addToScene(ViewRenderable viewRenderable, ArStatus status, JSONObject data) {

        final double Z_OFFSET = 0.5;
        final double Y_OFFSET = 0.1;

        Vector3 forward = arFragment.getArSceneView().getScene().getCamera().getForward();
        Vector3 worldPosition = arFragment.getArSceneView().getScene().getCamera().getWorldPosition();
        Vector3 position = Vector3.add(forward, worldPosition);
        position.y -= Y_OFFSET;
        position.z -= Z_OFFSET;

        Vector3 direction = Vector3.subtract(worldPosition, position);
        direction.y = position.y;

        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setWorldPosition(position);
        node.setLookDirection(direction);
        node.setParent(arFragment.getArSceneView().getScene());

        if (status == ArStatus.ANIMATION_LOADING) {
            animNode = node;
        } else if (status == ArStatus.PROFILE){
            profileNode = node;
            this.profileRenderable = viewRenderable;

            updateProfile(viewRenderable, data);
        } else if (status == ArStatus.ANIMATION_LINKED) {
            animNode = node;
            GifImageView gifImageView = viewRenderable.getView().findViewById(R.id.gif);
            gifImageView.setImageResource(R.drawable.linked);
        }

        Node render = new Node();
        render.setParent(node);
        render.setRenderable(viewRenderable);
    }

    private void updateProfile(ViewRenderable viewRenderable, JSONObject data) {
        ImageView profileView = viewRenderable.getView().findViewById(R.id.profilePic);
        TextView nameView = viewRenderable.getView().findViewById(R.id.name);
        TextView emailView = viewRenderable.getView().findViewById(R.id.email);
        TextView bioView = viewRenderable.getView().findViewById(R.id.bio);
        TextView linkView = viewRenderable.getView().findViewById(R.id.numLinks);

        String profilePic, name, email, bio, numLinks;
        profilePic = name = email = bio = numLinks = "";

        try {
            profilePic = data.getString("profilePic");
            name = data.getString("name");
            email = data.getString("email");
            bio = data.getString("bio");
            numLinks = data.getString("numLinks");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        profileView.setImageBitmap(ImageProcessing.decodeImageToBitmap(profilePic));
        nameView.setText(name);
        emailView.setText(email);
        bioView.setText(bio);
        linkView.setText(numLinks);
    }

    private void linkWithUser() {
        View profileView = profileRenderable.getView();
        Button linkButton = profileView.findViewById(R.id.linkButton);
        CardView backgroundCard = profileView.findViewById(R.id.cardView);

        linkButton.setBackgroundResource(R.drawable.rounded_corners_green);
        backgroundCard.setCardBackgroundColor(Color.parseColor("#55D862"));
        linkButton.setText("Linked");
    }

    private void snapShot() {
        try {
            Image image = Objects.requireNonNull(arFragment.getArSceneView().getArFrame()).acquireCameraImage();

            String base64 = ImageProcessing.processSnapShot(image, image.getWidth(), image.getHeight());

            VoiceService.requestDataFromEndpoint("https://live-link-308404.ue.r.appspot.com/get_face", base64);

        } catch (NotYetAvailableException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
