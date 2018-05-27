package AST;

import TYPES.*;
import SYMBOL_TABLE.*;



public class AST_DEC_FUNC extends AST_DEC {
	/****************/
	/* DATA MEMBERS */
	/****************/
	public String returnTypeName;
	public String name;
	public AST_TYPE_NAME_LIST params;
	public AST_STMT_LIST body;

	TYPE returnType = null;
	TYPE_CLASS_VAR_DEC_LIST type_list = null;

	public int posX;
	public int posY;

	/******************/
	/* CONSTRUCTOR(S) */

	/******************/



	public AST_DEC_FUNC(
		String returnTypeName,
		String name,
		AST_TYPE_NAME_LIST params,
		AST_STMT_LIST body
		,int posY
		,int posX)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.format("====================== Function -> %s\n", name);

		this.returnTypeName = returnTypeName;
		this.name = name;
		this.params = params;
		this.body = body;
		this.posX = posX;
		this.posY = posY - 1;
	}

	/************************************************************/
	/* The printing message for a function declaration AST node */
	/************************************************************/
	public void PrintMe()
	{
		/*************************************************/
		/* AST NODE TYPE = AST NODE FUNCTION DECLARATION */
		/*************************************************/
		System.out.format("FUNC(%s):%s\n",name,returnTypeName,posX, posY);

		/***************************************/
		/* RECURSIVELY PRINT params + body ... */
		/***************************************/
		if (params != null) params.PrintMe();
		if (body   != null) body.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FUNC(%s)\n:%s\n",name,returnTypeName));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (params != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,params.SerialNumber);
		if (body   != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}

	public TYPE SemantMe()
	{

		TYPE_FUNCTION t;
		UTILS.Log("DOING SEMANTE ME FOR FUNCTION",this.name,this.getClass().getName(),this.posY, this.posX);


		/*******************/
		/* [0] return type */
		/*******************/
		GetReturnType();
		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		SYMBOL_TABLE.getInstance().beginScope(returnType.TypeName(), returnType);

		/***************************/
		/* [2] Semant Input Params */
		/***************************/
		SemanteFunctionParmeters();


		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		CheckIfNameInScope();

		/***************************************************/
		/* [5] Enter the Function Type to the Symbol Table */
		/***************************************************/
		t = new TYPE_FUNCTION(returnType,name,type_list);
		SYMBOL_TABLE.getInstance().enter(name,t);

		/*********************************************************/
		/* [6] Return value is irrelevant for class declarations */
		/*********************************************************/
		return t;
	}


	public TYPE SemantMeBody()
	{
		TYPE returnType = null;

		returnType = SYMBOL_TABLE.getInstance().find(this.returnTypeName);





		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		SYMBOL_TABLE.getInstance().beginScope(returnType.TypeName(), returnType);


		/****************************/
		/* [2] Enter Parmeters to Symbol table*/
		/****************************/
		EnterParmeterToSymbolTable();
		/****************************/
		/* [3] SemanteMe Body*/
		/****************************/
		TYPE t= body.SemantMe();

			/*****************/
			/* [3] End Scope */
			/*****************/
			SYMBOL_TABLE.getInstance().endScope();

			return t;
	}

void SemanteFunctionParmeters() {
	TYPE t;
	TYPE name=null;
	TYPE_CLASS_VAR_DEC parmeter = null;
	TYPE_CLASS_VAR_DEC_LIST temp=null;
	this.type_list = null;

	for (AST_TYPE_NAME_LIST it = params; it != null; it = it.tail) {
		t = SYMBOL_TABLE.getInstance().find(it.head.type);

		name = SYMBOL_TABLE.getInstance().find(it.head.name);
		if (t == null) {
			UTILS.Error("non existing type" + it.head.type, this.name, this.getClass().getName(), this.posY, this.posX);
		}else if (!t.name.equals(it.head.type)  || !(t == TYPE_INT.getInstance()  || t instanceof TYPE_CLASS ))
		{
			UTILS.Error("Non exist Return type of this Parmeter" , this.name ,this.getClass().getName(),this.posY, this.posX);
		}else if (name != null)
			{
				/*****************/
				/* [Check if the name already in Scope */
				/*****************/
				UTILS.Error("This name already exist in the scope: " + it.head.name, this.name, this.getClass().getName(), this.posY, this.posX);

			} else {
			UTILS.Log("Enter type" + it.head.type, this.name, this.getClass().getName(), this.posY, this.posX);
			{
				t = it.head.SemantMe();

				parmeter = new TYPE_CLASS_VAR_DEC(t,it.head.name);
			}
			if(this.type_list == null) {
                this.type_list = new TYPE_CLASS_VAR_DEC_LIST(parmeter, null);
                temp = this.type_list;
			}
			    else{

                temp.tail = new  TYPE_CLASS_VAR_DEC_LIST(parmeter, null);
                temp= temp.tail;
			}


		}
	}
}

void CheckIfNameInScope() {

	TYPE t =SYMBOL_TABLE.getInstance().GetNameFromScope(this.name);
	TYPE_FUNCTION tf;
	TYPE_CLASS t1 =null;
	TYPE_CLASS t2= null;
	TYPE_CLASS_VAR_DEC_LIST l1=null,l2=null;

	if(t == null ) return;
    if(!(t instanceof TYPE_FUNCTION))  UTILS.Error("Symbol already exist", this.name, this.getClass().getName(), this.posY, this.posX);
		tf = (TYPE_FUNCTION) t;
		//if(this.type_list== null  && tf.params ==null) UTILS.Error("Function overloading is not allowed", this.name, this.getClass().getName(), this.posY, this.posX);
        if(tf.isBelongToSuperClass)
        {
        if(tf.returnType instanceof TYPE_CLASS && this.returnType instanceof TYPE_CLASS && !tf.returnType.name.equals(this.returnType.name))
            UTILS.Error("Return TYPE NOT EQUAL", this.name, this.getClass().getName(), this.posY, this.posX);
        else if(tf.returnType != this.returnType)
                UTILS.Error("Return TYPE NOT EQUAL", this.name, this.getClass().getName(), this.posY, this.posX);

            for (l1 = this.type_list, l2 = tf.params; l1 != null && l2 != null; l1 = l1.tail, l2 = l2.tail) {

                //Check if both Class
                if (l1.head.t instanceof TYPE_CLASS && l2.head.t instanceof TYPE_CLASS) {
                    t1 = (TYPE_CLASS) l1.head.t;
                    t2 = (TYPE_CLASS) l2.head.t;
                    if (!(t1.name.equals(t2.name)))
                    {
                        UTILS.Error("Function overloading is not allowed", this.name, this.getClass().getName(), this.posY, this.posX);
                    }
                    if ((l1 == null && l2 == null)) return;
                }
                }
                if ((l1 != null && l2 == null) || (l1 == null && l2 != null) ) UTILS.Error("Function overloading is not allowed", this.name, this.getClass().getName(), this.posY, this.posX);




        }else
            {
                UTILS.Error("Function overloading is not allowed", this.name, this.getClass().getName(), this.posY, this.posX);
            }



            //Check is param list are the same size


		//else UTILS.Error("Function overloading is not allowed", this.name, this.getClass().getName(), this.posY, this.posX);

	}

 void GetReturnType()
{
	returnType = SYMBOL_TABLE.getInstance().find(this.returnTypeName);

	if (returnType == TYPE_INT.getInstance() || returnType == TYPE_VOID.getInstance() ||( returnType instanceof TYPE_CLASS ) )
	{
		UTILS.Log("the Return type of this function is " + returnType.TypeName(), this.name ,this.getClass().getName(),this.posY, this.posX);

	}
	else if (returnType == null)
	{
		UTILS.Log("non existing return type", this.name ,this.getClass().getName(),this.posY, this.posX);

	}
	else
	{
		UTILS.Error("The Return Value of this function is not Class, Int Or Void",this.name,this.getClass().getName(),this.posY, this.posX);

		//System.out.format(">> ERROR [%d:%d] The Return Value of this function is not Class, Int Or Void\n",this.posY,this.posX);
	}
}

	void EnterParmeterToSymbolTable()
	{


		for (TYPE_CLASS_VAR_DEC_LIST it = type_list; it != null; it = it.tail) {
			SYMBOL_TABLE.getInstance().enter(it.head.name, it.head.t);
		}
	}
}


//    void CheckIfNameInScope() {
//
//        TYPE t =SYMBOL_TABLE.getInstance().GetNameFromScope(this.name);
//        TYPE_FUNCTION tf;
//        TYPE_CLASS t1 =null;
//        TYPE_CLASS t2= null;
//        TYPE_CLASS_VAR_DEC_LIST l1=null,l2=null;
//
//        if(t == null ) return;
//        if(!(t instanceof TYPE_FUNCTION))  UTILS.Error("Not a function", this.name, this.getClass().getName(), this.posY, this.posX);
//        tf = (TYPE_FUNCTION) t;
//        if(this.type_list== null  && tf.params ==null) UTILS.Error("Function overloading is not allowed", this.name, this.getClass().getName(), this.posY, this.posX);
//
//        for (l1 = this.type_list, l2 = tf.params; l1 != null && l2 != null; l1 = l1.tail, l2 = l2.tail) {
//
//            //Check if both Class
//            if (l1.head.t instanceof TYPE_CLASS && l2.head.t instanceof TYPE_CLASS) {
//                t1 = (TYPE_CLASS) l1.head.t;
//                t2 = (TYPE_CLASS) l2.head.t;
//                if (!(t1.name.equals(t2.name))) return;
//            }
//
//            //Check if Both Int
//            else if (!(l1.head.t == TYPE_INT.getInstance() && l2.head.t == TYPE_INT.getInstance())) {
//                return;
//            }
//        }
//
//
//        //Check is param list are the same size
//        if ((l1 == null && l2 != null) || (l1 != null && l2 == null)) return;
//
//        else UTILS.Error("Function overloading is not allowed", this.name, this.getClass().getName(), this.posY, this.posX);
//
//    }
