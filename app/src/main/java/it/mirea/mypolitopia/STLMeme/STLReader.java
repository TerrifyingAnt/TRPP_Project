package it.mirea.mypolitopia.STLMeme;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Package com.hc.opengl
 * Created by HuaChao on 2016/7/28.
 */
public class STLReader {
    private StlLoadListener stlLoadListener;

    /**
     *
     * @param path
     * @return
     * @throws IOException
     */
    public Model parserBinStlInSDCard(String path) throws IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        return parserBinStl(fis);
    }

    /**
     *
     * @param context
     * @param fileName
     * @return
     * @throws IOException
     */
    public Model parserBinStlInAssets(Context context, String fileName)
            throws IOException {

        //InputStream is = context.getAssets().open(fileName);

        return parserBinStl(context.getAssets().open(fileName));
    }

    /**
     * Проанализировать бинарный файл STL
     * @param in
     * @return
     * @throws IOException
     */
    public Model parserBinStl(InputStream in) throws IOException {
        if (stlLoadListener != null)
            stlLoadListener.onstart();
        Model model = new Model();
        // 80-байт фронта - заголовок файла для имени файла хранения;
        in.skip(80);

        // Принимать количество треугольных граней модели с четырьмя байтами целых чисел
        byte[] bytes = new byte[4];
        in.read(bytes);// Читайте количество треугольников
        int facetCount = Util.byte4ToInt(bytes, 0);
        model.setFacetCount(facetCount);
        if (facetCount == 0) {
            in.close();
            return model;
        }

        // Каждый треугольник занят 50 байтами
        byte[] facetBytes = new byte[50 * facetCount];
        // Читайте все треугольники в байтовые массивы
        in.read(facetBytes);
        // После того, как данные будут прочитаны, вы можете закрыть входной поток.
        in.close();


        parseModel(model, facetBytes);


        if (stlLoadListener != null)
            stlLoadListener.onFinished();
        return model;
    }

    /**
     * Анализировать данные модели, включая данные вершины, нормальные данные, диапазон пространства и т. Д.
     * @param model
     * @param facetBytes
     */
    private void parseModel(Model model, byte[] facetBytes) {
        int facetCount = model.getFacetCount();
        /*
         * Каждое треугольное лицо занимает фиксированную 50 байт, 50 байтов:
         * Метод треугольника: (1 вектор, эквивалентный точке) * (3D / Point) * (4-байтовая точка с плавающей запятой / размер) = 12 байт
         * Три точки координаты треугольника: (3 балла) * (3D / точка) * (4-байтовая точка с плавающей замыкающей точки) = 36 байт
         * Последние 2 байта используются для описания информации о атрибутах треугольника.
         */

        // Сохранить всю информацию о координате вершин, треугольник 3 верхних точка, одна вершина 3 координатных осей
        float[] verts = new float[facetCount * 3 * 3];

        // Сохранить всю трехконечную переносимость соответствия,
        // треугольник должен быть нормальным вектором, методом 3 очков
        // Когда модель нарисована, это метод для способа, соответствующего каждой вершине, поэтому длина хранения требуется * 3
        // Три вершины одного и того же треугольника одинаковы,
        // Следовательно, когда метод написан, необходимо только постоянно писать 3 того же подхода к тому же методу.
        float[] vnorms = new float[facetCount * 3 * 3];

        // Сохранить все свойства треугольника
        short[] remarks = new short[facetCount];

        int stlOffset = 0;
        try {
            for (int i = 0; i < facetCount; i++) {
                if (stlLoadListener != null) {
                    stlLoadListener.onLoading(i, facetCount);
                }
                for (int j = 0; j < 4; j++) {
                    float x = Util.byte4ToFloat(facetBytes, stlOffset);
                    float y = Util.byte4ToFloat(facetBytes, stlOffset + 4);
                    float z = Util.byte4ToFloat(facetBytes, stlOffset + 8);
                    stlOffset += 12;

                    if (j == 0) {// Способ вектора
                        vnorms[i * 9] = x;
                        vnorms[i * 9 + 1] = y;
                        vnorms[i * 9 + 2] = z;
                        vnorms[i * 9 + 3] = x;
                        vnorms[i * 9 + 4] = y;
                        vnorms[i * 9 + 5] = z;
                        vnorms[i * 9 + 6] = x;
                        vnorms[i * 9 + 7] = y;
                        vnorms[i * 9 + 8] = z;
                    } else {// три вершины
                        verts[i * 9 + (j - 1) * 3] = x;
                        verts[i * 9 + (j - 1) * 3 + 1] = y;
                        verts[i * 9 + (j - 1) * 3 + 2] = z;

                        // Записать максимальное минимальное значение в трех направлении оси координат в модели
                        if (i == 0 && j == 1) {
                            model.minX = model.maxX = x;
                            model.minY = model.maxY = y;
                            model.minZ = model.maxZ = z;
                        } else {
                            model.minX = Math.min(model.minX, x);
                            model.minY = Math.min(model.minY, y);
                            model.minZ = Math.min(model.minZ, z);
                            model.maxX = Math.max(model.maxX, x);
                            model.maxY = Math.max(model.maxY, y);
                            model.maxZ = Math.max(model.maxZ, z);
                        }
                    }
                }
                short r = Util.byte2ToShort(facetBytes, stlOffset);
                stlOffset = stlOffset + 2;
                remarks[i] = r;
            }
        } catch (Exception e) {
            if (stlLoadListener != null) {
                stlLoadListener.onFailure(e);
            } else {
                e.printStackTrace();
            }
        }

        // Установите данные, прочитанные в объект модели
        model.setVerts(verts);
        model.setVnorms(vnorms);
        model.setRemarks(remarks);

    }

    /**
     *
     */
    public static interface StlLoadListener {
        void onstart();

        void onLoading(int cur, int total);

        void onFinished();

        void onFailure(Exception e);
    }
}