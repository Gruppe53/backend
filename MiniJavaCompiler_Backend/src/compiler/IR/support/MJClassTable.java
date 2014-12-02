package compiler.IR.support;

import java.util.HashMap;

import compiler.Exceptions.*;
import compiler.IR.MJClass;

public class MJClassTable {
	public HashMap<String, MJClass> map = new HashMap<String, MJClass>();
	private HashMap<MJClass, MJSymbolTable> fieldmap = new HashMap<MJClass, MJSymbolTable>();
	private HashMap<MJClass, MJMethodTable> methodMap = new HashMap<MJClass, MJMethodTable>();

	public MJClassTable() {
		
	}

	public MJClass lookup(String name) throws ClassNotFound {
		MJClass decl = (MJClass) map.get(name);
		if (decl == null) {
			throw new ClassNotFound(name);
		}
		return decl;
	}
}
