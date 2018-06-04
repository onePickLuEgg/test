package org.iMage.shutterpile.impl;

import java.awt.image.BufferedImage;

import org.junit.Assert;

/**
 * The base class of all tests.
 *
 * @author Dominik Fuchss
 *
 */
public abstract class TestBase {
  /**
   * The maximum difference of a channel to be interpreted as correct.
   */
  private static final int DELTA = 1;

  protected static final int RED = 0xFFFF0000;
  protected static final int BLACK = 0xFF000000;
  protected static final int WHITE = 0xFFFFFFFF;
  protected static final int TRANSPARENT = 0x00000000;

  /**
   * Create an one colored image.
   *
   * @param color
   *          the color (ARGB)
   * @param width
   *          the width
   * @param height
   *          the height
   * @return the image
   */
  protected static final BufferedImage createImage(int color, int width, int height) {
    BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    for (int w = 0; w < result.getWidth(); w++) {
      for (int h = 0; h < result.getHeight(); h++) {
        result.setRGB(w, h, color);
      }
    }
    result.flush();
    return result;
  }

  /**
   * Check whether a image is an one colored image (prints max diff).
   *
   * @param image
   *          the image
   * @param shall
   *          the color
   */
  protected final void checkColor(BufferedImage image, final int shall) {
    this.checkColor(image, shall, true, 1);
  }

  private void checkColor(BufferedImage image, final int shall, boolean printDiff, int depth) {
    int mxDiff = 0;
    for (int w = 0; w < image.getWidth(); w++) {
      for (int h = 0; h < image.getHeight(); h++) {
        mxDiff = Math.max(TestBase.assertPxEqual(shall, image.getRGB(w, h), w, h), mxDiff);
      }
    }
    if (printDiff) {
      System.err.println("Maxdiff for test " + TestBase.getCallerOfMe(depth) + " was " + mxDiff);
    }
  }

  /**
   * Compares two images.
   *
   * @param shall
   *          the shall image
   * @param is
   *          the is image
   * @param printDiff
   *          indicates whether the max diff (color) has to be printed
   */
  protected static void compareImages(BufferedImage shall, BufferedImage is, boolean printDiff) {
    Assert.assertEquals(shall.getWidth(), is.getWidth());
    Assert.assertEquals(shall.getHeight(), is.getHeight());
    int mxDiff = 0;
    for (int w = 0; w < shall.getWidth(); w++) {
      for (int h = 0; h < shall.getHeight(); h++) {
        mxDiff = Math.max(TestBase.assertPxEqual(shall.getRGB(w, h), is.getRGB(w, h), w, h),
            mxDiff);
      }
    }
    if (printDiff) {
      System.err.println("Maxdiff for test " + TestBase.getCallerOfMe(0) + " was " + mxDiff);
    }

  }

  /**
   * Compares two pixel values (ARGB).
   *
   * @param shall
   *          the shall value
   * @param actual
   *          the actual (is) value
   * @param x
   *          the x coordinate
   * @param y
   *          the y coordinate
   * @return the maximum delta (found)
   * @see #DELTA
   */
  protected static int assertPxEqual(int shall, int actual, int x, int y) {
    return TestBase.assertPxEqual(shall, actual, x, y, TestBase.DELTA);
  }

  /**
   * Compares two pixel values (ARGB).
   *
   * @param shall
   *          the shall value
   * @param actual
   *          the actual (is) value
   * @param x
   *          the x coordinate
   * @param y
   *          the y coordinate
   * @param delta
   *          the maximum delta
   * @return the maximum delta (found)
   */
  protected static int assertPxEqual(int shall, int actual, int x, int y, int delta) {
    int shallA = (shall >> 24 & 0x000000FF);
    int shallR = (shall >> 16 & 0x000000FF);
    int shallG = (shall >> 8 & 0x000000FF);
    int shallB = (shall & 0x000000FF);

    int actualA = (actual >> 24 & 0x000000FF);
    int actualR = (actual >> 16 & 0x000000FF);
    int actualG = (actual >> 8 & 0x000000FF);
    int actualB = (actual & 0x000000FF);

    Assert.assertEquals("Pos: " + x + "," + y, shallA, actualA, delta);
    Assert.assertEquals("Pos: " + x + "," + y, shallR, actualR, delta);
    Assert.assertEquals("Pos: " + x + "," + y, shallG, actualG, delta);
    Assert.assertEquals("Pos: " + x + "," + y, shallB, actualB, delta);

    int mxDiff = Math.abs(shallA - actualA);
    mxDiff = Math.max(mxDiff, Math.abs(shallR - actualR));
    mxDiff = Math.max(mxDiff, Math.abs(shallG - actualG));
    mxDiff = Math.max(mxDiff, Math.abs(shallB - actualB));
    return mxDiff;
  }

  private static String getCallerOfMe(int skip) {
    return TestBase.getCallerOfMe(skip + 1, true);
  }

  private static String getCallerOfMe(int skip, boolean line) {
    StackTraceElement[] ste = new Error().getStackTrace();
    if (ste.length < 3 + skip) {
      return null;
    }
    return ste[2 + skip].getMethodName() + (line ? " Line " + ste[2 + skip].getLineNumber() : "");
  }
}
