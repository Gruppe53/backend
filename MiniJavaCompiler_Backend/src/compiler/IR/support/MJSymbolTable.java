package compiler.IR.support;

import java.util.HashMap;
import compiler.IR.MJVariable;

public class MJSymbolTable {

	private HashMap<String, MJVariable> map = new HashMap<String, MJVariable>();
	
	public HashMap<String, MJVariable> getMap() { return this.map; }
}
