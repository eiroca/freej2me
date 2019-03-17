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

public class SkinnedMesh extends Mesh {

  private Group skeleton;

  public SkinnedMesh(final VertexBuffer vertices, final IndexBuffer[] submeshes, final Appearance[] appearances, final Group skeleton) {
  }

  public SkinnedMesh(final VertexBuffer vertices, final IndexBuffer submesh, final Appearance appearance, final Group skeleton) {
  }

  public void addTransform(final Node bone, final int weight, final int firstVertex, final int numVertices) {
  }

  public void getBoneTransform(final Node bone, final Transform transform) {
  }

  public int getBoneVertices(final Node bone, final int[] indices, final float[] weights) {
    return 0;
  }

  public Group getSkeleton() {
    return skeleton;
  }

}
