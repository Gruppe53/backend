package compiler.IR.support;

import java.util.HashMap;
import java.util.HashSet;
import compiler.IR.MJMethod;

public class MJMethodTable {
	private HashMap<String, HashSet<MJMethod>> map = new HashMap<String, HashSet<MJMethod>>();

	private enum AddFlag {
		add, check
	};
}
