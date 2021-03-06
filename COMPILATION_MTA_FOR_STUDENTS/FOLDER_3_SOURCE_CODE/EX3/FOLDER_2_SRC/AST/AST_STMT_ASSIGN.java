package AST;

import TYPES.*;

public class AST_STMT_ASSIGN extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_EXP_VAR var;
	public AST_EXP exp;
	public int posX;
	public int  posY;


	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_EXP_VAR var,AST_EXP exp,int posY, int posX)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.exp = exp;
		this.posX = posX;
		this.posY = posY - 1;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (exp != null) exp.PrintMe();
		if (exp == null)  	System.out.print("STMS assign NIL to VAR\n");

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := right\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}
	public TYPE SemantMe()
	{
		UTILS.Log("DOING SEMANTE ME FOR STMT ASSIGN",this.getClass().getName(),this.posY, this.posX);

		TYPE t1 = null;
		TYPE t2 = null;

		if (var != null) t1 = var.SemantMe();
		UTILS.Log("VAR == " + var.toString(), this.getClass().getName(),this.posY, this.posX);

		if (exp != null) t2 = exp.SemantMe();
		UTILS.Log("EXP == " +  exp.toString(),this.getClass().getName(),this.posY, this.posX);

		if(t1 != null && t1 instanceof TYPE_CLASS )
		{
			if(t2 == null)
			{
				UTILS.Log("TO VAR: " + var.toString() + " ASSIGN NIL", this.getClass().getName(),this.posY, this.posX);
				return null;

			}
			else if(t2 instanceof TYPE_CLASS )
			{
				TYPE_CLASS t1Class = (TYPE_CLASS )t1;
				TYPE_CLASS t2Class = (TYPE_CLASS )t2;

				if( t1Class.name.equals(t2Class.name)) {
					UTILS.Log("TO VAR: " + var.toString() + " ASSIGN SAME CLASS", this.getClass().getName(), this.posY, this.posX);
					return null;
				}
					else
				{
					for(TYPE_CLASS tc =t2Class.father; tc !=null; tc=tc.father)
					{
						if (tc.name.equals(t1Class.name)) {
							UTILS.Log("TO VAR: " + var.toString() + " ASSIGN DERIVED CLASS", this.getClass().getName(), this.posY, this.posX);

							return null;

						}

					}

				}
			}
			else
			{
				UTILS.Error("TO VAR: " + var.toString() + " ASSIGN WRONG TYPE", this.getClass().getName(),this.posY, this.posX);

			}
		}

		if (t1 != t2)
		{
			UTILS.Error("type mismatch for var := exp",this.getClass().getName(),this.posY, this.posX);
			//System.out.format(">> ERROR [%d:%d] type mismatch for var := exp\n",this.posY,this.posX);
		}
		return null;
	}
}
