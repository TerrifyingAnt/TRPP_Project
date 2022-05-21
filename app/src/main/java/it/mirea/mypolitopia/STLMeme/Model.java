package it.mirea.mypolitopia.STLMeme;
import java.nio.FloatBuffer;


public class Model {
    // треугольный
    private int facetCount;
    // Координатный массив вершин
    private float[] verts;
    // Каждая вершина соответствует массиву номеров
    private float[] vnorms;
    // Каждая информация о собственности треугольника
    private short[] remarks;

    // Буфер преобразования массива вершин
    private FloatBuffer vertBuffer;

    // Каждая вершина соответствует методу преобразования буфера
    private FloatBuffer vnormBuffer;
    // Следование содержит максимальное значение, минимум всех точек в направлении X, Y, Z соответственно.
    float maxX;
    float minX;
    float maxY;
    float minY;
    float maxZ;
    float minZ;

    /**
     * вернуть центральную точку модели
     * Обратите внимание, что в загруженном исходном коде эта функция изменяется ледующисм образом.
     * @return
     */
    public Point getCentrePoint() {

        float cx = minX + (maxX - minX) / 2;
        float cy = minY + (maxY - minY) / 2;
        float cz = minZ + (maxZ - minZ) / 2;
        return new Point(cx, cy, cz);
    }

    /**
     * Максимальный радиус модели пакета
     * @return
     */
    public float getR() {
        float dx = (maxX - minX);
        float dy = (maxY - minY);
        float dz = (maxZ - minZ);
        float max = dx;
        if (dy > max)
            max = dy;
        if (dz > max)
            max = dz;
        return max;
    }

    /**
     * Установите массив вершины при настройке соответствующего буфера
     * @param verts
     */
    public void setVerts(float[] verts) {
       this.verts = verts;
        vertBuffer = Util.floatToBuffer(verts);
    }

    /**
     * Установите вектор массива вершины, установите соответствующий буфер
     * @param vnorms
     */
    public void setVnorms(float[] vnorms) {
        this.vnorms = vnorms;
        vnormBuffer = Util.floatToBuffer(vnorms);
    }


    public void multiplyVerts(int x) {
        for (int i = 0; i < verts.length; i++)
            verts[i] += x;
    }

    public void multiplyVnorms(int x) {
        for (int i = 0; i < verts.length; i++)
            vnorms[i] += x;
    }

    //···
    // Другие атрибуты, соответствующие установке, функции getter
    //···


    public float[] getVerts() {
        return verts;
    }

    public float[] getVnorms() {
        return vnorms;
    }

    public int getFacetCount() {
        return facetCount;
    }

    public void setFacetCount(int facetCount) {
        this.facetCount = facetCount;
    }

    public short[] getRemarks() {
        return remarks;
    }

    public void setRemarks(short[] remarks) {
        this.remarks = remarks;
    }

    public FloatBuffer getVertBuffer() {
        return vertBuffer;
    }

    public void setVertBuffer(FloatBuffer vertBuffer) {
        this.vertBuffer = vertBuffer;
    }

    public FloatBuffer getVnormBuffer() {
        return vnormBuffer;
    }

    public void setVnormBuffer(FloatBuffer vnormBuffer) {
        this.vnormBuffer = vnormBuffer;
    }
}