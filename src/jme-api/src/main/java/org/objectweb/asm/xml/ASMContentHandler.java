/***
 * ASM XML Adapter Copyright (c) 2004, Eugene Kuleshov All rights reserved.
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
package org.objectweb.asm.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A {@link org.xml.sax.ContentHandler ContentHandler} that transforms XML document into Java class
 * file. This class can be feeded by any kind of SAX 2.0 event producers, e.g. XML parser, XSLT or
 * XPath engines, or custom code.
 *
 * @see org.objectweb.asm.xml.SAXClassAdapter
 * @see org.objectweb.asm.xml.Processor
 *
 * @author Eugene Kuleshov
 */
public class ASMContentHandler extends DefaultHandler implements Opcodes {

  /**
   * Stack of the intermediate processing contexts.
   */
  private final List stack = new ArrayList();

  /**
   * Complete name of the current element.
   */
  String match = "";

  /**
   * <tt>true</tt> if the maximum stack size and number of local variables must be automatically
   * computed.
   */
  protected boolean computeMax;

  /**
   * Output stream to write result bytecode.
   */
  protected OutputStream os;

  /**
   * Current instance of the {@link ClassWriter ClassWriter} used to write class bytecode.
   */
  protected ClassWriter cw;

  /**
   * Map of the active {@link Label Label} instances for current method.
   */
  protected Map labels;

  private static final String BASE = "class";

  private final RuleSet RULES = new RuleSet();
  {
    RULES.add(ASMContentHandler.BASE, new ClassRule());
    RULES.add(ASMContentHandler.BASE + "/interfaces/interface", new InterfaceRule());
    RULES.add(ASMContentHandler.BASE + "/interfaces", new InterfacesRule());
    RULES.add(ASMContentHandler.BASE + "/outerclass", new OuterClassRule());
    RULES.add(ASMContentHandler.BASE + "/innerclass", new InnerClassRule());
    RULES.add(ASMContentHandler.BASE + "/source", new SourceRule());
    RULES.add(ASMContentHandler.BASE + "/field", new FieldRule());

    RULES.add(ASMContentHandler.BASE + "/method", new MethodRule());
    RULES.add(ASMContentHandler.BASE + "/method/exceptions/exception", new ExceptionRule());
    RULES.add(ASMContentHandler.BASE + "/method/exceptions", new ExceptionsRule());

    RULES.add(ASMContentHandler.BASE + "/method/annotationDefault",
        new AnnotationDefaultRule());

    RULES.add(ASMContentHandler.BASE + "/method/code/*", new OpcodesRule()); // opcodes

    RULES.add(ASMContentHandler.BASE + "/method/code/frame", new FrameRule());
    RULES.add(ASMContentHandler.BASE + "/method/code/frame/local", new FrameTypeRule());
    RULES.add(ASMContentHandler.BASE + "/method/code/frame/stack", new FrameTypeRule());

    RULES.add(ASMContentHandler.BASE + "/method/code/TABLESWITCH", new TableSwitchRule());
    RULES.add(ASMContentHandler.BASE + "/method/code/TABLESWITCH/label",
        new TableSwitchLabelRule());
    RULES.add(ASMContentHandler.BASE + "/method/code/LOOKUPSWITCH", new LookupSwitchRule());
    RULES.add(ASMContentHandler.BASE + "/method/code/LOOKUPSWITCH/label",
        new LookupSwitchLabelRule());

    RULES.add(ASMContentHandler.BASE + "/method/code/Label", new LabelRule());
    RULES.add(ASMContentHandler.BASE + "/method/code/TryCatch", new TryCatchRule());
    RULES.add(ASMContentHandler.BASE + "/method/code/LineNumber", new LineNumberRule());
    RULES.add(ASMContentHandler.BASE + "/method/code/LocalVar", new LocalVarRule());
    RULES.add(ASMContentHandler.BASE + "/method/code/Max", new MaxRule());

    RULES.add("*/annotation", new AnnotationRule());
    RULES.add("*/parameterAnnotation", new AnnotationParameterRule());
    RULES.add("*/annotationValue", new AnnotationValueRule());
    RULES.add("*/annotationValueAnnotation",
        new AnnotationValueAnnotationRule());
    RULES.add("*/annotationValueEnum", new AnnotationValueEnumRule());
    RULES.add("*/annotationValueArray", new AnnotationValueArrayRule());
  }

  private static interface OpcodeGroup {

    public static final int INSN = 0;
    public static final int INSN_INT = 1;
    public static final int INSN_VAR = 2;
    public static final int INSN_TYPE = 3;
    public static final int INSN_FIELD = 4;
    public static final int INSN_METHOD = 5;
    public static final int INSN_JUMP = 6;
    public static final int INSN_LDC = 7;
    public static final int INSN_IINC = 8;
    public static final int INSN_MULTIANEWARRAY = 9;
  }

  /**
   * Map of the opcode names to opcode and opcode group
   */
  static final Map OPCODES = new HashMap();
  static {
    ASMContentHandler.addOpcode("NOP", Opcodes.NOP, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("ACONST_NULL", Opcodes.ACONST_NULL, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("ICONST_M1", Opcodes.ICONST_M1, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("ICONST_0", Opcodes.ICONST_0, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("ICONST_1", Opcodes.ICONST_1, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("ICONST_2", Opcodes.ICONST_2, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("ICONST_3", Opcodes.ICONST_3, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("ICONST_4", Opcodes.ICONST_4, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("ICONST_5", Opcodes.ICONST_5, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LCONST_0", Opcodes.LCONST_0, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LCONST_1", Opcodes.LCONST_1, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("FCONST_0", Opcodes.FCONST_0, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("FCONST_1", Opcodes.FCONST_1, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("FCONST_2", Opcodes.FCONST_2, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DCONST_0", Opcodes.DCONST_0, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DCONST_1", Opcodes.DCONST_1, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("BIPUSH", Opcodes.BIPUSH, OpcodeGroup.INSN_INT);
    ASMContentHandler.addOpcode("SIPUSH", Opcodes.SIPUSH, OpcodeGroup.INSN_INT);
    ASMContentHandler.addOpcode("LDC", Opcodes.LDC, OpcodeGroup.INSN_LDC);
    ASMContentHandler.addOpcode("ILOAD", Opcodes.ILOAD, OpcodeGroup.INSN_VAR);
    ASMContentHandler.addOpcode("LLOAD", Opcodes.LLOAD, OpcodeGroup.INSN_VAR);
    ASMContentHandler.addOpcode("FLOAD", Opcodes.FLOAD, OpcodeGroup.INSN_VAR);
    ASMContentHandler.addOpcode("DLOAD", Opcodes.DLOAD, OpcodeGroup.INSN_VAR);
    ASMContentHandler.addOpcode("ALOAD", Opcodes.ALOAD, OpcodeGroup.INSN_VAR);
    ASMContentHandler.addOpcode("IALOAD", Opcodes.IALOAD, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LALOAD", Opcodes.LALOAD, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("FALOAD", Opcodes.FALOAD, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DALOAD", Opcodes.DALOAD, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("AALOAD", Opcodes.AALOAD, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("BALOAD", Opcodes.BALOAD, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("CALOAD", Opcodes.CALOAD, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("SALOAD", Opcodes.SALOAD, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("ISTORE", Opcodes.ISTORE, OpcodeGroup.INSN_VAR);
    ASMContentHandler.addOpcode("LSTORE", Opcodes.LSTORE, OpcodeGroup.INSN_VAR);
    ASMContentHandler.addOpcode("FSTORE", Opcodes.FSTORE, OpcodeGroup.INSN_VAR);
    ASMContentHandler.addOpcode("DSTORE", Opcodes.DSTORE, OpcodeGroup.INSN_VAR);
    ASMContentHandler.addOpcode("ASTORE", Opcodes.ASTORE, OpcodeGroup.INSN_VAR);
    ASMContentHandler.addOpcode("IASTORE", Opcodes.IASTORE, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LASTORE", Opcodes.LASTORE, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("FASTORE", Opcodes.FASTORE, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DASTORE", Opcodes.DASTORE, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("AASTORE", Opcodes.AASTORE, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("BASTORE", Opcodes.BASTORE, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("CASTORE", Opcodes.CASTORE, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("SASTORE", Opcodes.SASTORE, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("POP", Opcodes.POP, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("POP2", Opcodes.POP2, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DUP", Opcodes.DUP, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DUP_X1", Opcodes.DUP_X1, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DUP_X2", Opcodes.DUP_X2, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DUP2", Opcodes.DUP2, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DUP2_X1", Opcodes.DUP2_X1, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DUP2_X2", Opcodes.DUP2_X2, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("SWAP", Opcodes.SWAP, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("IADD", Opcodes.IADD, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LADD", Opcodes.LADD, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("FADD", Opcodes.FADD, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DADD", Opcodes.DADD, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("ISUB", Opcodes.ISUB, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LSUB", Opcodes.LSUB, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("FSUB", Opcodes.FSUB, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DSUB", Opcodes.DSUB, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("IMUL", Opcodes.IMUL, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LMUL", Opcodes.LMUL, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("FMUL", Opcodes.FMUL, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DMUL", Opcodes.DMUL, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("IDIV", Opcodes.IDIV, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LDIV", Opcodes.LDIV, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("FDIV", Opcodes.FDIV, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DDIV", Opcodes.DDIV, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("IREM", Opcodes.IREM, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LREM", Opcodes.LREM, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("FREM", Opcodes.FREM, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DREM", Opcodes.DREM, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("INEG", Opcodes.INEG, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LNEG", Opcodes.LNEG, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("FNEG", Opcodes.FNEG, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DNEG", Opcodes.DNEG, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("ISHL", Opcodes.ISHL, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LSHL", Opcodes.LSHL, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("ISHR", Opcodes.ISHR, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LSHR", Opcodes.LSHR, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("IUSHR", Opcodes.IUSHR, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LUSHR", Opcodes.LUSHR, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("IAND", Opcodes.IAND, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LAND", Opcodes.LAND, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("IOR", Opcodes.IOR, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LOR", Opcodes.LOR, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("IXOR", Opcodes.IXOR, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LXOR", Opcodes.LXOR, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("IINC", Opcodes.IINC, OpcodeGroup.INSN_IINC);
    ASMContentHandler.addOpcode("I2L", Opcodes.I2L, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("I2F", Opcodes.I2F, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("I2D", Opcodes.I2D, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("L2I", Opcodes.L2I, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("L2F", Opcodes.L2F, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("L2D", Opcodes.L2D, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("F2I", Opcodes.F2I, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("F2L", Opcodes.F2L, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("F2D", Opcodes.F2D, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("D2I", Opcodes.D2I, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("D2L", Opcodes.D2L, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("D2F", Opcodes.D2F, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("I2B", Opcodes.I2B, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("I2C", Opcodes.I2C, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("I2S", Opcodes.I2S, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LCMP", Opcodes.LCMP, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("FCMPL", Opcodes.FCMPL, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("FCMPG", Opcodes.FCMPG, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DCMPL", Opcodes.DCMPL, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DCMPG", Opcodes.DCMPG, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("IFEQ", Opcodes.IFEQ, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("IFNE", Opcodes.IFNE, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("IFLT", Opcodes.IFLT, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("IFGE", Opcodes.IFGE, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("IFGT", Opcodes.IFGT, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("IFLE", Opcodes.IFLE, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("IF_ICMPEQ", Opcodes.IF_ICMPEQ, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("IF_ICMPNE", Opcodes.IF_ICMPNE, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("IF_ICMPLT", Opcodes.IF_ICMPLT, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("IF_ICMPGE", Opcodes.IF_ICMPGE, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("IF_ICMPGT", Opcodes.IF_ICMPGT, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("IF_ICMPLE", Opcodes.IF_ICMPLE, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("IF_ACMPEQ", Opcodes.IF_ACMPEQ, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("IF_ACMPNE", Opcodes.IF_ACMPNE, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("GOTO", Opcodes.GOTO, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("JSR", Opcodes.JSR, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("RET", Opcodes.RET, OpcodeGroup.INSN_VAR);
    ASMContentHandler.addOpcode("IRETURN", Opcodes.IRETURN, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("LRETURN", Opcodes.LRETURN, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("FRETURN", Opcodes.FRETURN, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("DRETURN", Opcodes.DRETURN, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("ARETURN", Opcodes.ARETURN, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("RETURN", Opcodes.RETURN, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("GETSTATIC", Opcodes.GETSTATIC, OpcodeGroup.INSN_FIELD);
    ASMContentHandler.addOpcode("PUTSTATIC", Opcodes.PUTSTATIC, OpcodeGroup.INSN_FIELD);
    ASMContentHandler.addOpcode("GETFIELD", Opcodes.GETFIELD, OpcodeGroup.INSN_FIELD);
    ASMContentHandler.addOpcode("PUTFIELD", Opcodes.PUTFIELD, OpcodeGroup.INSN_FIELD);
    ASMContentHandler.addOpcode("INVOKEVIRTUAL", Opcodes.INVOKEVIRTUAL, OpcodeGroup.INSN_METHOD);
    ASMContentHandler.addOpcode("INVOKESPECIAL", Opcodes.INVOKESPECIAL, OpcodeGroup.INSN_METHOD);
    ASMContentHandler.addOpcode("INVOKESTATIC", Opcodes.INVOKESTATIC, OpcodeGroup.INSN_METHOD);
    ASMContentHandler.addOpcode("INVOKEINTERFACE", Opcodes.INVOKEINTERFACE, OpcodeGroup.INSN_METHOD);
    ASMContentHandler.addOpcode("INVOKEDYNAMIC", Opcodes.INVOKEDYNAMIC, OpcodeGroup.INSN_METHOD);
    ASMContentHandler.addOpcode("NEW", Opcodes.NEW, OpcodeGroup.INSN_TYPE);
    ASMContentHandler.addOpcode("NEWARRAY", Opcodes.NEWARRAY, OpcodeGroup.INSN_INT);
    ASMContentHandler.addOpcode("ANEWARRAY", Opcodes.ANEWARRAY, OpcodeGroup.INSN_TYPE);
    ASMContentHandler.addOpcode("ARRAYLENGTH", Opcodes.ARRAYLENGTH, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("ATHROW", Opcodes.ATHROW, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("CHECKCAST", Opcodes.CHECKCAST, OpcodeGroup.INSN_TYPE);
    ASMContentHandler.addOpcode("INSTANCEOF", Opcodes.INSTANCEOF, OpcodeGroup.INSN_TYPE);
    ASMContentHandler.addOpcode("MONITORENTER", Opcodes.MONITORENTER, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("MONITOREXIT", Opcodes.MONITOREXIT, OpcodeGroup.INSN);
    ASMContentHandler.addOpcode("MULTIANEWARRAY", Opcodes.MULTIANEWARRAY, OpcodeGroup.INSN_MULTIANEWARRAY);
    ASMContentHandler.addOpcode("IFNULL", Opcodes.IFNULL, OpcodeGroup.INSN_JUMP);
    ASMContentHandler.addOpcode("IFNONNULL", Opcodes.IFNONNULL, OpcodeGroup.INSN_JUMP);
  }

  private static void addOpcode(final String operStr, final int oper, final int group) {
    ASMContentHandler.OPCODES.put(operStr, new Opcode(oper, group));
  }

  static final Map TYPES = new HashMap();
  static {
    final String[] types = SAXCodeAdapter.TYPES;
    for (int i = 0; i < types.length; i++) {
      ASMContentHandler.TYPES.put(types[i], new Integer(i));
    }
  }

  /**
   * Constructs a new {@link ASMContentHandler ASMContentHandler} object.
   *
   * @param os output stream to write generated class.
   * @param computeMax <tt>true</tt> if the maximum stack size and the maximum number of local
   *        variables must be automatically computed. This value is passed to {@link ClassWriter
   *        ClassWriter} instance.
   */
  public ASMContentHandler(final OutputStream os, final boolean computeMax) {
    this.os = os;
    this.computeMax = computeMax;
  }

  /**
   * Returns the bytecode of the class that was build with underneath class writer.
   *
   * @return the bytecode of the class that was build with underneath class writer or null if there
   *         are no classwriter created.
   */
  public byte[] toByteArray() {
    return cw == null ? null : cw.toByteArray();
  }

  /**
   * Process notification of the start of an XML element being reached.
   *
   * @param ns - The Namespace URI, or the empty string if the element has no Namespace URI or if
   *        Namespace processing is not being performed.
   * @param lName - The local name (without prefix), or the empty string if Namespace processing is
   *        not being performed.
   * @param qName - The qualified name (with prefix), or the empty string if qualified names are not
   *        available.
   * @param list - The attributes attached to the element. If there are no attributes, it shall be
   *        an empty Attributes object.
   * @exception SAXException if a parsing error is to be reported
   */
  @Override
  public final void startElement(final String ns, final String lName, final String qName, final Attributes list) throws SAXException {
    // the actual element name is either in lName or qName, depending
    // on whether the parser is namespace aware
    final String name = (lName == null) || (lName.length() == 0) ? qName : lName;

    // Compute the current matching rule
    final StringBuffer sb = new StringBuffer(match);
    if (match.length() > 0) {
      sb.append('/');
    }
    sb.append(name);
    match = sb.toString();

    // Fire "begin" events for all relevant rules
    final Rule r = (Rule)RULES.match(match);
    if (r != null) {
      r.begin(name, list);
    }
  }

  /**
   * Process notification of the end of an XML element being reached.
   *
   * @param ns - The Namespace URI, or the empty string if the element has no Namespace URI or if
   *        Namespace processing is not being performed.
   * @param lName - The local name (without prefix), or the empty string if Namespace processing is
   *        not being performed.
   * @param qName - The qualified XML 1.0 name (with prefix), or the empty string if qualified names
   *        are not available.
   *
   * @exception SAXException if a parsing error is to be reported
   */
  @Override
  public final void endElement(final String ns, final String lName, final String qName) throws SAXException {
    // the actual element name is either in lName or qName, depending
    // on whether the parser is namespace aware
    final String name = (lName == null) || (lName.length() == 0) ? qName : lName;

    // Fire "end" events for all relevant rules in reverse order
    final Rule r = (Rule)RULES.match(match);
    if (r != null) {
      r.end(name);
    }

    // Recover the previous match expression
    final int slash = match.lastIndexOf('/');
    if (slash >= 0) {
      match = match.substring(0, slash);
    }
    else {
      match = "";
    }
  }

  /**
   * Process notification of the end of a document and write generated bytecode into output stream.
   *
   * @exception SAXException if parsing or writing error is to be reported.
   */
  @Override
  public final void endDocument() throws SAXException {
    try {
      os.write(toByteArray());
    }
    catch (final IOException ex) {
      throw new SAXException(ex.toString(), ex);
    }
  }

  /**
   * Return the top object on the stack without removing it. If there are no objects on the stack,
   * return <code>null</code>.
   *
   * @return the top object on the stack without removing it.
   */
  final Object peek() {
    final int size = stack.size();
    return size == 0 ? null : stack.get(size - 1);
  }

  /**
   * Pop the top object off of the stack, and return it. If there are no objects on the stack,
   * return <code>null</code>.
   *
   * @return the top object off of the stack.
   */
  final Object pop() {
    final int size = stack.size();
    return size == 0 ? null : stack.remove(size - 1);
  }

  /**
   * Push a new object onto the top of the object stack.
   *
   * @param object The new object
   */
  final void push(final Object object) {
    stack.add(object);
  }

  static final class RuleSet {

    private final Map rules = new HashMap();

    private final List lpatterns = new ArrayList();

    private final List rpatterns = new ArrayList();

    public void add(final String path, final Object rule) {
      String pattern = path;
      if (path.startsWith("*/")) {
        pattern = path.substring(1);
        lpatterns.add(pattern);
      }
      else if (path.endsWith("/*")) {
        pattern = path.substring(0, path.length() - 1);
        rpatterns.add(pattern);
      }
      rules.put(pattern, rule);
    }

    public Object match(final String path) {
      if (rules.containsKey(path)) { return rules.get(path); }

      final int n = path.lastIndexOf('/');
      for (final Iterator it = lpatterns.iterator(); it.hasNext();) {
        final String pattern = (String)it.next();
        if (path.substring(n).endsWith(pattern)) { return rules.get(pattern); }
      }

      for (final Iterator it = rpatterns.iterator(); it.hasNext();) {
        final String pattern = (String)it.next();
        if (path.startsWith(pattern)) { return rules.get(pattern); }
      }

      return null;
    }
  }

  /**
   * Rule
   */
  protected abstract class Rule {

    public void begin(final String name, final Attributes attrs)
        throws SAXException {
    }

    public void end(final String name) {
    }

    protected final Object getValue(final String desc, final String val)
        throws SAXException {
      Object value = null;
      if (val != null) {
        if ("Ljava/lang/String;".equals(desc)) {
          value = decode(val);
        }
        else if ("Ljava/lang/Integer;".equals(desc)
            || "I".equals(desc) || "S".equals(desc)
            || "B".equals(desc) || "C".equals(desc)
            || "Z".equals(desc)) {
          value = new Integer(val);

        }
        else if ("Ljava/lang/Short;".equals(desc)) {
          value = new Short(val);

        }
        else if ("Ljava/lang/Byte;".equals(desc)) {
          value = new Byte(val);

        }
        else if ("Ljava/lang/Character;".equals(desc)) {
          value = new Character(decode(val).charAt(0));

        }
        else if ("Ljava/lang/Boolean;".equals(desc)) {
          value = Boolean.valueOf(val);

        }
        else if ("Ljava/lang/Long;".equals(desc) || "J".equals(desc)) {
          value = new Long(val);
        }
        else if ("Ljava/lang/Float;".equals(desc) || "F".equals(desc)) {
          value = new Float(val);
        }
        else if ("Ljava/lang/Double;".equals(desc)
            || "D".equals(desc)) {
          value = new Double(val);
        }
        else if (Type.getDescriptor(Type.class).equals(desc)) {
          value = Type.getType(val);

        }
        else {
          // TODO use of default toString().
          throw new SAXException("Invalid value:" + val + " desc:"
              + desc + " ctx:" + this);
        }
      }
      return value;
    }

    private final String decode(final String val) throws SAXException {
      final StringBuffer sb = new StringBuffer(val.length());
      try {
        int n = 0;
        while (n < val.length()) {
          char c = val.charAt(n);
          if (c == '\\') {
            n++;
            c = val.charAt(n);
            if (c == '\\') {
              sb.append('\\');
            }
            else {
              n++; // skip 'u'
              sb.append((char)Integer.parseInt(val.substring(n,
                  n + 4), 16));
              n += 3;
            }
          }
          else {
            sb.append(c);
          }
          n++;
        }

      }
      catch (final RuntimeException ex) {
        throw new SAXException(ex);
      }
      return sb.toString();
    }

    protected final Label getLabel(final Object label) {
      Label lbl = (Label)labels.get(label);
      if (lbl == null) {
        lbl = new Label();
        labels.put(label, lbl);
      }
      return lbl;
    }

    // TODO verify move to stack
    protected final MethodVisitor getCodeVisitor() {
      return (MethodVisitor)peek();
    }

    protected final int getAccess(final String s) {
      int access = 0;
      if (s.indexOf("public") != -1) {
        access |= Opcodes.ACC_PUBLIC;
      }
      if (s.indexOf("private") != -1) {
        access |= Opcodes.ACC_PRIVATE;
      }
      if (s.indexOf("protected") != -1) {
        access |= Opcodes.ACC_PROTECTED;
      }
      if (s.indexOf("static") != -1) {
        access |= Opcodes.ACC_STATIC;
      }
      if (s.indexOf("final") != -1) {
        access |= Opcodes.ACC_FINAL;
      }
      if (s.indexOf("super") != -1) {
        access |= Opcodes.ACC_SUPER;
      }
      if (s.indexOf("synchronized") != -1) {
        access |= Opcodes.ACC_SYNCHRONIZED;
      }
      if (s.indexOf("volatile") != -1) {
        access |= Opcodes.ACC_VOLATILE;
      }
      if (s.indexOf("bridge") != -1) {
        access |= Opcodes.ACC_BRIDGE;
      }
      if (s.indexOf("varargs") != -1) {
        access |= Opcodes.ACC_VARARGS;
      }
      if (s.indexOf("transient") != -1) {
        access |= Opcodes.ACC_TRANSIENT;
      }
      if (s.indexOf("native") != -1) {
        access |= Opcodes.ACC_NATIVE;
      }
      if (s.indexOf("interface") != -1) {
        access |= Opcodes.ACC_INTERFACE;
      }
      if (s.indexOf("abstract") != -1) {
        access |= Opcodes.ACC_ABSTRACT;
      }
      if (s.indexOf("strict") != -1) {
        access |= Opcodes.ACC_STRICT;
      }
      if (s.indexOf("synthetic") != -1) {
        access |= Opcodes.ACC_SYNTHETIC;
      }
      if (s.indexOf("annotation") != -1) {
        access |= Opcodes.ACC_ANNOTATION;
      }
      if (s.indexOf("enum") != -1) {
        access |= Opcodes.ACC_ENUM;
      }
      if (s.indexOf("deprecated") != -1) {
        access |= Opcodes.ACC_DEPRECATED;
      }
      return access;
    }
  }

  /**
   * ClassRule
   */
  final class ClassRule extends Rule {

    @Override
    public final void begin(final String name, final Attributes attrs) {
      final int major = Integer.parseInt(attrs.getValue("major"));
      final int minor = Integer.parseInt(attrs.getValue("minor"));
      cw = new ClassWriter(computeMax ? ClassWriter.COMPUTE_MAXS : 0);
      final Map vals = new HashMap();
      vals.put("version", new Integer((minor << 16) | major));
      vals.put("access", attrs.getValue("access"));
      vals.put("name", attrs.getValue("name"));
      vals.put("parent", attrs.getValue("parent"));
      vals.put("source", attrs.getValue("source"));
      vals.put("signature", attrs.getValue("signature"));
      vals.put("interfaces", new ArrayList());
      push(vals);
      // values will be extracted in InterfacesRule.end();
    }
  }

  final class SourceRule extends Rule {

    @Override
    public void begin(final String name, final Attributes attrs) {
      final String file = attrs.getValue("file");
      final String debug = attrs.getValue("debug");
      cw.visitSource(file, debug);
    }
  }

  /**
   * InterfaceRule
   */
  final class InterfaceRule extends Rule {

    @Override
    public final void begin(final String name, final Attributes attrs) {
      ((List)((HashMap)peek()).get("interfaces")).add(attrs.getValue("name"));
    }
  }

  /**
   * InterfacesRule
   */
  final class InterfacesRule extends Rule {

    @Override
    public final void end(final String element) {
      final Map vals = (Map)pop();
      final int version = ((Integer)vals.get("version")).intValue();
      final int access = getAccess((String)vals.get("access"));
      final String name = (String)vals.get("name");
      final String signature = (String)vals.get("signature");
      final String parent = (String)vals.get("parent");
      final List infs = (List)vals.get("interfaces");
      final String[] interfaces = (String[])infs.toArray(new String[infs.size()]);
      cw.visit(version, access, name, signature, parent, interfaces);
      push(cw);
    }
  }

  /**
   * OuterClassRule
   */
  final class OuterClassRule extends Rule {

    @Override
    public final void begin(final String element, final Attributes attrs) {
      final String owner = attrs.getValue("owner");
      final String name = attrs.getValue("name");
      final String desc = attrs.getValue("desc");
      cw.visitOuterClass(owner, name, desc);
    }
  }

  /**
   * InnerClassRule
   */
  final class InnerClassRule extends Rule {

    @Override
    public final void begin(final String element, final Attributes attrs) {
      final int access = getAccess(attrs.getValue("access"));
      final String name = attrs.getValue("name");
      final String outerName = attrs.getValue("outerName");
      final String innerName = attrs.getValue("innerName");
      cw.visitInnerClass(name, outerName, innerName, access);
    }
  }

  /**
   * FieldRule
   */
  final class FieldRule extends Rule {

    @Override
    public final void begin(final String element, final Attributes attrs)
        throws SAXException {
      final int access = getAccess(attrs.getValue("access"));
      final String name = attrs.getValue("name");
      final String signature = attrs.getValue("signature");
      final String desc = attrs.getValue("desc");
      final Object value = getValue(desc, attrs.getValue("value"));
      push(cw.visitField(access, name, desc, signature, value));
    }

    @Override
    public void end(final String name) {
      ((FieldVisitor)pop()).visitEnd();
    }
  }

  /**
   * MethodRule
   */
  final class MethodRule extends Rule {

    @Override
    public final void begin(final String name, final Attributes attrs) {
      labels = new HashMap();
      final Map vals = new HashMap();
      vals.put("access", attrs.getValue("access"));
      vals.put("name", attrs.getValue("name"));
      vals.put("desc", attrs.getValue("desc"));
      vals.put("signature", attrs.getValue("signature"));
      vals.put("exceptions", new ArrayList());
      push(vals);
      // values will be extracted in ExceptionsRule.end();
    }

    @Override
    public final void end(final String name) {
      ((MethodVisitor)pop()).visitEnd();
      labels = null;
    }
  }

  /**
   * ExceptionRule
   */
  final class ExceptionRule extends Rule {

    @Override
    public final void begin(final String name, final Attributes attrs) {
      ((List)((HashMap)peek()).get("exceptions")).add(attrs.getValue("name"));
    }
  }

  /**
   * ExceptionsRule
   */
  final class ExceptionsRule extends Rule {

    @Override
    public final void end(final String element) {
      final Map vals = (Map)pop();
      final int access = getAccess((String)vals.get("access"));
      final String name = (String)vals.get("name");
      final String desc = (String)vals.get("desc");
      final String signature = (String)vals.get("signature");
      final List excs = (List)vals.get("exceptions");
      final String[] exceptions = (String[])excs.toArray(new String[excs.size()]);

      push(cw.visitMethod(access, name, desc, signature, exceptions));
    }
  }

  /**
   * TableSwitchRule
   */
  class TableSwitchRule extends Rule {

    @Override
    public final void begin(final String name, final Attributes attrs) {
      final Map vals = new HashMap();
      vals.put("min", attrs.getValue("min"));
      vals.put("max", attrs.getValue("max"));
      vals.put("dflt", attrs.getValue("dflt"));
      vals.put("labels", new ArrayList());
      push(vals);
    }

    @Override
    public final void end(final String name) {
      final Map vals = (Map)pop();
      final int min = Integer.parseInt((String)vals.get("min"));
      final int max = Integer.parseInt((String)vals.get("max"));
      final Label dflt = getLabel(vals.get("dflt"));
      final List lbls = (List)vals.get("labels");
      final Label[] labels = (Label[])lbls.toArray(new Label[lbls.size()]);
      getCodeVisitor().visitTableSwitchInsn(min, max, dflt, labels);
    }
  }

  /**
   * TableSwitchLabelRule
   */
  final class TableSwitchLabelRule extends Rule {

    @Override
    public final void begin(final String name, final Attributes attrs) {
      ((List)((HashMap)peek()).get("labels")).add(getLabel(attrs.getValue("name")));
    }
  }

  /**
   * LookupSwitchRule
   */
  final class LookupSwitchRule extends Rule {

    @Override
    public final void begin(final String name, final Attributes attrs) {
      final Map vals = new HashMap();
      vals.put("dflt", attrs.getValue("dflt"));
      vals.put("labels", new ArrayList());
      vals.put("keys", new ArrayList());
      push(vals);
    }

    @Override
    public final void end(final String name) {
      final Map vals = (Map)pop();
      final Label dflt = getLabel(vals.get("dflt"));
      final List keyList = (List)vals.get("keys");
      final List lbls = (List)vals.get("labels");
      final Label[] labels = (Label[])lbls.toArray(new Label[lbls.size()]);
      final int[] keys = new int[keyList.size()];
      for (int i = 0; i < keys.length; i++) {
        keys[i] = Integer.parseInt((String)keyList.get(i));
      }
      getCodeVisitor().visitLookupSwitchInsn(dflt, keys, labels);
    }
  }

  /**
   * LookupSwitchLabelRule
   */
  final class LookupSwitchLabelRule extends Rule {

    @Override
    public final void begin(final String name, final Attributes attrs) {
      final Map vals = (Map)peek();
      ((List)vals.get("labels")).add(getLabel(attrs.getValue("name")));
      ((List)vals.get("keys")).add(attrs.getValue("key"));
    }
  }

  /**
   * FrameRule
   */
  final class FrameRule extends Rule {

    @Override
    public void begin(final String name, final Attributes attrs) {
      final Map typeLists = new HashMap();
      typeLists.put("local", new ArrayList());
      typeLists.put("stack", new ArrayList());
      push(attrs.getValue("type"));
      push(attrs.getValue("count") == null
          ? "0"
          : attrs.getValue("count"));
      push(typeLists);
    }

    @Override
    public void end(final String name) {
      final Map typeLists = (Map)pop();
      final List locals = (List)typeLists.get("local");
      final int nLocal = locals.size();
      final Object[] local = locals.toArray();
      final List stacks = (List)typeLists.get("stack");
      final int nStack = stacks.size();
      final Object[] stack = stacks.toArray();
      final String count = (String)pop();
      final String type = (String)pop();
      if ("NEW".equals(type)) {
        getCodeVisitor().visitFrame(Opcodes.F_NEW,
            nLocal,
            local,
            nStack,
            stack);
      }
      else if ("FULL".equals(type)) {
        getCodeVisitor().visitFrame(Opcodes.F_FULL,
            nLocal,
            local,
            nStack,
            stack);
      }
      else if ("APPEND".equals(type)) {
        getCodeVisitor().visitFrame(Opcodes.F_APPEND,
            nLocal,
            local,
            0,
            null);
      }
      else if ("CHOP".equals(type)) {
        getCodeVisitor().visitFrame(Opcodes.F_CHOP,
            Integer.parseInt(count),
            null,
            0,
            null);
      }
      else if ("SAME".equals(type)) {
        getCodeVisitor().visitFrame(Opcodes.F_SAME, 0, null, 0, null);
      }
      else if ("SAME1".equals(type)) {
        getCodeVisitor().visitFrame(Opcodes.F_SAME1,
            0,
            null,
            nStack,
            stack);
      }
    }
  }

  final class FrameTypeRule extends Rule {

    @Override
    public void begin(final String name, final Attributes attrs) {
      final List types = (List)((HashMap)peek()).get(name);
      final String type = attrs.getValue("type");
      if ("uninitialized".equals(type)) {
        types.add(getLabel(attrs.getValue("label")));
      }
      else {
        final Integer t = (Integer)ASMContentHandler.TYPES.get(type);
        if (t == null) {
          types.add(type);
        }
        else {
          types.add(t);
        }
      }
    }
  }

  /**
   * LabelRule
   */
  final class LabelRule extends Rule {

    @Override
    public final void begin(final String name, final Attributes attrs) {
      getCodeVisitor().visitLabel(getLabel(attrs.getValue("name")));
    }
  }

  /**
   * TryCatchRule
   */
  final class TryCatchRule extends Rule {

    @Override
    public final void begin(final String name, final Attributes attrs) {
      final Label start = getLabel(attrs.getValue("start"));
      final Label end = getLabel(attrs.getValue("end"));
      final Label handler = getLabel(attrs.getValue("handler"));
      final String type = attrs.getValue("type");
      getCodeVisitor().visitTryCatchBlock(start, end, handler, type);
    }
  }

  /**
   * LineNumberRule
   */
  final class LineNumberRule extends Rule {

    @Override
    public final void begin(final String name, final Attributes attrs) {
      final int line = Integer.parseInt(attrs.getValue("line"));
      final Label start = getLabel(attrs.getValue("start"));
      getCodeVisitor().visitLineNumber(line, start);
    }
  }

  /**
   * LocalVarRule
   */
  final class LocalVarRule extends Rule {

    @Override
    public final void begin(final String element, final Attributes attrs) {
      final String name = attrs.getValue("name");
      final String desc = attrs.getValue("desc");
      final String signature = attrs.getValue("signature");
      final Label start = getLabel(attrs.getValue("start"));
      final Label end = getLabel(attrs.getValue("end"));
      final int var = Integer.parseInt(attrs.getValue("var"));
      getCodeVisitor().visitLocalVariable(name,
          desc,
          signature,
          start,
          end,
          var);
    }
  }

  /**
   * OpcodesRule
   */
  final class OpcodesRule extends Rule {

    // public boolean match( String match, String element) {
    // return match.startsWith( path) && OPCODES.containsKey( element);
    // }

    @Override
    public final void begin(final String element, final Attributes attrs)
        throws SAXException {
      final Opcode o = (Opcode)ASMContentHandler.OPCODES.get(element);
      if (o == null) { throw new SAXException("Invalid element: " + element + " at "
          + match); }

      switch (o.type) {
        case OpcodeGroup.INSN:
          getCodeVisitor().visitInsn(o.opcode);
          break;

        case OpcodeGroup.INSN_FIELD:
          getCodeVisitor().visitFieldInsn(o.opcode,
              attrs.getValue("owner"),
              attrs.getValue("name"),
              attrs.getValue("desc"));
          break;

        case OpcodeGroup.INSN_INT:
          getCodeVisitor().visitIntInsn(o.opcode,
              Integer.parseInt(attrs.getValue("value")));
          break;

        case OpcodeGroup.INSN_JUMP:
          getCodeVisitor().visitJumpInsn(o.opcode,
              getLabel(attrs.getValue("label")));
          break;

        case OpcodeGroup.INSN_METHOD:
          getCodeVisitor().visitMethodInsn(o.opcode,
              (o.opcode != Opcodes.INVOKEDYNAMIC) ? attrs.getValue("owner") : Opcodes.INVOKEDYNAMIC_OWNER,
              attrs.getValue("name"),
              attrs.getValue("desc"));
          break;

        case OpcodeGroup.INSN_TYPE:
          getCodeVisitor().visitTypeInsn(o.opcode,
              attrs.getValue("desc"));
          break;

        case OpcodeGroup.INSN_VAR:
          getCodeVisitor().visitVarInsn(o.opcode,
              Integer.parseInt(attrs.getValue("var")));
          break;

        case OpcodeGroup.INSN_IINC:
          getCodeVisitor().visitIincInsn(Integer.parseInt(attrs.getValue("var")),
              Integer.parseInt(attrs.getValue("inc")));
          break;

        case OpcodeGroup.INSN_LDC:
          getCodeVisitor().visitLdcInsn(getValue(attrs.getValue("desc"),
              attrs.getValue("cst")));
          break;

        case OpcodeGroup.INSN_MULTIANEWARRAY:
          getCodeVisitor().visitMultiANewArrayInsn(attrs.getValue("desc"),
              Integer.parseInt(attrs.getValue("dims")));
          break;

        default:
          throw new Error("Internal error");

      }
    }
  }

  /**
   * MaxRule
   */
  final class MaxRule extends Rule {

    @Override
    public final void begin(final String element, final Attributes attrs) {
      final int maxStack = Integer.parseInt(attrs.getValue("maxStack"));
      final int maxLocals = Integer.parseInt(attrs.getValue("maxLocals"));
      getCodeVisitor().visitMaxs(maxStack, maxLocals);
    }
  }

  final class AnnotationRule extends Rule {

    @Override
    public void begin(final String name, final Attributes attrs) {
      final String desc = attrs.getValue("desc");
      final boolean visible = Boolean.valueOf(attrs.getValue("visible"))
          .booleanValue();

      final Object v = peek();
      if (v instanceof ClassVisitor) {
        push(((ClassVisitor)v).visitAnnotation(desc, visible));
      }
      else if (v instanceof FieldVisitor) {
        push(((FieldVisitor)v).visitAnnotation(desc, visible));
      }
      else if (v instanceof MethodVisitor) {
        push(((MethodVisitor)v).visitAnnotation(desc, visible));
      }
    }

    @Override
    public void end(final String name) {
      final AnnotationVisitor av = (AnnotationVisitor)pop();
      if (av != null) {
        av.visitEnd();
      }
    }
  }

  final class AnnotationParameterRule extends Rule {

    @Override
    public void begin(final String name, final Attributes attrs) {
      final int parameter = Integer.parseInt(attrs.getValue("parameter"));
      final String desc = attrs.getValue("desc");
      final boolean visible = Boolean.valueOf(attrs.getValue("visible"))
          .booleanValue();

      push(((MethodVisitor)peek()).visitParameterAnnotation(parameter,
          desc,
          visible));
    }

    @Override
    public void end(final String name) {
      final AnnotationVisitor av = (AnnotationVisitor)pop();
      if (av != null) {
        av.visitEnd();
      }
    }
  }

  final class AnnotationValueRule extends Rule {

    @Override
    public void begin(final String nm, final Attributes attrs)
        throws SAXException {
      final AnnotationVisitor av = (AnnotationVisitor)peek();
      if (av != null) {
        av.visit(attrs.getValue("name"),
            getValue(attrs.getValue("desc"),
                attrs.getValue("value")));
      }
    }
  }

  final class AnnotationValueEnumRule extends Rule {

    @Override
    public void begin(final String nm, final Attributes attrs) {
      final AnnotationVisitor av = (AnnotationVisitor)peek();
      if (av != null) {
        av.visitEnum(attrs.getValue("name"),
            attrs.getValue("desc"),
            attrs.getValue("value"));
      }
    }
  }

  final class AnnotationValueAnnotationRule extends Rule {

    @Override
    public void begin(final String nm, final Attributes attrs) {
      final AnnotationVisitor av = (AnnotationVisitor)peek();
      push(av == null ? null
          : av.visitAnnotation(attrs.getValue("name"),
              attrs.getValue("desc")));
    }

    @Override
    public void end(final String name) {
      final AnnotationVisitor av = (AnnotationVisitor)pop();
      if (av != null) {
        av.visitEnd();
      }
    }
  }

  final class AnnotationValueArrayRule extends Rule {

    @Override
    public void begin(final String nm, final Attributes attrs) {
      final AnnotationVisitor av = (AnnotationVisitor)peek();
      push(av == null ? null : av.visitArray(attrs.getValue("name")));
    }

    @Override
    public void end(final String name) {
      final AnnotationVisitor av = (AnnotationVisitor)pop();
      if (av != null) {
        av.visitEnd();
      }
    }
  }

  final class AnnotationDefaultRule extends Rule {

    @Override
    public void begin(final String nm, final Attributes attrs) {
      final MethodVisitor av = (MethodVisitor)peek();
      push(av == null ? null : av.visitAnnotationDefault());
    }

    @Override
    public void end(final String name) {
      final AnnotationVisitor av = (AnnotationVisitor)pop();
      if (av != null) {
        av.visitEnd();
      }
    }
  }

  /**
   * Opcode
   */
  static final class Opcode {

    public final int opcode;

    public final int type;

    Opcode(final int opcode, final int type) {
      this.opcode = opcode;
      this.type = type;
    }
  }
}
