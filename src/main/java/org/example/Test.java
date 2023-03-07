package org.example;

import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.Header;
import nom.tam.fits.ImageHDU;
import nom.tam.image.compression.hdu.CompressedImageHDU;

import java.io.IOException;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        var fpack = readCompressed("fpack.fits.fz");
        var funpack = readFunpacked("funpack2.fits");

        System.out.println(Arrays.deepToString(fpack));
        System.out.println(Arrays.deepToString(funpack));

        // Test values
        System.out.println("Fpack and funpacked");
        System.out.println(Arrays.deepEquals(fpack, funpack));
    }

    private static float[][] attemptRescale(float[][] initPixels, double zZero, double zScale) {
        var data = initPixels.clone();
        for (int i = 0; i < initPixels.length; i++) {
            for (int j = 0; j < initPixels[i].length; j++) {
                var pi = Float.floatToIntBits(initPixels[i][j]);
                data[i][j] = (float) (zScale * pi + zZero);
            }
        }

        return data;
    }

    private static float[][] readCompressed(String name) {
        try (Fits f = new Fits(name)) {
            var hdu = ((CompressedImageHDU) f.read()[1]).asImageHDU();
            float[][] image = (float[][]) hdu.getKernel();
            return image;
        } catch (IOException | FitsException e) {
            throw new RuntimeException(e);
        }
    }

    private static Header getCompressedHeader() {
        try (Fits f = new Fits("fpack.fits.fz")) {
            return ((CompressedImageHDU) f.read()[1]).getHeader();
        } catch (IOException | FitsException e) {
            throw new RuntimeException(e);
        }
    }

    private static float[][] readFunpacked(String name) {
        try (Fits f = new Fits(name)) {
            var hdu = ((ImageHDU) f.getHDU(0));
            float[][] image = (float[][]) hdu.getKernel();
            return image;
        } catch (IOException | FitsException e) {
            throw new RuntimeException(e);
        }
    }
}
