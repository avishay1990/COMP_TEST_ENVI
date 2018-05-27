package AST;

import TYPES.*;
import SYMBOL_TABLE.*;


public class AST_DEC_CLASS extends AST_DEC
{
	/********/
	/* NAME */
	/********/
	public String name;
	public String fatherClass = "";

	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_DEC_LIST data_members;
	public int posX;
	public int  posY;

    public    TYPE_CLASS_VAR_DEC_LIST MemberFiledTypes =null;


    /******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_CLASS(String name,AST_DEC_LIST data_members,int posY, int posX)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.format("====================== Class -> %s\n", name);

		this.name = name;
		this.data_members = data_members;
		this.posX = posX ;
		this.posY = posY - 1 ;
	}

	public AST_DEC_CLASS(String name,String fatherClass, AST_DEC_LIST data_members,int posY, int posX)
	{

		this(name,data_members,posY,posX);
		this.fatherClass = fatherClass;
		System.out.format("LOG[%d,%d] This class : %s implement inherinace from %s\n",this.posX, this.posY,this.name, this.fatherClass);
	}




	/*********************************************************/
	/* The printing message for a class declaration AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		System.out.format("CLASS DEC = %s\n",name);
		if (data_members != null) data_members.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("CLASS\n%s",name));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,data_members.SerialNumber);
	}

	public TYPE SemantMe()
	{
        UTILS.Log("DOING SEMANTE ME FOR FUNCTION",this.name,this.getClass().getName(),this.posY, this.posX);



        /*************************/
        /* [1] Check If already exist */
        /*************************/
        if(SYMBOL_TABLE.getInstance().isNameInScope(this.name))
        {
            UTILS.Error("Symbol already exist in scope",this.name,this.getClass().getName(),this.posY, this.posX);
        }
		/*************************/
		/* [1] Begin Class Scope */
		/*************************/

		SYMBOL_TABLE.getInstance().beginScope();

		/*************************/
		/* [1] Enter Dummy Class Name */
		/*************************/
		SYMBOL_TABLE.getInstance().enter(name,null);


		/***************************/
		/* [2] Semant Data Members */
		/***************************/
        TYPE_CLASS_VAR_DEC_LIST SuperAllMemberField =null;

        TYPE_CLASS t = null;
		TYPE fatherType = null;
		TYPE_CLASS fatherClassType = null;


		if (!this.fatherClass.equals(""))
		{
			fatherType = SYMBOL_TABLE.getInstance().find(this.fatherClass);
			if(fatherType == null)	UTILS.Error("Extend ID: " + fatherClass + " do not exist" ,this.name,this.getClass().getName(),this.posY, this.posX);
			if(!(fatherType instanceof TYPE_CLASS)) UTILS.Error("Extend ID: " + fatherClass + " exist but not class type" ,this.name,this.getClass().getName(),this.posY, this.posX);
			fatherClassType = (TYPE_CLASS)fatherType;
		}


        SuperAllMemberField = GetAllMembers(fatherClassType);



        if (data_members != null) MemberFiledTypes = data_members.SemantMeClass(SuperAllMemberField);

		//MarkAllMemberFieldAsSuper();


		t = new TYPE_CLASS(fatherClassType,name, MemberFiledTypes);





		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

        /*****************/
        /* [4] Mark all MemberFiled as SuperFiled for Next classes*/
        /*****************/


		/************************************************/
		/* [5] Enter the Class Type to the Symbol Table */
		/************************************************/
		SYMBOL_TABLE.getInstance().enter(name,t);

		/*********************************************************/
		/* [6] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;
	}

    TYPE_CLASS_VAR_DEC_LIST GetAllMembers( TYPE_CLASS father)
    {
        TYPE_CLASS_VAR_DEC_LIST t = null ;
        TYPE_CLASS_VAR_DEC_LIST res ;

        if (father != null) {

            // get first father class members
            res = father.data_members;
            father = father.father;


            while (father != null){
                res.ConcateTypeList(father.data_members);
                father = father.father;
            }
            return res;
        }
        else
            return null;


    }


    public void MarkAllMemberFieldAsSuper()
    {

        TYPE_FUNCTION tf= null;
        TYPE_CLASS_VAR_DEC_LIST t;
        for (t= this.MemberFiledTypes; t!= null; t= t.tail)
        {
            if(t.head.t instanceof TYPE_FUNCTION) {
                tf = (TYPE_FUNCTION) t.head.t;
                tf.MakeBelongToSuperClass();
                tf.isBelongToSuperClass = true;
                t.head.isFromSuperClass = true;
            }
        }

    }



}



