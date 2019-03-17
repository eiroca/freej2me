/**
 * This file is part of FreeJ2ME.
 *
 * FreeJ2ME is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * FreeJ2ME is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with FreeJ2ME. If not,
 * see http://www.gnu.org/licenses/
 */
package net.eiroca.j2me.host.freej2me;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.microedition.midlet.MIDletStateChangeException;
import org.recompile.freej2me.Config;
import org.recompile.freej2me.ILCDOwner;
import org.recompile.freej2me.LCD;
/*
 * FreeJ2ME - AWT
 */
import org.recompile.mobile.Mobile;
import org.recompile.mobile.MobilePlatform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FreeeJ2meHost implements KeyListener, MouseListener, ILCDOwner {

  private static Logger logger = LoggerFactory.getLogger(FreeeJ2meHost.class);

  private final Frame main;
  int lcdWidth;
  int lcdHeight;

  private final LCD lcd;

  private int xborder;
  private int yborder;

  Config config;
  private boolean useNokiaControls = true;
  private final boolean rotateDisplay = false;
  int limitFPS = 0;

  public FreeeJ2meHost(final Class<?> midlet) {
    main = new Frame("FreeJ2ME");
    main.setSize(350, 450);
    main.setBackground(new Color(0, 0, 64));
    try {
      main.setIconImage(ImageIO.read(main.getClass().getResourceAsStream("/org/recompile/icon.png")));
    }
    catch (final Exception e) {
    }

    main.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosing(final WindowEvent e) {
        System.exit(0);
      }
    });

    // Setup Device //
    lcdWidth = 240;
    lcdHeight = 320;
    final MobilePlatform p = new MobilePlatform(lcdWidth, lcdHeight);
    Mobile.setPlatform(p);

    lcd = new LCD(this);
    lcd.setFocusable(true);
    main.add(lcd);

    config = new Config();
    config.onChange = () -> settingsChanged();

    Mobile.getPlatform().setPainter(() -> lcd.paint(lcd.getGraphics()));

    lcd.addKeyListener(this);

    lcd.addMouseListener(this);

    main.addComponentListener(new ComponentAdapter() {

      @Override
      public void componentResized(final ComponentEvent e) {
        resize();
      }
    });

    main.setVisible(true);
    main.pack();

    resize();
    main.setSize(lcdWidth + xborder, lcdHeight + yborder);

    if (Mobile.getPlatform().init(midlet)) {
      config.init();
      settingsChanged();

      try {
        Mobile.getPlatform().run();
      }
      catch (final MIDletStateChangeException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    }
    else {
      FreeeJ2meHost.logger.info("Couldn't load jar...");
    }
  }

  private void settingsChanged() {
    final int w = Integer.parseInt(config.settings.get("width"));
    final int h = Integer.parseInt(config.settings.get("height"));
    limitFPS = Integer.parseInt(config.settings.get("fps"));
    if (limitFPS > 0) {
      limitFPS = 1000 / limitFPS;
    }

    final String sound = config.settings.get("sound");
    if (sound.equals("on")) {
      Mobile.getPlatform().sound = true;
    }
    else if (sound.equals("off")) {
      Mobile.getPlatform().sound = false;
    }

    final String nokia = config.settings.get("nokia");
    if (nokia.equals("on")) {
      useNokiaControls = true;
    }
    else if (nokia.equals("off")) {
      useNokiaControls = false;
    }

    if ((lcdWidth != w) || (lcdHeight != h)) {
      lcdWidth = w;
      lcdHeight = h;
      Mobile.getPlatform().resizeLCD(w, h);
      resize();
      main.setSize(lcdWidth + xborder, lcdHeight + yborder);
    }
  }

  private int getMobileKey(final int keycode) {
    if (useNokiaControls) {
      switch (keycode) {
        case KeyEvent.VK_UP:
          return Mobile.NOKIA_UP;
        case KeyEvent.VK_DOWN:
          return Mobile.NOKIA_DOWN;
        case KeyEvent.VK_LEFT:
          return Mobile.NOKIA_LEFT;
        case KeyEvent.VK_RIGHT:
          return Mobile.NOKIA_RIGHT;
      }
    }

    switch (keycode) {
      case KeyEvent.VK_0:
        return Mobile.KEY_NUM0;
      case KeyEvent.VK_1:
        return Mobile.KEY_NUM1;
      case KeyEvent.VK_2:
        return Mobile.KEY_NUM2;
      case KeyEvent.VK_3:
        return Mobile.KEY_NUM3;
      case KeyEvent.VK_4:
        return Mobile.KEY_NUM4;
      case KeyEvent.VK_5:
        return Mobile.KEY_NUM5;
      case KeyEvent.VK_6:
        return Mobile.KEY_NUM6;
      case KeyEvent.VK_7:
        return Mobile.KEY_NUM7;
      case KeyEvent.VK_8:
        return Mobile.KEY_NUM8;
      case KeyEvent.VK_9:
        return Mobile.KEY_NUM9;
      case KeyEvent.VK_ASTERISK:
        return Mobile.KEY_STAR;
      case KeyEvent.VK_NUMBER_SIGN:
        return Mobile.KEY_POUND;
      //
      case KeyEvent.VK_NUMPAD0:
        return Mobile.KEY_NUM0;
      case KeyEvent.VK_NUMPAD7:
        return Mobile.KEY_NUM1;
      case KeyEvent.VK_NUMPAD8:
        return Mobile.KEY_NUM2;
      case KeyEvent.VK_NUMPAD9:
        return Mobile.KEY_NUM3;
      case KeyEvent.VK_NUMPAD4:
        return Mobile.KEY_NUM4;
      case KeyEvent.VK_NUMPAD5:
        return Mobile.KEY_NUM5;
      case KeyEvent.VK_NUMPAD6:
        return Mobile.KEY_NUM6;
      case KeyEvent.VK_NUMPAD1:
        return Mobile.KEY_NUM7;
      case KeyEvent.VK_NUMPAD2:
        return Mobile.KEY_NUM8;
      case KeyEvent.VK_NUMPAD3:
        return Mobile.KEY_NUM9;
      //
      case KeyEvent.VK_UP:
        return Mobile.KEY_NUM2;
      case KeyEvent.VK_DOWN:
        return Mobile.KEY_NUM8;
      case KeyEvent.VK_LEFT:
        return Mobile.KEY_NUM4;
      case KeyEvent.VK_RIGHT:
        return Mobile.KEY_NUM6;
      //
      case KeyEvent.VK_ENTER:
        return Mobile.KEY_NUM5;
      //
      case KeyEvent.VK_Q:
        return Mobile.NOKIA_SOFT1;
      case KeyEvent.VK_W:
        return Mobile.NOKIA_SOFT2;
      case KeyEvent.VK_E:
        return Mobile.KEY_STAR;
      case KeyEvent.VK_R:
        return Mobile.KEY_POUND;
      //
      case KeyEvent.VK_A:
        return -1;
      case KeyEvent.VK_Z:
        return -2;
      // Config
      case KeyEvent.VK_ESCAPE:
        config.start();
    }
    return keycode;
  }

  private void resize() {
    xborder = main.getInsets().left + main.getInsets().right;
    yborder = main.getInsets().top + main.getInsets().bottom;

    final double vw = (main.getWidth() - xborder) * 1;
    final double vh = (main.getHeight() - yborder) * 1;

    double nw = lcdWidth;
    double nh = lcdHeight;

    nw = vw;
    nh = nw * ((double)lcdHeight / (double)lcdWidth);

    if (nh > vh) {
      nh = vh;
      nw = nh * ((double)lcdWidth / (double)lcdHeight);
    }

    lcd.updateScale((int)nw, (int)nh);
  }

  @Override
  public void keyPressed(final KeyEvent e) {
    if (config.isRunning) {
      config.keyPressed(getMobileKey(e.getKeyCode()));
    }
    else {
      Mobile.getPlatform().keyPressed(getMobileKey(e.getKeyCode()));
    }
  }

  @Override
  public void keyReleased(final KeyEvent e) {
    if (config.isRunning) {
      config.keyReleased(getMobileKey(e.getKeyCode()));
    }
    else {
      Mobile.getPlatform().keyReleased(getMobileKey(e.getKeyCode()));
    }
  }

  @Override
  public void keyTyped(final KeyEvent e) {
  }

  @Override
  public void mousePressed(final MouseEvent e) {
    final int x = (int)((e.getX() - lcd.cx) * lcd.scalex);
    final int y = (int)((e.getY() - lcd.cy) * lcd.scaley);
    Mobile.getPlatform().pointerPressed(x, y);
  }

  @Override
  public void mouseReleased(final MouseEvent e) {
    final int x = (int)((e.getX() - lcd.cx) * lcd.scalex);
    final int y = (int)((e.getY() - lcd.cy) * lcd.scaley);
    Mobile.getPlatform().pointerReleased(x, y);
  }

  @Override
  public void mouseExited(final MouseEvent e) {
  }

  @Override
  public void mouseEntered(final MouseEvent e) {
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
  }

  @Override
  public double getLCDHeight() {

    return lcdHeight;
  }

  @Override
  public double getLCDWidth() {
    return lcdWidth;
  }

  @Override
  public boolean isRunning() {
    return config.isRunning;
  }

  @Override
  public BufferedImage getLCD() {
    return config.getLCD();
  }

  @Override
  public int getLimitFPS() {
    return limitFPS;
  }

}
