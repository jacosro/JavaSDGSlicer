package tfm.variables;

import tfm.nodes.Vertex;

public abstract class VariableAction<T> {

    private Vertex node;
    private T value;

    protected VariableAction(Vertex node, T value) {
        this.node = node;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Vertex getNode() {
        return node;
    }

    public void setNode(Vertex node) {
        this.node = node;
    }

    public abstract boolean isDeclaration();

    public abstract boolean isUse();
}
