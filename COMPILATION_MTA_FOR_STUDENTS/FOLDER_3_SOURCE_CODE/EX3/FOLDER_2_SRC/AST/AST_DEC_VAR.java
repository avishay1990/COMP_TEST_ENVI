package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_VAR extends AST_DEC
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public String type;
	public String name;
	public AST_EXP initialValue;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_VAR(String type,String name,AST_EXP initialValue)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.format("====================== VAR DECLARATION -->  %s \n", name);


		this.type = type;
		this.name = name;
		this.initialValue = initialValue;
	}

	/********************************************************/
	/* The printing message for a declaration list AST node */
	/********************************************************/
	public void PrintMe()
	{
		/********************************/
		/* AST NODE TYPE = AST DEC LIST */
		/********************************/
		if (initialValue != null) System.out.format("VAR-DEC(%s):%s := initialValue\n",name,type);
		if (initialValue == null) System.out.format("VAR-DEC(%s):%s                \n",name,type);

		/**************************************/
		/* RECURSIVELY PRINT initialValue ... */
		/**************************************/
		if (initialValue != null) initialValue.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("VAR\nDEC(%s)\n:%s",name,type));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (initialValue != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,initialValue.SerialNumber);

	}

	public TYPE SemantMe()
	{
		TYPE t;
		TYPE intValues= null;

		/****************************/
		/* [1] Check If Type exists */
		/****************************/
		t = SYMBOL_TABLE.getInstance().find(type);

		if(t == TYPE_VOID.getInstance()) System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,type);

		if (t == null)
		{
			System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,type);
			System.exit(0);
		}

		/**************************************/
		/* [2] Check That Name does NOT exist in the currnet scope */
		/**************************************/
		if (SYMBOL_TABLE.getInstance().isNameInScope(name))
		{
			System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n",2,2,name);
		}


		/***************************************************/
		/* [3] Check if right type equal to left type */
		/***************************************************/
		if (this.initialValue != null)
		{
				intValues= this.initialValue.SemantMe();
				if (intValues!=null &&  intValues.getClass() == t.getClass())
				{
					System.out.format(">> LOG [%d:%d] Both types are the same. %s %s\n",2,2,type , intValues.TypeName());
				}
				else
				{
					System.out.format(">> ERROR [%d:%d] declaration type and init values are not the same. %s %s\n",2,2,type , intValues.TypeName());
					System.exit(0);
				}
		}
		else{
			System.out.format(">> LOG [%d:%d] initial values are null\n",2,2);
		}


		/***************************************************/
		/* [3] Enter the Function Type to the Symbol Table */
		/***************************************************/
		SYMBOL_TABLE.getInstance().enter(name,t);

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return t;
	}

}