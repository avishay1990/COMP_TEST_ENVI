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
	public AST_EXP_CALL(String funcName,AST_EXP_LIST params,int posY, int posX)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		this.funcName = funcName;
		this.params = params;
		this.posX = posX;
		this.posY = posY - 1;
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

		//System.out.format("LOG: IN SEMANTE ME OF ASr EXP CALL\n",this.posY,this.posX);


		TYPE te = SYMBOL_TABLE.getInstance().find(funcName);
		if (te instanceof TYPE_FUNCTION)
		{
			TYPE_FUNCTION t = (TYPE_FUNCTION) te;

			 if (t.Get_Return_Type() instanceof TYPE_CLASS
			 || t.Get_Return_Type() == TYPE_INT.getInstance()
			 || t.Get_Return_Type() == TYPE_VOID.getInstance() ){

				 // DOINT SEMANTE ME TO THE PARAMETERS OD THE FUNCTION
	 				//System.out.format("LOG: GOING TO DO SEMANTE ME TO FUNC PARAMETERS",this.posY,this.posX);
	 				params.SemantMe();

					return t.Get_Return_Type();

			 	}else if( t.Get_Return_Type() == null)
				{
					UTILS.Error("Func Return is null" ,this.funcName,this.getClass().getName(),this.posY, this.posX);
					//System.out.format("---->ERROR[%d,%d]: IN AST_EXP_CALL Func Return is null\n",this.posY,this.posX);
				}else
				{
					UTILS.Error("Func Return Unknown TYPE" ,this.funcName,this.getClass().getName(),this.posY, this.posX);

					//System.out.format("---->ERROR[%d,%d]: IN AST_EXP_CALL Func Return Unknown TYPE\n",this.posY,this.posX);
				}

		}
		else if( te == null)
		{
			UTILS.Error("Instane is Null" ,this.funcName,this.getClass().getName(),this.posY, this.posX);

			//System.out.format("---->ERROR[%d,%d]: IN AST_EXP_CALL Instane is Null\n",this.posY,this.posX);
		}else
		{
			UTILS.Error("Instance Not func" ,this.funcName,this.getClass().getName(),this.posY, this.posX);

			//System.out.format("---->ERROR[%d,%d]: IN AST_EXP_CALL instance Not func, func does not exist\n",this.posY,this.posX);
		}




		return null;
	}



}
