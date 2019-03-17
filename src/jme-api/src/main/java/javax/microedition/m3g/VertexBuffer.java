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

public class VertexBuffer extends Object3D {

  public VertexBuffer() {
  }

  public VertexArray getColors() {
    return new VertexArray(0, 0, 0);
  }

  public int getDefaultColor() {
    return 0;
  }

  public VertexArray getNormals() {
    return new VertexArray(0, 0, 0);
  }

  public VertexArray getPositions(final float[] scaleBias) {
    return new VertexArray(0, 0, 0);
  }

  public VertexArray getTexCoords(final int index, final float[] scaleBias) {
    return new VertexArray(0, 0, 0);
  }

  public int getVertexCount() {
    return 0;
  }

  public void setColors(final VertexArray colors) {
  }

  public void setDefaultColor(final int ARGB) {
  }

  public void setNormals(final VertexArray normals) {
  }

  public void setPositions(final VertexArray positions, final float scale, final float[] bias) {
  }

  public void setTexCoords(final int index, final VertexArray texCoords, final float scale, final float[] bias) {
  }

}
