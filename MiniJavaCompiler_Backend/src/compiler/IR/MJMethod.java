package compiler.IR;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import compiler.CODE.LC3.LC3;
import compiler.CODE.LC3.LC3label;

public class MJMethod extends IR {

	private MJType returnType;
	private String name;
	private LinkedList<MJVariable> parameters;
	private MJBlock body;
	private boolean isStatic;
	private boolean isPublic;
	private boolean isEntry;
	private int varCount;
	private int maxStackSize;
	private LC3label label;
	private int slot = -1;

	public MJMethod() {}
	
	public MJMethod(MJType returnType, String name, LinkedList<MJVariable> parList,
			MJBlock b, boolean isStatic, boolean isPublic) {
		this.returnType = returnType;
		this.name = name;
		this.parameters = parList;
		this.body = b;
		this.isStatic = isStatic;
		this.isPublic = isPublic;
		this.isEntry = false;
	}

	public String getName() {
		return name;
	}

	public LinkedList<MJVariable> getParameters() {
		return parameters;
	}

	public MJType getReturnType() {
		return returnType;
	}

	public MJBlock getBody() {
		return body;
	}

	public boolean isStatic() {
		return this.isStatic;
	}

	public boolean isPublic() {
		return this.isPublic;
	}

	public void setVarCount(int cnt) {
		this.varCount = cnt;
	}

	public int getVarCount() {
		return this.varCount;
	}

	public void setMaxStackSize(int maxStackSize) {
		this.maxStackSize = maxStackSize;
	}

	public LC3label getLabel() {
		return this.label;
	}

	public void setLabel(LC3label label) {
		this.label = label;
	}

	public boolean isEntry() {
		return isEntry;
	}

	public void setEntry(boolean isEntry) {
		this.isEntry = isEntry;
	}

	public void setSlot(int slot) {
		this.slot  = slot;
	}

	public int getSlot() {
		return this.slot;
	}
	
}
