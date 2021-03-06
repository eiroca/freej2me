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

public abstract class Node extends Transformable {

  public static final int NONE = 144;
  public static final int ORIGIN = 145;
  public static final int X_AXIS = 146;
  public static final int Y_AXIS = 147;
  public static final int Z_AXIS = 148;

  private Node alignRef;
  private float alphaFactor;
  private boolean picking = false;
  private boolean rendering = false;
  private int scope;

  public void align(final Node reference) {
  }

  public Node getAlignmentReference(final int axis) {
    return alignRef;
  }

  public int getAlignmentTarget(final int axis) {
    return 0;
  }

  public float getAlphaFactor() {
    return alphaFactor;
  }

  public Node getParent() {
    return this;
  }

  public int getScope() {
    return scope;
  }

  public boolean getTransformTo(final Node target, final Transform transform) {
    return false;
  }

  public boolean isPickingEnabled() {
    return picking;
  }

  public boolean isRenderingEnabled() {
    return rendering;
  }

  public void setAlignment(final Node zRef, final int zTarget, final Node yRef, final int yTarget) {
  }

  public void setAlphaFactor(final float value) {
    alphaFactor = value;
  }

  public void setPickingEnable(final boolean enable) {
    picking = enable;
  }

  public void setRenderingEnable(final boolean enable) {
    rendering = enable;
  }

  public void setScope(final int value) {
    scope = value;
  }

}
