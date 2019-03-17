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
package com.mascotcapsule.micro3d.v3;

import java.util.ArrayList;

public class FigureLayout {

  private int scalex = 1;
  private int scaley = 1;
  private int centerx = 1;
  private int centery = 1;
  private int pwidth = 1;
  private int pheight = 1;
  private int selected = 0;
  private final ArrayList<AffineTrans> trans = new ArrayList<>();

  public FigureLayout() {
  }

  public FigureLayout(final AffineTrans atrans, final int sx, final int sy, final int cx, final int cy) {
    scalex = sx;
    scaley = sy;
    centerx = cx;
    centery = cy;
    trans.add(atrans);
  }

  public final AffineTrans getAffineTrans() {
    return trans.get(selected);
  }

  public final void setAffineTrans(final AffineTrans atrans) {
    if (trans.size() == 0) {
      trans.add(atrans);
    }
    else {
      trans.set(selected, atrans);
    }
  }

  public final void setAffineTransArray(final AffineTrans[] atrans) {
    for (final AffineTrans atran : atrans) {
      trans.add(atran);
    }
  }

  public final void setAffineTrans(final AffineTrans[] atrans) {
    setAffineTransArray(atrans);
  }

  public final void selectAffineTrans(final int i) {
    selected = i;
  }

  public final int getScaleX() {
    return scalex;
  }

  public final int getScaleY() {
    return scaley;
  }

  public final void setScale(final int x, final int y) {
    scalex = x;
    scaley = y;
  }

  public final int getParallelWidth() {
    return pwidth;
  }

  public final int getParallelHeight() {
    return pheight;
  }

  public final void setParallelSize(final int w, final int h) {
    pwidth = w;
    pheight = h;
  }

  public final int getCenterX() {
    return centerx;
  }

  public final int getCenterY() {
    return centery;
  }

  public final void setCenter(final int x, final int y) {
    centerx = x;
    centery = y;
  }

  public final void setPerspective(final int x, final int y, final int z) {
  }

  public final void setPerspective(final int x, final int y, final int z, final int w) {
  }

}
