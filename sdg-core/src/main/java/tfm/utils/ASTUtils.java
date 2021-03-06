package tfm.utils;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.*;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class ASTUtils {

    public static BlockStmt blockWrapper(Statement statement) {
        if (statement.isBlockStmt())
            return statement.asBlockStmt();

        return new BlockStmt(new NodeList<>(statement));
    }

    public static boolean isLoop(Statement statement) {
        return statement.isWhileStmt()
                || statement.isDoStmt()
                || statement.isForStmt()
                || statement.isForEachStmt();
    }

    public static Statement findFirstAncestorStatementFrom(Statement statement, Predicate<Statement> predicate) {
        if (predicate.test(statement)) {
            return statement;
        }

        if (!statement.getParentNode().isPresent()) {
            return new EmptyStmt();
        }

        return findFirstAncestorStatementFrom((Statement) statement.getParentNode().get(), predicate);
    }

    /**
     * Clones an entire AST by cloning the root node of the given node
     * @param node - a node of the AST
     * @return the root node of the cloned AST
     */
    public static Node cloneAST(Node node) {
        return node.findRootNode().clone();
    }

    public static boolean isContained(Node upper, Node contained) {
        Optional<Node> optionalParent = contained.getParentNode();

        if (!optionalParent.isPresent()) {
            return false;
        }

        Node parent = optionalParent.get();

        return Objects.equals(parent, upper) || isContained(upper, parent);
    }

    public static boolean switchHasDefaultCase(SwitchStmt stmt) {
        return switchGetDefaultCase(stmt) != null;
    }

    public static SwitchEntryStmt switchGetDefaultCase(SwitchStmt stmt) {
        for (SwitchEntryStmt entry : stmt.getEntries())
            if (!entry.getLabel().isPresent())
                return entry;
        return null;
    }

    public static boolean equalsWithRange(Node n1, Node n2) {
        return Objects.equals(n1.getRange(), n2.getRange()) && Objects.equals(n1, n2);
    }

    public static boolean equalsWithRangeInCU(Node n1, Node n2) {
        // Find the compilation unit of each node
        Optional<CompilationUnit> optionalCompilationUnit1 = n1.findCompilationUnit();
        Optional<CompilationUnit> optionalCompilationUnit2 = n2.findCompilationUnit();

        // If they are inside the same compilation unit, compare with range
        if (optionalCompilationUnit1.isPresent() && optionalCompilationUnit2.isPresent()) {
            return Objects.equals(optionalCompilationUnit1.get(), optionalCompilationUnit2.get())
                    && equalsWithRange(n1, n2);
        }

        // If not, just compare with range
        return equalsWithRange(n1, n2);
    }
}
