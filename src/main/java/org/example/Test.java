package org.example;

import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.ImageHDU;
import nom.tam.image.compression.hdu.CompressedImageHDU;

import java.io.IOException;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        var orig = readCompressed();
        var funpack = readFunpacked();

        // Test values
        System.out.println(Arrays.deepEquals(orig, funpack));

        System.out.println("A corner:");
        System.out.println(orig[0][0]);
        System.out.println(funpack[0][0]);
    }

    private static float[][] readCompressed() {
        try (Fits f = new Fits("original.fits.fz")) {
            var hdu = ((CompressedImageHDU) f.getHDU(0)).asImageHDU();
            float[][] image = (float[][]) hdu.getKernel();
            return image;
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
