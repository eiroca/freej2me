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

public abstract class Transformable extends Object3D {

  public void getCompositeTransform(final Transform transform) {
  }

  public void getOrientation(final float[] angleAxis) {
  }

  public void getScale(final float[] xyz) {
  }

  public void getTransform(final Transform transform) {
  }

  public void getTranslation(final float[] xyz) {
  }

  public void postRotate(final float angle, final float ax, final float ay, final float az) {
  }

  public void preRotate(final float angle, final float ax, final float ay, final float az) {
  }

  public void scale(final float sx, final float sy, final float sz) {
  }

  public void setOrientation(final float angle, final float ax, final float ay, final float az) {
  }

  public void setScale(final float sx, final float sy, final float sz) {
  }

  public void setTransform(final Transform transform) {
  }

  public void setTranslation(final float tx, final float ty, final float tz) {
  }

  public void translate(final float tx, final float ty, final float tz) {
  }

}
