package AST;

import TYPES.*;

public class AST_EXP_BINOP extends AST_EXP
{
	int OP;
	public AST_EXP left;
	public AST_EXP right;
	public int posX;
	public int  posY;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/

	public AST_EXP_BINOP(AST_EXP left,AST_EXP right,int OP,int posY, int posX)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.OP = OP;
		this.posX = posX;
		this.posY = posY - 1 ;

		System.out.format("====================== exp -> exp BINOP(%s) exp\n", convert_OP());

	}

	private String convert_OP ()
	{
		String sOP="";
		if (OP == 0) {sOP = "+";}
		if (OP == 1) {sOP = "-";}
		if (OP == 2) {sOP = "*";}
		if (OP == 3) {sOP = "/";}
		if (OP == 4) {sOP = "<";}
		if (OP == 5) {sOP = ">";}
		if (OP == 6) {sOP = "=";}

		return sOP;
	}

	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
		{

			/*********************************/
			/* CONVERT OP to a printable sOP */
			/*********************************/

			String sOP= convert_OP();

			/*************************************/
			/* AST NODE TYPE = AST SUBSCRIPT VAR */
			/*************************************/
			System.out.print("AST NODE BINOP EXP\n");
			System.out.format("BINOP EXP(%s)\n",sOP);

			/**************************************/
			/* RECURSIVELY PRINT left + right ... */
			/**************************************/
			if (left != null) left.PrintMe();
			if (right != null) right.PrintMe();

			/***************************************/
			/* PRINT Node to AST GRAPHVIZ DOT file */
			/***************************************/
			AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("BINOP(%s)",sOP));

			/****************************************/
			/* PRINT Edges to AST GRAPHVIZ DOT file */
			/****************************************/
			if (left  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,left.SerialNumber);
			if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,right.SerialNumber);
		}


	public TYPE SemantMe() {
		TYPE t1 = null;
		TYPE t2 = null;

		if (left != null) t1 = left.SemantMe();
		if (right != null) t2 = right.SemantMe();

		//if (right != null) t2 = right.SemantMe();

		if ((t1 == TYPE_INT.getInstance()) && (t2 == TYPE_INT.getInstance())) {
			return TYPE_INT.getInstance();
		} else if (t1 instanceof TYPE_CLASS && OP == 6 ) {
			if (t2 == null) return TYPE_INT.getInstance();
			if (t2 instanceof TYPE_CLASS) {
				TYPE_CLASS t1Class = (TYPE_CLASS) t1;
				TYPE_CLASS t2Class = (TYPE_CLASS) t2;

				if (t2Class.name.equals(t1Class.name)) return TYPE_INT.getInstance();
				else {
					TYPE_CLASS tc;
					for (tc = t2Class.father; tc != null; tc = tc.father) {
						if (tc.name.equals(t1Class.name)) {

							return TYPE_INT.getInstance();

						}

					}
					if (tc == null)
						UTILS.Error("right " + t2Class.name + " not derived left " + t1Class.name, "BINOP", this.getClass().getName(), this.posY, this.posX);


				}
			}

		} else {
			UTILS.Error("In This Binop not both sides are INT", this.getClass().getName(), this.posY, this.posX);


		}
		return null;
	}

}
