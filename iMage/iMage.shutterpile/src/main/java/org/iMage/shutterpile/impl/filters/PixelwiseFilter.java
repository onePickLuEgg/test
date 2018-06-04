package org.iMage.shutterpile.impl.filters;

import java.awt.image.BufferedImage;

import org.iMage.shutterpile.port.IFilter;

/**
 * This class can be used as base class for filters which operates pixel per pixel and only needs
 * its color.
 *
 * @author Dominik Fuchss
 *
 */
abstract class PixelwiseFilter implements IFilter {
  @Override
  public final BufferedImage apply(BufferedImage image) {
    if (image == null) {
      throw new IllegalArgumentException("Null is no image");
    }
    BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
    for (int i = 0; i < result.getWidth(); i++) {
      for (int q = 0; q < result.getHeight(); q++) {
        result.setRGB(i, q, this.getPixelColor(image.getRGB(i, q)));
      }
    }
    result.flush();
    return result;
  }

  /**
   * Convert the original pixel color to a new color.
   *
   * @param color
   *          the original color
   * @return the new color
   */
  protected abstract int getPixelColor(int color);
}
