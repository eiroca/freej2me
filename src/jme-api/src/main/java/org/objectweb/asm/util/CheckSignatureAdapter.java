/***
 * ASM: a very small and fast Java bytecode manipulation framework Copyright (c) 2000-2007 INRIA,
 * France Telecom All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: 1. Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. 2. Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.objectweb.asm.util;

import org.objectweb.asm.signature.SignatureVisitor;

/**
 * A {@link SignatureVisitor} that checks that its methods are properly used.
 *
 * @author Eric Bruneton
 */
public class CheckSignatureAdapter implements SignatureVisitor {

  /**
   * Type to be used to check class signatures. See
   * {@link #CheckSignatureAdapter(int, SignatureVisitor) CheckSignatureAdapter}.
   */
  public static final int CLASS_SIGNATURE = 0;

  /**
   * Type to be used to check method signatures. See
   * {@link #CheckSignatureAdapter(int, SignatureVisitor) CheckSignatureAdapter}.
   */
  public static final int METHOD_SIGNATURE = 1;

  /**
   * Type to be used to check type signatures.See
   * {@link #CheckSignatureAdapter(int, SignatureVisitor) CheckSignatureAdapter}.
   */
  public static final int TYPE_SIGNATURE = 2;

  private static final int EMPTY = 1;

  private static final int FORMAL = 2;

  private static final int BOUND = 4;

  private static final int SUPER = 8;

  private static final int PARAM = 16;

  private static final int RETURN = 32;

  private static final int SIMPLE_TYPE = 64;

  private static final int CLASS_TYPE = 128;

  private static final int END = 256;

  /**
   * Type of the signature to be checked.
   */
  private final int type;

  /**
   * State of the automaton used to check the order of method calls.
   */
  private int state;

  /**
   * <tt>true</tt> if the checked type signature can be 'V'.
   */
  private boolean canBeVoid;

  /**
   * The visitor to which this adapter must delegate calls. May be <tt>null</tt>.
   */
  private final SignatureVisitor sv;

  /**
   * Creates a new {@link CheckSignatureAdapter} object.
   *
   * @param type the type of signature to be checked. See {@link #CLASS_SIGNATURE},
   *        {@link #METHOD_SIGNATURE} and {@link #TYPE_SIGNATURE}.
   * @param sv the visitor to which this adapter must delegate calls. May be <tt>null</tt>.
   */
  public CheckSignatureAdapter(final int type, final SignatureVisitor sv) {
    this.type = type;
    state = CheckSignatureAdapter.EMPTY;
    this.sv = sv;
  }

  // class and method signatures

  @Override
  public void visitFormalTypeParameter(final String name) {
    if ((type == CheckSignatureAdapter.TYPE_SIGNATURE)
        || ((state != CheckSignatureAdapter.EMPTY) && (state != CheckSignatureAdapter.FORMAL) && (state != CheckSignatureAdapter.BOUND))) { throw new IllegalStateException(); }
    CheckMethodAdapter.checkIdentifier(name, "formal type parameter");
    state = CheckSignatureAdapter.FORMAL;
    if (sv != null) {
      sv.visitFormalTypeParameter(name);
    }
  }

  @Override
  public SignatureVisitor visitClassBound() {
    if (state != CheckSignatureAdapter.FORMAL) { throw new IllegalStateException(); }
    state = CheckSignatureAdapter.BOUND;
    final SignatureVisitor v = sv == null ? null : sv.visitClassBound();
    return new CheckSignatureAdapter(CheckSignatureAdapter.TYPE_SIGNATURE, v);
  }

  @Override
  public SignatureVisitor visitInterfaceBound() {
    if ((state != CheckSignatureAdapter.FORMAL) && (state != CheckSignatureAdapter.BOUND)) { throw new IllegalArgumentException(); }
    final SignatureVisitor v = sv == null ? null : sv.visitInterfaceBound();
    return new CheckSignatureAdapter(CheckSignatureAdapter.TYPE_SIGNATURE, v);
  }

  // class signatures

  @Override
  public SignatureVisitor visitSuperclass() {
    if ((type != CheckSignatureAdapter.CLASS_SIGNATURE) || ((state & (CheckSignatureAdapter.EMPTY | CheckSignatureAdapter.FORMAL | CheckSignatureAdapter.BOUND)) == 0)) { throw new IllegalArgumentException(); }
    state = CheckSignatureAdapter.SUPER;
    final SignatureVisitor v = sv == null ? null : sv.visitSuperclass();
    return new CheckSignatureAdapter(CheckSignatureAdapter.TYPE_SIGNATURE, v);
  }

  @Override
  public SignatureVisitor visitInterface() {
    if (state != CheckSignatureAdapter.SUPER) { throw new IllegalStateException(); }
    final SignatureVisitor v = sv == null ? null : sv.visitInterface();
    return new CheckSignatureAdapter(CheckSignatureAdapter.TYPE_SIGNATURE, v);
  }

  // method signatures

  @Override
  public SignatureVisitor visitParameterType() {
    if ((type != CheckSignatureAdapter.METHOD_SIGNATURE)
        || ((state & (CheckSignatureAdapter.EMPTY | CheckSignatureAdapter.FORMAL | CheckSignatureAdapter.BOUND | CheckSignatureAdapter.PARAM)) == 0)) { throw new IllegalArgumentException(); }
    state = CheckSignatureAdapter.PARAM;
    final SignatureVisitor v = sv == null ? null : sv.visitParameterType();
    return new CheckSignatureAdapter(CheckSignatureAdapter.TYPE_SIGNATURE, v);
  }

  @Override
  public SignatureVisitor visitReturnType() {
    if ((type != CheckSignatureAdapter.METHOD_SIGNATURE)
        || ((state & (CheckSignatureAdapter.EMPTY | CheckSignatureAdapter.FORMAL | CheckSignatureAdapter.BOUND | CheckSignatureAdapter.PARAM)) == 0)) { throw new IllegalArgumentException(); }
    state = CheckSignatureAdapter.RETURN;
    final SignatureVisitor v = sv == null ? null : sv.visitReturnType();
    final CheckSignatureAdapter cv = new CheckSignatureAdapter(CheckSignatureAdapter.TYPE_SIGNATURE, v);
    cv.canBeVoid = true;
    return cv;
  }

  @Override
  public SignatureVisitor visitExceptionType() {
    if (state != CheckSignatureAdapter.RETURN) { throw new IllegalStateException(); }
    final SignatureVisitor v = sv == null ? null : sv.visitExceptionType();
    return new CheckSignatureAdapter(CheckSignatureAdapter.TYPE_SIGNATURE, v);
  }

  // type signatures

  @Override
  public void visitBaseType(final char descriptor) {
    if ((type != CheckSignatureAdapter.TYPE_SIGNATURE) || (state != CheckSignatureAdapter.EMPTY)) { throw new IllegalStateException(); }
    if (descriptor == 'V') {
      if (!canBeVoid) { throw new IllegalArgumentException(); }
    }
    else {
      if ("ZCBSIFJD".indexOf(descriptor) == -1) { throw new IllegalArgumentException(); }
    }
    state = CheckSignatureAdapter.SIMPLE_TYPE;
    if (sv != null) {
      sv.visitBaseType(descriptor);
    }
  }

  @Override
  public void visitTypeVariable(final String name) {
    if ((type != CheckSignatureAdapter.TYPE_SIGNATURE) || (state != CheckSignatureAdapter.EMPTY)) { throw new IllegalStateException(); }
    CheckMethodAdapter.checkIdentifier(name, "type variable");
    state = CheckSignatureAdapter.SIMPLE_TYPE;
    if (sv != null) {
      sv.visitTypeVariable(name);
    }
  }

  @Override
  public SignatureVisitor visitArrayType() {
    if ((type != CheckSignatureAdapter.TYPE_SIGNATURE) || (state != CheckSignatureAdapter.EMPTY)) { throw new IllegalStateException(); }
    state = CheckSignatureAdapter.SIMPLE_TYPE;
    final SignatureVisitor v = sv == null ? null : sv.visitArrayType();
    return new CheckSignatureAdapter(CheckSignatureAdapter.TYPE_SIGNATURE, v);
  }

  @Override
  public void visitClassType(final String name) {
    if ((type != CheckSignatureAdapter.TYPE_SIGNATURE) || (state != CheckSignatureAdapter.EMPTY)) { throw new IllegalStateException(); }
    CheckMethodAdapter.checkInternalName(name, "class name");
    state = CheckSignatureAdapter.CLASS_TYPE;
    if (sv != null) {
      sv.visitClassType(name);
    }
  }

  @Override
  public void visitInnerClassType(final String name) {
    if (state != CheckSignatureAdapter.CLASS_TYPE) { throw new IllegalStateException(); }
    CheckMethodAdapter.checkIdentifier(name, "inner class name");
    if (sv != null) {
      sv.visitInnerClassType(name);
    }
  }

  @Override
  public void visitTypeArgument() {
    if (state != CheckSignatureAdapter.CLASS_TYPE) { throw new IllegalStateException(); }
    if (sv != null) {
      sv.visitTypeArgument();
    }
  }

  @Override
  public SignatureVisitor visitTypeArgument(final char wildcard) {
    if (state != CheckSignatureAdapter.CLASS_TYPE) { throw new IllegalStateException(); }
    if ("+-=".indexOf(wildcard) == -1) { throw new IllegalArgumentException(); }
    final SignatureVisitor v = sv == null ? null : sv.visitTypeArgument(wildcard);
    return new CheckSignatureAdapter(CheckSignatureAdapter.TYPE_SIGNATURE, v);
  }

  @Override
  public void visitEnd() {
    if (state != CheckSignatureAdapter.CLASS_TYPE) { throw new IllegalStateException(); }
    state = CheckSignatureAdapter.END;
    if (sv != null) {
      sv.visitEnd();
    }
  }
}
