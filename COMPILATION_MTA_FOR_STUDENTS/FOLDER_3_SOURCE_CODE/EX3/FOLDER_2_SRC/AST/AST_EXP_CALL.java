package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_CALL extends AST_EXP
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public String funcName;
	public AST_EXP_LIST params;
	public int posX;
	public int  posY;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_CALL(String funcName,AST_EXP_LIST params,int posX, int posY)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		this.funcName = funcName;
		this.params = params;
		this.posX = posX;
		this.posY = posY;
	}

	/************************************************************/
	/* The printing message for a function declaration AST node */
	/************************************************************/
	public void PrintMe()
	{
		/*************************************************/
		/* AST NODE TYPE = AST NODE FUNCTION DECLARATION */
		/*************************************************/
		System.out.format("CALL(%s)\nWITH:\n",funcName);

		/***************************************/
		/* RECURSIVELY PRINT params + body ... */
		/***************************************/
		if (params != null) params.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("CALL(%s)\nWITH",funcName));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,params.SerialNumber);
	}

	public TYPE SemantMe()
	{
		TYPE te = SYMBOL_TABLE.getInstance().find(funcName);
		if (te instanceof TYPE_FUNCTION)
		{
			TYPE_FUNCTION t = (TYPE_FUNCTION) te;

			 if (t.Get_Return_Type() instanceof TYPE_CLASS
			 || t.Get_Return_Type() == TYPE_INT.getInstance()
			 || t.Get_Return_Type() == TYPE_VOID.getInstance() ){

					return t.Get_Return_Type();

			 	}else if( t.Get_Return_Type() == null)
				{
					System.out.format("---->ERROR[%d,%d]: IN AST_EXP_CALL Func Return is null",this.posX,this.posY);
				}else
				{
					System.out.format("---->ERROR[%d,%d]: IN AST_EXP_CALL Func Return Unknown TYPE",this.posX,this.posY);
				}

		}
		else if( te == null)
		{
			System.out.format("---->ERROR[%d,%d]: IN AST_EXP_CALL Instane is Null",this.posX,this.posY);
		}else
		{
			System.out.format("---->ERROR[%d,%d]: IN AST_EXP_CALL instance Not func",this.posX,this.posY);
		}
		return null;
	}


}
