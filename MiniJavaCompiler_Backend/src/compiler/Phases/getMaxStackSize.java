package compiler.Phases;

import compiler.Exceptions.VisitorException;
import compiler.IR.*;
import compiler.IR.support.IRElementVisitor;

public class getMaxStackSize extends IRElementVisitor<Integer> {

	public static int visit(MJBlock body) {
		try {
			return (new getMaxStackSize()).visitStatement(body);
		} catch (VisitorException e) {
			// this cannot happen!
		}
		return 0;
	}

	public Integer visitProgram(MJProgram e) throws VisitorException {
		return null;
	}

	public Integer visitClass(MJClass e) throws VisitorException {
		return null;
	}

	public Integer visitVariable(MJVariable e) throws VisitorException {
		return null;
	}

	public Integer visitType(MJType e) throws VisitorException {
		return null;
	}

	public Integer visitMethod(MJMethod e) throws VisitorException {
		return null;
	}

	public Integer visitStatement(MJBlock e) throws VisitorException {
		int maxsize = 0;
		
		for (MJStatement s : e.getStatements()) {
			maxsize = Math.max(maxsize, visitStatement(s));
		}
		return maxsize;
	}

	
	public Integer visitStatement(MJIf e) throws VisitorException {
		return visitStatement(e.getIfBlock());
	}

	
	public Integer visitStatement(MJIfElse e) throws VisitorException {
		return Math.max(visitStatement(e.getIfBlock()), visitStatement(e.getElseBlock()));
	}

	
	public Integer visitStatement(MJWhile e) throws VisitorException {
		return Math.max(visitExpression(e.getCondition()), 
				1 + visitStatement(e.getBlock()));
	}

	
	public Integer visitStatement(MJAssign e) throws VisitorException {
		return visitExpression(e.getLhs()) + visitExpression(e.getRhs());	
	}

	
	public Integer visitStatement(MJPrint e) throws VisitorException {
		return 1 + visitExpression(e.getParameter());
	}

	
	public Integer visitStatement(MJPrintln e) throws VisitorException {
		return 1 + visitExpression(e.getParameter());
	}

	
	public Integer visitStatement(MJMethodCallStmt e) throws VisitorException {
		return visitExpression(e.getMethodCallExpr());
	}

	
	public Integer visitStatement(MJReturn e) throws VisitorException {
		return visitExpression(e.getArgument());
	}

	private Integer visitExpression(MJBinaryOp e) throws VisitorException {
		return Math.max(visitExpression(e.getLhs()), visitExpression(e.getRhs())) + 1;
	}

	
	public Integer visitExpression(MJAnd e) throws VisitorException {
		return visitExpression((MJBinaryOp)e);
	}

	
	public Integer visitExpression(MJEqual e) throws VisitorException {
		return visitExpression((MJBinaryOp)e);
	}

	
	public Integer visitExpression(MJLess e) throws VisitorException {
		return visitExpression((MJBinaryOp)e);
	}

	
	public Integer visitExpression(MJPlus e) throws VisitorException {
		return visitExpression((MJBinaryOp)e);
	}

	
	public Integer visitExpression(MJMinus e) throws VisitorException {
		return visitExpression((MJBinaryOp)e);
	}

	
	public Integer visitExpression(MJMult e) throws VisitorException {
		return visitExpression((MJBinaryOp)e);
	}

	private Integer visitExpression(MJUnaryOp e) throws VisitorException {
		return visitExpression(e.getArgument());
	}

	
	public Integer visitExpression(MJUnaryMinus e) throws VisitorException {
		return visitExpression((MJUnaryOp)e);
	}

	
	public Integer visitExpression(MJNegate e) throws VisitorException {
		return visitExpression((MJUnaryOp)e);
	}

	
	public Integer visitExpression(MJNew e) throws VisitorException {
		return 1;
	}

	
	public Integer visitExpression(MJNewArray e) throws VisitorException {
		return 1+visitExpression(e.getSize());
	}

	
	public Integer visitExpression(MJMethodCallExpr e) throws VisitorException {
		int maxsize = 0;
		
		maxsize += e.getArguments().size();
		
		for (MJExpression arg : e.getArguments()) {
			maxsize += visitExpression(arg);
		}
		
		maxsize += 6;
		maxsize += 1;
		
		return maxsize;
	}

	
	public Integer visitExpression(MJParentheses e) throws VisitorException {
		return visitExpression(e.getArgument());
	}

	
	public Integer visitExpression(MJBoolean e) throws VisitorException {
		return 1;
	}

	
	public Integer visitExpression(MJInteger e) throws VisitorException {
		return 1;
	}

	
	public Integer visitExpression(MJString e) throws VisitorException {
		return 1;
	}

	
	public Integer visitExpression(MJIdentifier e) throws VisitorException {
		return 1;
	}

	
	public Integer visitExpression(MJArray e) throws VisitorException {
		return visitExpression(e.getArray()) + visitExpression(e.getIndex());
	}

	
	public Integer visitExpression(MJSelector e) throws VisitorException {
		return visitExpression(e.getField()) + visitExpression(e.getObject());
	}

	
	public Integer visitExpression(MJNoExpression e) throws VisitorException {
		return 0;
	}

}
