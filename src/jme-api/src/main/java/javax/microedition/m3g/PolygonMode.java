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
package javax.microedition.m3g;

public class PolygonMode extends Object3D {

  public static final int CULL_BACK = 160;
  public static final int CULL_FRONT = 161;
  public static final int CULL_NONE = 162;
  public static final int SHADE_FLAT = 164;
  public static final int SHADE_SMOOTH = 165;
  public static final int WINDING_CCW = 168;
  public static final int WINDING_CW = 169;

  private int culling = PolygonMode.CULL_BACK;
  private int shading = PolygonMode.SHADE_FLAT;
  private int winding = PolygonMode.WINDING_CCW;

  public PolygonMode() {
  }

  public int getCulling() {
    return culling;
  }

  public int getShading() {
    return shading;
  }

  public int getWinding() {
    return winding;
  }

  public boolean isLocalCameraLightingEnabled() {
    return false;
  }

  public boolean isPerspectiveCorrectionEnabled() {
    return false;
  }

  public boolean isTwoSidedLightingEnabled() {
    return false;
  }

  public void setCulling(final int mode) {
    culling = mode;
  }

  public void setLocalCameraLightingEnable(final boolean enable) {
  }

  public void setPerspectiveCorrectionEnable(final boolean enable) {
  }

  public void setShading(final int mode) {
    shading = mode;
  }

  public void setTwoSidedLightingEnable(final boolean enable) {
  }

  public void setWinding(final int mode) {
    winding = mode;
  }

}
