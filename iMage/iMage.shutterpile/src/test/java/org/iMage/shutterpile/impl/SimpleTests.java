package org.iMage.shutterpile.impl;

import java.awt.image.BufferedImage;

import org.iMage.shutterpile.impl.supplier.ImageWatermarkSupplier;
import org.iMage.shutterpile.port.IWatermarkSupplier;
import org.junit.Test;

/**
 * Some tests to ensure basic functionality.
 *
 * @author Dominik Fuchss
 *
 */
public class SimpleTests extends TestBase {

//@formatter:off
  /*
   * RED    = 0xFFFF0000
   * BLACK  = 0xFF000000
   *
   * Apply filters to black
   * GrayScale => 0xFF000000
   * Threshold => 0xFF000000
   * Alpha     => 0x7F000000 (127,0,0,0)
   *
   * overlay (alpha blending. see https://de.wikipedia.org/wiki/Alpha_Blending)  =>
   * Here : ARGB
   * Mark (A) = (0.4980..,0,0,0)
   * RED  (B) = (1,1,0,0)
   *
   * A over B:
   *
   * a' = 0.4980.. + (1-0.4980..)*1 = 1
   *
   * C' = 1 / a' * (a(A)     * c(A) + (1 - a(A))    * a(B) * c(B))
   * r' = 1 / 1  * (0.4980.. * 0    + (1 - 0.4980..)  * 1    * 1   ) = 0.5019..
   * g' = 1 / 1  * (0.4980.. * 0    + (1 - 0.4980..)  * 1    * 0   ) = 0
   * b' = 1 / 1  * (0.4980.. * 0    + (1 - 0.4980..)  * 1    * 0   ) = 0
   *
   * => new color = (1,0.5019..,0,0) === (255,128,0,0) == 0xFF800000
   *
   */
//@formatter:on

  private static final int WATERMARK_COLOR_SHALL = 0x7F000000;
  private static final int GENERATED_COLOR_SHALL = 0xFF800000;

  /**
   * Check alpha blending on 1x1 images.
   *
   * @throws Exception
   *           if sth goes wrong
   */
  @Test
  public void testAlphaBlending1x1() throws Exception {
    IWatermarkSupplier watermarkSupplier = new ImageWatermarkSupplier(
        createImage(TestBase.BLACK, 1, 1));
    BufferedImage wm = watermarkSupplier.getWatermark();

    Watermarker wmk = new Watermarker(watermarkSupplier);

    this.checkColor(wm, SimpleTests.WATERMARK_COLOR_SHALL);

    BufferedImage result = wmk.generate(createImage(TestBase.RED, 1, 1), 1);
    this.checkColor(result, SimpleTests.GENERATED_COLOR_SHALL);

  }

  /**
   * Check expansion (fit).
   *
   * @throws Exception
   *           if sth goes wrong
   */
  @Test
  public void checkExpansionFit() throws Exception {
    IWatermarkSupplier watermarkSupplier = new ImageWatermarkSupplier(
        createImage(TestBase.BLACK, 5, 5));
    BufferedImage watermark = watermarkSupplier.getWatermark();
    Watermarker wmk = new Watermarker(watermarkSupplier);

    this.checkColor(watermark, SimpleTests.WATERMARK_COLOR_SHALL);
    BufferedImage red = createImage(TestBase.RED, 10, 10);

    BufferedImage row1 = wmk.generate(red, 1);
    BufferedImage row2 = wmk.generate(red, 2);
    BufferedImage row5 = wmk.generate(red, 5);

    this.checkColor(row1, SimpleTests.GENERATED_COLOR_SHALL);
    this.checkColor(row2, SimpleTests.GENERATED_COLOR_SHALL);
    this.checkColor(row5, SimpleTests.GENERATED_COLOR_SHALL);
  }

  /**
   * Check expansion (non fit).
   *
   * @throws Exception
   *           if sth goes wrong
   */
  @Test
  public void checkExpansionNoFit() throws Exception {
    IWatermarkSupplier watermarkSupplier = new ImageWatermarkSupplier(
        createImage(TestBase.BLACK, 5, 5));

    BufferedImage watermark = watermarkSupplier.getWatermark();
    Watermarker wmk = new Watermarker(watermarkSupplier);

    this.checkColor(watermark, SimpleTests.WATERMARK_COLOR_SHALL);
    BufferedImage red = createImage(TestBase.RED, 13, 13);

    BufferedImage row1 = wmk.generate(red, 1);
    BufferedImage row2 = wmk.generate(red, 2);
    BufferedImage row5 = wmk.generate(red, 5);

    this.checkColor(row1, SimpleTests.GENERATED_COLOR_SHALL);
    this.checkColor(row2, SimpleTests.GENERATED_COLOR_SHALL);
    this.checkColor(row5, SimpleTests.GENERATED_COLOR_SHALL);
  }

  /**
   * Check alpha blending on two transparent images.
   */
  @Test
  public void checkTransparentImages() {
    BufferedImage img = createImage(TestBase.TRANSPARENT, 5, 5);
    BufferedImage wm = createImage(TestBase.TRANSPARENT, 5, 5);
    IWatermarkSupplier wmk = new ImageWatermarkSupplier(wm);
    wm = wmk.getWatermark();
    this.checkColor(wm, TestBase.TRANSPARENT);
    // This may cause a DivBy0-Exception iff not checked in algorithm (A over B) ..
    BufferedImage res = new Watermarker(wmk).generate(img, 1);
    this.checkColor(res, TestBase.TRANSPARENT);
  }

  /**
   * Check alpha blending for transparent wm image.
   */
  @Test
  public void checkOneTransparentImage1() {
    BufferedImage img = createImage(TestBase.RED, 5, 5);
    BufferedImage wm = createImage(TestBase.TRANSPARENT, 5, 5);
    IWatermarkSupplier wmk = new ImageWatermarkSupplier(wm);
    wm = wmk.getWatermark();
    this.checkColor(wm, TestBase.TRANSPARENT);
    // This may cause a DivBy0-Exception iff not checked in algorithm ..
    BufferedImage res = new Watermarker(wmk).generate(img, 1);
    this.checkColor(res, TestBase.RED);
  }

  /**
   * Check alpha blending for transparent base image.
   */
  @Test
  public void checkOneTransparentImage2() {
    BufferedImage img = createImage(TestBase.TRANSPARENT, 5, 5);
    BufferedImage wm = createImage(TestBase.RED, 5, 5);
    IWatermarkSupplier wmk = new ImageWatermarkSupplier(wm);
    wm = wmk.getWatermark();
    this.checkColor(wm, 0x7F555555);
    // This may cause a DivBy0-Exception iff not checked in algorithm ..
    BufferedImage res = new Watermarker(wmk).generate(img, 1);
    this.checkColor(res, 0x7F555555);
  }
}
