package AST;

import TYPES.*;

public class AST_TYPE_NAME_LIST extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_TYPE_NAME head;
	public AST_TYPE_NAME_LIST tail;
	public int posX;
	public int  posY;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_TYPE_NAME_LIST(AST_TYPE_NAME head,AST_TYPE_NAME_LIST tail,int posY, int posX)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		if (tail != null) System.out.print("====================== TYPE-NAMEs -> TYPE-NAME TYPE-NAMEs\n");
		if (tail == null) System.out.print("====================== TYPE-NAMEs -> TYPE-NAME      \n");

		this.head = head;
		this.tail = tail;
		this.posX = posX;
		this.posY = posY - 1;
	}

	/******************************************************/
	/* The printing message for a type name list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST TYPE NAME LIST */
		/**************************************/
		System.out.print("AST TYPE NAME LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null) head.PrintMe();
		if (tail != null) tail.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"TYPE-NAME\nLIST\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}

	public TYPE_LIST SemantMe()
	{
		if (tail == null)
		{
			return new TYPE_LIST(
				head.SemantMe(),
				null);
		}
		else
		{
			return new TYPE_LIST(
				head.SemantMe(),
				tail.SemantMe());
		}
	}
}
