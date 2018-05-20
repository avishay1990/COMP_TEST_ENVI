package AST;

import TYPES.*;

public class AST_DEC_LIST extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_DEC head;
	public AST_DEC_LIST tail;
	public int  type;
	public int posX;
	public int  posY ;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_LIST(AST_DEC head,AST_DEC_LIST tail, int type,int posY, int posX)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		if ( type == 1){
			if (tail != null) System.out.print("====================== Programs -> program Program\n");
			if (tail == null) System.out.print("====================== Programs -> Program      \n");
		}

		if ( type == 2){
			if (tail != null) System.out.print("====================== cfields -> cfiled cfields\n");
			if (tail == null) System.out.print("====================== cfields -> cfield      \n");
		}

		this.head = head;
		this.tail = tail;
		this.type = type;
		this.posX = posX;
		this.posY = posY -1;
	}

	public TYPE_LIST SemantMe()
	{
		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		TYPE t= null;
		TYPE_LIST res= null;
		if (head != null) t = head.SemantMe();
		if (tail != null) res= tail.SemantMe();

		if(type == 2 && tail == null)
		{
			return new TYPE_LIST(t,null);
		}
		else if (type == 2 )
		{
			return new TYPE_LIST(t,res);
		}
		else
		{
			return null;
		}
	}

	/********************************************************/
	/* The printing message for a declaration list AST node */
	/********************************************************/
	public void PrintMe()
	{
		/********************************/
		/* AST NODE TYPE = AST DEC LIST */
		/********************************/
		if (type == 1) System.out.print("AST NODE PROGRAM (DEC LIST)\n");
		if (type == 2) System.out.print("AST NODE CFILED LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null) head.PrintMe();
		if (tail != null) tail.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		if (type == 1) AST_GRAPHVIZ.getInstance().logNode(SerialNumber,"PROGRAM\n");
		if (type == 2) AST_GRAPHVIZ.getInstance().logNode(SerialNumber,"CFIELD\nLIST\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}
}
