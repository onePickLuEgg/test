package org.iMage.shutterpile.impl.supplier;

import java.awt.image.BufferedImage;

import org.iMage.shutterpile.impl.filters.GrayscaleFilter;
import org.iMage.shutterpile.impl.filters.ThresholdFilter;
import org.iMage.shutterpile.impl.util.ImageUtils;
import org.iMage.shutterpile.port.IFilter;
import org.iMage.shutterpile.port.IWatermarkSupplier;

/**
 * This class realizes a {@link IWatermarkSupplier} which uses a {@link BufferedImage} to generate
 * the watermark.
 *
 * @author Dominik Fuchss
 *
 */
public final class ImageWatermarkSupplier implements IWatermarkSupplier {

  /**
   * This factor indicates by how much the transparency is increased. The new value alpha' is
   * calculated by <em>alpha' = (alpha * FACTOR) / 100</em>
   */
  public static final int DEFAULT_FACTOR = 50;

  private final IFilter gsf = new GrayscaleFilter();
  private final IFilter thf = new ThresholdFilter();

  private final BufferedImage watermarkInput;
  private final boolean useGrayscaleFilter;

  private BufferedImage createdWatermark;

  /**
   * Create the {@link IWatermarkSupplier} by base image of watermark. (Use GrayscaleFilter)
   *
   * @param watermarkInput
   *          the base image to create the watermark
   * @see #ImageWatermarkSupplier(BufferedImage, boolean)
   */
  public ImageWatermarkSupplier(BufferedImage watermarkInput) {
    this(watermarkInput, true);
  }

  /**
   * Create the {@link IWatermarkSupplier} by base image of watermark.
   *
   * @param watermarkInput
   *          the base image to create the watermark
   * @param useGrayscaleFilter
   *          indicates whether a {@link GrayscaleFilter} shall applied upon the input image
   */
  public ImageWatermarkSupplier(BufferedImage watermarkInput, boolean useGrayscaleFilter) {
    this.watermarkInput = watermarkInput;
    this.useGrayscaleFilter = useGrayscaleFilter;
  }

  @Override
  public BufferedImage getWatermark() {
    if (this.createdWatermark == null) {
      // Create ARGB image as filters need (A)RGB
      BufferedImage watermark = ImageUtils.createARGBImage(this.watermarkInput);
      // Apply GrayscaleFilter
      if (this.useGrayscaleFilter) {
        watermark = this.gsf.apply(watermark);
      }
      // Apply ThresholdFilter
      watermark = this.thf.apply(watermark);
      // Set alpha value / create ARGB as we guarantee an ARBG-Image
      watermark = ImageUtils.createARGBImage(watermark);
      this.applyAlpha(watermark);
      this.createdWatermark = watermark;
    }
    return this.createdWatermark;
  }

  private void applyAlpha(BufferedImage wm) {
    for (int i = 0; i < wm.getWidth(); i++) {
      for (int q = 0; q < wm.getHeight(); q++) {
        int color = wm.getRGB(i, q);
        int alpha = color >> 24 & 0x000000FF;
        alpha = (alpha * ImageWatermarkSupplier.DEFAULT_FACTOR) / 100;
        wm.setRGB(i, q, (color & 0x00FFFFFF) | (alpha << 24));
      }
    }
    wm.flush();
  }

}
