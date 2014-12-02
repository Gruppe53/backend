package compiler.IR.support;

import compiler.Exceptions.VisitorException;
import compiler.IR.*;

public abstract class IRElementVisitorWithArgument<A> {

	public abstract A visitProgram(MJProgram e, A state) throws VisitorException;
	public abstract A visitClass(MJClass e, A state) throws VisitorException;
	public abstract A visitVariable(MJVariable e, A state) throws VisitorException;
	public abstract A visitType(MJType e, A state) throws VisitorException;
	public abstract A visitMethod(MJMethod e, A state) throws VisitorException;

	public A visitStatement(MJStatement e, A state) throws VisitorException {
		if (e instanceof MJBlock) return visitStatement((MJBlock)e, state);
		if (e instanceof MJIfElse) return visitStatement((MJIfElse)e, state);
		if (e instanceof MJIf) return visitStatement((MJIf)e, state);
		if (e instanceof MJWhile) return visitStatement((MJWhile)e, state);
		if (e instanceof MJAssign) return visitStatement((MJAssign)e, state);
		if (e instanceof MJPrint) return visitStatement((MJPrint)e, state);
		if (e instanceof MJPrintln) return visitStatement((MJPrintln)e, state);
		if (e instanceof MJMethodCallStmt) return visitStatement((MJMethodCallStmt)e, state);
		if (e instanceof MJReturn) return visitStatement((MJReturn)e, state);
		throw new VisitorException("unknown statement class "+e.getClass().getName());
	}
	
	public abstract A visitStatement(MJBlock e, A state) throws VisitorException;
	public abstract A visitStatement(MJIf e, A state) throws VisitorException;
	public abstract A visitStatement(MJIfElse e, A state) throws VisitorException;
	public abstract A visitStatement(MJWhile e, A state) throws VisitorException;
	public abstract A visitStatement(MJAssign e, A state) throws VisitorException;
	public abstract A visitStatement(MJPrint e, A state) throws VisitorException;
	public abstract A visitStatement(MJPrintln e, A state) throws VisitorException;
	public abstract A visitStatement(MJMethodCallStmt e, A state) throws VisitorException;
	public abstract A visitStatement(MJReturn e, A state) throws VisitorException;

	public A visitExpression(MJExpression e, A state) throws VisitorException {
		if (e instanceof MJAnd) return visitExpression((MJAnd)e, state);
		if (e instanceof MJEqual) return visitExpression((MJEqual)e, state);
		if (e instanceof MJLess) return visitExpression((MJLess)e, state);
		if (e instanceof MJPlus) return visitExpression((MJPlus)e, state);
		if (e instanceof MJMinus) return visitExpression((MJMinus)e, state);
		if (e instanceof MJMult) return visitExpression((MJMult)e, state);
		if (e instanceof MJUnaryMinus) return visitExpression((MJUnaryMinus)e, state);
		if (e instanceof MJNegate) return visitExpression((MJNegate)e, state);
		if (e instanceof MJNewArray) return visitExpression((MJNewArray)e, state);
		if (e instanceof MJNew) return visitExpression((MJNew)e, state);
		if (e instanceof MJMethodCallExpr) return visitExpression((MJMethodCallExpr)e, state);
		if (e instanceof MJParentheses) return visitExpression((MJParentheses)e, state);
		if (e instanceof MJBoolean) return visitExpression((MJBoolean)e, state);
		if (e instanceof MJInteger) return visitExpression((MJInteger)e, state);
		if (e instanceof MJString) return visitExpression((MJString)e, state);
		if (e instanceof MJSelector) return visitExpression((MJSelector)e, state);
		if (e instanceof MJArray) return visitExpression((MJArray)e, state);
		if (e instanceof MJIdentifier) return visitExpression((MJIdentifier)e, state);
		if (e instanceof MJNoExpression) return visitExpression((MJNoExpression)e, state);
		
		throw new VisitorException("unknown expression class "+e.getClass().getName());
	}
	public abstract A visitExpression(MJAnd e, A state) throws VisitorException;
	public abstract A visitExpression(MJEqual e, A state) throws VisitorException;
	public abstract A visitExpression(MJLess e, A state) throws VisitorException;
	public abstract A visitExpression(MJPlus e, A state) throws VisitorException;
	public abstract A visitExpression(MJMinus e, A state) throws VisitorException;
	public abstract A visitExpression(MJMult e, A state) throws VisitorException;
	public abstract A visitExpression(MJUnaryMinus e, A state) throws VisitorException;
	public abstract A visitExpression(MJNegate e, A state) throws VisitorException;
	public abstract A visitExpression(MJNew e, A state) throws VisitorException;
	public abstract A visitExpression(MJNewArray e, A state) throws VisitorException;
	public abstract A visitExpression(MJMethodCallExpr e, A state) throws VisitorException;
	public abstract A visitExpression(MJParentheses e, A state) throws VisitorException;
	public abstract A visitExpression(MJBoolean e, A state) throws VisitorException;
	public abstract A visitExpression(MJInteger e, A state) throws VisitorException;
	public abstract A visitExpression(MJString e, A state) throws VisitorException;
	
	public abstract A visitExpression(MJIdentifier e, A state) throws VisitorException;
	public abstract A visitExpression(MJArray e, A state) throws VisitorException;
	public abstract A visitExpression(MJSelector e, A state) throws VisitorException;
	
	public abstract A visitExpression(MJNoExpression e, A state) throws VisitorException;
}