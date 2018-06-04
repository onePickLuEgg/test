package org.iMage.shutterpile.impl.filters;

/**
 * Filters all pixels that have a 'grayscale color' above a certain threshold and set them to
 * transparent. Pixels below the threshold won't be altered.
 *
 * @author Dominik Fuchss
 */
public final class ThresholdFilter extends PixelwiseFilter {

  /**
   * If the value surpasses the upper threshold value, the color is set directly to transparent.
   */
  public static final int DEFAULT_THRESHOLD = 127;

  private final int threshold;

  /**
   * Will invoke {@link #ThresholdFilter(int)} with the default threshold value
   * {@link #DEFAULT_THRESHOLD}.
   */
  public ThresholdFilter() {
    this.threshold = ThresholdFilter.DEFAULT_THRESHOLD;
  }

  /**
   * Create a new ThresholdFilter by lower threshold.
   *
   * @param threshold
   *          the lower threshold (see {@link #DEFAULT_THRESHOLD})
   */
  public ThresholdFilter(int threshold) {
    if (threshold < 0 || threshold > 255) {
      throw new IllegalArgumentException("Out of bounds [0,255]");
    }
    this.threshold = threshold;
  }

  @Override
  protected int getPixelColor(int color) {
    // Same procedure as in GrayScaleFilter ..
    int red = color >> 16 & 0x000000FF;
    int green = color >> 8 & 0x000000FF;
    int blue = color & 0x000000FF;
    int u = (red + green + blue) / 3;

    if ((u & 0xFF) > this.threshold) {
      // Set to transparent, if gray value is greater than threshold
      return 0x00000000;
    }
    return color;
  }

}