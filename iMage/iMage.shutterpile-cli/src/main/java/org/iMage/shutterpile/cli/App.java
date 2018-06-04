package org.iMage.shutterpile.cli;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.iMage.shutterpile.impl.Watermarker;
import org.iMage.shutterpile.impl.supplier.ImageWatermarkSupplier;
import org.iMage.shutterpile.port.IWatermarkSupplier;
import org.iMage.shutterpile.port.IWatermarker;

/**
 * * This class parses all command line parameters and creates an Image with watermark.
 *
 * @author Dominik Fuchss
 *
 */
public final class App {
  private App() {
    throw new IllegalAccessError();
  }

  private static final String CMD_OPTION_ORIGINAL_IMAGE = "o";
  private static final String CMD_OPTION_WATERMARK_IMAGE = "w";
  private static final String CMD_OPTION_RETURN_IMAGE = "r";
  private static final String CMD_OPTION_WATERMARKS_PER_ROW = "n";
  private static final String CMD_OPTION_COLORED = "c";

  private static final int DEFAULT_WATERMARKS_PER_ROW = 5;

  /**
   * The main method.<br>
   * Possible arguments:<br>
   * <b>o</b> - the path to the original image to which the watermark is to be applied<br>
   * <b>w</b> - the path to the watermark image<br>
   * <b>r</b> - the path to returned image<br>
   * <b>n</b> - the number of watermarks in a line (default: 5) (see
   * {@link IWatermarker#generate(java.awt.image.BufferedImage, java.awt.image.BufferedImage, int)
   * IWatermarker#generate})<br>
   * <b>c</b> - indication, whether the used watermark should be colored (default: false)
   *
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    CommandLine cmd = null;
    try {
      cmd = App.doCommandLineParsing(args);
    } catch (ParseException e) {
      System.err.println("Wrong command line arguments given: " + e.getMessage());
      System.exit(1);
    }

    int watermarksPerRow = cmd.hasOption(App.CMD_OPTION_WATERMARKS_PER_ROW)
        ? Integer.parseInt(cmd.getOptionValue(App.CMD_OPTION_WATERMARKS_PER_ROW))
        : App.DEFAULT_WATERMARKS_PER_ROW;

    boolean colored = cmd.hasOption(CMD_OPTION_COLORED);

    if (watermarksPerRow <= 0) {
      System.err.println("WatermarksPerRow must be greater than 0");
      System.exit(1);
    }

    BufferedImage input = null;
    BufferedImage watermarkInput = null;
    File output = null;
    try {
      File iFile = App.ensureFile(cmd.getOptionValue(App.CMD_OPTION_ORIGINAL_IMAGE), false);
      File wFile = App.ensureFile(cmd.getOptionValue(App.CMD_OPTION_WATERMARK_IMAGE), false);

      input = ImageIO.read(iFile);
      watermarkInput = ImageIO.read(wFile);

      output = App.ensureFile(cmd.getOptionValue(App.CMD_OPTION_RETURN_IMAGE), true);
    } catch (IOException e) {
      System.err.println("Cannot process all files: " + e.getMessage());
      System.exit(1);
    }

    IWatermarkSupplier wms = new ImageWatermarkSupplier(watermarkInput, !colored);
    IWatermarker wm = new Watermarker(wms);
    BufferedImage outputImage = wm.generate(input, watermarksPerRow);

    try {
      ImageIO.write(outputImage, "png", output);
    } catch (IOException e) {
      System.err.println("Could not save image: " + e.getMessage());
      System.exit(1);
    }
  }

  /**
   * Ensure that a file exists (or create if allowed by parameter).
   *
   * @param path
   *          the path to the file
   * @param create
   *          indicates whether creation is allowed
   * @return the file
   * @throws IOException
   *           if something went wrong
   */
  private static File ensureFile(String path, boolean create) throws IOException {
    File file = new File(path);
    if (file.exists()) {
      return file;
    }
    if (create) {
      file.createNewFile();
      return file;
    }

    // File not available
    throw new IOException("The specified file does not exist: " + path);
  }

  /**
   * Parse and check command line arguments
   *
   * @param args
   *          command line arguments given by the user
   * @return CommandLine object encapsulating all options
   * @throws ParseException
   *           if wrong command line parameters or arguments are given
   */
  private static CommandLine doCommandLineParsing(String[] args) throws ParseException {
    Options options = new Options();
    Option opt;

    /*
     * Define command line options and arguments
     */
    opt = new Option(App.CMD_OPTION_ORIGINAL_IMAGE, "image-input", true, "path to input image");
    opt.setRequired(true);
    opt.setType(String.class);
    options.addOption(opt);

    opt = new Option(App.CMD_OPTION_WATERMARK_IMAGE, "watermark-input", true,
        "path to watermark image");
    opt.setRequired(true);
    opt.setType(String.class);
    options.addOption(opt);

    opt = new Option(App.CMD_OPTION_RETURN_IMAGE, "image-output", true, "path to output image");
    opt.setRequired(true);
    opt.setType(String.class);
    options.addOption(opt);

    opt = new Option(App.CMD_OPTION_WATERMARKS_PER_ROW, "count-per-row", true,
        "number of watermarks in a line");
    opt.setRequired(false);
    opt.setType(Integer.class);
    options.addOption(opt);

    opt = new Option(App.CMD_OPTION_COLORED, "colored", false, "colored watermark");
    opt.setRequired(false);
    opt.setType(Boolean.class);
    options.addOption(opt);

    CommandLineParser parser = new DefaultParser();
    return parser.parse(options, args);
  }

}
