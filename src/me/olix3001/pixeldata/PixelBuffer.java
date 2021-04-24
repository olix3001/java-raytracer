package me.olix3001.pixeldata;

public class PixelBuffer {
    private PixelData[][] pixels;
    private int width, height;

    public PixelBuffer(int width, int height) {
        this.width = width;
        this.height = height;

        this.pixels = new PixelData[width][height];
    }

    public void setPixel(int x, int y, PixelData pixel) {
        pixels[x][y] = pixel;
    }

    public PixelData getPixel(int x, int y) {
        return pixels[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public PixelBuffer clone() {
        PixelBuffer clone = new PixelBuffer(width, height);
        for (int i = 0; i<pixels.length; i++) {
            System.arraycopy(pixels[i], 0, clone.pixels[i], 0, pixels[i].length);
        }
        return clone;
    }
}
