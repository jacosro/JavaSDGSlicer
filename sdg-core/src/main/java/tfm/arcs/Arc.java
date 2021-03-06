package tfm.arcs;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.io.Attribute;
import tfm.arcs.cfg.ControlFlowArc;
import tfm.arcs.pdg.ConditionalControlDependencyArc;
import tfm.arcs.pdg.ControlDependencyArc;
import tfm.arcs.pdg.DataDependencyArc;
import tfm.arcs.sdg.CallArc;
import tfm.arcs.sdg.ParameterInOutArc;
import tfm.arcs.sdg.SummaryArc;
import tfm.nodes.GraphNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Arc extends DefaultEdge {

    private String label;

    protected Arc() {
    }

    protected Arc(String label) {
        this.label = label;
    }

    /** @see ControlFlowArc */
    public final boolean isControlFlowArc() {
        return this instanceof ControlFlowArc;
    }

    public final ControlFlowArc asControlFlowArc() {
        if (isControlFlowArc())
            return (ControlFlowArc) this;
        throw new UnsupportedOperationException("Not a ControlFlowArc");
    }

    /** @see ControlFlowArc.NonExecutable */
    public final boolean isExecutableControlFlowArc() {
        return isControlFlowArc() && !isNonExecutableControlFlowArc();
    }

    /** @see ControlFlowArc.NonExecutable */
    public final boolean isNonExecutableControlFlowArc() {
        return this instanceof ControlFlowArc.NonExecutable;
    }

    /** @see ControlDependencyArc */
    public final boolean isControlDependencyArc() {
        return this instanceof ControlDependencyArc;
    }

    public final ControlDependencyArc asControlDependencyArc() {
        if (isControlDependencyArc())
            return (ControlDependencyArc) this;
        throw new UnsupportedOperationException("Not a ControlDependencyArc");
    }

    /** @see DataDependencyArc */
    public final boolean isDataDependencyArc() {
        return this instanceof DataDependencyArc;
    }

    public final DataDependencyArc asDataDependencyArc() {
        if (isDataDependencyArc())
            return (DataDependencyArc) this;
        throw new UnsupportedOperationException("Not a DataDependencyArc");
    }

    public boolean isInterproceduralInputArc() {
        return false;
    }

    public boolean isInterproceduralOutputArc() {
        return false;
    }

    /** @see CallArc */
    public final boolean isCallArc() {
        return this instanceof CallArc;
    }

    public final CallArc asCallArc() {
        if (isCallArc())
            return (CallArc) this;
        throw new UnsupportedOperationException("Not a CallArc");
    }

    /** @see ParameterInOutArc */
    public final boolean isParameterInOutArc() {
        return this instanceof ParameterInOutArc;
    }

    public final ParameterInOutArc asParameterInOutArc() {
        if (isParameterInOutArc())
            return (ParameterInOutArc) this;
        throw new UnsupportedOperationException("Not a ParameterInOutArc");
    }

    /** @see SummaryArc */
    public final boolean isSummaryArc() {
        return this instanceof SummaryArc;
    }

    public final SummaryArc asSummaryArcArc() {
        if (isSummaryArc())
            return (SummaryArc) this;
        throw new UnsupportedOperationException("Not a SummaryArc");
    }

    /** @see ConditionalControlDependencyArc */
    public final boolean isConditionalControlDependencyArc() {
        return this instanceof ConditionalControlDependencyArc;
    }

    public final boolean isUnconditionalControlDependencyArc() {
        return isControlDependencyArc() && !isConditionalControlDependencyArc();
    }

    public final ControlDependencyArc asConditionalControlDependencyArc() {
        if (isConditionalControlDependencyArc())
            return (ConditionalControlDependencyArc) this;
        throw new UnsupportedOperationException("Not a ConditionalControlDependencyArc");
    }

    @Override
    public String toString() {
        return String.format("%s{%d -> %d}", getClass().getName(),
                ((GraphNode<?>) getSource()).getId(), ((GraphNode<?>) getTarget()).getId());
    }

    public Map<String, Attribute> getDotAttributes() {
        return new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (!o.getClass().equals(this.getClass()))
            return false;
        return Objects.equals(getSource(), ((Arc) o).getSource())
                && Objects.equals(getTarget(), ((Arc) o).getTarget())
                && Objects.equals(getLabel(), ((Arc) o).getLabel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), getLabel(), getSource(), getTarget());
    }

    public String getLabel() {
        return label;
    }
}
