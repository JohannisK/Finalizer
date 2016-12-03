package nl.johannisk.finalizer.processor;

import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * AnnotationProcesser for {@link nl.johannisk.finalizer.annotation.FinalizeVars} and {@link nl.johannisk.finalizer.annotation.MutableVar} annotations.
 */

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"nl.johannisk.finalizer.annotation.FinalizeVars",
        "nl.johannisk.finalizer.annotation.MutableVar"})
public class FinalizerProcessor extends AbstractProcessor {
    private Trees trees;
    private TreeMaker treeMaker;

    /**
     * Initialize the FinalizerProcessor.
     * @param processingEnvironment The Java processing environment
     */
    @Override
    public void init(final ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        final JavacProcessingEnvironment javacProcessingEnvironment =
                (JavacProcessingEnvironment) processingEnvironment;
        this.trees = Trees.instance(processingEnvironment);
        this.treeMaker =
                TreeMaker.instance(javacProcessingEnvironment.getContext());
    }

    /**
     * Process the current processing round in the compilation process.
     * @param annotations
     * @param roundEnvironment The current processing round environment
     */
    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
                           final RoundEnvironment roundEnvironment) {
        if (!roundEnvironment.processingOver()) {
            processRootElements(roundEnvironment.getRootElements());
        }
        return false;
    }

    private void processRootElements(final Set<? extends Element> rootElements) {
        rootElements.forEach(this::processRootElement);
    }

    private void processRootElement(final Element element) {
        JCTree tree = (JCTree) trees.getTree(element);
        tree.accept(new FinalizerTranslator(treeMaker));
    }
}
