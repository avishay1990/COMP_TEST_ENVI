package AST;

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;
	public int posX;
	public int  posY;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_WHILE(AST_EXP cond,AST_STMT_LIST body,int posX, int posY)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.print("====================== WHILE STMS\n");

		this.cond = cond;
		this.body = body;
		this.posX = posX;
		this.posY = posY;
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


}
