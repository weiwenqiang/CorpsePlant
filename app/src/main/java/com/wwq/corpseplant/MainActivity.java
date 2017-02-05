package com.wwq.corpseplant;

import android.app.Activity;
import android.os.Bundle;

import com.wwq.cclayer.WelcomeLayer;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

public class MainActivity extends Activity {
    private CCDirector ccDirector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CCGLSurfaceView surfaceView = new CCGLSurfaceView(this);
        setContentView(surfaceView);

        ccDirector = CCDirector.sharedDirector();
        ccDirector.attachInView(surfaceView);

        ccDirector.setDisplayFPS(true);
        ccDirector.setScreenSize(480, 320);
        ccDirector.setDeviceOrientation(CCDirector.kCCDeviceOrientationLandscapeLeft);

        CCScene scene = CCScene.node();

        scene.addChild(new WelcomeLayer());

        ccDirector.runWithScene(scene);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ccDirector.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ccDirector.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ccDirector.end();
    }
}
