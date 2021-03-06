package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_VAR_FIELD extends AST_EXP_VAR
{
	public AST_EXP_VAR var;
	public String fieldName;
	public int posX;
	public int  posY;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_VAR_FIELD(AST_EXP_VAR var,String fieldName,int posY, int posXS)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.format("====================== var -> var DOT ID( %s )\n",fieldName);
		this.var = var;
		this.fieldName = fieldName;
		this.posX = posX;
		this.posY = posY - 1;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.format("FIELD\nNAME\n(___.%s)\n",fieldName);

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) var.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FIELD\nVAR\n___.%s",fieldName));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}
	public TYPE SemantMe()
	{
		TYPE t = null;
		TYPE_CLASS tc = null;

		/******************************/
		/* [1] Recursively semant var */
		/******************************/
		if (var != null) t = var.SemantMe();

		if(t== null)  			UTILS.Error("Symbol Not Found" ,this.fieldName,this.getClass().getName(),this.posY, this.posX);

		/*********************************/
		/* [2] Make sure type is a class */
		/*********************************/
		if (t.isClass() == false)
		{
			UTILS.Error("access field of a non-class variable" ,this.fieldName,this.getClass().getName(),this.posY, this.posX);
			//System.out.format(">> ERROR [%d:%d] access %s field of a non-class variable\n",this.posY,this.posX,fieldName);
			//System.exit(0);
		}
		else
		{
			tc = (TYPE_CLASS) t;
		}

		/************************************/
		/* [3] Look for fiedlName inside tc */
		/************************************/
		for (TYPE_CLASS_VAR_DEC_LIST it=tc.data_members;it != null;it=it.tail)
		{
			if (it.head.name.equals(fieldName))
			{
				return it.head.t;
			}
		}

		/*********************************************/
		/* [4] fieldName does not exist in class var */
		/*********************************************/
		UTILS.Error("field does not exist in class" ,this.fieldName,this.getClass().getName(),this.posY, this.posX);


		return null;
	}
}
