package org.iMage.shutterpile.impl.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

/**
 * This class contains some useful methods to work with {@link BufferedImage BufferedImages}.
 *
 * @author Dominik Fuchss
 *
 */
public final class ImageUtils {

  private ImageUtils() {
    throw new IllegalAccessError();
  }

  /**
   * Create an ARGB {@link BufferedImage} by the original input image.
   *
   * @param input
   *          the input image
   * @return the converted (new) image
   */
  public static BufferedImage createARGBImage(BufferedImage input) {
    BufferedImage res = null;
    if (input.getType() != BufferedImage.TYPE_INT_ARGB) {
      return ARGBConverter.convert(input);
    } else {
      ColorModel cm = input.getColorModel();
      boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
      res = new BufferedImage(cm, input.copyData(null), isAlphaPremultiplied, null);
    }
    return res;
  }

  /**
   * Scale Image by width (set width and the height will calculated).
   *
   * @param input
   *          image to scale
   *
   * @param width
   *          the target width
   *
   * @return scaled image
   */
  public static BufferedImage scaleWidth(BufferedImage input, int width) {
    if (width <= 0) {
      throw new IllegalArgumentException("width cannot be <= 0");
    }
    Image scaled = input.getScaledInstance(width, -1, Image.SCALE_SMOOTH);
    int height = scaled.getHeight(null);
    if (height <= 0) {
      throw new IllegalArgumentException("height would be 0");
    }
    BufferedImage res = new BufferedImage(width, height, input.getType());
    Graphics2D g2d = res.createGraphics();
    g2d.drawImage(scaled, 0, 0, null);
    g2d.dispose();
    res.flush();
    return res;
  }
}
