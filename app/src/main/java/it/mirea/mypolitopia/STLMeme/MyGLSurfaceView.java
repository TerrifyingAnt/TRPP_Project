package it.mirea.mypolitopia.STLMeme;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import it.mirea.mypolitopia.Map.Cell;
import it.mirea.mypolitopia.Map.Field;
import it.mirea.mypolitopia.R;
import it.mirea.mypolitopia.ViewActivity;

public class MyGLSurfaceView extends GLSurfaceView {


    private final GLRenderer glRenderer;
    private Cell[][] field = Field.getInstance();

    /**
     *
     * @param context
     * @param field
     */
    public MyGLSurfaceView(Context context, Cell[][] field) {
        super(context);

        glRenderer = new GLRenderer(this.getContext(), field);
        setRenderer(glRenderer);

    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float previousX;
    private float previousY;

    /**
     *
     * @param e
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - previousX;
                float dy = y - previousY;

                // reverse direction of rotation above the mid-line
/*                if (y > getHeight() / 2) {
                    dx = dx * -1;
                }*/

                // reverse direction of rotation to left of the mid-line
/*                if (x < getWidth() / 2) {
                    dy = dy * -1;
                }*/

                //if (glRenderer.getEyeX() + dx > -5 && glRenderer.getEyeX() + dx < 5 && glRenderer.getEyeY() + dy > -7 && glRenderer.getEyeY() + dy < 6) {
                glRenderer.setEyeCoords(glRenderer.getEyeX() + dy / 50, glRenderer.getEyeY() + dx / 50, glRenderer.getEyeZ());
                glRenderer.setCenterCoords(glRenderer.getCenterX() + dy / 50, glRenderer.getCenterY() + dx / 50, glRenderer.getCenterZ());
                //}
            case MotionEvent.ACTION_DOWN:
                x = e.getX();
                y = e.getY();
                //float[] ray = glRenderer.getViewRay(x, y);
                /*if (ray[2] < -0.0000001f || ray[2] > 0.0000001f) {
                    float t = -glRenderer.getEyeZ() / ray[2];
                    int k = 0;
                   System.out.println(String.valueOf(glRenderer.getEyeX() + ray[0] * t) + " " +
                            String.valueOf(glRenderer.getEyeY() + ray[1] * t) + " " + String.valueOf(glRenderer.getEyeZ() + ray[2] * t) + " " + String.valueOf(t) + " " + String.valueOf(x) + " " + String.valueOf(y));
                    for (int i = 0; i < field.length; i++) {
                        for (int j = 0; j < field.length; j++) {
                            //System.out.println(String.valueOf((i) * glRenderer.getEyeX() ) + " " + String.valueOf(j * glRenderer.getEyeY()));
                                //System.out.println("Nice " + String.valueOf(i) + " " + String.valueOf(j));

                        }
                    }
                }*/
                glRenderer.setXY(x, y);
                int ind[] = new int[2];

        }
                requestRender();
                previousX = x;
                previousY = y;
                return true;

    }
}
