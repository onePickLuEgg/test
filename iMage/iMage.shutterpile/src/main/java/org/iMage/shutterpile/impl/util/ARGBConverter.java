package org.iMage.shutterpile.impl.util;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * HelperClass: Convert a {@link BufferedImage} to {@link BufferedImage#TYPE_INT_ARGB}.
 *
 * @author Dominik Fuchss
 *
 */
public final class ARGBConverter {
  private ARGBConverter() {
    throw new IllegalAccessError();
  }

  private static final Function<BufferedImage, BufferedImage> DEFAULT_CONVERTER = ARGBConverter::slowConvert;
  private static final Map<Integer, Function<BufferedImage, BufferedImage>> CONVERTERS = new HashMap<>() {
    private static final long serialVersionUID = 1L;

    {
      this.put(BufferedImage.TYPE_INT_ARGB, ARGBConverter::copy);
      this.put(BufferedImage.TYPE_4BYTE_ABGR, ARGBConverter::from4ByteABGR);
      this.put(BufferedImage.TYPE_3BYTE_BGR, ARGBConverter::from3ByteBGR);
    }
  };

  /**
   * Convert a {@link BufferedImage} to {@link BufferedImage#TYPE_INT_ARGB}.
   *
   * @param input
   *          the input image
   * @return the <em>new</em> image
   */
  public static BufferedImage convert(BufferedImage input) {
    Function<BufferedImage, BufferedImage> converter = CONVERTERS.get(input.getType());
    if (converter == null) {
      converter = DEFAULT_CONVERTER;
    }
    return converter.apply(input);
  }

  private static BufferedImage from4ByteABGR(BufferedImage input) {
    int w = input.getWidth();
    int h = input.getHeight();
    BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    int[] data = input.getData().getPixels(0, 0, w, h, (int[]) null);
    int[] newData = Arrays.copyOf(data, data.length);
    res.getRaster().setPixels(0, 0, w, h, newData);
    res.flush();
    return res;
  }

  private static BufferedImage from3ByteBGR(BufferedImage input) {
    int w = input.getWidth();
    int h = input.getHeight();
    BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    int[] data = input.getData().getPixels(0, 0, w, h, (int[]) null);
    int[] newData = Arrays.copyOf(data, data.length * 4 / 3);
    for (int i = 0; i < newData.length / 4; i++) {
      newData[4 * i + 0] = data[3 * i + 0];
      newData[4 * i + 1] = data[3 * i + 1];
      newData[4 * i + 2] = data[3 * i + 2];
      newData[4 * i + 3] = 255;
    }
    res.getRaster().setPixels(0, 0, w, h, newData);
    res.flush();
    return res;
  }

  private static BufferedImage copy(BufferedImage input) {
    ColorModel cm = input.getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    return new BufferedImage(cm, input.copyData(null), isAlphaPremultiplied, null);
  }

  private static BufferedImage slowConvert(BufferedImage input) {
    int w = input.getWidth();
    int h = input.getHeight();
    BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    for (int i = 0; i < w; i++) {
      for (int q = 0; q < h; q++) {
        res.setRGB(i, q, input.getRGB(i, q));
      }
    }
    return res;
  }

}
