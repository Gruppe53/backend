package compiler.Phases;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import compiler.Compiler;
import compiler.IR.*;
import compiler.IR.support.IRElementVisitorWithArgument;
import compiler.CODE.CODE;
import compiler.CODE.LC3.*;
import compiler.Exceptions.ClassNotFound;
import compiler.Exceptions.CodeGenException;
import compiler.Exceptions.VisitorException;

public class CodeGen extends IRElementVisitorWithArgument<CODE> {

	public static void check(IR ir, CODE code) throws CodeGenException {

		try {
			new CodeGen().visitProgram(ir.getProgram(), code);
		} catch (VisitorException e) {
			throw new CodeGenException(e.getMessage());
		}

	}

	@Override
	public CODE visitProgram(MJProgram e, CODE code) throws VisitorException {

		prepareIR(e, code);
		addRuntimeSetup(e, code);

		for (MJClass c : e.getClasses()) {
			visitClass(c, code);
		}

		addHelperFunctions(code);
		return null;
	}

	@Override
	public CODE visitClass(MJClass e, CODE code) throws VisitorException {
		IR.currentClass = e;
				
		for (MJMethod meth : e.getMethodList()) {
			IR.currentMethod = meth;
			code.add(meth.getLabel());
			visitMethod(meth,code);
		}
		return null;
	}

	@Override
	public CODE visitVariable(MJVariable e, CODE state) throws VisitorException {
		System.err.println("This should not be reached.");
		System.exit(-1);
		return null;
	}

	@Override
	public CODE visitType(MJType e, CODE state) throws VisitorException {
		System.err.println("This should not be reached.");
		System.exit(-1);
		return null;
	}

	@Override
	public CODE visitMethod(MJMethod e, CODE state) throws VisitorException {
		state.comment(" METHOD "+e.getName());
		// save return address (r7) in word 3 of stack
		// STR R7 R6 3
		state.commentline(" save SFP R7 and initialize SP R5");
		state.add( new LC3STR(LC3regs.R7, CODE.SFP, 2) );
		if (e.isEntry()) {
			state.add(new LC3LEA(CODE.TMP0, state.arguments));
			state.add( new LC3STR(CODE.TMP0, CODE.SFP, 3));
		}
		state.add( new LC3ADD(CODE.SP, CODE.SFP, 3+e.getVarCount()));
		state.commentline(" body ");		
		visitStatement(e.getBody(), state);
		state.comment(" METHOD END "+e.getName());
		return null;
	}

	@Override
	public CODE visitStatement(MJBlock e, CODE code) throws VisitorException {
		for (MJStatement s : e.getStatements()) {
			visitStatement(s, code);
		}
		return null;
	}

	@Override
	public CODE visitStatement(MJIf e, CODE code) throws VisitorException {
		code.comment(" IF ");
		code.commentline(" Check if condition is true ");
		if(e.getCondition().equals(true)){
			code.commentline(" get the ifBlock ");
			visitStatement(e.getIfBlock(), code);
			code.pop(CODE.TMP0);
			code.commentline(" do ifBlock ");
			code.push(CODE.TMP0);
		}
		
		code.comment(" IF END ");

		return null;
	}

	@Override
	public CODE visitStatement(MJIfElse e, CODE code) throws VisitorException {
		code.comment(" IF/ELSE ");
		
		code.commentline(" Check if true ");
		if(e.getCondition().equals(true)){
			code.commentline(" get ifBlock ");
			visitStatement(e.getIfBlock(), code);
			code.pop(CODE.TMP0);
			code.commentline(" do ifBlock ");
			code.push(CODE.TMP0);
		}
		else {
			code.commentline(" get the elseBlock ");
			visitStatement(e.getElseBlock(), code);
			code.pop(CODE.TMP0);
			code.commentline(" do elseBlock ");
			code.push(CODE.TMP0);
		}
		code.comment(" IF/ELSE END ");

		return null;
	}
	
	@Override
	public CODE visitStatement(MJWhile e, CODE code) throws VisitorException {
		code.comment(" WHILE BEGIN ");
		code.commentline(" get the Block ");
		visitStatement(e.getBlock(), code);
		code.pop(CODE.TMP0);
		code.commentline(" get the condition ");
		visitExpression(e.getCondition(), code);
		if(e.getCondition().equals(true)){
			code.commentline(" do Block ");
			code.push(CODE.TMP0);
		}
		
		code.comment(" WHILE END");

		return null;
	}

	@Override
	public CODE visitStatement(MJAssign e, CODE code) throws VisitorException {

		code.comment(" ASSIGN ");

		code.commentline(" rhs ");
		visitExpression(e.getRhs(), code);

		code.commentline( " lhs ");
		visitExpression(e.getLhs(), code, true);

		code.commentline( " store ");
		code.pop2(CODE.TMP0, CODE.TMP1);
		code.add( new LC3STR(CODE.TMP0, CODE.TMP1, 0));

		code.comment(" ASSIGN END");

		return null;
	}

	@Override
	public CODE visitStatement(MJPrint e, CODE code) throws VisitorException {
		LC3label cont = code.newLabel();

		preparePrintArgument(e.getParameter(), cont, code);
		code.add(cont);
		code.add( new LC3TRAP(0x22));	
		return null;
	}

	private void preparePrintArgument(MJExpression parameter, LC3label cont,
			CODE code) throws VisitorException {
		visitExpression(parameter, code);

		MJType t = parameter.getType();

		if (t.isClass()) { 
			if (t.getName().equals("String")) {			
				LC3label nullstring = code.newLabel();
				LC3label nullcont = code.newLabel();
				code.pop(CODE.TMP0);
				code.add( new LC3BRZ(nullcont));
				code.add( new LC3LDR(CODE.TMP0, CODE.TMP0, 0));
				code.add( new LC3BR(cont));
				code.add( nullcont);
				code.add( new LC3LEA(CODE.TMP0, nullstring));
				code.add( new LC3BR(cont));
				code.add( nullstring);
				code.add( new LC3string("null"));
			} else {
				throw new CodeGenException("Cannot print other classes than String.");
			}
		} else if (t.isInt()){
			// integer must be translated to string first
			LC3label intcont = code.newLabel();
			LC3label inttostringaddr = code.newLabel();

			code.add(new LC3BR(intcont));
			code.add(inttostringaddr);
			code.addData( new LC3labeldata(code.inttostring));
			code.add(intcont);
			code.add( new LC3LD(CODE.TMP1, inttostringaddr));
			code.add( new LC3JSRR(CODE.TMP1));
			code.pop(CODE.TMP0);
		} else if (t.isBoolean()) {
			// boolean must be translated to string first
			LC3label flab = code.newLabel();
			LC3label tstr = code.newLabel();
			LC3label fstr = code.newLabel();
			code.pop(CODE.TMP0);
			code.add( new LC3BRZ(flab));
			code.add( new LC3LEA( CODE.TMP0, tstr ));
			code.add( new LC3BR(cont));
			code.add( flab );
			code.add( new LC3LEA( CODE.TMP0, fstr ));
			code.add( new LC3BR(cont));
			code.add( tstr );
			code.add( new LC3string("true"));
			code.add( fstr );
			code.add( new LC3string("false"));
		}
	}

	@Override
	public CODE visitStatement(MJPrintln e, CODE code) throws VisitorException {
		LC3label cont = code.newLabel();
		LC3label newline = code.newLabel();

		preparePrintArgument(e.getParameter(), cont, code);

		code.add( new LC3BR(cont));
		code.add(newline);
		code.add( new LC3labeldata(code.newlineroutine));
		code.add(cont);
		code.add( new LC3TRAP(0x22));	
		code.add( new LC3LD(CODE.TMP1, newline));
		code.add( new LC3JSRR(CODE.TMP1));
		return null;
	}

	@Override
	public CODE visitStatement(MJMethodCallStmt e, CODE code)
			throws VisitorException {
		code.comment(" CALL "+e.getMethodCallExpr().getTarget().getName());
		handleMethodCall(e.getMethodCallExpr(), code);
		code.comment(" CALL END "+e.getMethodCallExpr().getTarget().getName());
		return null;
	}

	private void handleMethodCall(MJMethodCallExpr methodCallExpr, CODE code) throws VisitorException {
		LC3label blab = code.newLabel();
		LC3label dlab = code.newLabel();
		LC3label mlab = code.newLabel();
		code.add(new LC3BR(blab));
		code.commentline(" slot for SP ");
		code.add(dlab);
		code.add(new LC3int(0));
		if (methodCallExpr.getTarget().isStatic()) {
			code.commentline(" address of method to call ");
			code.add(mlab);
			code.add(new LC3labeldata( methodCallExpr.getTarget().getLabel()));
		}
		code.add(blab);
		code.commentline(" save SP ");
		code.add(new LC3ST(CODE.SP, dlab));
		code.commentline(" save SFP ");
		code.add(new LC3STR(CODE.SFP, CODE.SP, 1));
		code.commentline(" increment SP to save arguments and make space for admin area ");
		code.add(new LC3ADD(CODE.SP, CODE.SP, 3));

		code.commentline(" push arguments ");
		for (MJExpression arg : methodCallExpr.getArguments()) {
			code.commentline(" argument ");
			visitExpression(arg, code);
		}

		code.commentline(" get method address and jump to it");
		if (methodCallExpr.getTarget().isStatic()) {
			code.add(new LC3LD(CODE.TMP0, mlab));
		} else {
			code.commentline(" get object");
			visitExpression(methodCallExpr.getObject(),code);
			code.pop(CODE.TMP0);
			code.commentline(" get VMT");
			code.add( new LC3LDR(CODE.TMP0, CODE.TMP0, 0));
			code.commentline(" get slot from VMT");
			code.add( new LC3LDR(CODE.TMP0, CODE.TMP0, methodCallExpr.getTarget().getSlot()));
		}
		code.commentline(" set new SFP (this is the old SP)");
		code.add(new LC3LD(CODE.SFP, dlab));
		code.add(new LC3JSRR( CODE.TMP0 ));
		code.commentline(" once returned reset SP (this is the SFP)");
		code.add( new LC3ADD(CODE.SP, CODE.SFP, 0));
		code.commentline(" restore the old SFP - this was stored at offset one from the SFP");
		code.add( new LC3LDR(CODE.SFP, CODE.SFP, 1));
	}

	@Override
	public CODE visitStatement(MJReturn e, CODE code) throws VisitorException {
		code.comment(" RETURN BEGIN");
		if ( !(e.getArgument() instanceof MJNoExpression) ) {
			code.commentline( " return value ");
			visitExpression(e.getArgument(), code);
			code.pop(CODE.TMP0);
			code.commentline( " put in stack frame ");
			code.add( new LC3STR(CODE.TMP0, CODE.SFP, 0));
		}

		code.commentline(" get return PC from stack frame");
		code.add( new LC3LDR(LC3regs.R7, CODE.SFP, 2) );
		code.add( new LC3RET());
		code.comment(" RETURN END");

		return null;
	}

	@Override
	public CODE visitExpression(MJAnd e, CODE code) throws VisitorException {
		code.comment(" ADD BEGIN ");
		code.commentline(" ADD lhs ");
		visitExpression(e.getLhs(), code);
		code.commentline(" ADD rhs ");
		visitExpression(e.getRhs(), code);
		code.pop2(CODE.TMP0, CODE.TMP1);
		code.add(new LC3AND(CODE.TMP0, CODE.TMP0, CODE.TMP1));
		code.push( CODE.TMP0 );
		code.comment(" ADD END ");
		return null;
	}

	@Override
	public CODE visitExpression(MJEqual e, CODE code) throws VisitorException {
		code.comment(" EQUAL ");
		code.commentline(" lhs ");
		visitExpression(e.getLhs(), code);
		code.commentline(" rhs ");
		visitExpression(e.getRhs(), code);
		code.commentline(" compute lhs - rhs -- this works also for bool ");
		code.pop2(CODE.TMP0, CODE.TMP1);
		code.add( new LC3NOT(CODE.TMP1, CODE.TMP1));
		code.add( new LC3ADD(CODE.TMP1, CODE.TMP1, 1));
		code.add( new LC3ADD(CODE.TMP0, CODE.TMP0, CODE.TMP1));

		// if they are equal, Z flag is set

		LC3label ltrue = code.newLabel();
		LC3label lend = code.newLabel();

		code.commentline(" if they are equal, the result is zero ");
		code.add( new LC3BRZ(ltrue) );
		code.commentline(" if not zero, push 0 ");
		code.push(CODE.CONST0);
		code.add( new LC3BR(lend));
		code.add(ltrue);
		code.commentline(" if zero, push 1 ");
		code.push(CODE.CONST1);
		code.add(lend);
		code.comment(" EQUAL END ");
		return null;
	}

	@Override
	public CODE visitExpression(MJLess e, CODE code) throws VisitorException {


		code.comment(" LESS ");

		code.commentline(" lhs ");
		visitExpression(e.getLhs(), code);
		code.commentline(" rhs ");
		visitExpression(e.getRhs(), code);

		code.commentline("  compute lhs - rhs  ");
		code.pop2(CODE.TMP0, CODE.TMP1);
		code.add( new LC3NOT(CODE.TMP1, CODE.TMP1));
		code.add( new LC3ADD(CODE.TMP1, CODE.TMP1, 1));
		code.add( new LC3ADD(CODE.TMP0, CODE.TMP0, CODE.TMP1));

		// if they are less, N flag is set
		LC3label ltrue = code.newLabel();
		LC3label lend = code.newLabel();

		code.commentline(" if lhs < rhs, N flag is set");
		code.add( new LC3BRN(ltrue) );
		code.commentline(" if not negative, push 0 ");
		code.push(CODE.CONST0);
		code.add( new LC3BR(lend));
		code.add(ltrue);
		code.commentline(" if negative, push 1 ");
		code.push(CODE.CONST1);
		code.add(lend);
		code.comment(" LESS END ");
		return null;
	}

	@Override
	public CODE visitExpression(MJPlus e, CODE code) throws VisitorException {
		code.comment(" PLUS BEGIN");
		code.commentline(" lhs ");
		visitExpression(e.getLhs(), code);
		code.commentline(" rhs ");
		visitExpression(e.getRhs(), code);

		if (e.getType().isInt()) {
			code.commentline(" add integers ");
			code.pop2( CODE.TMP0, CODE.TMP1);
			code.add( new LC3ADD(CODE.TMP0, CODE.TMP0, CODE.TMP1));
			code.push( CODE.TMP0 );
		} else {
			// we need a string object that can hold the sum of the two strings

			LC3label cont = code.newLabel();

			code.commentline(" add strings ");
			code.add(new LC3LD( CODE.TMP0, 1));
			code.add( new LC3BR(cont));
			code.add( new LC3labeldata(code.addstrings));
			code.add(cont);
			code.add( new LC3JSRR(CODE.TMP0));
		}
		code.comment(" PLUS END");
		return null;
	}

	@Override
	public CODE visitExpression(MJMinus e, CODE code) throws VisitorException {
		code.comment(" MINUS BEGIN ");
		code.commentline(" lhs ");
		visitExpression(e.getLhs(), code);
		code.commentline(" rhs ");
		visitExpression(e.getRhs(), code);
		code.commentline(" subtract integers ");
		code.pop2( CODE.TMP0, CODE.TMP1);
		code.add( new LC3NOT(CODE.TMP1, CODE.TMP1));
		code.add( new LC3ADD(CODE.TMP1, CODE.TMP1, 1)); 
		code.add( new LC3ADD(CODE.TMP0, CODE.TMP0, CODE.TMP1));
		code.push( CODE.TMP0 );
		
		code.comment(" MINUS END ");
		return null;
	}

	@Override
	public CODE visitExpression(MJMult e, CODE code) throws VisitorException {
		LC3label cont = code.newLabel();
		code.comment(" MULT BEGIN ");
		code.commentline(" lhs ");
		visitExpression(e.getLhs(), code);
		code.commentline(" rhs ");
		visitExpression(e.getRhs(), code);
		code.commentline(" load address of multiplication routine ");
		code.add( new LC3LD(CODE.TMP0, 1));
		code.add( new LC3BR(cont));
		code.add( new LC3labeldata(code.multiply));
		code.add(cont);
		code.commentline(" and jump there");
		code.add( new LC3JSRR(CODE.TMP0));
		code.comment(" MULT END ");
		return null;
	}

	@Override
	public CODE visitExpression(MJUnaryMinus e, CODE code)
			throws VisitorException {
		code.comment(" UNARY MINUS BEGIN ");
		code.commentline(" argument ");
		visitExpression(e.getArgument(), code);
		code.commentline(" negate ");
		code.pop(CODE.TMP0);
		code.add( new LC3NOT(CODE.TMP0, CODE.TMP0) );
		code.add( new LC3ADD(CODE.TMP0, CODE.TMP0, 1) );
		code.push(CODE.TMP0);
		code.comment(" UNARY MINUS END ");
		return null;
	}

	@Override
	public CODE visitExpression(MJNegate e, CODE code) throws VisitorException {
		//TODO Negate virker ikke
		code.comment(" NEGATE BEGIN ");
		if(e.getType().isBoolean()) {
			code.commentline(" argument ");
			visitExpression(e.getArgument(), code);
			code.pop(CODE.TMP1);
			code.commentline(" negate ");
			code.add( new LC3NOT(CODE.TMP1, CODE.TMP1) );
			code.push(CODE.TMP1);
		}
		
		code.comment(" NEGATE END ");
		return null;
	}

	@Override
	public CODE visitExpression(MJNew e, CODE code) throws VisitorException {
		LC3label l = code.newLabel();
		LC3label c = code.newLabel();
		LC3label newmethod = code.newLabel();

		code.comment(" NEW BEGIN ");
		code.add( new LC3BR(c));
		code.commentline(" address of allocation routine ");
		code.add(newmethod);
		code.add( new LC3labeldata(code.newroutine));
		code.commentline(" size of object to allocate ");
		code.add(l);
		code.add( new LC3int( e.getType().getDecl().getSize()));
		code.commentline(" load address and size and invoke new method ");
		code.add(c);
		code.add( new LC3LD(CODE.TMP0, l));
		code.push(CODE.TMP0);
		code.add( new LC3LD(CODE.TMP1, newmethod));
		code.add( new LC3JSRR(CODE.TMP1));
		code.commentline(" save VMT label");
		code.pop(CODE.TMP0);
		code.add( new LC3LD(CODE.TMP1, e.getType().getDecl().getVMTlabel()));
		code.add( new LC3STR(CODE.TMP1, CODE.TMP0, 0));
		code.push(CODE.TMP0);
		code.commentline(" new HP is address, is on stack");
		
		code.comment(" NEW END ");
		return null;
	}

	@Override
	public CODE visitExpression(MJNewArray e, CODE code)
			throws VisitorException {
		LC3label sizelab = code.newLabel();
		LC3label contlab = code.newLabel();
		LC3label errorlab = code.newLabel();
		LC3label newmethod = code.newLabel();
		LC3label multmethod = code.newLabel();
		LC3label nullify = code.newLabel();

		code.comment(" NEW ARRAY BEGIN ");
		code.add( new LC3BR(contlab));
		code.commentline(" address of allocation routine ");
		code.add(newmethod);
		code.add( new LC3labeldata(code.newroutine));
		code.add(multmethod);
		code.add( new LC3labeldata(code.multiply));
		code.add(nullify);
		code.add( new LC3labeldata(code.nullify));
		code.commentline(" size of element type to allocate ");
		code.add(sizelab);
		code.add( new LC3int( e.getType().getBaseType().getSize()));
		code.add(errorlab);
		code.add(contlab);
		code.commentline(" compute size of array ");
		visitExpression(e.getSize(), code);
		code.commentline(" look at top of stack without popping it ");
		code.add( new LC3LDR(CODE.TMP0, CODE.SP, -1));
		code.commentline(" size must not be negative ");
		code.add( new LC3BRN(errorlab) );
		code.commentline(" compute required space (size * sizeof elementtype)");
		code.push( CODE.TMP0 );
		code.commentline(" load size of elements");
		code.add( new LC3LD(CODE.TMP0, sizelab));
		code.push(CODE.TMP0);
		code.commentline(" multiply ");
		code.add( new LC3LD(CODE.TMP1, multmethod));
		code.add( new LC3JSRR(CODE.TMP1));
		code.commentline(" get size ");
		code.pop( CODE.TMP0 );
		code.commentline(" increment with 2 to store size of array and array");
		code.add( new LC3ADD(CODE.TMP0, CODE.TMP0, 2));
		code.commentline(" size must not be negative ");
		code.add( new LC3BRN(errorlab) );
		code.push(CODE.HP);
		code.push(CODE.TMP0);
		code.commentline(" load address and size and invoke method ");
		code.add( new LC3LD(CODE.TMP1, newmethod));
		code.add( new LC3JSRR(CODE.TMP1));
		code.commentline(" top of stack is new address ");
		code.add( new LC3LD( CODE.TMP1, nullify));
		code.add( new LC3JSRR( CODE.TMP1 ));
		code.commentline(" get size of array and store as first element");
		code.pop( CODE.TMP0 );
		code.add( new LC3STR( CODE.TMP0, CODE.HP, 0));
		code.add( new LC3ADD( CODE.TMP0, CODE.HP, 2));
		code.add( new LC3STR( CODE.TMP0, CODE.HP, 1));
		code.push( CODE.HP );
		code.comment(" NEW ARRAY END ");
		return null;
	}

	@Override
	public CODE visitExpression(MJMethodCallExpr e, CODE code)
			throws VisitorException {
		code.comment(" CALL "+e.getTarget().getName());
		handleMethodCall(e, code);

		if (!e.getTarget().getReturnType().isVoid()) {
			code.commentline(" the first cell on the stack contains the result, so increase SP by one");
			code.add(new LC3ADD(CODE.SP, CODE.SP, 1));
		}

		code.comment(" CALL END "+e.getTarget().getName());
		return null;
	}

	@Override
	public CODE visitExpression(MJParentheses e, CODE code)
			throws VisitorException {
		visitExpression(e.getArgument(), code);
		return null;
	}

	@Override
	public CODE visitExpression(MJBoolean e, CODE code)
			throws VisitorException {
		code.comment(" BOOLEAN CONST "+e.getValue());
		if (e.isTrue()) {
			code.push(CODE.CONST1);						
		} else {
			code.push(CODE.CONST0);			
		}		
		return null;
	}

	@Override
	public CODE visitExpression(MJInteger e, CODE code)
			throws VisitorException {
		LC3regs r = null;
		code.comment(" INT CONST "+ e.getValue());
		if (e.getValue() == 0) {
			r = CODE.CONST0;
		} else if (e.getValue() == 1) {
			r = CODE.CONST1;
		} else {
			r = CODE.TMP0;
			code.add( new LC3LD(CODE.TMP0, 3));
		}
		code.push( r );

		if (e.getValue() != 0 && e.getValue() != 1) {
			LC3label cont = code.newLabel();
			code.add( new LC3BR(cont) );
			code.commentline(" value ");
			code.add( new LC3int(e.getValue()) );
			code.add(cont);
			code.comment(" INT CONST END");
		}
		return null;
	}

	@Override
	public CODE visitExpression(MJString e, CODE code) throws VisitorException {
		// we need to allocate an object on the heap
		// plus allocate space for the string

		code.comment(" STRING CONST BEGIN ");
		LC3label o = code.newLabel();
		LC3label oa = code.newLabel();
		LC3label c = code.newLabel();
		LC3label cont = code.newLabel();

		String s = e.getValue();

		if (s.startsWith("\"")) {
			s = s.substring(1);
		}

		if (s.endsWith("\"")) {
			s = s.substring(0, s.length()-1);
		}

		code.add( new LC3BR(cont));
		code.add( c );
		code.add( new LC3string(s));
		code.add( o );
		code.add( new LC3labeldata(c) );
		code.add( new LC3int(e.getValue().length()));
		code.add( oa );
		code.add( new LC3labeldata(o));
		code.add(cont);
		code.add( new LC3LD(CODE.TMP0, oa));
		code.push(CODE.TMP0);
		code.comment(" STRING CONST END ");
		return null;
	}

	public CODE visitExpression(MJIdentifier e, CODE code)
			throws VisitorException {
		return visitExpression(e, code, false);
	}

	public CODE visitExpression(MJIdentifierClass e, CODE code, boolean lvalue)
			throws VisitorException {

		if (e instanceof MJSelector) return visitExpression((MJSelector)e, code, lvalue);
		if (e instanceof MJArray) return visitExpression((MJArray)e, code, lvalue);
		if (e instanceof MJIdentifier) return visitExpression((MJIdentifier)e, code, lvalue);

		return code;
	}

	public CODE visitExpression(MJIdentifier e, CODE code, boolean lvalue)
			throws VisitorException {
		code.comment(" IDENTIFIER " + e.getName() + " "
				+ (lvalue ? "lhs " : "rhs "));

		// identifier denotes a variable or parameter

		if (lvalue) {
			code.commentline(" push address ");
			int offset = e.getDeclaration().getOffset();
			if (offset == -1) {
			}
			code.add(new LC3ADD(CODE.TMP0, CODE.SFP, 3 + offset));
			code.push(CODE.TMP0);
		} else {
			code.commentline(" push value ");
			code.add(new LC3LDR(CODE.TMP0, CODE.SFP, 3 + e.getDeclaration().getOffset()));
			code.push(CODE.TMP0);
		}
		code.comment(" IDENTIFIER END ");
		return null;
	}

	@Override
	public CODE visitExpression(MJArray e, CODE code) throws VisitorException {
		return visitExpression(e, code, false);
	}

	public CODE visitExpression(MJArray e, CODE code, boolean lvalue) throws VisitorException {
		LC3label sizelab = code.newLabel();
		LC3label contlab = code.newLabel();
		LC3label errorlab = code.newLabel();
		LC3label multmethod = code.newLabel();

		code.comment(" ARRAY BEGIN ");

		code.add( new LC3BR(contlab));
		if (e.getType().getSize()>1) {
			code.add(multmethod);
			code.add( new LC3labeldata(code.multiply));
			code.commentline(" size of element type ");
			code.add( sizelab );
			code.add( new LC3int(e.getType().getSize()));
		}
		code.add(errorlab);
		code.add( new LC3LD( CODE.TMP0, 1));
		code.add( new LC3JSRR( CODE.TMP0));
		code.add( new LC3labeldata(code.indexoutofbounds));
		code.add(contlab);
		code.commentline(" generate code for identifier ");
		visitExpression(e.getArray(), code);
		code.commentline(" check for NPE");
		LC3label notnull = code.newLabel();

		code.add( new LC3LDR(CODE.TMP0, CODE.SP, -1));
		code.add( new LC3BRNP( notnull ));

		// throw NPE

		code.add( new LC3LD( CODE.TMP0, 1));
		code.add( new LC3JSRR( CODE.TMP0));
		code.add( new LC3labeldata(code.nullpointer));

		code.add( notnull);

		code.commentline(" look at top of stack without popping it ");
		code.commentline(" generate code for index ");
		visitExpression(e.getIndex(), code);								// stack: array index
		code.comment(" check index against array boundaries (0 and size, stored at first slot)");
		code.commentline(" look at top of stack without popping it ");
		code.add( new LC3LDR(CODE.TMP1, CODE.SP, -2));
		code.add( new LC3LDR(CODE.TMP0, CODE.SP, -1));
		code.add( new LC3LDR(CODE.TMP1, CODE.TMP1, 0));
		code.add( new LC3NOT(CODE.TMP1, CODE.TMP1));
		code.add( new LC3ADD(CODE.TMP1, CODE.TMP1, 1));
		code.add( new LC3ADD(CODE.TMP0, CODE.TMP0, CODE.TMP1));
		code.commentline(" must be negative (index - size)");
		code.add( new LC3BRZP(errorlab));
		code.commentline(" look at top of stack without popping it ");
		code.add( new LC3LDR(CODE.TMP0, CODE.SP, -1));
		code.commentline(" size must not be negative ");
		code.add( new LC3BRN(errorlab) );
		if (e.getType().getSize()>1) {
			code.commentline(" load size of elements");
			code.add( new LC3LD(CODE.TMP0, sizelab));
			code.push(CODE.TMP0);
			code.commentline(" multiply ");
			code.add( new LC3LD(CODE.TMP1, multmethod));
			code.add( new LC3JSRR(CODE.TMP1));
		}
		code.commentline(" get offset ");
		code.pop(CODE.TMP0);
		code.commentline(" size must not be negative ");
		code.add( new LC3BRN(errorlab) );
		code.commentline(" get address ");
		code.pop(CODE.TMP1);
		code.add( new LC3LDR( CODE.TMP1, CODE.TMP1, 1));
		code.add( new LC3ADD( CODE.TMP1, CODE.TMP1, CODE.TMP0));

		if (lvalue) {
			code.push( CODE.TMP1 );
		} else {
			code.add( new LC3LDR(CODE.TMP0, CODE.TMP1, 0) );
			code.push(CODE.TMP0);
		}
		code.comment(" ARRAY END ");
		return null;
	}

	@Override
	public CODE visitExpression(MJSelector e, CODE code)
			throws VisitorException {
		return visitExpression(e, code, false);
	}

	public CODE visitExpression(MJSelector e, CODE code, boolean lvalue)
			throws VisitorException {
		code.comment(" SELECTOR BEGIN " + (lvalue?"lhs":"rhs"));
		code.commentline(" get adress ");
		visitExpression(e.getObject(), code);

		// this should leave the value of the object on the stack

		int offs = e.getField().getDeclaration().getOffset();

		if (offs>1) {
			code.commentline(" compute offset ");
			code.pop(CODE.TMP0);

			LC3label Lcont = code.newLabel();
			LC3label Loffs = code.newLabel();

			code.add( new LC3BR(Lcont));
			code.add(Loffs);
			code.add( new LC3int( offs ));
			code.add(Lcont);
			code.add( new LC3LD(CODE.TMP1, Loffs));
			code.add( new LC3ADD(CODE.TMP0, CODE.TMP0, CODE.TMP1));
		} else {
			if (offs==1) {
				code.pop(CODE.TMP0);
				code.add( new LC3ADD(CODE.TMP0, CODE.TMP0, CODE.CONST1) );
			}
		}

		// TMP0 contains reference plus offset

		if (lvalue) {
			if (offs!=0) {
				code.commentline("push address");
				code.push(CODE.TMP0);
			}
		} else {
			if (offs==0) {
				code.pop(CODE.TMP0);
			}
			code.commentline(" get value from address and push");
			code.add( new LC3LDR(CODE.TMP0, CODE.TMP0, 0));
			code.push(CODE.TMP0);
		}

		code.comment(" SELECTOR END ");
		return null;
	}

	@Override
	public CODE visitExpression(MJNoExpression e, CODE state)
			throws VisitorException {
		return null;
	}

	private int assignFieldOffsets(int i, MJClass c) throws CodeGenException {

		int offset = i;

		if ( c.getFieldOffsets() != -1 ) {
			return c.getFieldOffsets();
		}

		if (!c.getName().equals("Object")) {
			offset = assignFieldOffsets( offset, c.getSuperClass().getDecl());
		}

		offset = assignOffsets(offset, c.getFieldList());
		c.setFieldOffsets(offset);
		return offset;
	}

	private int findLocalVars(int offset, MJBlock body) throws CodeGenException {

		offset = assignOffsets(offset, body.getVariables());

		for (MJStatement s: body.getStatements()) {

			if (s instanceof MJBlock) {
				offset = findLocalVars(offset, (MJBlock)s);
			}

			if (s instanceof MJIfElse) {
				MJIfElse s_if = (MJIfElse) s;
				offset = findLocalVars(offset, s_if.getIfBlock());
				if (s_if.getElseBlock()!=null) {
					offset = findLocalVars(offset, s_if.getElseBlock());
				}
			}

			if (s instanceof MJWhile) {
				MJWhile s_w = (MJWhile) s;
				offset = findLocalVars(offset, s_w.getBlock());
			}
		}

		return offset;
	}

	private int assignOffsets(int offset, LinkedList<MJVariable> fieldList) throws CodeGenException {


		for (MJVariable f : fieldList) {
			f.setOffset(offset);

			if (Compiler.isDebug()) {
				System.err.println("    Offset "+f.getName()+" "+offset);
			}

			offset += f.getType().getSize();
		}

		return offset;
	}

	private void addHelperFunctions(CODE code) throws CodeGenException {

		code.comment(" helper functions ");
		code.comment("");

		code.comment(" translate top of stack to string, pushes result");
		code.add( code.inttostring );

		code.startComment();
		code.commentLine("This algorithm takes the 2's complement representation of a signed");
		code.commentLine("integer, within the range -999 to +999, and converts it into an ASCII"); 
		code.commentLine("string consisting of a sign digit, followed by three decimal digits."); 
		code.endComment();

		code.pop( CODE.TMP0 );
		code.push(CODE.RET);

		LC3label asciibuf = code.newLabel();
		LC3label asciiMinus = code.newLabel();
		LC3label asciiOffset = code.newLabel();
		LC3label begin10000 = code.newLabel();
		LC3label neg10000 = code.newLabel();
		LC3label loop10000 = code.newLabel();
		LC3label end10000 = code.newLabel();
		LC3label pos10000 = code.newLabel();
		LC3label begin1000 = code.newLabel();
		LC3label neg1000 = code.newLabel();
		LC3label loop1000 = code.newLabel();
		LC3label end1000 = code.newLabel();
		LC3label pos1000 = code.newLabel();
		LC3label begin100 = code.newLabel();
		LC3label neg100 = code.newLabel();
		LC3label loop100 = code.newLabel();
		LC3label end100 = code.newLabel();
		LC3label pos100 = code.newLabel();
		LC3label begin10 = code.newLabel();
		LC3label neg10 = code.newLabel();
		LC3label loop10 = code.newLabel();
		LC3label end10 = code.newLabel();
		LC3label pos10 = code.newLabel();
		LC3label begin1 = code.newLabel();
		LC3label neg1 = code.newLabel();
		LC3label pos1 = code.newLabel();
		LC3label no10 = code.newLabel();
		LC3label no100 = code.newLabel();
		LC3label no1000 = code.newLabel();
		LC3label no10000 = code.newLabel();

		code.add( new LC3LEA(CODE.TMP1, asciibuf));
		code.add( new LC3ADD(CODE.TMP0, CODE.TMP0, 0));
		code.add( new LC3BRZP(begin10000));

		code.add( new LC3LD(CODE.RET, asciiMinus));
		code.add( new LC3STR(CODE.RET, CODE.TMP1, 0));
		code.add( new LC3ADD(CODE.TMP1, CODE.TMP1, 1));
		code.add( new LC3NOT(CODE.TMP0, CODE.TMP0));
		code.add( new LC3ADD(CODE.TMP0, CODE.TMP0, 1));

		code.add(begin10000);

		//code.add( new LC3LD(CODE.RET, asciiOffset));
		code.add( new LC3AND(CODE.RET, CODE.RET, 0));

		code.add( new LC3LD(CODE.CONST0, neg10000));
		code.add( loop10000 );
		code.add( new LC3ADD( CODE.TMP0, CODE.TMP0, CODE.CONST0));
		code.add( new LC3BRN(end10000));
		code.add( new LC3ADD(CODE.RET, CODE.RET, 1));
		code.add( new LC3BR( loop10000 ));
		code.startComment();

		code.add( end10000);
		code.add( new LC3ADD( CODE.RET, CODE.RET, 0));
		code.add( new LC3BRZ(no10000));
		code.add( new LC3LD( CODE.CONST0, asciiOffset));
		code.add( new LC3ADD( CODE.RET, CODE.RET, CODE.CONST0));
		code.add( new LC3STR( CODE.RET, CODE.TMP1, 0));
		code.add( new LC3ADD( CODE.TMP1, CODE.TMP1, 1));
		code.add( no10000 );
		code.add( new LC3LD(CODE.CONST0, pos10000));
		code.add( new LC3ADD( CODE.TMP0, CODE.TMP0, CODE.CONST0 ));
		code.startComment();
		code.add( new LC3AND(CODE.RET, CODE.RET, 0));

		code.add( begin1000 ); 
		code.add( new LC3LD( CODE.CONST0, neg1000));
		code.add( loop1000 );
		code.add( new LC3ADD( CODE.TMP0, CODE.TMP0, CODE.CONST0));
		code.add( new LC3BRN(end1000));
		code.add( new LC3ADD( CODE.RET, CODE.RET, 1));
		code.add( new LC3BR( loop1000));
		code.startComment();

		code.add(end1000);
		code.add( new LC3ADD( CODE.RET, CODE.RET, 0));
		code.add( new LC3BRZ(no1000));
		code.add( new LC3LD( CODE.CONST0, asciiOffset));
		code.add( new LC3ADD( CODE.RET, CODE.RET, CODE.CONST0));
		code.add( new LC3STR( CODE.RET, CODE.TMP1, 0));
		code.add( new LC3ADD( CODE.TMP1, CODE.TMP1, 1));
		code.add( no1000 );
		code.add( new LC3LD(CODE.CONST0, pos1000));
		code.add( new LC3ADD( CODE.TMP0, CODE.TMP0, CODE.CONST0 ));
		code.startComment();
		code.add( new LC3AND(CODE.RET, CODE.RET, 0));

		code.add( begin100 ); 
		code.add( new LC3LD( CODE.CONST0, neg100));
		code.add( loop100 );
		code.add( new LC3ADD( CODE.TMP0, CODE.TMP0, CODE.CONST0));
		code.add( new LC3BRN(end100));
		code.add( new LC3ADD( CODE.RET, CODE.RET, 1));
		code.add( new LC3BR( loop100));
		code.startComment();

		code.add(end100);
		code.add( new LC3ADD( CODE.RET, CODE.RET, 0));
		code.add( new LC3BRZ(no100));
		code.add( new LC3LD( CODE.CONST0, asciiOffset));
		code.add( new LC3ADD( CODE.RET, CODE.RET, CODE.CONST0));
		code.add( new LC3STR( CODE.RET, CODE.TMP1, 0));
		code.add( new LC3ADD( CODE.TMP1, CODE.TMP1, 1));
		code.add( no100 );
		code.add( new LC3LD(CODE.CONST0, pos100));
		code.add( new LC3ADD( CODE.TMP0, CODE.TMP0, CODE.CONST0 ));
		code.startComment();
		code.add( new LC3AND(CODE.RET, CODE.RET, 0));

		code.add( begin10 ); 
		code.add( new LC3LD( CODE.CONST0, neg10));
		code.add( loop10 );
		code.add( new LC3ADD( CODE.TMP0, CODE.TMP0, CODE.CONST0));
		code.add( new LC3BRN(end10));
		code.add( new LC3ADD( CODE.RET, CODE.RET, 1));
		code.add( new LC3BR( loop10));
		code.startComment();

		code.add(end10);
		code.add( new LC3ADD( CODE.RET, CODE.RET, 0));
		code.add( new LC3BRZ(no10));
		code.add( new LC3LD( CODE.CONST0, asciiOffset));
		code.add( new LC3ADD( CODE.RET, CODE.RET, CODE.CONST0));
		code.add( new LC3STR( CODE.RET, CODE.TMP1, 0));
		code.add( new LC3ADD( CODE.TMP1, CODE.TMP1, 1));
		code.add( no10 );
		code.add( new LC3ADD( CODE.TMP0, CODE.TMP0, 10 ));
		code.startComment();
		code.add( new LC3LD( CODE.RET, asciiOffset));

		code.add( begin1 );
		code.add( new LC3ADD(CODE.RET, CODE.RET, CODE.TMP0));
		code.add( new LC3STR(CODE.RET, CODE.TMP1, 0));
		code.add( new LC3ADD( CODE.TMP1, CODE.TMP1, 1));		
		code.add( new LC3AND(CODE.CONST0, CODE.CONST0, 0) );
		code.add( new LC3STR( CODE.CONST0, CODE.TMP1, 0));
		code.pop(CODE.RET);
		code.add( new LC3LEA(CODE.TMP0, asciibuf));
		code.push(CODE.TMP0);
		code.add( new LC3RET());

		code.comment("data");
		code.add(asciibuf);
		code.space(7);
		code.add(asciiMinus);
		code.add(new LC3int(0x2D));
		code.add(asciiOffset);
		code.add(new LC3int(0x30));
		code.add(neg10000);
		code.add(new LC3int(-10000));
		code.add(neg1000);
		code.add(new LC3int(-1000));
		code.add(neg100);
		code.add(new LC3int(-100));
		code.add(neg10);
		code.add(new LC3int(-10));
		code.add(neg1);
		code.add(new LC3int(-1));
		code.add(pos10000);
		code.add(new LC3int(10000));
		code.add(pos1000);
		code.add(new LC3int(1000));
		code.add(pos100);
		code.add(new LC3int(100));
		code.add(pos10);
		code.add(new LC3int(10));
		code.add(pos1);
		code.add(new LC3int(1));

		code.comment(" print newline ");
		code.add( code.newlineroutine );
		code.push( CODE.RET );

		LC3label nlcont = code.newLabel();
		LC3label newline = code.newLabel();
		code.add( new LC3LD(CODE.TMP0, 1));
		code.add( new LC3BR(nlcont));
		code.add( new LC3labeldata(newline));
		code.add( newline );
		code.add( new LC3string("\\n"));
		code.add( nlcont );
		code.add( new LC3TRAP(0x22) );
		code.pop( CODE.RET );
		code.add( new LC3RET());

		code.add( code.newroutine );
		code.comment(" create an object with size top of stack, result in HP");

		code.pop(CODE.TMP0);
		code.add( new LC3comment("allocate object"));
		code.add( new LC3NOT(CODE.TMP0, CODE.TMP0));
		code.add( new LC3ADD(CODE.TMP0, CODE.TMP0, 1));
		code.add( new LC3ADD(CODE.HP, CODE.HP, CODE.TMP0));

		// HP contains new heap pointer
		// check whether this is smaller than SP

		LC3label Lok = code.newLabel();
		LC3label Lsppos = code.newLabel();
		LC3label Lerr = code.newLabel();
		LC3label Lsub = code.newLabel();

		code.add( new LC3ADD(CODE.SP, CODE.SP, 0 ));
		code.add( new LC3BRP(Lsppos));
		code.add( new LC3ADD(CODE.HP, CODE.HP, 0));
		code.add( new LC3BRP(Lerr));
		code.add( Lsub );
		// subtract

		code.add( new LC3ADD(CODE.TMP1, CODE.SP, 0));
		code.add( new LC3NOT(CODE.TMP1, CODE.TMP1));
		code.add( new LC3ADD(CODE.TMP1, CODE.TMP1, 1));
		code.add( new LC3ADD(CODE.TMP1, CODE.HP, CODE.TMP1));

		code.add( new LC3BRP(Lok));
		code.add( new LC3BR(Lerr));
		code.add( Lsppos );

		code.add( new LC3ADD( CODE.HP, CODE.HP, 0));
		code.add( new LC3BRN(Lok));

		code.add( new LC3BR( Lsub ));

		code.add( Lerr );
		code.add( new LC3TRAP(0x25));

		code.add( Lok);
		code.push(CODE.HP);
		code.add( new LC3RET());

		code.comment(" nullify ");
		code.commentline(" overwrites memory area a to b with 0s ");
		code.commentline(" expects operands in top of stack ");
		code.commentline(" assumes a<b!!! ");
		code.add( code.nullify );

		LC3label eraseloop = code.newLabel();

		code.pop2(CODE.TMP0, CODE.TMP1);
		code.push( CODE.TMP0 );
		code.add( new LC3NOT(CODE.TMP0, CODE.TMP0));
		code.add( new LC3ADD(CODE.TMP0, CODE.TMP0, 1));
		code.add( new LC3ADD(CODE.TMP1, CODE.TMP1, CODE.TMP0));
		code.pop( CODE.TMP0 );
		code.add( eraseloop );
		code.add( new LC3STR( CODE.CONST0, CODE.TMP0, 0));
		code.add( new LC3ADD( CODE.TMP0, CODE.TMP0, 1));
		code.add( new LC3ADD( CODE.TMP1, CODE.TMP1, -1));
		code.add( new LC3BRP(eraseloop));
		code.add( new LC3RET());

		code.comment(" multiplication routine ");
		code.commentline(" expects operands on top of stack ");
		code.add( code.multiply);
		LC3label l = code.newLabel();
		LC3label zero = code.newLabel();
		LC3label apos = code.newLabel();
		LC3label aneg_bpos = code.newLabel();
		LC3label negateb = code.newLabel();
		LC3label mullab = code.newLabel();
		LC3label nosign = code.newLabel();

		code.commentline(" while loop that multiplies a and b, R7 is sum ");

		code.pop2(CODE.TMP0, CODE.TMP1);
		code.commentline(" get a and b");
		code.commentline(" check signs ");
		code.commentline(" CONST0 is used to store the flag of the result");
		code.commentline(" 0 means positive (default), 1 negative");
		code.add( new LC3ADD( CODE.TMP0, CODE.TMP0, 0));
		code.commentline(" if one is zero we're done");
		code.add( new LC3BRZ(zero));
		code.commentline(" if a is positive, jump");
		code.add( new LC3BRP(apos));
		code.commentline(" negate a ");
		code.add( new LC3NOT(CODE.TMP0, CODE.TMP0));
		code.add( new LC3ADD(CODE.TMP0, CODE.TMP0, 1));
		code.add( new LC3ADD( CODE.TMP1, CODE.TMP1, 0));
		code.commentline(" if one is zero we're done");
		code.add( new LC3BRZ(zero));
		code.commentline(" if b is positive, jump");
		code.add( new LC3BRP(aneg_bpos));
		code.commentline(" a is neg, b is too");
		code.commentline(" negate b");
		code.add( negateb );
		code.add( new LC3NOT(CODE.TMP1, CODE.TMP1));
		code.add( new LC3ADD(CODE.TMP1, CODE.TMP1, 1));
		code.commentline(" go, multiply!");
		code.add( new LC3BR(l));
		code.add( aneg_bpos);
		code.commentline(" a is negative, b is positive");
		code.commentline(" set flag to 1 for negative result");
		code.add( new LC3ADD(CODE.CONST0, CODE.CONST0, 1));
		code.commentline(" go, multiply!");
		code.add( new LC3BR(l));
		code.add( apos );
		code.commentline( " a is positive ");
		code.add( new LC3ADD( CODE.TMP1, CODE.TMP1, 0));
		code.commentline(" if one is zero we're done");
		code.add( new LC3BRZ(zero));
		code.commentline(" if b is positive, go multiply!");
		code.add( new LC3BRP(l));
		code.commentline(" a is pos, b is neg");
		code.commentline(" set flag to 1 for negative result");
		code.add( new LC3ADD(CODE.CONST0, CODE.CONST0, 1));
		code.add( new LC3BR( negateb ));

		code.comment(" multiply ");
		code.add(l);
		code.push(CODE.RET);
		code.commentline(" reset sum");
		code.add( new LC3AND(CODE.RET, CODE.RET, 0));
		code.add(mullab);
		code.add( new LC3ADD( CODE.RET, CODE.RET, CODE.TMP0));  
		code.add( new LC3ADD( CODE.TMP1, CODE.TMP1, -1));
		code.add( new LC3BRP(mullab) );
		code.commentline(" adjust sign ");
		code.add( new LC3ADD( CODE.CONST0, CODE.CONST0, 0));
		code.add( new LC3BRZ( nosign ));
		code.add( new LC3NOT(CODE.RET, CODE.RET));
		code.add( new LC3ADD(CODE.RET, CODE.RET, 1));
		code.add(nosign);
		code.commentline( " reset CONST0 ");
		code.add( new LC3AND( CODE.CONST0, CODE.CONST0, 0));
		code.commentline(" transfer sum to TMP0, get RET from stack");
		code.add( new LC3ADD( CODE.TMP0, CODE.RET, 0));
		code.pop(CODE.RET);
		code.commentline( " result in R0 ");
		code.push(CODE.TMP0);
		code.add( new LC3RET());
		code.commentline( " one factor was 0");
		code.add( zero );
		code.push(CODE.CONST0);
		code.add(new LC3RET());


		code.comment(" null pointer exception ");
		code.commentline( " prints error message and exits");
		code.add( code.nullpointer );

		LC3label npeerrmsg = code.newLabel();

		code.add( new LC3LEA( CODE.TMP0, npeerrmsg ));
		code.add( new LC3TRAP(0x22));
		code.add( new LC3TRAP(0x25));
		code.add( npeerrmsg);
		code.add( new LC3string("Null pointer exception\\n"));

		code.comment(" index out of bounds exception ");
		code.commentline( " prints error message and exits");
		code.add( code.indexoutofbounds );

		LC3label iooberrmsg = code.newLabel();

		code.add( new LC3LEA( CODE.TMP0, iooberrmsg ));
		code.add( new LC3TRAP(0x22));
		code.add( new LC3TRAP(0x25));
		code.add( iooberrmsg);
		code.add( new LC3string("Index out of bounds exception\\n"));

		code.comment(" add two strings ");
		code.commentline( " expects args on top of stack, puts result on stack");
		code.add( code.addstrings );

		LC3label stringlab = code.newLabel();
		LC3label stringcont = code.newLabel();
		LC3label newmethod = code.newLabel();
		LC3label anotnull = code.newLabel();
		LC3label bnotnull = code.newLabel();
		LC3label nullstring = code.newLabel();
		LC3label _nullstring = code.newLabel();

		code.add( new LC3LDR( CODE.TMP0, CODE.SP, -2 ));
		code.add( new LC3BRNP(anotnull));
		code.add( new LC3LEA( CODE.TMP0, nullstring));
		code.add( new LC3STR( CODE.TMP0, CODE.SP, -2));
		code.add( new LC3BR( bnotnull));
		code.add(anotnull);
		code.add( new LC3LDR( CODE.TMP0, CODE.SP, -1 ));
		code.add( new LC3BRNP(bnotnull));
		code.add( new LC3LEA( CODE.TMP0, nullstring));
		code.add( new LC3STR( CODE.TMP0, CODE.SP, -1));
		code.add( new LC3BR( bnotnull));
		code.add( nullstring);
		code.add( new LC3labeldata( _nullstring ));
		code.add( new LC3int(5));
		code.add( _nullstring);
		code.add( new LC3string("null"));
		code.add(bnotnull);
		code.pop2( CODE.TMP0, CODE.TMP1 );
		code.push( CODE.RET);
		code.commentline(" compute combined length ");
		code.add( new LC3LDR(CODE.RET, CODE.TMP0, 1));
		code.push(CODE.TMP0);
		code.add( new LC3ADD(CODE.TMP0, CODE.RET, 0));
		code.add( new LC3LDR(CODE.RET, CODE.TMP1, 1));
		code.push(CODE.TMP1);
		code.add( new LC3ADD(CODE.TMP0, CODE.TMP0, CODE.RET));
		code.add( new LC3BR(stringcont));
		code.add(newmethod);
		code.add( new LC3labeldata(code.newroutine));
		code.add(stringlab);
		try {
			code.add( new LC3int(IR.classes.lookup("String").getSize()));
		} catch (ClassNotFound exc) {
			System.err.println("Could not find class String. This cannot happen.");
			System.exit(-1);
		}

		code.commentline(" copy string ");

		LC3label loopentry = code.newLabel();
		LC3label loopentryaddr = code.newLabel();

		code.add(loopentryaddr);
		code.add( new LC3labeldata(loopentry));

		code.add(loopentry);
		code.push(CODE.RET);
		code.add( new LC3LDR(CODE.TMP0, CODE.TMP0, 0) );

		LC3label loopdone = code.newLabel();
		LC3label loop = code.newLabel();
		code.add(loop);
		code.add( new LC3LDR( CODE.RET, CODE.TMP0, 0 ));
		code.add( new LC3BRZ(loopdone));
		code.add( new LC3STR( CODE.RET, CODE.TMP1, 0));
		code.add( new LC3ADD( CODE.TMP0, CODE.TMP0, 1));
		code.add( new LC3ADD( CODE.TMP1, CODE.TMP1, 1));
		code.add( new LC3BR(loop));
		code.add( loopdone );
		code.pop(CODE.RET);
		code.add( new LC3RET());

		code.add(stringcont);

		code.commentline(" invoke new method ");
		code.add( new LC3LD(CODE.TMP1, stringlab));
		code.add( new LC3ADD(CODE.TMP0, CODE.TMP1, CODE.TMP0));
		code.push(CODE.TMP0);
		code.add( new LC3LD(CODE.TMP1, newmethod));
		code.add( new LC3JSRR(CODE.TMP1));

		// now HP contains reference to the new object

		// the string is stored after the object
		code.commentline(" initialize object pointed to by HP ");
		code.commentline(" initialize string ");

		code.add( new LC3ADD(CODE.TMP0, CODE.HP, 2) );
		code.add( new LC3STR(CODE.TMP0, CODE.HP, 0) );

		// set the length
		code.commentline(" initialize length ");
		code.pop(CODE.TMP0);
		code.add( new LC3STR(CODE.TMP0, CODE.HP, 1) );

		// now we "only" need to copy the two strings...

		code.commentline(" get two strings ");
		code.pop2(CODE.TMP0, CODE.TMP1);

		// we start with the lhs, so back to the stack with you...
		code.push(CODE.TMP1);

		code.commentline(" copy first string from TMP0->0 to HP->0 ");
		code.add( new LC3LDR(CODE.TMP1, CODE.HP, 0) );
		// code.pop(CODE.TMP0);
		code.add( new LC3LD(CODE.RET, loopentryaddr));
		code.add( new LC3JSRR( CODE.RET));

		code.commentline(" copy second string from TMP0->0 to HP->0 ");
		code.pop(CODE.TMP0);
		code.add( new LC3LD(CODE.RET, loopentryaddr));
		code.add( new LC3JSRR( CODE.RET));
		code.pop( CODE.RET );
		code.push( CODE.HP );
		code.add( new LC3RET() );


	}

	private void prepareIR(MJProgram e, CODE code) throws CodeGenException {

		computeClassSizes(e, code);
		annotateMethods(e, code);
		assignVariableOffsets(e);
		assignMethodOffsets(e);
		computeStackSizes(e);

	}

	private void computeClassSizes(MJProgram e, CODE code) throws CodeGenException {

		for (MJClass c : e.getClasses()) {
			LC3label vmtlabel = code.newLabel();
			c.setVMTlabel(vmtlabel);

			computeClassSizeHelper(c, new HashSet<MJClass>());
		}
	}

	private void annotateMethods(MJProgram e, CODE code) {
		for (MJClass c : e.getClasses()) {

			for (MJMethod m : c.getMethodList()) {

				// assign labels to each method

				LC3label l = code.newLabel();
				m.setLabel(l);

			}
		}

	}

	private void assignVariableOffsets(MJProgram e) throws CodeGenException {
		for (MJClass c : e.getClasses()) {

			if (Compiler.isDebug()) {
				System.err.println("Class "+c.getName());
			}

			assignFieldOffsets(0, c);

			for (MJMethod m : c.getMethodList()) {

				if (Compiler.isDebug()) {
					System.err.println("  Method "+m.getName());
				}

				int offset = 0;
				offset = assignOffsets(offset, m.getParameters());

				offset = findLocalVars(offset, m.getBody());

				m.setVarCount(offset);
			}
		}

	}

	private void assignMethodOffsets(MJProgram e) {

		HashSet<MJClass> visited = new HashSet<MJClass>();
		
		for (MJClass c : e.getClasses()) {
			assignMethodOffsetsHelper(c, visited);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void assignMethodOffsetsHelper(MJClass c, HashSet<MJClass> visited) {

		int slot=0;
		HashMap<Integer, MJMethod> methodTable = new HashMap<Integer, MJMethod>();
		
		if (visited.contains(c)) return;
		visited.add(c);
		
		if (!c.isTop()) {
			MJClass sc = c.getSuperClass().getDecl();
			assignMethodOffsetsHelper(sc, visited);
			methodTable = (HashMap<Integer,MJMethod>)sc.getMethodTable().clone();
			slot = methodTable.size();
		}
		
		c.setMethodTable(methodTable);
		for (MJMethod m : c.getMethodList()) {
			if (m.isStatic()) continue;
			
			// Check whether a super class defines a method with the same parameter types
			
			String methodName = m.getName();
				
			MJMethod goal = null;
			boolean match = false;
			
			for (MJMethod sm : methodTable.values()) {
				if (!sm.getName().equals(methodName)) continue;
				if (sm.getParameters().size()!=m.getParameters().size()) continue;

				LinkedList<MJVariable> methodParameters = m.getParameters();
				LinkedList<MJVariable> supermethodParameters = sm.getParameters();

				match = true;

				for (int i=1;i<methodParameters.size();i++) {
					MJVariable par = methodParameters.get(i);
					MJVariable superpar = supermethodParameters.get(i);

					if (!par.getType().isSame(superpar.getType())) {
						match = false;
						break;
					}
				}

				if (match==false)
					continue;

				goal=sm;

				break;
	
			}

			if (match==false) {
				m.setSlot(slot);
				slot++;
			} else {
				m.setSlot(goal.getSlot());
			}
			
			methodTable.put(m.getSlot(), m);
		}

	}

	private void computeStackSizes(MJProgram e) {
		for (MJClass c : e.getClasses()) {

			if (Compiler.isDebug()) {
				System.err.println("Class " + c.getName());
			}

			int maxStackSize;

			for (MJMethod m : c.getMethodList()) {

				if (Compiler.isDebug()) {
					System.err.print("  Method " + m.getName());
				}

				maxStackSize = 3;

				maxStackSize += m.getVarCount();

				maxStackSize += getMaxStackSize.visit(m.getBody());

				// if the method returns something the stack size must at least
				// be 4

				if (!m.getReturnType().isVoid() && maxStackSize == 3) {
					maxStackSize = 4;
				}

				System.err.println(" MSS " + maxStackSize);
				m.setMaxStackSize(maxStackSize);
			}
		}
	}


	private void computeClassSizeHelper(MJClass c, HashSet<MJClass> visited) throws CodeGenException {

		int size=1;

		if (visited.contains(c)) return;

		visited.add(c);

		if (!c.isTop()) {
			MJClass sc = c.getSuperClass().getDecl();
			computeClassSizeHelper(sc, visited);
			size=sc.getSize();
		}

		for (MJVariable f : c.getFieldList()) {
			int fieldsize = f.getType().getSize();

			size += fieldsize;
		}

		c.setSize(size);

		if (Compiler.isDebug()) {
			System.err.println("Size Class " + c.getName() + ": " + size);
		}

	}
	
	private void addRuntimeSetup(MJProgram e, CODE code) throws CodeGenException {
        code.commentline(" initialize CONST0 and CONST1");
        code.add(new LC3AND(CODE.CONST0, CODE.CONST0, 0));
        code.add(new LC3ADD(CODE.CONST1, CODE.CONST0, 1));
        LC3label cont = code.newLabel();
        LC3label data = code.newLabel();
        LC3label heap = code.newLabel();
        code.commentline(" set SFP and HP ");
        code.add( new LC3LD(CODE.SFP, data));
        code.add( new LC3LD(CODE.HP, heap));
        code.add( new LC3BR(cont) );
        code.commentline(" data for SFP and HP");
        code.add(data);
        code.add( new LC3int(code.getFramestack()) );
        code.add(heap);
        code.add( new LC3int(code.getHeap()));
        String[] args = Compiler.getArguments();
        if (args.length>0) {
            code.commentline(" arguments to main");
            code.commentline(" string contents");
            LC3label[] contlabs = new LC3label[args.length];
            LC3label[] strlabs = new LC3label[args.length];
            for (int i=0;i<args.length;i++) {
                contlabs[i] = code.newLabel();
                code.add( contlabs[i] );
                code.add( new LC3string(args[i]));
            }
            code.commentline(" strings");
            for (int i=0;i<args.length;i++) {
                strlabs[i] = code.newLabel();
                code.add( strlabs[i]);
                code.add( new LC3labeldata(contlabs[i]) );
                code.add( new LC3int(args[i].length()+1));    
            }
            code.commentline(" args array");
            code.add(code.arguments);
            code.add(new LC3int(args.length));
            for (int i=0;i<args.length;i++) {
                code.add( new LC3labeldata(strlabs[i]));
            }
        }
        code.comment("Virtual Method Tables");
        
        for (MJClass c : e.getClasses()) {
            LC3label vmtlabel = c.getVMTlabel();
            LC3label actlabel = code.newLabel();
            
            code.comment("BEGIN " + c.getName()+" virtual method table");
            code.add(vmtlabel);
            code.add( new LC3labeldata(actlabel));
            code.add(actlabel);
            if (c.getMethodTable().size()==0) {
                code.comment("DUMMY value for empty VMT");
                code.add(new LC3int(0));
            }
            for (int idx=0; idx<c.getMethodTable().size(); idx++) {
                MJMethod m = c.getMethodTable().get(idx);
                code.commentline(" "+idx+" "+m.getName());
                code.add( new LC3labeldata(m.getLabel()));
            }
            code.comment("END " + c.getName()+" virtual method table");
        }
        code.add(cont);
    }
}
