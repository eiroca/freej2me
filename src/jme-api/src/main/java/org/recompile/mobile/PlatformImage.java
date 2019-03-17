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
package org.recompile.mobile;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class PlatformImage extends javax.microedition.lcdui.Image {

  protected BufferedImage canvas;
  protected PlatformGraphics gc;

  public BufferedImage getCanvas() {
    return canvas;
  }

  @Override
  public PlatformGraphics getGraphics() {
    return gc;
  }

  protected void createGraphics() {
    gc = new PlatformGraphics(this);
    gc.setColor(0x000000);
  }

  public PlatformImage(final int Width, final int Height) {
    // Create blank Image
    width = Width;
    height = Height;

    canvas = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_ARGB);
    createGraphics();

    gc.setColor(0xFFFFFF);
    gc.fillRect(0, 0, width, height);
    gc.setColor(0x000000);

    platformImage = this;
  }

  public PlatformImage(final String name) {
    // Create Image from resource name
    Mobile.debug("Image From Resource Name");
    BufferedImage temp;
    final InputStream stream = Mobile.getPlatform().loader.getMIDletResourceAsStream(name);
    if (stream == null) {
      Mobile.log("Couldn't Load Image Stream (can't find " + name + ")");
    }
    else {
      try {
        temp = ImageIO.read(stream);
        width = temp.getWidth();
        height = temp.getHeight();

        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        createGraphics();

        gc.drawImage(temp, 0, 0);
      }
      catch (final Exception e) {
        Mobile.log("Couldn't Load Image Stream " + name);
        e.printStackTrace();
      }
    }
    platformImage = this;
  }

  public PlatformImage(final InputStream stream) {
    // Create Image from InputStream
    Mobile.debug("Image From Stream");
    BufferedImage temp;
    try {
      temp = ImageIO.read(stream);
      width = temp.getWidth();
      height = temp.getHeight();

      canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      createGraphics();

      gc.drawImage(temp, 0, 0);
    }
    catch (final Exception e) {
      Mobile.warn("Couldn't Load Image Stream");
    }

    platformImage = this;
  }

  public PlatformImage(final Image source) {
    // Create Image from Image
    width = source.platformImage.width;
    height = source.platformImage.height;

    canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    createGraphics();

    gc.drawImage(source.platformImage, 0, 0);

    platformImage = this;
  }

  public PlatformImage(final byte[] imageData, final int imageOffset, final int imageLength) {
    // Create Image from Byte Array Range (Data is PNG, JPG, etc.)
    try {
      final byte[] range = Arrays.copyOfRange(imageData, imageOffset, imageOffset + imageLength);
      final InputStream stream = new ByteArrayInputStream(range);

      BufferedImage temp;

      temp = ImageIO.read(stream);
      width = temp.getWidth();
      height = temp.getHeight();

      canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      createGraphics();

      gc.drawImage(temp, 0, 0);
    }
    catch (final Exception e) {
      Mobile.warn("Couldn't Load Image Data From Byte Array");
    }

    platformImage = this;
  }

  public PlatformImage(final int[] rgb, final int Width, final int Height, final boolean processAlpha) {
    // createRGBImage (Data is ARGB pixel data)
    width = Width;
    height = Height;

    if (width < 1) {
      width = 1;
    }
    if (height < 1) {
      height = 1;
    }

    canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    createGraphics();

    gc.drawRGB(rgb, 0, width, 0, 0, width, height, true);

    platformImage = this;
  }

  public PlatformImage(final Image image, final int x, final int y, final int Width, final int Height, final int transform) {
    // Create Image From Sub-Image, Transformed //
    final BufferedImage sub = image.platformImage.canvas.getSubimage(x, y, Width, Height);

    canvas = PlatformImage.transformImage(sub, transform);
    createGraphics();

    width = canvas.getWidth();
    height = canvas.getHeight();

    platformImage = this;
  }

  @Override
  public void getRGB(final int[] rgbData, final int offset, final int scanlength, final int x, final int y, final int width, final int height) {
    canvas.getRGB(x, y, width, height, rgbData, offset, scanlength);
  }

  public int getARGB(final int x, final int y) {
    return canvas.getRGB(x, y);
  }

  public static BufferedImage transformImage(final BufferedImage image, final int transform) {
    final int width = image.getWidth();
    final int height = image.getHeight();
    int out_width = width;
    int out_height = height;
    final AffineTransform af = new AffineTransform();
    switch (transform) {
      case Sprite.TRANS_NONE:
        break;
      case Sprite.TRANS_ROT90:
        af.translate(0, width);
        af.rotate((Math.PI * 3) / 2);
        out_width = height;
        out_height = width;
        break;
      case Sprite.TRANS_ROT180:
        af.translate(width, height);
        af.rotate(Math.PI);
        break;
      case Sprite.TRANS_ROT270:
        af.translate(height, 0);
        af.rotate(Math.PI / 2);
        out_width = height;
        out_height = width;
        break;
      case Sprite.TRANS_MIRROR:
        af.translate(width, 0);
        af.scale(-1, 1);
        break;
      case Sprite.TRANS_MIRROR_ROT90:
        af.translate(width, 0);
        af.scale(-1, 1);
        af.translate(height, 0);
        af.rotate(Math.PI / 2);
        out_width = height;
        out_height = width;
        break;
      case Sprite.TRANS_MIRROR_ROT180:
        af.translate(width, 0);
        af.scale(-1, 1);
        af.translate(width, height);
        af.rotate(Math.PI);
        break;
      case Sprite.TRANS_MIRROR_ROT270:
        af.translate(height, 0);
        af.rotate((Math.PI * 3) / 2);
        af.translate(width, 0);
        af.scale(-1, 1);
        out_width = height;
        out_height = width;
        break;
    }
    final BufferedImage transimage = new BufferedImage(out_width, out_height, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D gc = transimage.createGraphics();
    gc.drawImage(image, af, null);
    return transimage;
  }

}
