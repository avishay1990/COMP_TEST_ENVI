package AST;


import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;

public class AST_STMT_ASSIGN_NEW extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_EXP_VAR var;
	public String name;
	public int posX;
	public int  posY;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN_NEW(AST_EXP_VAR var,String name,int posY, int posX)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN NEW ID SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.name = name;
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
		System.out.print("AST NODE ASSIGN NEW STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/

		if (var != null) var.PrintMe();
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("ASSIGN\nleft := new (%s)\n", name));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}


	public TYPE SemantMe()
	{
		UTILS.Log("DOING SEMANTE ME FOR STMT ASSIGN NEW",this.getClass().getName(),this.posY, this.posX);

		TYPE t1 = null;
		TYPE t2 = null;

		if (var != null) t1 = var.SemantMe();
		UTILS.Log("VAR == " + var.toString(), this.getClass().getName(),this.posY, this.posX);

		t2 = SYMBOL_TABLE.getInstance().find(name);

		if(t1 != null && t1 instanceof TYPE_CLASS)
		{
			if(t2 == null)
			{
				UTILS.Error("SYMBOL " + var.toString() + "NOT EXIST", this.getClass().getName(),this.posY, this.posX);
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
