package it.mirea.mypolitopia.STLMeme;

import static javax.microedition.khronos.opengles.GL10.GL_BLEND;
import static javax.microedition.khronos.opengles.GL10.GL_LIGHTING;
import static javax.microedition.khronos.opengles.GL10.GL_MULTISAMPLE;
import static it.mirea.mypolitopia.Map.Resource.ResourceType.GOLD;
import static it.mirea.mypolitopia.Map.Resource.ResourceType.STONE;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.util.DisplayMetrics;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import it.mirea.mypolitopia.Map.Cell;
import it.mirea.mypolitopia.Map.Field;
import it.mirea.mypolitopia.Map.Resource;


public class GLRenderer implements GLSurfaceView.Renderer {
    float x = -1;
    float y = -1;
    float colorK = 100000;
    private Model model; // плитка карты
    private Model city; // модель города
    private Model npc; // модель нпс
    private Point mCenterPoint; // хз хачем
    public Point eye = new Point(-15, 3, 10); // точка спавна камеры x - z, y -> x, z -> y
    private Point up = new Point(0, 0, 1); // ось вверх
    public Point center = new Point(-7, 3, 3); // куда смотрит камера
    private float mScalef = 1;
    private float mDegree = 0;
    private Cell[][] field;
    public volatile float mAngle; // юзалось для вращения модельки
    float[] ambient = {0.4f, 0.4f, 0.4f, 1.0f,}; // цвет окружения
    float[] specular = {0.25f, 0.25f, 0.25f, 1.0f,}; // цвет блика
    float[] lightPosition = {0f, 0f, -15f, 0f}; // позиция света
    float[] materialAmb = {1f, 1f, 1f, 1.0f}; //
    float [] materialDiff = {0.5f, 0.5f, 0.5f, 1.0f}; // Диффузное отражение установлено синим
    float[] materialSpec = {0.2f, 0.2f, 0.2f, 1.0f}; //
    float[] dG = {0.15f, 0.35f, 0.03f, 1.0f}; // зеленый
    float[] dW = {0.21f, 0.63f, 0.84f, 1.0f}; // синий
    float[] dS = {0.32f, 0.32f, 0.32f, 1.0f}; // серый
    float[] dT = {0.46f, 0.25f, 0.06f, 1.0f}; // коричневый
    float[] dGl = {1f, 0.98f, 0.08f, 1.0f}; // желтый
    float[] dP = {0.4f, 0.16f, 0.65f, 1f}; // фиолетовый
    private MatrixGrabber mg = new MatrixGrabber();

    /**
     * конструктор, где создаются модельки
     * @param context
     * @param field
     */
    public GLRenderer(Context context, Cell[][] field) {
        try {
            this.field = field;
            //model = new Model();
            model = new STLReader().parserBinStlInAssets(context, "new_tile.stl");
            city = new STLReader().parserBinStlInAssets(context, "xyi.stl");
            npc = new STLReader().parserBinStlInAssets(context, "npc.stl");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * рисуем и задаем материалы
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {

        field = Field.getInstance();
        float[] diffuseGround = {0.15f, 0.35f, 0.03f, 1.0f}; // зеленый
        float[] diffuseWater = {0.21f, 0.63f, 0.84f, 1.0f}; // синий
        float[] diffuseStone = {0.32f, 0.32f, 0.32f, 1.0f}; // серый
        float[] diffuseTrees = {0.46f, 0.25f, 0.06f, 1.0f}; // коричневый
        float[] diffuseGold = {1f, 0.98f, 0.08f, 1.0f}; // желтый
        float[] diffusePlayer = {0.4f, 0.16f, 0.65f, 1f}; // фиолетовый
        Point[][] coords = new Point[field.length][field.length];

        // Очистить экран и кэш глубины
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // сброс текущей модели наблюдения
        gl.glLoadIdentity();

        // глаз смотрит на происхождение
        GLU.gluLookAt(gl, eye.x, eye.y, eye.z, center.x,
                center.y, center.z, up.x, up.y, up.z);

        // Для того, чтобы иметь трехмерное ощущение, сделать модель постоянно вращаться, изменяя значение MDegree.
        //gl.glRotatef(mDegree, 0, 1, 0);

        // продолжить модель к представлению, просто установите
        gl.glScalef(mScalef, mScalef, mScalef);
        // переместить модель в начало
        gl.glTranslatef(0, 0,0);


        //===================begin==============================//

        // Разрешить каждую вершину установить вектор
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        // Разрешить настройки вершин
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // Разрешить настройки цвета


        for(int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                switch (field[i][j].getType()) {
                    case WATER:
                        // Отражение рассеянного света
                        diffuseWater[2] += j/colorK;
                        field[i][j].setColor(diffuseWater);
                        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, Util.floatToBuffer(diffuseWater));
                        break;

                    case GROUND:
                        switch (field[i][j].getResource().getResource()) {
                            case TREE:
                                // Отражение рассеянного света
                                diffuseTrees[2] += j/colorK;
                                field[i][j].setColor(diffuseTrees);
                                gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, Util.floatToBuffer(diffuseTrees));
                                break;
                            case STONE:
                                // Отражение рассеянного света
                                diffuseStone[2] += j/colorK;
                                field[i][j].setColor(diffuseStone);
                                gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, Util.floatToBuffer(diffuseStone));
                                break;
                            case GOLD:
                                // Отражение рассеянного света
                                diffuseGold[2] += j/colorK;
                                field[i][j].setColor(diffuseGold);
                                gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, Util.floatToBuffer(diffuseGold));
                                break;

                            default:
                                // Отражение рассеянного света
                                diffuseGround[2] += j/colorK;
                                field[i][j].setColor(diffuseGround);
                                gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, Util.floatToBuffer(diffuseGround));
                                break;
                        }
                }
                if (field[i][j].getActive()) {
                    gl.glTranslatef(0f, 0f, 1f);
                    gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.getFacetCount() * 3);
                    gl.glTranslatef(0f, 0f, -1f);
                }
                else {
                    gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.getFacetCount() * 3);
                }

                // установка города
                /*if (field[i][j].getBoolCity()) {
                    gl.glTranslatef(0, 1f, 0);
                    gl.glNormalPointer(GL10.GL_FLOAT, 0, city.getVnormBuffer());
                    // Установить треугольную вершинную вершину источника данных
                    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, city.getVertBuffer());
                    gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, Util.floatToBuffer(diffuseStone));
                    gl.glDrawArrays(GL10.GL_TRIANGLES, 0, city.getFacetCount() * 3);
                    gl.glTranslatef(0, -1f, 0);
                    gl.glNormalPointer(GL10.GL_FLOAT, 0, city.getVnormBuffer());
                    // Установить треугольную вершинную вершину источника данных
                    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, city.getVertBuffer());
                    gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, Util.floatToBuffer(diffuseStone));
                    gl.glDrawArrays(GL10.GL_TRIANGLES, 0, city.getFacetCount() * 3);
                }*/
                // установка игрока
                /*if (field[i][j].isNPC()) {
                    gl.glTranslatef(0, 1f, 0);
                    gl.glNormalPointer(GL10.GL_FLOAT, 0, npc.getVnormBuffer());
                    // Установить треугольную вершинную вершину источника данных
                    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, npc.getVertBuffer());
                    gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, Util.floatToBuffer(diffusePlayer));
                    gl.glDrawArrays(GL10.GL_TRIANGLES, 0, npc.getFacetCount() * 3);
                    gl.glTranslatef(0, -1f, 0);
                    gl.glNormalPointer(GL10.GL_FLOAT, 0, npc.getVnormBuffer());
                    // Установить треугольную вершинную вершину источника данных
                    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, npc.getVertBuffer());*/
                //}
                //gl.glTranslatef(1f, 0, 1.5f);
                gl.glTranslatef(1f, 1.5f, 0); // x - z, y -> x, z -> y
                coords[i][j] = new Point(i * 1f + getEyeX(), j * 1.5f + getEyeY(), 0 - getEyeZ());
                // Установить метод векторных данных источника данных
                gl.glNormalPointer(GL10.GL_FLOAT, 0, model.getVnormBuffer());
                // Установить треугольную вершинную вершину источника данных
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.getVertBuffer());
/*                diffuseWater[1] += i/colorK;
                diffuseGround[1] += i/colorK;
                diffuseGold[1] += i/colorK;
                diffuseTrees[1] += i/colorK;
                diffuseStone[1] += i/colorK;*/
            }

            if (i % 2 == 0) {
                gl.glTranslatef(-((float) field.length + 1), -1.5f * ((float) field.length), 0);
            }
            else {
                gl.glTranslatef(-((float) field.length), -1.5f * ((float) field.length) + 1.5f, 0);
            }
/*            if(x > 0 && y > 0) {
                // Turn off lighting
                gl.glDisable(GL_LIGHTING);

                // Turn off antialiasing
                gl.glDisable(GL_BLEND);
                gl.glDisable(GL_MULTISAMPLE);

                //touched(x, y);

                openLight(gl);
            }*/
            gl.glNormalPointer(GL10.GL_FLOAT, 0, model.getVnormBuffer());
            // Установить треугольную вершинную вершину источника данных
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.getVertBuffer());
            gl.glTranslatef(-1f, 0, 0); // 1.5 - красивенько

            gl.glNormalPointer(GL10.GL_FLOAT, 0, model.getVnormBuffer());
            // Установить треугольную вершинную вершину источника данных
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.getVertBuffer());
        }
        // Отмените настройку вершины
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        // Остановка Векторные настройки
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        float[] ray = getViewRay(x, y);
        //System.out.println("----------------->" + String.valueOf(ray[0]) + " " + String.valueOf(ray[1]) + " " + String.valueOf(ray[2]));
        /*for(int i = 0; i < coords.length; i++) {
            for (int j = 0; j < coords.length; j++) {
                System.out.println(String.valueOf(coords[i][j].getX())+ " " + String.valueOf(coords[i][j].getY()) + " " + String.valueOf(coords[i][j].getZ()));
            }
        }*/

            //=====================end============================//
    }

    /**
     *
     * @param gl
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // Установите размер сцены OpenGL, (0, 0) указывает верхний левый угол визуального порта в окне, (ширина, высота) определяет размер просмотра просмотра.
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_PROJECTION); // Установите матрицу проекции
        gl.glLoadIdentity(); // Установите матрицу в качестве блок-матрицы, эквивалентной для сброса матрицы
        GLU.gluPerspective(gl, 50.0f, ((float) width) / height, 1f, 100f);// Установить диапазон перспективы

        // Объявлены следующие два предложения, все преобразования в будущем являются моделями (т.е. мы рисуем графику)
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        mg.getCurrentState(gl);
    }

    /**
     *
     * @param gl
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glEnable(GL10.GL_DEPTH_TEST); // включить кэш глубины
        gl.glClearDepthf(1.0f); // установить значение кэша глубины
        gl.glDepthFunc(GL10.GL_LEQUAL); // Установить функцию сравнения кэша глубины
        openLight(gl);
        //enableMaterial(gl);

        gl.glShadeModel(GL10.GL_SMOOTH);// Установите режим тени GL_SMooth
        float r = model.getR();

        // R - радиус, а не диаметр, поэтому отношение декомпрессии может быть рассчитано с использованием 0,5 / R
        mScalef = 0.5f / r;
        mCenterPoint = model.getCentrePoint();

    }

    /**
     *
     * @param gl
     */
    public void openLight(GL10 gl) {

        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, Util.floatToBuffer(ambient));
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, Util.floatToBuffer(materialDiff));
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, Util.floatToBuffer(specular));
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, Util.floatToBuffer(lightPosition));


    }

    /**
     *
     * @param gl
     */
    public void enableMaterial(GL10 gl) {
        // Отражение материала от окружающего света
        //gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, Util.floatToBuffer(materialAmb));
        // Отражение рассеянного света
        //gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, Util.floatToBuffer(materialDiff));
        // Отражение зеркального света
        //gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, Util.floatToBuffer(materialSpec));

    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public float[] getViewRay(float x, float y)
    {

        int height =  Resources.getSystem().getDisplayMetrics().heightPixels;
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        // view port
        int[] viewport = { 0, 0, width, height };

        // far eye point
        float[] eyes = new float[4];
        GLU.gluUnProject(x, height - y, 1f, mg.mModelView, 0, mg.mProjection, 0, viewport, 0, eyes, 0);

        // fix
        if (eyes[3] != 0)
        {
            eyes[0] = eyes[0] / eyes[3];
            eyes[1] = eyes[1] / eyes[3];
            eyes[2] = eyes[2] / eyes[3];
        }

        // ray vector
        float[] ray = { eyes[0] - getEyeX(), eyes[1] - getEyeY(), eyes[2] - getEyeZ(), 0.0f };
        //float[] ray = { getCenterX() - getEyeX(), getCenterY() - getEyeY(), getCenterZ() - getEyeZ(), 0.0f };
        return ray;
    }



    // метод выбора клетки
    /*public int[] touched(float x, float y) {
        int ind[] = new int[2];
        if (x != -1 && y != -1) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(4);
            byteBuffer.order(ByteOrder.nativeOrder());
            GLES20.glFinish();
            GLES20.glReadPixels(Math.round(x), Math.round(y), 1, 1,
                    GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, byteBuffer);
            byte[] byteArray = new byte[4];
            byteBuffer.get(byteArray);
            double r = ( byteArray[0]);
            double g = ( byteArray[1]);
            double b = ( byteArray[2]);
            String key = r + " " + g + " " + b;
            for (int i = 0; i < field.length; i++) {
                for (int j = 0; j < field.length; j++) {
                    field[i][j].setActive(false);
                }
            }

            boolean k = true;
            for (int i = 0; i < field.length; i++) {
                if (!k) {
                    break;
                }
                for (int j = 0; j < field.length; j++) {
                    if (!k) {
                        break;
                    }
                    System.out.println(String.valueOf(b) + " " + String.valueOf(Math.round(field[i][j].getColor()[2] * 1000)));
                    if ((int) b == Math.round(field[i][j].getColor()[2] * 1000) && (int) g == Math.round(field[i][j].getColor()[1] * 1000)) {
                        field[i][j].setActive(true);
                        ind = new int[2];
                        ind[0] = i;
                        ind[1] = j;
                        k = false;
                        break;
                    }
                }
            }
        }
        return ind;
    }
*/
    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // ГЕТТЕРЫ КАМЕРЫ
    public float getEyeX() {
        return eye.getX();
    }

    public float getEyeY() {
        return eye.getY();
    }

    public float getEyeZ() {
        return eye.getZ();
    }

    public float getCenterX() {
        return center.getX();
    }

    public float getCenterY() {
        return center.getY();
    }

    public float getCenterZ() {
        return center.getZ();
    }

    // СЕТТЕРЫ КАМЕРЫ
    public void setEyeX(float x) {
         this.eye.setX(x);
    }

    public void setEyeY(float y) {
        eye.setY(y);
    }

    public void setEyeZ(float z) {
        eye.setZ(z);
    }

    public void setCenterX(float x) {
        center.setX(x);
    }

    public void setCenterY(float y) {
        center.setY(y);
    }

    public void setCenterZ(float z) {
        center.setZ(z);
    }

    public void setEyeCoords(float x, float y, float z) {
        eye.setCoord(x, y, z);
    }

    public void setCenterCoords(float x, float y, float z) {
        center.setCoord(x, y, z);
    }


}

