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
 *
 */
package com.nokia.mid.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import org.recompile.mobile.Mobile;
import org.recompile.mobile.PlatformGraphics;

public class DirectUtils {

  public static Image createImage(final byte[] imageData, final int imageOffset, final int imageLength) {
    Mobile.debug("Nokia Create Image A");
    return Image.createImage(imageData, imageOffset, imageLength);
  }

  public static Image createImage(final int width, final int height, final int ARGBcolor) {
    Mobile.debug("Nokia Create Image B");
    final Image image = Image.createImage(width, height);
    final Graphics gc = image.getGraphics();
    gc.setColor(ARGBcolor);
    gc.fillRect(0, 0, width, height);
    return image;
  }

  public static DirectGraphics getDirectGraphics(final javax.microedition.lcdui.Graphics g) {
    Mobile.debug("Nokia DirectGraphics");
    return (PlatformGraphics)g;
  }

}
