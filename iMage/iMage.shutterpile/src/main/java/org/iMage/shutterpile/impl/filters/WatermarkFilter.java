package org.iMage.shutterpile.impl.filters;

import java.awt.image.BufferedImage;
import java.util.Objects;

import org.iMage.shutterpile.impl.util.ImageUtils;
import org.iMage.shutterpile.port.IFilter;
import org.iMage.shutterpile.port.IWatermarkSupplier;

/**
 * This {@link IFilter Filter} adds a watermark ({@link BufferedImage}) to an image.
 *
 * @author Dominik Fuchss
 *
 */
public final class WatermarkFilter implements IFilter {
  private final BufferedImage watermark;
  private int watermarksPerRow;

  /**
   * Create a the WatermarkFilter.
   *
   * @param watermark
   *          the watermark image as provided by a {@link IWatermarkSupplier}.
   * @param watermarksPerRow
   *          the number of watermarks in a line (this is meant as desired value. the possible
   *          surplus is drawn)
   */
  public WatermarkFilter(BufferedImage watermark, int watermarksPerRow) {
    Objects.requireNonNull(watermark);
    this.watermark = watermark;
    this.setWatermarksPerRow(watermarksPerRow);
  }

  @Override
  public BufferedImage apply(BufferedImage input) {
    int imgWidth = input.getWidth();
    int imgHeight = input.getHeight();

    int watermarkWidth = imgWidth / this.watermarksPerRow;
    int watermarkHeight;
    if (watermarkWidth <= 0) {
      throw new IllegalArgumentException("watermark width would be too small");
    }
    BufferedImage watermark = ImageUtils.createARGBImage(this.watermark);
    watermark = ImageUtils.scaleWidth(watermark, watermarkWidth);
    watermarkHeight = watermark.getHeight();

    BufferedImage result = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
    for (int w = 0; w < imgWidth; w += watermarkWidth) {
      for (int h = 0; h < imgHeight; h += watermarkHeight) {
        this.mergeImages(input, watermark, w, h, result);
      }
    }
    result.flush();
    return result;
  }

  /**
   * Set the number of watermarks in a line (this is meant as desired value. the possible surplus is
   * drawn)
   *
   * @param watermarksPerRow
   *          the target value of watermarks in a line (shall be &gt; 0)
   */
  public void setWatermarksPerRow(int watermarksPerRow) {
    if (watermarksPerRow < 1) {
      throw new IllegalArgumentException("watermarksPerRow must be >= 1");
    }
    this.watermarksPerRow = watermarksPerRow;
  }

  /**
   * Draw a small image to a bigger image using alpha compositing.
   *
   * @param source
   *          the source image (large image)
   * @param toDraw
   *          the image to be drawn on top of <em>source</em>
   * @param x
   *          the start index (horizontal) in <em>source</em>
   * @param y
   *          the start index (vertical) in <em>source</em>
   * @param target
   *          a target image (same size as <em>source</em>) to draw the new image
   */
  private void mergeImages(BufferedImage source, BufferedImage toDraw, int x, int y,
      BufferedImage target) {
    int sW = source.getWidth();
    int sH = source.getHeight();
    int dW = toDraw.getWidth();
    int dH = toDraw.getHeight();

    for (int w = 0; w < dW && w + x < sW; w++) {
      for (int h = 0; h < dH && h + y < sH; h++) {
        target.setRGB(x + w, y + h, this.merge(toDraw.getRGB(w, h), source.getRGB(x + w, y + h)));
      }
    }
  }

  /**
   * Calculate color after draw A over B (see:
   * <a href="https://de.wikipedia.org/wiki/Alpha_Blending">Alpha Blending</a>).
   *
   * @param inputA
   *          the color (ARGB) which shall drawn over <em>B</em>
   * @param inputB
   *          the color (ARGB) that is painted over <em>A</em>
   * @return the result color (ARGB)
   */
  private int merge(int inputA, int inputB) {
    int aA = (inputA >> 24 & 0x000000FF);
    int rA = (inputA >> 16 & 0x000000FF);
    int gA = (inputA >> 8 & 0x000000FF);
    int bA = (inputA & 0x000000FF);

    int aB = (inputB >> 24 & 0x000000FF);
    int rB = (inputB >> 16 & 0x000000FF);
    int gB = (inputB >> 8 & 0x000000FF);
    int bB = (inputB & 0x000000FF);

    int a = (255 * aA + (255 - aA) * aB) / 255;
    // If fully transparent do nothing
    if (a == 0) {
      return 0x00000000;
    }

    int r = (255 * aA * rA + (255 - aA) * aB * rB) / (255 * a);
    int g = (255 * aA * gA + (255 - aA) * aB * gB) / (255 * a);
    int b = (255 * aA * bA + (255 - aA) * aB * bB) / (255 * a);

    return a << 24 | r << 16 | g << 8 | b;
  }

}
