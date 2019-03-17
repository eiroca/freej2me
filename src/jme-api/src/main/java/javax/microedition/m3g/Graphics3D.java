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

import java.util.Hashtable;
import org.recompile.mobile.Mobile;

public class Graphics3D {

  public static final int ANTIALIAS = 2;
  public static final int DITHER = 4;
  public static final int OVERWRITE = 16;
  public static final int TRUE_COLOR = 8;

  private static Hashtable properties;

  private int viewx;
  private int viewy;
  private int vieww;
  private int viewh;
  private float near;
  private float far;

  public int addLight(final Light light, final Transform transform) {
    return 0;
  }

  public void bindTarget(final java.lang.Object target) {
  }

  public void bindTarget(final java.lang.Object target, final boolean depthBuffer, final int hints) {
  }

  public void clear(final Background background) {
  }

  public Camera getCamera(final Transform transform) {
    return new Camera();
  }

  public float getDepthRangeFar() {
    return far;
  }

  public float getDepthRangeNear() {
    return near;
  }

  public int getHints() {
    return 0;
  }

  public static Graphics3D getInstance() {
    return Mobile.getGraphics3D();
  }

  public Light getLight(final int index, final Transform transform) {
    return new Light();
  }

  public int getLightCount() {
    return 0;
  }

  public static Hashtable getProperties() {
    return Graphics3D.properties;
  }

  public Object getTarget() {
    return null;
  }

  public int getViewportHeight() {
    return viewh;
  }

  public int getViewportWidth() {
    return vieww;
  }

  public int getViewportX() {
    return viewx;
  }

  public int getViewportY() {
    return viewy;
  }

  public boolean isDepthBufferEnabled() {
    return false;
  }

  public void releaseTarget() {
  }

  public void render(final Node node, final Transform transform) {
  }

  public void render(final VertexBuffer vertices, final IndexBuffer triangles, final Appearance appearance, final Transform transform) {
  }

  public void render(final VertexBuffer vertices, final IndexBuffer triangles, final Appearance appearance, final Transform transform, final int scope) {
  }

  public void render(final World world) {
  }

  public void resetLights() {
  }

  public void setCamera(final Camera camera, final Transform transform) {
  }

  public void setDepthRange(final float Near, final float Far) {
    near = Near;
    far = Far;
  }

  public void setLight(final int index, final Light light, final Transform transform) {
  }

  public void setViewport(final int x, final int y, final int width, final int height) {
    viewx = x;
    viewy = y;
    vieww = width;
    viewh = height;
  }

}
