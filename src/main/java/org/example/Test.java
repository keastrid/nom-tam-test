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
        var fpack = readCompressed();
        var funpack = readFunpacked();

        // Test values
        System.out.println(Arrays.deepEquals(fpack, funpack));

        System.out.println("A corner:");
        System.out.println(fpack[0][0]);
        System.out.println(funpack[0][0]);

        //getCompressedHeader().dumpHeader(System.out);

        System.out.println(Arrays.deepEquals(attemptRescale(fpack, 1, 1), funpack));

        System.out.println();
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

    private static float[][] readCompressed() {
        try (Fits f = new Fits("fpack.fits.fz")) {
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

    private static float[][] readFunpacked() {
        try (Fits f = new Fits("funpack.fits")) {
            var hdu = ((ImageHDU) f.getHDU(0));
            float[][] image = (float[][]) hdu.getKernel();
            return image;
        } catch (IOException | FitsException e) {
            throw new RuntimeException(e);
        }
    }
}
