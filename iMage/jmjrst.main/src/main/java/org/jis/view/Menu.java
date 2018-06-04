/*
 * Copyright 2007 Johannes Geppert
 *
 * Licensed under the GPL, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.fsf.org/licensing/licenses/gpl.txt
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.jis.view;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import org.iMage.plugins.JmjrstPlugin;
import org.iMage.plugins.PluginManager;
import org.jis.Main;
import org.jis.listner.MenuListner;

/**
 * @author <a href="http://www.jgeppert.com">Johannes Geppert</a>
 *
 *         <p>
 *         This is Menu of the GUI
 *         </p>
 */
public class Menu extends JMenuBar {
  private static final long serialVersionUID = 1232107393895691717L;

  public JMenuItem gener;
  public JMenuItem zippen;
  public JMenuItem gallerie;
  public JMenuItem exit;
  public JMenuItem set_quality;
  public JMenuItem info;
  public JMenuItem look_windows;
  public JMenuItem look_windows_classic;
  public JMenuItem look_nimbus;
  public JMenuItem look_metal;
  public JMenuItem look_motif;
  public JMenuItem look_gtk;
  public JMenuItem update_check;

  /**
   * @param m
   *          a reference to the Main class
   */
  public Menu(Main m) {
    super();
    JMenu datei = new JMenu(m.mes.getString("Menu.0"));
    JMenu option = new JMenu(m.mes.getString("Menu.1"));
    JMenu optionen_look = new JMenu(m.mes.getString("Menu.2"));
    JMenu plugins = this.createPluginMenu(m);
    JMenu about = new JMenu(m.mes.getString("Menu.3"));

    this.gener = new JMenuItem(m.mes.getString("Menu.4"));
    URL url = ClassLoader.getSystemResource("icons/media-playback-start.png");
    this.gener.setIcon(new ImageIcon(url));

    url = ClassLoader.getSystemResource("icons/preferences-desktop-theme.png");
    optionen_look.setIcon(new ImageIcon(url));

    this.zippen = new JMenuItem(m.mes.getString("Menu.13"));
    url = ClassLoader.getSystemResource("icons/package-x-generic.png");
    this.zippen.setIcon(new ImageIcon(url));

    this.gallerie = new JMenuItem(m.mes.getString("Menu.14"));
    url = ClassLoader.getSystemResource("icons/text-html.png");
    this.gallerie.setIcon(new ImageIcon(url));

    this.exit = new JMenuItem(m.mes.getString("Menu.5"));
    url = ClassLoader.getSystemResource("icons/system-log-out.png");
    this.exit.setIcon(new ImageIcon(url));

    this.set_quality = new JMenuItem(m.mes.getString("Menu.6"));
    url = ClassLoader.getSystemResource("icons/preferences-system.png");
    this.set_quality.setIcon(new ImageIcon(url));

    this.info = new JMenuItem(m.mes.getString("Menu.7"));
    url = ClassLoader.getSystemResource("icons/help-browser.png");
    this.info.setIcon(new ImageIcon(url));

    this.update_check = new JMenuItem(m.mes.getString("Menu.15"));
    url = ClassLoader.getSystemResource("icons/system-software-update.png");
    this.update_check.setIcon(new ImageIcon(url));

    this.look_windows = new JMenuItem(m.mes.getString("Menu.8"));
    this.look_windows_classic = new JMenuItem(m.mes.getString("Menu.9"));
    this.look_nimbus = new JMenuItem(m.mes.getString("Menu.16"));
    this.look_metal = new JMenuItem(m.mes.getString("Menu.10"));
    this.look_motif = new JMenuItem(m.mes.getString("Menu.11"));
    this.look_gtk = new JMenuItem(m.mes.getString("Menu.12"));

    this.gener.setEnabled(false);
    this.zippen.setEnabled(false);
    this.gallerie.setEnabled(false);

    datei.add(this.gener);
    datei.add(this.zippen);
    datei.add(this.gallerie);

    datei.addSeparator();
    datei.add(this.exit);
    option.add(optionen_look);
    option.add(this.set_quality);
    option.addSeparator();
    option.add(this.update_check);
    about.add(this.info);
    this.add(datei);
    this.add(option);

    // Add Plugins between options and about menu
    this.add(plugins);

    this.add(about);

    MenuListner al = new MenuListner(m, this);
    this.exit.addActionListener(al);
    this.gener.addActionListener(al);
    this.zippen.addActionListener(al);
    this.gallerie.addActionListener(al);
    this.set_quality.addActionListener(al);
    this.info.addActionListener(al);
    this.look_windows.addActionListener(al);
    this.look_windows_classic.addActionListener(al);
    this.look_nimbus.addActionListener(al);
    this.look_metal.addActionListener(al);
    this.look_motif.addActionListener(al);
    this.look_gtk.addActionListener(al);
    this.update_check.addActionListener(al);

    UIManager.LookAndFeelInfo uii[] = UIManager.getInstalledLookAndFeels();
    for (int i = 0; i < uii.length; i++) {
      if (uii[i].toString()
          .substring(uii[i].toString().lastIndexOf(" ") + 1, uii[i].toString().lastIndexOf("]")) //$NON-NLS-1$ //$NON-NLS-2$
          .equalsIgnoreCase("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")) {
        optionen_look.add(this.look_windows);
      }
      if (uii[i].toString()
          .substring(uii[i].toString().lastIndexOf(" ") + 1, uii[i].toString().lastIndexOf("]")) //$NON-NLS-1$ //$NON-NLS-2$
          .equalsIgnoreCase("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel")) {
        optionen_look.add(this.look_windows_classic);
      }
      if (uii[i].toString()
          .substring(uii[i].toString().lastIndexOf(" ") + 1, uii[i].toString().lastIndexOf("]")) //$NON-NLS-1$ //$NON-NLS-2$
          .equalsIgnoreCase("com.sun.java.swing.plaf.motif.MotifLookAndFeel")) {
        optionen_look.add(this.look_motif);
      }
      if (uii[i].toString()
          .substring(uii[i].toString().lastIndexOf(" ") + 1, uii[i].toString().lastIndexOf("]")) //$NON-NLS-1$ //$NON-NLS-2$
          .equalsIgnoreCase("javax.swing.plaf.metal.MetalLookAndFeel")) {
        optionen_look.add(this.look_metal);
      }
      if (uii[i].toString()
          .substring(uii[i].toString().lastIndexOf(" ") + 1, uii[i].toString().lastIndexOf("]")) //$NON-NLS-1$ //$NON-NLS-2$
          .equalsIgnoreCase("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
        optionen_look.add(this.look_gtk);
      }
      if (uii[i].toString()
          .substring(uii[i].toString().lastIndexOf(" ") + 1, uii[i].toString().lastIndexOf("]")) //$NON-NLS-1$ //$NON-NLS-2$
          .equalsIgnoreCase("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel")) {
        optionen_look.add(this.look_nimbus);
      }
    }
  }

  /**
   * Create the "Plugin"-Menu.
   *
   * @param m
   *          the main reference
   * @return the "Plugin"-Menu
   */
  private JMenu createPluginMenu(Main m) {
    JMenu plugins = new JMenu(m.mes.getString("Plugins.Menu"));
    JMenu addPlugins = new JMenu(m.mes.getString("Plugins.Add"));
    plugins.add(addPlugins);

    // Templates for Start and configure
    String startTemplate = m.mes.getString("Plugins.Start");
    String configTemplate = m.mes.getString("Plugins.Configure");

    for (JmjrstPlugin plugin : PluginManager.getPlugins()) {
      plugin.init(m);

      JMenuItem start = new JMenuItem(String.format(startTemplate, plugin.getName()));
      start.addActionListener(e -> plugin.run());
      addPlugins.add(start);

      if (!plugin.isConfigurable()) {
        continue;
      }

      JMenuItem conf = new JMenuItem(String.format(configTemplate, plugin.getName()));
      conf.addActionListener(e -> plugin.configure());
      addPlugins.add(conf);
    }

    if (addPlugins.getMenuComponentCount() == 0) {
      // No Plugins ..
      JMenuItem none = new JMenuItem(m.mes.getString("Plugins.None"));
      none.setEnabled(false);
      addPlugins.add(none);
    }

    return plugins;
  }
}
