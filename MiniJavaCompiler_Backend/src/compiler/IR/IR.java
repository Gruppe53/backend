package compiler.IR;

import java.util.HashSet;

import compiler.IR.support.MJClassTable;
import compiler.IR.support.MJScopeStack;

public class IR {

	public MJProgram program;

	public static MJClassTable classes = new MJClassTable();
	public static MJScopeStack stack = new MJScopeStack();
	public static MJMethod currentMethod = null;
	public static MJClass currentClass = null;
	
	public IR() {
	}

	public IR(MJProgram program) {
		this.program = program;
	}

	public MJProgram getProgram() {
		return program;
	}

}
