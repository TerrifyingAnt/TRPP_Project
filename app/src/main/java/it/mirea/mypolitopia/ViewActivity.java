package it.mirea.mypolitopia;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;


import androidx.constraintlayout.widget.ConstraintLayout;

import javax.microedition.khronos.opengles.GL;

import it.mirea.mypolitopia.Map.Cell;
import it.mirea.mypolitopia.Map.Field;
import it.mirea.mypolitopia.STLMeme.GLRenderer;
import it.mirea.mypolitopia.STLMeme.MatrixGrabber;
import it.mirea.mypolitopia.STLMeme.MatrixTrackingGL;
import it.mirea.mypolitopia.STLMeme.MyGLSurfaceView;

public class ViewActivity extends Activity {
    private boolean supportsEs2;
    private MyGLSurfaceView glView;
    private float rotateDegreen = 0;
    private GLRenderer glRenderer;
    Cell[][] field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkSupported();
        Intent i = getIntent();
        field = Field.getInstance();

        if (supportsEs2) {
            setContentView(R.layout.glsurfaceview);
            //glView = (GLSurfaceView) this.findViewById(R.id.renderer_view);
            glView = new MyGLSurfaceView(this, field);
            glView.setGLWrapper(new GLSurfaceView.GLWrapper() {
                public GL wrap(GL gl) {
                    return new MatrixTrackingGL(gl);
                }
            });
            setContentView(glView);
        } else {
            setContentView(R.layout.activity_main);
            Toast.makeText(this, "Текущее устройство не поддерживает OpenGL ES 2.0!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (glView != null) {
            glView.onResume();
        }


    }

    private void checkSupported() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        supportsEs2 = configurationInfo.reqGlEsVersion >= 0x2000;

        boolean isEmulator = Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86"));

        supportsEs2 = supportsEs2 || isEmulator;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (glView != null) {
            glView.onPause();
        }
    }

}
