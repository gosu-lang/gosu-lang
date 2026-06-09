/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.lang.gosuc.simple;

import gw.internal.ext.org.objectweb.asm.*;
import gw.internal.ext.org.objectweb.asm.signature.SignatureReader;
import gw.internal.ext.org.objectweb.asm.signature.SignatureVisitor;

/**
 * Extracts the set of types referenced by a compiled Gosu class file and records each
 * as a {@code producer -> consumer} edge in the {@link IncrementalCompilationManager}'s
 * dependency graph, where the consumer is the class being visited.
 *
 * <h3>Two-phase walk</h3>
 *
 * The visitor runs in two phases against the same {@link ClassReader}:
 *
 * <ol>
 *   <li><b>Constant-pool scan</b> ({@link #collectClassDependenciesFromConstantPool},
 *       called from the constructor) -- a fast O(n) sweep over the constant pool that
 *       catches every {@code CONSTANT_Class} entry. This picks up references from
 *       method bodies (local variables, instantiations, casts, ...) without paying
 *       the cost of a full instruction-level visitor.</li>
 *   <li><b>Structural visit</b> ({@code reader.accept(visitor, SKIP_FRAMES)}, run by
 *       the caller in {@link IncrementalCompilationManager#trackDependencies}) -- the
 *       standard ASM {@link ClassVisitor} callbacks add signature-level type refs
 *       that the constant pool alone wouldn't surface (e.g. generic type parameters
 *       reachable only through the {@code Signature} attribute, annotation descriptors,
 *       local-variable signatures inside method bodies).</li>
 * </ol>
 *
 * Both phases route every discovered type through {@link #maybeAddDependentType}, which
 * filters via {@link IncrementalCompilationManager#shouldTrackType}. Duplicate
 * registrations of the same producer are harmless -- the manager's consumer set is a
 * {@code Set}.
 *
 * <p>The split is deliberate: each phase covers cases the other misses, and the
 * constant-pool scan is fast enough that the redundancy isn't a perf concern.
 */
public class DependenciesClassVisitor extends ClassVisitor {
    private static final int CONSTANT_CLASS_TAG = 7;
    private static final int ASM_API_VERSION = Opcodes.ASM5;

    private final IncrementalCompilationManager incrementalCompilationManager;
    private final String consumerFqcn;

    // TODO consider using a string interner here and in the IncrementalCompilationManager
    public DependenciesClassVisitor(ClassReader reader, IncrementalCompilationManager incrementalCompilationManager) {
        super(ASM_API_VERSION);
        this.consumerFqcn = getFqcn(reader.getClassName());
        this.incrementalCompilationManager = incrementalCompilationManager;
        // Mark consumerFqcn as present in this session's dependency tracking as producer.
        // The type will appear in the dep file even if no consumer relationships are
        // recorded for it. This is called  for every compiled type to maintain a
        // complete registry.
        incrementalCompilationManager.getOrCreateConsumerSet(consumerFqcn);
        collectClassDependenciesFromConstantPool(reader);
    }

    private static String getFqcn(String internalClassName) {
        return Type.getObjectType(internalClassName).getClassName();
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        maybeAddClassTypesFromSignature(signature);
        if (superName != null) {
            Type type = Type.getObjectType(superName);
            maybeAddDependentType(type);
        }
        if (interfaces != null) {
            for (String s : interfaces) {
                Type interfaceType = Type.getObjectType(s);
                maybeAddDependentType(interfaceType);
            }
        }
    }

    // Phase 1 of the two-phase walk (see class Javadoc).
    private void collectClassDependenciesFromConstantPool(ClassReader reader) {
        char[] charBuffer = new char[reader.getMaxStringLength()];
        for (int i = 1; i < reader.getItemCount(); i++) {
            int itemOffset = reader.getItem(i);
            // See the JVM Spec.
            if (itemOffset > 0 && reader.readByte(itemOffset - 1) == CONSTANT_CLASS_TAG) {
                // A CONSTANT_Class entry, read the class descriptor
                String classDescriptor = reader.readUTF8(itemOffset, charBuffer);
                Type type = Type.getObjectType(classDescriptor);
                maybeAddDependentType(type);
            }
        }
    }

    private void maybeAddClassTypesFromSignature(String signature) {
        if (signature != null) {
            SignatureReader signatureReader = new SignatureReader(signature);
            signatureReader.accept(new SignatureVisitor(ASM_API_VERSION) {
                @Override
                public void visitClassType(String className) {
                    Type type = Type.getObjectType(className);
                    maybeAddDependentType(type);
                }
            });
        }
    }

    protected void maybeAddDependentType(Type type) {
        while (type.getSort() == Type.ARRAY) {
            type = type.getElementType();
        }
        if (type.getSort() != Type.OBJECT) {
            return;
        }
        String producerFqcn = type.getClassName();

        if (incrementalCompilationManager.shouldTrackType(producerFqcn)) {
            incrementalCompilationManager.recordTypeDependency(producerFqcn, consumerFqcn);
        }
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        maybeAddClassTypesFromSignature(signature);
        maybeAddDependentType(Type.getType(desc));
        return new DepFieldVisitor();
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        maybeAddClassTypesFromSignature(signature);
        addTypesFromMethodDescriptor(desc);
        if (exceptions != null) {
            for (String s : exceptions) {
                Type exceptionType = Type.getObjectType(s);
                maybeAddDependentType(exceptionType);
            }
        }
        return new DepMethodVisitor();
    }

    private void addTypesFromMethodDescriptor(String desc) {
        Type methodType = Type.getMethodType(desc);
        maybeAddDependentType(methodType.getReturnType());
        for (Type argType : methodType.getArgumentTypes()) {
            maybeAddDependentType(argType);
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        maybeAddDependentType(Type.getType(desc));
        return new DepAnnotationVisitor();
    }

    private static boolean isAccessible(int access) {
        return (access & Opcodes.ACC_PRIVATE) == 0;
    }

    private class DepFieldVisitor extends FieldVisitor {
        public DepFieldVisitor() {
            super(ASM_API_VERSION);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            maybeAddDependentType(Type.getType(descriptor));
            return new DepAnnotationVisitor();
        }

        @Override
        public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
            maybeAddDependentType(Type.getType(descriptor));
            return new DepAnnotationVisitor();
        }
    }

    private class DepMethodVisitor extends MethodVisitor {
        protected DepMethodVisitor() {
            super(ASM_API_VERSION);
        }

        @Override
        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
            maybeAddClassTypesFromSignature(signature);
            maybeAddDependentType(Type.getType(desc));
            super.visitLocalVariable(name, desc, signature, start, end, index);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            maybeAddDependentType(Type.getType(descriptor));
            return new DepAnnotationVisitor();
        }

        @Override
        public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
            maybeAddDependentType(Type.getType(descriptor));
            return new DepAnnotationVisitor();
        }

        @Override
        public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
            maybeAddDependentType(Type.getType(descriptor));
            return new DepAnnotationVisitor();
        }

        @Override
        public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
            addTypesFromMethodDescriptor(descriptor);
            maybeAddDependentType(Type.getObjectType(bootstrapMethodHandle.getOwner()));

            for (Object arg : bootstrapMethodArguments) {
                addDependentTypeFromBootstrapMethodArgument(arg);
            }
        }

        private void addDependentTypeFromBootstrapMethodArgument(Object arg) {
            if (arg instanceof Type) {
                maybeAddDependentType((Type) arg);
            } else if (arg instanceof Handle) {
                maybeAddDependentType(Type.getObjectType(((Handle) arg).getOwner()));
            }
        }
    }

    private class DepAnnotationVisitor extends AnnotationVisitor {
        public DepAnnotationVisitor() {
            super(DependenciesClassVisitor.ASM_API_VERSION);
        }

        @Override
        public void visit(String name, Object value) {
            if (value instanceof Type) {
                maybeAddDependentType((Type) value);
            }
        }

        @Override
        public AnnotationVisitor visitArray(String name) {
            return this;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String name, String descriptor) {
            maybeAddDependentType(Type.getType(descriptor));
            return this;
        }
    }
}
