package org.iMage.shutterpile.impl.filters;

/**
 * This class realizes a simple grayscale filter.
 *
 * @author Dominik Fuchss
 *
 */
public final class GrayscaleFilter extends PixelwiseFilter {

  @Override
  protected int getPixelColor(int color) {
    int red = color >> 16 & 0x000000FF;
    int green = color >> 8 & 0x000000FF;
    int blue = color & 0x000000FF;
    int alpha = color >> 24 & 0x000000FF;
    int u = (red + green + blue) / 3;
    return alpha << 24 | u << 16 | u << 8 | u;
  }

}
