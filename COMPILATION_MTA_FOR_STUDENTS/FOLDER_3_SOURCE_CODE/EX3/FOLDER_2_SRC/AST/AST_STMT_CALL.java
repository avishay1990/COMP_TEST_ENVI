package AST;

import TYPES.*;
import SYMBOL_TABLE.*;


public class AST_STMT_CALL extends AST_STMT
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_EXP_CALL callExp;
	public int posX;
	public int  posY;


	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_CALL(AST_EXP_CALL callExp,int posY, int posX)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.print("====================== CALL EXP STMS \n");

		this.callExp = callExp;
		this.posX = posX;
		this.posY = posY - 1;
	}

	public void PrintMe()
	{
		callExp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STMT\nCALL"));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,callExp.SerialNumber);
	}


	public TYPE SemantMe()
	{
		if ( this.callExp !=null)
				return callExp.SemantMe();
		else
			{
				System.out.format(">> ERROR [%d:%d] CallExp is null\n",this.posY,this.posX);
				System.exit(0);
			}
			return null;

	}

}
