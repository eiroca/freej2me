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

public class ImageItem extends Item {

  public static final int LAYOUT_CENTER = 3;
  public static final int LAYOUT_DEFAULT = 0;
  public static final int LAYOUT_LEFT = 1;
  public static final int LAYOUT_NEWLINE_AFTER = 0x200;
  public static final int LAYOUT_NEWLINE_BEFORE = 0x100;
  public static final int LAYOUT_RIGHT = 2;

  private Image image;
  private String altText;
  private int appearance;
  private int layout;

  public ImageItem(final String label, final Image img, final int Layout, final String alt) {
    setLabel(label);
    layout = Layout;
    image = img;
    altText = alt;
  }

  public ImageItem(final String label, final Image img, final int Layout, final String alt, final int appearanceMode) {
    setLabel(label);
    layout = Layout;
    image = img;
    altText = alt;
    appearance = appearanceMode;
  }

  public String getAltText() {
    return altText;
  }

  public int getAppearanceMode() {
    return appearance;
  }

  public Image getImage() {
    return image;
  }

  @Override
  public int getLayout() {
    return layout;
  }

  public void setAltText(final String text) {
    altText = text;
  }

  public void setImage(final Image img) {
    image = img;
  }

  @Override
  public void setLayout(final int Layout) {
    layout = Layout;
  }

}
