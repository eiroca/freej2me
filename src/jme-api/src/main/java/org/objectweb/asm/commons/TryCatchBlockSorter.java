package org.objectweb.asm.commons;

import java.util.Collections;
import java.util.Comparator;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * Sorts the exception handlers in a method innermost-to-outermost. This allows the programmer to
 * add handlers without worrying about ordering them correctly with respect to existing, in-code
 * handlers.
 *
 * Behavior is only defined for properly-nested handlers. If any "try" blocks overlap (something
 * that isn't possible in Java code) then this may not do what you want. In fact, this adapter just
 * sorts by the length of the "try" block, taking advantage of the fact that a given try block must
 * be larger than any block it contains).
 *
 * @author Adrian Sampson
 */
public class TryCatchBlockSorter extends MethodNode {

  private final MethodVisitor mv;

  public TryCatchBlockSorter(final MethodVisitor mv, final int access, final String name, final String desc, final String signature, final String[] exceptions) {
    super(access, name, desc, signature, exceptions);
    this.mv = mv;
  }

  @Override
  public void visitEnd() {
    // Compares TryCatchBlockNodes by the length of their "try" block.
    final Comparator comp = new Comparator() {

      @Override
      public int compare(final Object o1, final Object o2) {
        final int len1 = blockLength((TryCatchBlockNode)o1);
        final int len2 = blockLength((TryCatchBlockNode)o2);
        return len1 - len2;
      }

      private int blockLength(final TryCatchBlockNode block) {
        final int startidx = instructions.indexOf(block.start);
        final int endidx = instructions.indexOf(block.end);
        return endidx - startidx;
      }
    };
    Collections.sort(tryCatchBlocks, comp);
    if (mv != null) {
      accept(mv);
    }
  }
}
