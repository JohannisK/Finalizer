package nl.johannisk.finalizer.processor;

import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.SourceVersion;
import java.util.Set;

/**
 * Created by johankragt on 25/11/2016.
 */

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class FinalizerProcessor extends AbstractProcessor {
    private Trees trees;
    private TreeTranslator visitor;

    @Override
    public void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        JavacProcessingEnvironment javacProcessingEnvironment = (JavacProcessingEnvironment)processingEnvironment;
        this.trees = Trees.instance(processingEnvironment);
        TreeMaker treeMaker = TreeMaker.instance(javacProcessingEnvironment.getContext());
        visitor = new FinalizerTranslator(treeMaker);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (!roundEnvironment.processingOver()) {
            Set<? extends Element> elements = roundEnvironment.getRootElements();
            elements.forEach(element -> {
                JCTree tree = (JCTree) trees.getTree(element);
                tree.accept(visitor);
            });
        }
        return true;
    }

}
