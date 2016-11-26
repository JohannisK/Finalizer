package nl.johannisk.finalizer.processor;

import com.sun.tools.javac.tree.*;

/**
 * Created by johankragt on 25/11/2016.
 */
public class FinalizerTranslator extends TreeTranslator {

    final TreeMaker treeMaker;
    boolean visitVarDefinitions = false;

    public FinalizerTranslator (final TreeMaker treeMaker) {
        this.treeMaker = treeMaker;
    }

    @Override
    public void visitClassDef(JCTree.JCClassDecl var1) {
        if(isFinalizeVarsAnnotation(var1.getModifiers())) {
            visitVarDefinitions = true;
        }
        super.visitClassDef(var1);
    }

    @Override
    public void visitVarDef(JCTree.JCVariableDecl variableDeclaration) {
        super.visitVarDef(variableDeclaration);
        if (!isMutableVarAnnotation(variableDeclaration.getModifiers()) && !isAlreadyFinal(variableDeclaration.getModifiers()) && visitVarDefinitions) {
            JCTree.JCModifiers modifiers = treeMaker.Modifiers(variableDeclaration.mods.flags + 16);
            variableDeclaration.mods = modifiers;
            this.result = variableDeclaration;
        }
    }

    private boolean isMutableVarAnnotation(JCTree.JCModifiers modifiers) {
        return modifiers.toString().contains("@MutableVar()");
    }

    private boolean isFinalizeVarsAnnotation(JCTree.JCModifiers modifiers) {
        return modifiers.toString().contains("@FinalizeVars()");
    }

    private boolean isAlreadyFinal(JCTree.JCModifiers modifiers) {
        return modifiers.toString().contains("final");
    }
}
