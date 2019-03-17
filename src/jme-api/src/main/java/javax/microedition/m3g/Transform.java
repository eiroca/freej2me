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

public class Transform {

  public Transform() {
  }

  public Transform(final Transform transform) {
  }

  public void get(final float[] matrix) {
  }

  public void invert() {
  }

  public void postMultiply(final Transform transform) {
  }

  public void postRotate(final float angle, final float ax, final float ay, final float az) {
  }

  public void postRotateQuat(final float qx, final float qy, final float qz, final float qw) {
  }

  public void postScale(final float sx, final float sy, final float sz) {
  }

  public void postTranslate(final float tx, final float ty, final float tz) {
  }

  public void set(final float[] matrix) {
  }

  public void set(final Transform transform) {
  }

  public void setIdentity() {
  }

  public void transform(final float[] vectors) {
  }

  public void transform(final VertexArray in, final float[] out, final boolean W) {
  }

  public void transpose() {
  }

}
