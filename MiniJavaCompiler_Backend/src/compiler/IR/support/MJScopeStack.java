package compiler.IR.support;

import java.util.Stack;

public class MJScopeStack {

	private Stack<MJSymbolTable> stack = new Stack<MJSymbolTable>();
	public Stack<MJSymbolTable> getStack() { return this.stack; }
}
