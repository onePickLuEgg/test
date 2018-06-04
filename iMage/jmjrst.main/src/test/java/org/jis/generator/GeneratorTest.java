package org.jis.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Sommersemester 2018 - Übungsblatt 1 - Aufgabe 2
 */
public class GeneratorTest {
  /**
   * Class under test.
   */
  private Generator generator;

  private int imageHeight, imageWidth;
  private static final File TEST_DIR = new File("target/dataTest");
  private static final String IMAGE_FILE = "/picture.jpg";

  /**
   * Input for test cases
   */
  private static BufferedImage testImage;
  /**
   * Metadata for saving the image
   */
  private IIOMetadata imeta;
  /**
   * output from test cases
   */
  private BufferedImage rotatedImageTestResult;

  /**
   * Aufgabe 2 h) Teil 1: Sicherstellen, dass das Ausgabeverzeichnis existiert und leer ist.
   */
  @BeforeClass
  public static void beforeClass() {
    if (TEST_DIR.exists()) {
      for (File f : TEST_DIR.listFiles()) {
        f.delete();
      }
    } else {
      TEST_DIR.mkdirs();
    }
  }

  /**
   * Aufgabe 2 c)
   * 
   */
  @Before
  public void setUp() {
    generator = new Generator(null, 0);

    testImage = null;
    imeta = null;
    rotatedImageTestResult = null;

    try (ImageInputStream iis = ImageIO
        .createImageInputStream(this.getClass().getResourceAsStream(IMAGE_FILE));) {
      ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
      reader.setInput(iis, true);
      ImageReadParam params = reader.getDefaultReadParam();
      testImage = reader.read(0, params);
      imageHeight = testImage.getHeight();
      imageWidth = testImage.getWidth();
      imeta = reader.getImageMetadata(0);
      reader.dispose();
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  /**
   * Aufgabe 2 h) Teil 2: Automatisches Speichern von testImage.
   */
  @After
  public void tearDown() {
    SimpleDateFormat sdf = new SimpleDateFormat("HHmmss_SSS");
    String time = sdf.format(new Date());

    File outputFile = new File(TEST_DIR + "/rotatedPicture_" + time + ".jpg");

    if (rotatedImageTestResult != null) {
      try (FileOutputStream fos = new FileOutputStream(outputFile);
          ImageOutputStream ios = ImageIO.createImageOutputStream(fos);) {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        writer.setOutput(ios);

        ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
        iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // mode explicit necessary

        // set JPEG Quality
        iwparam.setCompressionQuality(1f);
        writer.write(imeta, new IIOImage(rotatedImageTestResult, null, null), iwparam);
        writer.dispose();
      } catch (IOException e) {
        fail();
      }
    }
  }

  /**
   * Aufgabe 2 d) Teil 1
   */
  @Test
  public void testRotateImage_RotateImage0() {
    rotatedImageTestResult = generator.rotateImage(testImage, 0);

    assertTrue(imageEquals(testImage, rotatedImageTestResult));
  }

  /**
   * Aufgabe 2 d) Teil 2
   */
  @Test
  public void testRotateImage_RotateNull0() {
    rotatedImageTestResult = generator.rotateImage(null, 0);

    assertTrue(null == rotatedImageTestResult);
  }

  /**
   * Aufgabe 2 e)
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRotateImage_Rotate1() {
    generator.rotateImage(testImage, 0.5);
  }

  /**
   * Aufgabe 2 f) Teil 1
   */
  @Test
  public void testRotateImage_Rotate90() {
    rotatedImageTestResult = generator.rotateImage(testImage, Generator.ROTATE_90);

    assertEquals(testImage.getHeight(), rotatedImageTestResult.getWidth());
    assertEquals(testImage.getWidth(), rotatedImageTestResult.getHeight());

    for (int i = 0; i < imageHeight; i++) {
      for (int j = 0; j < imageWidth; j++) {
        assertEquals(testImage.getRGB(j, i), rotatedImageTestResult.getRGB(imageHeight - 1 - i, j));
      }
    }
  }

  /**
   * Aufgabe 2 f) Teil 2
   */
  @Test
  public void testRotateImage_Rotate270() {
    rotatedImageTestResult = generator.rotateImage(testImage, Generator.ROTATE_270);

    assertEquals(testImage.getHeight(), rotatedImageTestResult.getWidth());
    assertEquals(testImage.getWidth(), rotatedImageTestResult.getHeight());

    for (int i = 0; i < imageHeight; i++) {
      for (int j = 0; j < imageWidth; j++) {
        assertEquals(testImage.getRGB(j, i), rotatedImageTestResult.getRGB(i, imageWidth - 1 - j));
      }
    }
  }

  /**
   * Aufgabe 2 g)
   */
  @Test
  public void testRotateImage_Rotate180() {
    rotatedImageTestResult = generator.rotateImage(testImage, Math.toRadians(180));

    assertEquals(testImage.getHeight(), rotatedImageTestResult.getHeight());
    assertEquals(testImage.getWidth(), rotatedImageTestResult.getWidth());

    for (int i = 0; i < imageHeight; i++) {
      for (int j = 0; j < imageWidth; j++) {
        assertEquals(testImage.getRGB(j, i),
            rotatedImageTestResult.getRGB(imageWidth - 1 - j, imageHeight - 1 - i));
      }
    }
  }

  /**
   * Check if two images are identical - pixel wise.
   * 
   * @param expected
   *          the expected image
   * @param actual
   *          the actual image
   * @return true if images are equal, false otherwise.
   */
  protected static boolean imageEquals(BufferedImage expected, BufferedImage actual) {
    if (expected == null || actual == null) {
      return false;
    }

    if (expected.getHeight() != actual.getHeight()) {
      return false;
    }

    if (expected.getWidth() != actual.getWidth()) {
      return false;
    }

    for (int i = 0; i < expected.getHeight(); i++) {
      for (int j = 0; j < expected.getWidth(); j++) {
        if (expected.getRGB(j, i) != actual.getRGB(j, i)) {
          return false;
        }
      }
    }

    return true;
  }

}
