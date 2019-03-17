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

public class Mesh extends Node {

  private Appearance appearance;
  private IndexBuffer indexbuffer;
  private VertexBuffer vertexbuffer;

  public Mesh() {
    /* DELETE THIS */
  }

  public Mesh(final VertexBuffer vertices, final IndexBuffer[] submeshes, final Appearance[] appearances) {
  }

  public Mesh(final VertexBuffer vertices, final IndexBuffer submesh, final Appearance appearance) {
  }

  public Appearance getAppearance(final int index) {
    return appearance;
  }

  public IndexBuffer getIndexBuffer(final int index) {
    return indexbuffer;
  }

  public int getSubmeshCount() {
    return 0;
  }

  public VertexBuffer getVertexBuffer() {
    return vertexbuffer;
  }

  public void setAppearance(final int index, final Appearance a) {
    appearance = a;
  }

}
