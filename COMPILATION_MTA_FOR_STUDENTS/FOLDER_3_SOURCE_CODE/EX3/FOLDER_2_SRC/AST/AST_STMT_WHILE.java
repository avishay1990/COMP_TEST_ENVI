package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;
	public int posX;
	public int  posY;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_WHILE(AST_EXP cond,AST_STMT_LIST body,int posY, int posX)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.print("====================== WHILE STMS\n");

		this.cond = cond;
		this.body = body;
		this.posX = posX;
		this.posY = posY - 1;
	}
	public void PrintMe()
		{
			/*************************************/
			/* AST NODE TYPE = AST SUBSCRIPT VAR */
			/*************************************/
			System.out.print("AST NODE STMT WHILE\n");

			/**************************************/
			/* RECURSIVELY PRINT left + right ... */
			/**************************************/
			if (cond != null) cond.PrintMe();
			if (body != null) body.PrintMe();

			/***************************************/
			/* PRINT Node to AST GRAPHVIZ DOT file */
			/***************************************/
			AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"WHILE (left)\nTHEN right");

			/****************************************/
			/* PRINT Edges to AST GRAPHVIZ DOT file */
			/****************************************/
			if (cond != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);
			if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
		}


		public TYPE SemantMe()
		{
			/****************************/
			/* [0] Semant the Condition */
			/****************************/
			if (cond == null)
				{
					System.out.format(">> ERROR [%d:%d] condition is null\n",this.posY,this.posX);
					System.exit(0);

				}

			if (cond.SemantMe() != TYPE_INT.getInstance())
			{
				System.out.format(">> ERROR [%d:%d] condition inside IF is not integral\n",this.posY,this.posX);
			}

			/*************************/
			/* [1] Begin Class Scope */
			/*************************/
			SYMBOL_TABLE.getInstance().beginScope();

			/***************************/
			/* [2] Semant Data Members */
			/***************************/
			body.SemantMe();

			/*****************/
			/* [3] End Scope */
			/*****************/
			SYMBOL_TABLE.getInstance().endScope();

			/*********************************************************/
			/* [4] Return value is irrelevant for class declarations */
			/*********************************************************/
			return null;
		}

}
