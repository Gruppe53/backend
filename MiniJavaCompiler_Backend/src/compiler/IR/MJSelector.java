package compiler.IR;

public class MJSelector extends MJIdentifier {

	private MJIdentifierClass object;
	private MJIdentifier field;

	public MJSelector() {}
	
	public MJSelector(MJIdentifierClass t, MJIdentifier field) {
		this.object = t;
		this.field = field;
	}

	public MJIdentifierClass getObject() {
		return object;
	}

	public MJIdentifier getField() {
		return field;
	}

	public void setField(MJIdentifier _field) {
		this.field = _field;
	}
	
	public void setObject(MJIdentifier _object) {
		this.object = _object;
	}

}
