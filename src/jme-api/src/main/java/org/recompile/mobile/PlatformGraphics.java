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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import com.nokia.mid.ui.DirectGraphics;

public class PlatformGraphics extends javax.microedition.lcdui.Graphics implements DirectGraphics {

  protected BufferedImage canvas;
  protected Graphics2D gc;

  protected Color awtColor;

  protected int strokeStyle = Graphics.SOLID;

  protected Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);

  public PlatformGraphics platformGraphics;
  public PlatformImage platformImage;

  public PlatformGraphics(final PlatformImage image) {
    canvas = image.getCanvas();
    gc = canvas.createGraphics();
    platformImage = image;

    platformGraphics = this;

    clipX = 0;
    clipY = 0;
    clipWidth = canvas.getWidth();
    clipHeight = canvas.getHeight();

    setColor(0, 0, 0);
    gc.setBackground(new Color(0, 0, 0, 0));
    gc.setFont(font.platformFont.awtFont);
  }

  public Graphics2D getGraphics2D() {
    return gc;
  }

  public BufferedImage getCanvas() {
    return canvas;
  }

  public void clearRect(final int x, final int y, final int width, final int height) {
    gc.clearRect(x, y, width, height);
  }

  @Override
  public void copyArea(final int subx, final int suby, final int subw, final int subh, int x, int y, final int anchor) {
    x = AnchorX(x, subw, anchor);
    y = AnchorY(y, subh, anchor);

    final BufferedImage sub = canvas.getSubimage(subx, suby, subw, subh);

    gc.drawImage(sub, x, y, null);
  }

  @Override
  public void drawArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
    gc.drawArc(x, y, width, height, startAngle, arcAngle);
  }

  @Override
  public void drawChar(final char character, final int x, final int y, final int anchor) {
    drawString(Character.toString(character), x, y, anchor);
  }

  @Override
  public void drawChars(final char[] data, final int offset, final int length, final int x, final int y, final int anchor) {
    drawString(data.toString(), x, y, anchor);
  }

  @Override
  public void drawImage(final Image image, int x, int y, final int anchor) {
    try {
      final int imgWidth = image.getWidth();
      final int imgHeight = image.getHeight();

      x = AnchorX(x, imgWidth, anchor);
      y = AnchorY(y, imgHeight, anchor);

      gc.drawImage(image.platformImage.getCanvas(), x, y, null);
    }
    catch (final Exception e) {
      Mobile.log("drawImage A:" + e.getMessage());
    }
  }

  public void drawImage(final Image image, final int x, final int y) {
    try {
      gc.drawImage(image.platformImage.getCanvas(), x, y, null);
    }
    catch (final Exception e) {
      Mobile.log("drawImage B:" + e.getMessage());
    }
  }

  public void drawImage(final BufferedImage image, final int x, final int y) {
    // called by Platform Image
    try {
      gc.drawImage(image, x, y, null);
    }
    catch (final Exception e) {
      Mobile.log("drawImage C:" + e.getMessage());
    }
  }

  public void flushGraphics(final Image image, final int x, final int y, final int width, final int height) {
    // called by MobilePlatform.flushGraphics/repaint
    try {
      final BufferedImage sub = image.platformImage.getCanvas().getSubimage(x, y, width, height);
      gc.drawImage(sub, x, y, null);
      //gc.drawImage(image.platformImage.getCanvas(), 0, 0, null);
    }
    catch (final Exception e) {
      Mobile.debug("flushGraphics A:" + e.getMessage());
    }
  }

  @Override
  public void drawRegion(final Image image, final int subx, final int suby, final int subw, final int subh, final int transform, int x, int y, final int anchor) {
    try {
      if (transform == 0) {
        final BufferedImage sub = image.platformImage.getCanvas().getSubimage(subx, suby, subw, subh);
        x = AnchorX(x, subw, anchor);
        y = AnchorY(y, subh, anchor);
        gc.drawImage(sub, x, y, null);
      }
      else {
        final PlatformImage sub = new PlatformImage(image, subx, suby, subw, subh, transform);
        x = AnchorX(x, sub.width, anchor);
        y = AnchorY(y, sub.height, anchor);
        gc.drawImage(sub.getCanvas(), x, y, null);
      }
    }
    catch (final Exception e) {
      Mobile.debug("drawRegion A (x:" + x + " y:" + y + " w:" + subw + " h:" + subh + "):" + e.getMessage());
    }
  }

  @Override
  public void drawRGB(final int[] rgbData, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final boolean processAlpha) {
    if ((width < 1) || (height < 1)) { return; }
    if (!processAlpha) {
      for (int i = offset; i < rgbData.length; i++) {
        rgbData[i] &= 0x00FFFFFF;
        rgbData[i] |= 0xFF000000;
      }
    }
    else { // Fix Alpha //
      for (int i = offset; i < rgbData.length; i++) {
        rgbData[i] |= 0x00000000;
        rgbData[i] &= 0xFFFFFFFF;
      }
    }
    // Copy from new image.  This avoids some problems with games that don't
    // properly adapt to different display sizes.
    final BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    temp.setRGB(0, 0, width, height, rgbData, offset, scanlength);
    gc.drawImage(temp, x, y, null);
  }

  @Override
  public void drawLine(final int x1, final int y1, final int x2, final int y2) {
    gc.drawLine(x1, y1, x2, y2);
  }

  @Override
  public void drawRect(final int x, final int y, final int width, final int height) {
    gc.drawRect(x, y, width, height);
  }

  @Override
  public void drawRoundRect(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight) {
    gc.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
  }

  @Override
  public void drawString(final String str, int x, int y, final int anchor) {
    if (str != null) {
      x = AnchorX(x, gc.getFontMetrics().stringWidth(str), anchor);
      y = (y + gc.getFontMetrics().getAscent()) - 1;
      y = AnchorY(y, gc.getFontMetrics().getHeight(), anchor);
      gc.drawString(str, x, y);
    }
  }

  @Override
  public void drawSubstring(final String str, final int offset, final int len, final int x, final int y, final int anchor) {
    if (str.length() >= (offset + len)) {
      drawString(str.substring(offset, offset + len), x, y, anchor);
    }
  }

  @Override
  public void fillArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
    gc.fillArc(x, y, width, height, startAngle, arcAngle);
  }

  @Override
  public void fillRect(final int x, final int y, final int width, final int height) {
    gc.fillRect(x, y, width, height);
  }

  @Override
  public void fillRoundRect(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight) {
    gc.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
  }

  //public int getBlueComponent() { }
  //public Font getFont() { return font; }
  //public int getColor() { return color; }
  //public int getGrayScale() { }
  //public int getGreenComponent() { }
  //public int getRedComponent() { }
  //public int getStrokeStyle() { return strokeStyle; }

  @Override
  public void setColor(final int rgb) {
    setColor((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
  }

  @Override
  public void setColor(final int r, final int g, final int b) {
    color = (r << 16) + (g << 8) + b;
    awtColor = new Color(r, g, b);
    gc.setColor(awtColor);
  }

  @Override
  public void setFont(final Font font) {
    super.setFont(font);
    gc.setFont(font.platformFont.awtFont);
  }
  //public void setGrayScale(int value)
  //public void setStrokeStyle(int style)

  @Override
  public void setClip(final int x, final int y, final int width, final int height) {
    gc.setClip(x, y, width, height);
    clipX = (int)gc.getClipBounds().getX();
    clipY = (int)gc.getClipBounds().getY();
    clipWidth = (int)gc.getClipBounds().getWidth();
    clipHeight = (int)gc.getClipBounds().getHeight();
  }

  @Override
  public void clipRect(final int x, final int y, final int width, final int height) {
    gc.clipRect(x, y, width, height);
    clipX = (int)gc.getClipBounds().getX();
    clipY = (int)gc.getClipBounds().getY();
    clipWidth = (int)gc.getClipBounds().getWidth();
    clipHeight = (int)gc.getClipBounds().getHeight();
  }

  //public int getTranslateX() { }
  //public int getTranslateY() { }

  @Override
  public void translate(final int x, final int y) {
    translateX += x;
    translateY += y;
    gc.translate((double)x, (double)y);
    //translateX = (int)gc.getTransform().getTranslateX();
    //translateY = (int)gc.getTransform().getTranslateY();
  }

  private int AnchorX(final int x, final int width, final int anchor) {
    int xout = x;
    if ((anchor & Graphics.HCENTER) > 0) {
      xout = x - (width / 2);
    }
    if ((anchor & Graphics.RIGHT) > 0) {
      xout = x - width;
    }
    if ((anchor & Graphics.LEFT) > 0) {
      xout = x;
    }
    return xout;
  }

  private int AnchorY(final int y, final int height, final int anchor) {
    int yout = y;
    if ((anchor & Graphics.VCENTER) > 0) {
      yout = y - (height / 2);
    }
    if ((anchor & Graphics.TOP) > 0) {
      yout = y;
    }
    if ((anchor & Graphics.BOTTOM) > 0) {
      yout = y - height;
    }
    if ((anchor & Graphics.BASELINE) > 0) {
      yout = y + height;
    }
    return yout;
  }

  public void setAlphaRGB(final int ARGB) {
    gc.setColor(new Color(ARGB, true));
  }

  /*
  	****************************
  		Nokia Direct Graphics
  	****************************
  */
  // http://www.j2megame.org/j2meapi/Nokia_UI_API_1_1/com/nokia/mid/ui/DirectGraphics.html

  private int colorAlpha;

  @Override
  public int getNativePixelFormat() {
    return DirectGraphics.TYPE_INT_8888_ARGB;
  }

  @Override
  public int getAlphaComponent() {
    return colorAlpha;
  }

  @Override
  public void setARGBColor(final int argbColor) {
    colorAlpha = (argbColor >>> 24) & 0xFF;
    setColor(argbColor);
  }

  @Override
  public void drawImage(final javax.microedition.lcdui.Image img, int x, int y, final int anchor, final int manipulation) {
    Mobile.debug("Nokia drawImage");
    final BufferedImage image = manipulateImage(img.platformImage.getCanvas(), manipulation);
    x = AnchorX(x, image.getWidth(), anchor);
    y = AnchorY(y, image.getHeight(), anchor);
    drawImage(image, x, y);
  }

  @Override
  public void drawPixels(final byte[] pixels, final byte[] transparencyMask, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int manipulation, final int format) {
    Mobile.debug("drawPixels A"); // Found In Use

    final int[] Type1 = {
        0xFFFFFFFF, 0xFF000000, 0x00FFFFFF, 0x00000000
    };
    int c = 0;
    int[] data;
    BufferedImage temp;
    switch (format) {
      case -1: // TYPE_BYTE_1_GRAY_VERTICAL // used by Monkiki's Castles
        data = new int[pixels.length * 8];
        int row = 0;
        int col = 0;
        for (int b = (offset / 8); b < pixels.length; b++) {
          for (int j = 0; j < 8; j++) {
            c = ((pixels[b] >> j) & 1);
            if (transparencyMask != null) {
              c |= (((transparencyMask[b] >> j) & 1) ^ 1) << 1;
            }
            data[((row + j) * width) + col] = Type1[c];
          }
          col++;
          if (col == width) {
            col = 0;
            row += 8;
          }
        }

        temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        temp.setRGB(0, 0, width, height, data, 0, scanlength);
        drawImage(manipulateImage(temp, manipulation), x, y);
        break;

      case 1: // TYPE_BYTE_1_GRAY // used by Monkiki's Castles
        data = new int[pixels.length * 8];

        for (int i = (offset / 8); i < pixels.length; i++) {
          for (int j = 7; j >= 0; j--) {
            c = ((pixels[i] >> j) & 1);
            if (transparencyMask != null) {
              c |= (((transparencyMask[i] >> j) & 1) ^ 1) << 1;
            }
            data[(i * 8) + (7 - j)] = Type1[c];
          }
        }
        temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        temp.setRGB(0, 0, width, height, data, 0, scanlength);
        drawImage(manipulateImage(temp, manipulation), x, y);
        break;

      default:
        Mobile.log("drawPixels A : Format " + format + " Not Implemented");
    }
  }

  @Override
  public void drawPixels(final int[] pixels, final boolean transparency, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int manipulation, final int format) {
    Mobile.debug("drawPixels B"); // Found In Use
    final BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    temp.setRGB(0, 0, width, height, pixels, offset, scanlength);
    final BufferedImage temp2 = manipulateImage(temp, manipulation);
    drawImage(temp2, x, y);
  }

  @Override
  public void drawPixels(final short[] pixels, final boolean transparency, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int manipulation, final int format) {
    Mobile.debug("drawPixels C" + format); // Found In Use
    final int[] data = new int[pixels.length];

    for (int i = 0; i < pixels.length; i++) {
      data[i] = pixelToColor(pixels[i], format);
    }

    final BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    temp.setRGB(0, 0, width, height, data, offset, scanlength);
    drawImage(manipulateImage(temp, manipulation), x, y);
  }

  @Override
  public void drawPolygon(final int[] xPoints, final int xOffset, final int[] yPoints, final int yOffset, final int nPoints, final int argbColor) {
    final int temp = color;
    setColor(argbColor);
    for (int i = 0; i < nPoints; i++) {
      xPoints[i] += xOffset;
      yPoints[i] += yOffset;
    }
    gc.drawPolygon(xPoints, yPoints, nPoints);
    setColor(temp);
  }

  @Override
  public void drawTriangle(final int x1, final int y1, final int x2, final int y2, final int x3, final int y3, final int argbColor) {
    Mobile.debug("drawTriange");
    final int temp = color;
    setColor(argbColor);
    gc.drawPolygon(new int[] {
        x1, x2, x3
    }, new int[] {
        y1, y2, y3
    }, 3);
    setColor(temp);
  }

  @Override
  public void fillPolygon(final int[] xPoints, final int xOffset, final int[] yPoints, final int yOffset, final int nPoints, final int argbColor) {
    final int temp = color;
    setColor(argbColor);
    for (int i = 0; i < nPoints; i++) {
      xPoints[i] += xOffset;
      yPoints[i] += yOffset;
    }
    gc.fillPolygon(xPoints, yPoints, nPoints);
    setColor(temp);
  }

  @Override
  public void fillTriangle(final int x1, final int y1, final int x2, final int y2, final int x3, final int y3, final int argbColor) {
    Mobile.debug("fillTriangle"); // Found In Use
    final int temp = color;
    setColor(argbColor);
    gc.fillPolygon(new int[] {
        x1, x2, x3
    }, new int[] {
        y1, y2, y3
    }, 3);
    setColor(temp);
  }

  @Override
  public void getPixels(final byte[] pixels, final byte[] transparencyMask, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int format) {
    Mobile.log("getPixels A");
  }

  @Override
  public void getPixels(final int[] pixels, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int format) {
    Mobile.debug("getPixels B");
    canvas.getRGB(x, y, width, height, pixels, offset, scanlength);
  }

  @Override
  public void getPixels(final short[] pixels, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int format) {
    Mobile.debug("getPixels C"); // Found In Use
    int i = offset;
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        pixels[i] = colorToShortPixel(canvas.getRGB(col + x, row + y), format);
        i++;
      }
    }
  }

  private int pixelToColor(final short c, final int format) {
    int a = 0xFF;
    int r = 0;
    int g = 0;
    int b = 0;
    switch (format) {
      case DirectGraphics.TYPE_USHORT_1555_ARGB:
        a = ((c >> 15) & 0x01) * 0xFF;
        r = (c >> 10) & 0x1F;
        g = (c >> 5) & 0x1F;
        b = c & 0x1F;
        r = (r << 3) | (r >> 2);
        g = (g << 3) | (g >> 2);
        b = (b << 3) | (b >> 2);
        break;
      case DirectGraphics.TYPE_USHORT_444_RGB:
        r = (c >> 8) & 0xF;
        g = (c >> 4) & 0xF;
        b = c & 0xF;
        r = (r << 4) | r;
        g = (g << 4) | g;
        b = (b << 4) | b;
        break;
      case DirectGraphics.TYPE_USHORT_4444_ARGB:
        a = (c >> 12) & 0xF;
        r = (c >> 8) & 0xF;
        g = (c >> 4) & 0xF;
        b = c & 0xF;
        a = (a << 4) | a;
        r = (r << 4) | r;
        g = (g << 4) | g;
        b = (b << 4) | b;
        break;
      case DirectGraphics.TYPE_USHORT_555_RGB:
        r = (c >> 10) & 0x1F;
        g = (c >> 5) & 0x1F;
        b = c & 0x1F;
        r = (r << 3) | (r >> 2);
        g = (g << 3) | (g >> 2);
        b = (b << 3) | (b >> 2);
        break;
      case DirectGraphics.TYPE_USHORT_565_RGB:
        r = (c >> 11) & 0x1F;
        g = (c >> 5) & 0x3F;
        b = c & 0x1F;
        r = (r << 3) | (r >> 2);
        g = (g << 2) | (g >> 4);
        b = (b << 3) | (b >> 2);
        break;
    }
    return (a << 24) | (r << 16) | (g << 8) | b;
  }

  private short colorToShortPixel(final int c, final int format) {
    int a = 0;
    int r = 0;
    int g = 0;
    int b = 0;
    int out = 0;
    switch (format) {
      case DirectGraphics.TYPE_USHORT_1555_ARGB:
        a = c >>> 31;
        r = ((c >> 19) & 0x1F);
        g = ((c >> 11) & 0x1F);
        b = ((c >> 3) & 0x1F);
        out = (a << 15) | (r << 10) | (g << 5) | b;
        break;
      case DirectGraphics.TYPE_USHORT_444_RGB:
        r = ((c >> 20) & 0xF);
        g = ((c >> 12) & 0xF);
        b = ((c >> 4) & 0xF);
        out = (r << 8) | (g << 4) | b;
        break;
      case DirectGraphics.TYPE_USHORT_4444_ARGB:
        a = ((c >>> 28) & 0xF);
        r = ((c >> 20) & 0xF);
        g = ((c >> 12) & 0xF);
        b = ((c >> 4) & 0xF);
        out = (a << 12) | (r << 8) | (g << 4) | b;
        break;
      case DirectGraphics.TYPE_USHORT_555_RGB:
        r = ((c >> 19) & 0x1F);
        g = ((c >> 11) & 0x1F);
        b = ((c >> 3) & 0x1F);
        out = (r << 10) | (g << 5) | b;
        break;
      case DirectGraphics.TYPE_USHORT_565_RGB:
        r = ((c >> 19) & 0x1F);
        g = ((c >> 10) & 0x3F);
        b = ((c >> 3) & 0x1F);
        out = (r << 11) | (g << 5) | b;
        break;
    }
    return (short)out;
  }

  private BufferedImage manipulateImage(final BufferedImage image, final int manipulation) {
    final int HV = DirectGraphics.FLIP_HORIZONTAL | DirectGraphics.FLIP_VERTICAL;
    final int H90 = DirectGraphics.FLIP_HORIZONTAL | DirectGraphics.ROTATE_90;
    switch (manipulation) {
      case DirectGraphics.FLIP_HORIZONTAL:
        return PlatformImage.transformImage(image, Sprite.TRANS_MIRROR);
      case DirectGraphics.FLIP_VERTICAL:
        return PlatformImage.transformImage(image, Sprite.TRANS_MIRROR_ROT180);
      case DirectGraphics.ROTATE_90:
        return PlatformImage.transformImage(image, Sprite.TRANS_ROT90);
      case DirectGraphics.ROTATE_180:
        return PlatformImage.transformImage(image, Sprite.TRANS_ROT180);
      case DirectGraphics.ROTATE_270:
        return PlatformImage.transformImage(image, Sprite.TRANS_ROT270);
      case HV:
        return PlatformImage.transformImage(image, Sprite.TRANS_ROT180);
      case H90:
        return PlatformImage.transformImage(PlatformImage.transformImage(image, Sprite.TRANS_MIRROR), Sprite.TRANS_ROT270);
    }
    return image;
  }

}
