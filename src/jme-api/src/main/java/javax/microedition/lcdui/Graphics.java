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

import org.recompile.mobile.PlatformGraphics;
import org.recompile.mobile.PlatformImage;

public class Graphics {

  public static final int BASELINE = 64;
  public static final int BOTTOM = 32;
  public static final int DOTTED = 1;
  public static final int HCENTER = 1;
  public static final int LEFT = 4;
  public static final int RIGHT = 8;
  public static final int SOLID = 0;
  public static final int TOP = 16;
  public static final int VCENTER = 2;

  protected int translateX = 0;
  protected int translateY = 0;

  protected int clipX = 0;
  protected int clipY = 0;
  protected int clipWidth = 0;
  protected int clipHeight = 0;

  protected int color = 0xFFFFFF;
  protected Font font = Font.getDefaultFont();
  protected int strokeStyle = Graphics.SOLID;

  public PlatformImage platformImage;
  public PlatformGraphics platformGraphics;

  public void copyArea(final int x_src, final int y_src, final int width, final int height, final int x_dest, final int y_dest, final int anchor) {
  }

  public void drawArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
  }

  public void drawChar(final char character, final int x, final int y, final int anchor) {
  }

  public void drawChars(final char[] data, final int offset, final int length, final int x, final int y, final int anchor) {
  }

  public void drawImage(final Image img, final int x, final int y, final int anchor) {
  }

  public void drawLine(final int x1, final int y1, final int x2, final int y2) {
  }

  public void drawRect(final int x, final int y, final int width, final int height) {
  }

  public void drawRegion(final Image src, final int x_src, final int y_src, final int width, final int height, final int transform, final int x_dest, final int y_dest, final int anchor) {
  }

  public void drawRGB(final int[] rgbData, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final boolean processAlpha) {
  }

  public void drawRoundRect(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight) {
  }

  public void drawString(final String str, final int x, final int y, final int anchor) {
  }

  public void drawSubstring(final String str, final int offset, final int len, final int x, final int y, final int anchor) {
  }

  public void fillArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
  }

  public void fillRect(final int x, final int y, final int width, final int height) {
  }

  public void fillRoundRect(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight) {
  }

  public void fillTriangle(final int x1, final int y1, final int x2, final int y2, final int x3, final int y3) {
  }

  public int getColor() {
    return color;
  }

  public int getDisplayColor(final int trycolor) {
    return trycolor;
  }

  public Font getFont() {
    return font;
  }

  public int getGrayScale() {
    final int r = (color >> 16) & 0xFF;
    final int g = (color >> 8) & 0xFF;
    final int b = color & 0xFF;
    return ((r + g + b) / 3) & 0xFF;
  }

  public int getBlueComponent() {
    return color & 0xFF;
  }

  public int getGreenComponent() {
    return (color >> 8) & 0xFF;
  }

  public int getRedComponent() {
    return (color >> 16) & 0xFF;
  }

  public int getStrokeStyle() {
    return strokeStyle;
  }

  public void clipRect(final int x, final int y, final int width, final int height) {
  }

  public void setClip(final int x, final int y, final int width, final int height) {
  }

  public int getClipHeight() {
    return clipHeight;
  }

  public int getClipWidth() {
    return clipWidth;
  }

  public int getClipX() {
    return clipX;
  }

  public int getClipY() {
    return clipY;
  }

  public void translate(final int x, final int y) {
    translateX += x;
    translateY += y;
  }

  public int getTranslateX() {
    return translateX;
  }

  public int getTranslateY() {
    return translateY;
  }

  public void setColor(final int RGB) {
    color = RGB;
  }

  public void setColor(final int red, final int green, final int blue) {
    color = (red << 16) + (green << 8) + blue;
  }

  public void setFont(final Font newfont) {
    font = newfont;
  }

  public void setGrayScale(int value) {
    value = value & 0xFF;
    color = (value << 16) + (value << 8) + value;
  }

  public void setStrokeStyle(final int style) {
    strokeStyle = style;
  }

}
