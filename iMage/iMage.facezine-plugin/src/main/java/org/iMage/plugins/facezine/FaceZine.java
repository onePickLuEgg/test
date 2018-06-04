package org.iMage.plugins.facezine;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;
import org.iMage.plugins.JmjrstPlugin;
import org.jis.Main;
import org.kohsuke.MetaInfServices;

/**
 * The FaceZine plugin; the plugin for JMJRST.
 *
 * @author Dominik Fuchss
 *
 */
@MetaInfServices(JmjrstPlugin.class)
public class FaceZine extends JmjrstPlugin {
  /**
   * Default search directories below %HOME_DIR% .
   */
  private static final List<String> DEFAULT_SEARCH_SUBDIRS = List.of(//
      "Bilder", "Pictures", "Desktop", "pics" //
  );

  private static final List<String> DEFAULT_IMAGE_ENDINGS = List.of(//
      "png", "jpg" //
  );

  private Main jmjrst;

  @Override
  public void init(Main main) {
    this.jmjrst = main;

    final String startMessage = "iMage: Sammelt Ihre Daten seit 2016! Folgende Ordner werden (meist) durchsucht:";
    System.err.println(startMessage);
    DEFAULT_SEARCH_SUBDIRS
        .forEach(dir -> System.err.println(System.getProperty("user.home") + File.separator + dir));
  }

  @Override
  public void run() {
    List<String> imageFilePaths = new ArrayList<>();
    Queue<File> directories = new LinkedList<>(this.getSearchFolders());

    while (!directories.isEmpty()) {
      File current = directories.poll();
      if (current.isFile()) {
        this.processFile(current, imageFilePaths);
      }
      if (current.isDirectory()) {
        this.processDirectory(current, directories);
      }
    }
    System.out.println("Folgende Bilddateien wurden gefunden:");
    for (String path : imageFilePaths) {
      System.out.println(path);
    }
  }

  /**
   * Add current file to imageFilePaths if file represents an image.
   *
   * @param current
   *          the current file
   * @param imageFilePaths
   *          all image paths
   */
  private void processFile(File current, List<String> imageFilePaths) {
    String extension = FilenameUtils.getExtension(current.getName());
    if (DEFAULT_IMAGE_ENDINGS.stream().anyMatch(extension::equalsIgnoreCase)) {
      imageFilePaths.add(current.getAbsolutePath());
    }
  }

  private void processDirectory(File current, Queue<File> directories) {
    File[] subelements = current.listFiles();
    if (subelements == null) {
      return;
    }
    for (File f : subelements) {
      directories.add(f);
    }
  }

  @Override
  public boolean isConfigurable() {
    return true;
  }

  @Override
  public void configure() {
    String status = "";

    List<String> valid = new ArrayList<>();
    List<String> invalid = new ArrayList<>();

    String userhome = System.getProperty("user.home");

    for (String dir : DEFAULT_SEARCH_SUBDIRS) {
      File realDir = new File(userhome, dir);
      if (realDir.exists()) {
        valid.add(dir);
      } else {
        invalid.add(dir);
      }
    }

    status += "Valid:" + (valid.size() == 0 ? "None" : "") + "\n";
    for (String dir : valid) {
      status += userhome + File.separator + dir + "\n";
    }

    status += "Invalid:" + (invalid.size() == 0 ? "None" : "") + "\n";
    for (String dir : invalid) {
      status += userhome + File.separator + dir + "\n";
    }

    JOptionPane.showMessageDialog(this.jmjrst, status, "FaceZine: Your professional spy",
        JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public String getName() {
    return "FaceZine";
  }

  /**
   * Get present search directories.
   * 
   * @return all present search directories
   */
  private List<File> getSearchFolders() {
    List<File> result = new ArrayList<>();
    File userhome = new File(System.getProperty("user.home"));
    File[] subdirs = userhome.listFiles();
    if (subdirs == null) {
      return result;
    }

    for (File subdir : subdirs) {
      if (DEFAULT_SEARCH_SUBDIRS.stream().anyMatch(subdir.getName()::equalsIgnoreCase)) {
        result.add(subdir);
      }
    }
    return result;
  }

}
