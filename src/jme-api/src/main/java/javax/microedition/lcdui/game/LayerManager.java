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
package javax.microedition.lcdui.game;

import java.awt.Shape;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import org.recompile.mobile.Mobile;
import org.recompile.mobile.PlatformGraphics;

public class LayerManager {

  private final Vector<Layer> layers;

  private final Image canvas;
  private final PlatformGraphics gc;
  private Shape clip;

  private int x;
  private int y;
  private int width;
  private int height;

  public LayerManager() {
    layers = new Vector<>();

    width = Mobile.getPlatform().lcdWidth;
    height = Mobile.getPlatform().lcdHeight;

    canvas = Image.createImage(width, height);
    gc = canvas.platformImage.getGraphics();
  }

  public void append(final Layer l) {
    layers.add(l);
  }

  public Layer getLayerAt(final int index) {
    return layers.get(index);
  }

  public int getSize() {
    return layers.size();
  }

  public void insert(final Layer l, final int index) {
    layers.add(index, l);
  }

  public void paint(final Graphics g, final int xdest, final int ydest) {
    g.translate(xdest + x, ydest + y);
    for (int i = 0; i < layers.size(); i++) {
      layers.get(layers.size() - (i + 1)).paint(g);
    }
    g.translate(0 - (xdest + x), 0 - (ydest + y));
  }

  public void remove(final Layer l) {
    layers.remove(l);
  }

  public void setViewWindow(final int wx, final int wy, final int wwidth, final int wheight) {
    x = wx;
    y = wy;
    width = wwidth;
    height = wheight;
  }

}
