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
package javax.microedition.lcdui;

import java.io.IOException;
import java.io.InputStream;
import org.recompile.mobile.Mobile;
import org.recompile.mobile.PlatformImage;

public class Image {

  public PlatformImage platformImage;

  public int width;
  public int height;

  public static Image createImage(final byte[] imageData, final int imageOffset, final int imageLength) {
    Mobile.debug("Create Image from image data ");
    return new PlatformImage(imageData, imageOffset, imageLength);
  }

  public static Image createImage(final Image source) {
    Mobile.debug("Create Image from Image ");
    return new PlatformImage(source);
  }

  public static Image createImage(final Image img, final int x, final int y, final int width, final int height, final int transform) {
    Mobile.debug("Create Image from sub-image ");
    return new PlatformImage(width - x, height - y);
  }

  public static Image createImage(final InputStream stream) {
    Mobile.debug("Create Image stream");
    return new PlatformImage(stream);
  }

  public static Image createImage(final int width, final int height) {
    Mobile.debug("Create Image w,h " + width + ", " + height);
    return new PlatformImage(width, height);
  }

  public static Image createImage(final String name) throws IOException {
    Mobile.debug("Create Image " + name);
    return new PlatformImage(name);
  }

  public static Image createRGBImage(final int[] rgb, final int width, final int height, final boolean processAlpha) {
    Mobile.debug("Create Image RGB " + width + ", " + height);
    return new PlatformImage(rgb, width, height, processAlpha);
  }

  public Graphics getGraphics() {
    return platformImage.getGraphics();
  }

  public int getHeight() {
    return height;
  }

  public void getRGB(final int[] rgbData, final int offset, final int scanlength, final int x, final int y, final int width, final int height) {
  }

  public int getWidth() {
    return width;
  }

  public boolean isMutable() {
    return true;
  }

}
