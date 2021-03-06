package compiler.Phases;

import compiler.CODE.CODE;
import compiler.Exceptions.CodeGenException;
import compiler.IR.*;

public class Backend {

	public static void generate(IR ir) throws CodeGenException {
		CODE code = new CODE();
		CodeGen.check(ir, code);
		code.dump();
	}
}
