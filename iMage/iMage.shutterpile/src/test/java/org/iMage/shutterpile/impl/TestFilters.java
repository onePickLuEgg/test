package org.iMage.shutterpile.impl;

import org.iMage.shutterpile.impl.filters.GrayscaleFilter;
import org.iMage.shutterpile.impl.filters.ThresholdFilter;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests to test behavior of the filters.
 *
 * @author Dominik Fuchss
 *
 */
public class TestFilters extends TestBase {

//@formatter:off
  /*
   * RED    = 0xFFFF0000
   * BLACK  = 0xFF000000
   *
   * Apply filters to black
   * GrayScale => 0xFF000000
   * Threshold => 0xFF000000
   *
   * Apply filters to red
   * GrayScale => 0xFF555555 ((0+0+FF) / 3 => 55)
   * Threshold => 0xFF555555 (55 < 7F)
   *
   */
//@formatter:on

  private GrayscaleFilter gsf;
  private ThresholdFilter thf;

  /**
   * Setup the filters.
   */
  @Before
  public void setupFilters() {
    this.gsf = new GrayscaleFilter();
    this.thf = new ThresholdFilter();
  }

  /**
   * Test some values for the {@link GrayscaleFilter}.
   */
  @Test
  public void testSimpleGrayscaleFilter() {
    this.checkColor(this.gsf.apply(createImage(TestBase.BLACK, 10, 10)), TestBase.BLACK);
    this.checkColor(this.gsf.apply(createImage(TestBase.RED, 10, 10)), 0xFF555555);
  }

  /**
   * Test some values for the default {@link ThresholdFilter}.
   */
  @Test
  public void testSimpleThresholdFilter() {
    this.checkColor(this.thf.apply(createImage(TestBase.BLACK, 10, 10)), TestBase.BLACK);
    this.checkColor(this.thf.apply(createImage(TestBase.RED, 10, 10)), TestBase.RED);
    this.checkColor(this.thf.apply(createImage(TestBase.WHITE, 10, 10)), TestBase.TRANSPARENT);
  }

  /**
   * Check bounds of the default threshold filter.
   */
  @Test
  public void testBoundsThF() {
    // Check for RGB = 127
    this.checkColor(this.thf.apply(createImage(0xFF7F7F7F, 1, 1)), 0xFF7F7F7F);
    // Check for R|G|B = 128
    this.checkColor(this.thf.apply(createImage(0xFF808080, 1, 1)), TestBase.TRANSPARENT);
  }
}
