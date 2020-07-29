package tfm.nodes.exceptionsensitive;

import com.github.javaparser.ast.body.CallableDeclaration;

/** A node that represents the exit of a declaration, serving the function of 'Exit' in the initial CFG. */
public class NormalExitNode extends ExitNode {
    public NormalExitNode(CallableDeclaration<?> astNode) {
        super("normal exit", astNode);
    }
}