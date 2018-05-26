package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
public class AST_STMT_RETURN extends AST_STMT
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_EXP exp;
	public int posX;
	public int  posY;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_RETURN(AST_EXP exp,int posY, int posX)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.print("====================== RETURN STMS\n");
		this.exp = exp;
		this.posX = posX;
		this.posY = posY - 1;
	}

	/************************************************************/
	/* The printing message for a function declaration AST node */
	/************************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE STMT RETURN\n");

		/*****************************/
		/* RECURSIVELY PRINT exp ... */
		/*****************************/
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"RETURN");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}


	public TYPE SemantMe()
	{
		TYPE function_type =SYMBOL_TABLE.getInstance().getFunctionType();


		if(function_type==null)
		{
				//error
				UTILS.Error("Function type not found",this.getClass().getName(),this.posY, this.posX);
				//System.out.format(">> ERROR [%d:%d] function type not found\n",this.posY,this.posX);

		}
		else
		{
				TYPE exp_type=null;
				if (exp != null)
				{
					exp_type=exp.SemantMe();
					if(exp_type!=null)
					{
						if(exp_type.getClass()==function_type.getClass())
						{
							return exp_type;
						}
						else
						{
							//error
							UTILS.Error("Exp type and function type are diffrent",this.getClass().getName(),this.posY, this.posX);

							//System.out.format(">> ERROR [%d:%d] exp type and function type are diffrent\n",this.posY,this.posX);

						}
					}
				}
				else
				{
					if(function_type instanceof TYPE_VOID)
					{
							return TYPE_VOID.getInstance();
					}
					else
					{
						//error
						UTILS.Error("fuction type not void, return with no type value!",this.getClass().getName(),this.posY, this.posX);

						//System.out.format(">> ERROR [%d:%d] fuction type not void, return with no type value! \n",this.posY,this.posX);
					}
				}
		}
		return null;
	}
}
