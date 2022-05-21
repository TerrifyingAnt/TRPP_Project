package it.mirea.mypolitopia.STLMeme;

public class Point {
    public float x;
    public float y;
    public float z;

    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setCoord(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }



    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }


}