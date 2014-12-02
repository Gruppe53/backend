package compiler.IR;

import java.util.HashMap;
import java.util.LinkedList;

import compiler.CODE.LC3.LC3label;

public class MJClass extends IR {

	private String name;
	private MJType superClass;
	private LinkedList<MJMethod> methodList = new LinkedList<MJMethod>();
	private LinkedList<MJVariable> fieldList = new LinkedList<MJVariable>();
	private boolean isTop = false;
	private int size;
	private int fieldOffsets = -1;
	private HashMap<Integer,MJMethod> methodTable = new HashMap<Integer,MJMethod>();
	private LC3label vmtlabel;

	public MJClass() {}
	
	// this we have just for the main class

	public MJClass(String name, MJMethod md) {
		this.name = name;
		LinkedList<MJMethod> methodList = new LinkedList<MJMethod>();
		methodList.addFirst(md);
		this.methodList = methodList;
		this.superClass = MJType.getClassType("Object");
	}

	// and this is for the general case

	public MJClass(String name, String superClassName,
			LinkedList<MJVariable> vdl, LinkedList<MJMethod> mdl) {
		this.name = name;
		this.fieldList = vdl;
		this.methodList = mdl;
		this.superClass = MJType.getClassType(superClassName);
	}

	public LinkedList<MJMethod> getMethodList() {
		return methodList;
	}

	public String getName() {
		return name;
	}

	public LinkedList<MJVariable> getFieldList() {
		return fieldList;
	}

	public void setSuperClass(MJType superClass) {
		this.superClass = superClass;
	}

	public MJType getSuperClass() {
		return superClass;
	}

	public boolean isTop() {
		return this.isTop;
	}

	public void setAsTop() {
		this.isTop = true;
		
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getFieldOffsets() {
		return fieldOffsets;
	}

	public void setFieldOffsets(int fieldOffsets) {
		this.fieldOffsets = fieldOffsets;
	}

	public HashMap<Integer, MJMethod> getMethodTable() {
		return this.methodTable;
	}

	public void setMethodTable(HashMap<Integer, MJMethod> methodTable) {
		this.methodTable = methodTable;
	}

	public void setVMTlabel(LC3label vmtlabel) {
		this.vmtlabel = vmtlabel;
	}

	public LC3label getVMTlabel() {
		return this.vmtlabel;
	}
}
