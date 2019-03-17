package org.recompile.mobile.refactor;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class MethodRewriter extends MethodVisitor implements Opcodes {

  public MethodRewriter(final MethodVisitor visitor) {
    super(Opcodes.ASM7);
    this.mv = visitor;
  }

  @Override
  public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, boolean isInterface) {
    if ((opcode == Opcodes.INVOKEVIRTUAL) && name.equals("getResourceAsStream") && owner.equals("java/lang/Class")) {
      mv.visitMethodInsn(Opcodes.INVOKESTATIC, "org/recompile/mobile/Mobile", name, "(Ljava/lang/Class;Ljava/lang/String;)Ljava/io/InputStream;", isInterface);
    }
    else {
      super.visitMethodInsn(opcode, owner, name, desc, isInterface);
    }
  }
}