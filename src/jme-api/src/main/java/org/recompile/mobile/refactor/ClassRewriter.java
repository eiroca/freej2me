package org.recompile.mobile.refactor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassRewriter extends ClassVisitor {

  public ClassRewriter(final ClassVisitor visitor) {
    super(Opcodes.ASM7);
    this.cv = visitor;
  }

  @Override
  public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
    return new MethodRewriter(cv.visitMethod(access, name, desc, signature, exceptions));
  }

}
