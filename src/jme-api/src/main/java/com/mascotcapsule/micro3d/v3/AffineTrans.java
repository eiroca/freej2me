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

public class AffineTrans {

  public int m00;
  public int m01;
  public int m02;
  public int m03;
  public int m10;
  public int m11;
  public int m12;
  public int m13;
  public int m20;
  public int m21;
  public int m22;
  public int m23;

  private int rotx;
  private int roty;
  private int rotz;

  public AffineTrans() {
  }

  public AffineTrans(final int m00, final int m01, final int m02, final int m03, final int m10, final int m11, final int m12, final int m13, final int m20, final int m21, final int m22, final int m23) {
    this.m00 = m00;
    this.m01 = m01;
    this.m02 = m02;
    this.m03 = m03;
    this.m10 = m10;
    this.m11 = m11;
    this.m12 = m12;
    this.m13 = m13;
    this.m20 = m20;
    this.m21 = m21;
    this.m22 = m22;
    this.m23 = m23;
  }

  public AffineTrans(final AffineTrans a) {
    m00 = a.m00;
    m01 = a.m01;
    m02 = a.m02;
    m03 = a.m03;
    m10 = a.m10;
    m11 = a.m11;
    m12 = a.m12;
    m13 = a.m13;
    m20 = a.m20;
    m21 = a.m21;
    m22 = a.m22;
    m23 = a.m23;
  }

  public AffineTrans(final int[][] a) {
    m00 = a[0][0];
    m01 = a[0][1];
    m02 = a[0][2];
    m03 = a[0][3];
    m10 = a[1][0];
    m11 = a[1][1];
    m12 = a[1][2];
    m13 = a[1][3];
    m20 = a[2][0];
    m21 = a[2][1];
    m22 = a[2][2];
    m23 = a[2][3];
  }

  public AffineTrans(final int[] a) {
    m00 = a[0];
    m01 = a[1];
    m02 = a[2];
    m03 = a[3];
    m10 = a[4];
    m11 = a[5];
    m12 = a[6];
    m13 = a[7];
    m20 = a[8];
    m21 = a[9];
    m22 = a[10];
    m23 = a[11];
  }

  public AffineTrans(final int[] a, final int offset) {
    m00 = a[offset + 0];
    m01 = a[offset + 1];
    m02 = a[offset + 2];
    m03 = a[offset + 3];
    m10 = a[offset + 4];
    m11 = a[offset + 5];
    m12 = a[offset + 6];
    m13 = a[offset + 7];
    m20 = a[offset + 8];
    m21 = a[offset + 9];
    m22 = a[offset + 10];
    m23 = a[offset + 11];

  }

  public final void get(final int[] a) {
    a[0] = m00;
    a[1] = m01;
    a[2] = m02;
    a[3] = m03;
    a[4] = m10;
    a[5] = m11;
    a[6] = m12;
    a[7] = m13;
    a[8] = m20;
    a[9] = m21;
    a[10] = m22;
    a[11] = m23;
  }

  public final void get(final int[] a, final int offset) {
    a[offset + 0] = m00;
    a[offset + 1] = m01;
    a[offset + 2] = m02;
    a[offset + 3] = m03;
    a[offset + 4] = m10;
    a[offset + 5] = m11;
    a[offset + 6] = m12;
    a[offset + 7] = m13;
    a[offset + 8] = m20;
    a[offset + 9] = m21;
    a[offset + 10] = m22;
    a[offset + 11] = m23;
  }

  public final void set(final int[] a, final int offset) {
    m00 = a[offset + 0];
    m01 = a[offset + 1];
    m02 = a[offset + 2];
    m03 = a[offset + 3];
    m10 = a[offset + 4];
    m11 = a[offset + 5];
    m12 = a[offset + 6];
    m13 = a[offset + 7];
    m20 = a[offset + 8];
    m21 = a[offset + 9];
    m22 = a[offset + 10];
    m23 = a[offset + 11];
  }

  public final void set(final int m00, final int m01, final int m02, final int m03, final int m10, final int m11, final int m12, final int m13, final int m20, final int m21, final int m22, final int m23) {
    this.m00 = m00;
    this.m01 = m01;
    this.m02 = m02;
    this.m03 = m03;
    this.m10 = m10;
    this.m11 = m11;
    this.m12 = m12;
    this.m13 = m13;
    this.m20 = m20;
    this.m21 = m21;
    this.m22 = m22;
    this.m23 = m23;
  }

  public final void set(final AffineTrans a) {
    m00 = a.m00;
    m01 = a.m01;
    m02 = a.m02;
    m03 = a.m03;
    m10 = a.m10;
    m11 = a.m11;
    m12 = a.m12;
    m13 = a.m13;
    m20 = a.m20;
    m21 = a.m21;
    m22 = a.m22;
    m23 = a.m23;
  }

  public final void set(final int[][] a) {
    m00 = a[0][0];
    m01 = a[0][1];
    m02 = a[0][2];
    m03 = a[0][3];
    m10 = a[1][0];
    m11 = a[1][1];
    m12 = a[1][2];
    m13 = a[1][3];
    m20 = a[2][0];
    m21 = a[2][1];
    m22 = a[2][2];
    m23 = a[2][3];
  }

  public final void set(final int[] a) {
    m00 = a[0];
    m01 = a[1];
    m02 = a[2];
    m03 = a[3];
    m10 = a[4];
    m11 = a[5];
    m12 = a[6];
    m13 = a[7];
    m20 = a[8];
    m21 = a[9];
    m22 = a[10];
    m23 = a[11];
  }

  public final Vector3D transPoint(final Vector3D v) {
    return v; // ?
  }

  public final Vector3D transform(final Vector3D v) {
    return v; // ?
  }

  public final void multiply(final AffineTrans a) {
    mul(a);
  }

  public final void mul(final AffineTrans a) {
    // big multiply function goes here
  }

  public final void multiply(final AffineTrans a1, final AffineTrans a2) {
    mul(a1, a2);
  }

  public final void mul(final AffineTrans a1, final AffineTrans a2) {
  }

  public final void setRotationX(final int x) {
    rotx = x;
  }

  public final void setRotationY(final int y) {
    roty = y;
  }

  public final void setRotationZ(final int z) {
    rotz = z;
  }

  public final void setIdentity() {
  }

  public final void rotationX(final int x) {
    rotx = x;
  }

  public final void rotationY(final int y) {
    roty = y;
  }

  public final void rotationZ(final int z) {
    rotz = z;
  }

  public final void rotationV(final Vector3D vector3d, final int i) {
  }

  public final void setRotation(final Vector3D vector3d, final int i) {
  }

  public final void setViewTrans(final Vector3D vector3d, final Vector3D vector3d1, final Vector3D vector3d2) {
  }

  public final void lookAt(final Vector3D vector3d, final Vector3D vector3d1, final Vector3D vector3d2) {
  }

}
