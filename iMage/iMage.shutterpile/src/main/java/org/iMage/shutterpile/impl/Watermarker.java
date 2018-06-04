package org.iMage.shutterpile.impl;

import java.awt.image.BufferedImage;
import java.util.Objects;

import org.iMage.shutterpile.impl.filters.WatermarkFilter;
import org.iMage.shutterpile.port.IWatermarkSupplier;
import org.iMage.shutterpile.port.IWatermarker;

/**
 * This class realizes a {@link IWatermarker} which uses {@link BufferedImage BufferedImages} as
 * watermark.
 *
 * @author Dominik Fuchss
 *
 */
public class Watermarker implements IWatermarker {

  private final IWatermarkSupplier iws;

  /**
   * Create the watermarker by {@link IWatermarkSupplier}.
   *
   * @param iws
   *          the watermark supplier
   */
  public Watermarker(IWatermarkSupplier iws) {
    Objects.requireNonNull(iws);
    this.iws = iws;
  }

  @Override
  public BufferedImage generate(BufferedImage input, int watermarksPerRow) {
    BufferedImage watermark = this.iws.getWatermark();
    WatermarkFilter wmf = new WatermarkFilter(watermark, watermarksPerRow);
    return wmf.apply(input);
  }

}
