package nl.johannisk.finalizer.processor;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.tree.JCTree;

/**
 * Visitor for {@link nl.johannisk.finalizer.annotation.FinalizeVars} and {@link nl.johannisk.finalizer.annotation.MutableVar} annotataions.
 * Variables in Types annotated with {@link nl.johannisk.finalizer.annotation.FinalizeVars} are amended with the final modifier
 * unless annotated with the {@link nl.johannisk.finalizer.annotation.MutableVar} annotation or if they are already volatile.
 */

public class FinalizerTranslator extends TreeTranslator {

    private final TreeMaker treeMaker;
    private boolean shouldVisitVarDefinitions = false;

    /**
     * Initialises the FinalTranslator with a {@link TreeMaker}.
     * @param treeMaker a TreeMaker
     */
    public FinalizerTranslator(final TreeMaker treeMaker) {
        this.treeMaker = treeMaker;
    }

    /**
     * Visits the class definition and checks if the (@link {@link nl.johannisk.finalizer.annotation.FinalizeVars} annotation is present.
     * @param classDeclaration a class declaration
     */
    @Override
    public void visitClassDef(final JCTree.JCClassDecl classDeclaration) {
        if (isFinalizeVarsAnnotation(classDeclaration.getModifiers())) {
            shouldVisitVarDefinitions = true;
        }
        super.visitClassDef(classDeclaration);
    }

    /**
     * Visits the variable definitions and finalises it unless the {@link nl.johannisk.finalizer.annotation.MutableVar} annotation is present.
     * @param variableDeclaration a variable declaration
     */
    @Override
    public void visitVarDef(final JCTree.JCVariableDecl variableDeclaration) {
        super.visitVarDef(variableDeclaration);
        JCTree.JCModifiers modifiers = variableDeclaration.getModifiers();
        if (shouldBeMadeFinal(variableDeclaration, modifiers)) {
            variableDeclaration.mods = treeMaker.Modifiers(variableDeclaration.mods.flags | Flags.FINAL);
        }
        this.result = variableDeclaration;
    }

    private boolean shouldBeMadeFinal(final JCTree.JCVariableDecl variableDeclaration, final JCTree.JCModifiers modifiers) {
        return  !isMutableVarAnnotation(modifiers) &&
                !isVolatile(variableDeclaration.getModifiers()) &&
                shouldVisitVarDefinitions;
    }

    private boolean isMutableVarAnnotation(final JCTree.JCModifiers modifiers) {
        return modifiers.toString().contains("@MutableVar()");
    }

    private boolean isFinalizeVarsAnnotation(final JCTree.JCModifiers modifiers) {
        return modifiers.toString().contains("@FinalizeVars()");
    }

    private boolean isVolatile(final JCTree.JCModifiers modifiers) {
        return (modifiers.flags & Flags.VOLATILE) > 0;
    }
}
