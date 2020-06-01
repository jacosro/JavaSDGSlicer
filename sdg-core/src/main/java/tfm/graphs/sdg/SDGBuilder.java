package tfm.graphs.sdg;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tfm.arcs.Arc;
import tfm.graphs.pdg.PDG;
import tfm.graphs.sdg.sumarcs.NaiveSummaryArcsBuilder;
import tfm.graphs.sdg.sumarcs.SummaryArcsBuilder;
import tfm.nodes.GraphNode;
import tfm.nodes.TypeNodeFactory;
import tfm.nodes.type.NodeType;
import tfm.utils.Context;

class SDGBuilder extends VoidVisitorAdapter<Context> {

    SDG sdg;

    public SDGBuilder(SDG sdg) {
        this.sdg = sdg;
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration, Context context) {
        if (!methodDeclaration.getBody().isPresent()) {
            return;
        }

        context.setCurrentMethod(methodDeclaration);

        // Build PDG and add to SDGGraph
        PDG pdg = new PDG();
        pdg.build(methodDeclaration);

        assert pdg.isBuilt();
        assert pdg.getRootNode().isPresent();

        // Add all nodes from PDG to SDG
        for (GraphNode<?> node : pdg.vertexSet()) {
            sdg.addNode(node);
        }

        // Add all arcs from PDG to SDG
        for (Arc arc : pdg.edgeSet()) {
            if (arc.isControlDependencyArc()) {
                sdg.addControlDependencyArc(pdg.getEdgeSource(arc), pdg.getEdgeTarget(arc));
            } else {
                sdg.addDataDependencyArc(pdg.getEdgeSource(arc), pdg.getEdgeTarget(arc), arc.getLabel());
            }
        }

        GraphNode<MethodDeclaration> methodDeclarationNode = pdg.getRootNode().get();

        // Add CFG
        sdg.setMethodCFG(methodDeclaration, pdg.getCfg());

        // Add output node
        if (methodDeclaration.getType().isVoidType()) {
            // If method return type is void, do nothing
            return;
        }

        GraphNode<EmptyStmt> outputNode = sdg.addNode("output", new EmptyStmt(), TypeNodeFactory.fromType(NodeType.METHOD_OUTPUT));

        sdg.addControlDependencyArc(methodDeclarationNode, outputNode);

        // Add return arc from all return statements to the output node
        pdg.getCfg().vertexSet().stream()
                .filter(node -> node.getAstNode() instanceof ReturnStmt)
                .map(node -> (GraphNode<ReturnStmt>) node)
                .forEach(node -> sdg.addDataDependencyArc(node, outputNode));
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Context context) {
//        if (sdgGraph.getRootNode() != null) {
//            throw new IllegalStateException("¡Solo podemos procesar una clase por el momento!");
//        }

        if (classOrInterfaceDeclaration.isInterface()) {
            throw new IllegalArgumentException("¡Las interfaces no estan permitidas!");
        }

        context.setCurrentClass(classOrInterfaceDeclaration);

        classOrInterfaceDeclaration.getMembers().accept(this, context);

        // Once every PDG is built, expand method call nodes of each one
        // and link them to the corresponding method declaration node
        MethodCallReplacer methodCallReplacer = new MethodCallReplacer(sdg);
        methodCallReplacer.replace(context);



        // 3. Build summary arcs
        SummaryArcsBuilder summaryArcsBuilder = new NaiveSummaryArcsBuilder(sdg);
        summaryArcsBuilder.visit();
    }

    @Override
    public void visit(CompilationUnit compilationUnit, Context context) {
        context.setCurrentCU(compilationUnit);

        super.visit(compilationUnit, context);
    }
}