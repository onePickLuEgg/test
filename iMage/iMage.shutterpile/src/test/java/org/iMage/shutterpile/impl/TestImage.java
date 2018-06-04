package org.iMage.shutterpile.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.iMage.shutterpile.impl.supplier.ImageWatermarkSupplier;
import org.junit.Before;
import org.junit.Test;

/**
 * Test watermarking with different images (not known by the students).
 *
 * @author Dominik Fuchss
 *
 */
public class TestImage extends TestBase {

  private BufferedImage tichyBase;
  private ImageWatermarkSupplier fziWMS, kitWMS;

  /**
   * Load test images.
   *
   * @throws IOException
   *           if file not found or corrupted
   */
  @Before
  public void loadImages() throws IOException {
    Class<?> clazz = TestBase.class;
    this.tichyBase = ImageIO.read(clazz.getResourceAsStream("/images/tichy.png"));
    this.fziWMS = new ImageWatermarkSupplier(
        ImageIO.read(clazz.getResourceAsStream("/images/fzi.png")));
    this.kitWMS = new ImageWatermarkSupplier(
        ImageIO.read(clazz.getResourceAsStream("/images/kit.png")));
  }

  /**
   * Use tichy.png and fzi.png in images.
   * 
   * @throws Exception
   *           if sth goes wrong
   */
  @Test
  public void testImageTichyFZI() throws Exception {
    Class<?> clazz = this.getClass();

    BufferedImage shall = ImageIO.read(clazz.getResource("/images/tichy-fzi-20.png"));
    BufferedImage is = new Watermarker(this.fziWMS).generate(tichyBase, 20);

    compareImages(shall, is, true);
  }

  /**
   * Use tichy.png and kit.png in images.
   * 
   * @throws Exception
   *           if sth goes wrong
   */
  @Test
  public void testImageTichyKIT() throws Exception {
    Class<?> clazz = this.getClass();

    BufferedImage shall = ImageIO.read(clazz.getResource("/images/tichy-kit-30.png"));
    BufferedImage is = new Watermarker(this.kitWMS).generate(tichyBase, 30);

    compareImages(shall, is, true);
  }

}
