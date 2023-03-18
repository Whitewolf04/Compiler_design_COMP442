package table_generator;

import AST_generator.SyntaxTreeNode;

public abstract class Visitor {
    abstract public void visit(SyntaxTreeNode node);
}
