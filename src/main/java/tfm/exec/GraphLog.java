package tfm.exec;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import tfm.graphs.Graph;
import tfm.utils.Logger;

import java.io.IOException;
import java.util.Comparator;
import java.util.stream.Collectors;

public abstract class GraphLog<G extends Graph<?>, V extends VoidVisitor<?>> {

    static final String CFG = "cfg";
    static final String PDG = "pdg";
    static final String SDG = "sdg";

    G graph;
    V visitor;

    abstract void visit(Node node);


    void log() throws IOException {
        Logger.log(
                "****************************\n" +
                "*           GRAPH          *\n" +
                "****************************"
        );
        Logger.log(graph);
        Logger.log(
                "****************************\n" +
                "*         GRAPHVIZ         *\n" +
                "****************************"
        );
        Logger.log(graph.toGraphvizRepresentation());
        Logger.log();

        generatePNGs();
    }

    abstract void generatePNGs() throws IOException;

    abstract void openVisualRepresentation() throws IOException;
}