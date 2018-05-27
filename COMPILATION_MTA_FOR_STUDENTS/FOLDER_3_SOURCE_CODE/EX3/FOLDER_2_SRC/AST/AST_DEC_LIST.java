package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
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
			if( type ==1 ) {
					TYPE_LIST res =SemantMeDeclaretion();
					 SemantMeMethodBody();
					 return res;
			}
			else
			{
				UTILS.Error("Enter to SemanteMe but object not type 1" + type ,this.getClass().getName(),this.posY, this.posX);

				return null;

			}
	}

	public TYPE_CLASS_VAR_DEC_LIST SemantMeClass( TYPE_CLASS_VAR_DEC_LIST superMember)
	{


        if (type ==2)
		{
				TYPE_CLASS_VAR_DEC_LIST DataMemberList= null;
				TYPE_CLASS_VAR_DEC_LIST MethodList = null;
				TYPE_CLASS_VAR_DEC_LIST ClassTypeList;

            	EnterAllSuperMemberTypeToSymbolTable(superMember);
				DataMemberList =	SemantMeDataMembers();
				MethodList = SemantMeMethod();




            if(MethodList!= null) SemantMeMethodBody();
				//LOG !!
				if (DataMemberList == null) UTILS.Log("Data Memeber List is NULL " + type ,this.getClass().getName(),this.posY, this.posX);
				if (MethodList == null) UTILS.Log("Method Memeber List is NULL " + type ,this.getClass().getName(),this.posY, this.posX);

				if (DataMemberList == null && MethodList == null) return null;
				else if(DataMemberList == null) return MethodList;
				else if(MethodList == null) return DataMemberList;
				else
				{
					DataMemberList.ConcateTypeList(MethodList);
					ClassTypeList = DataMemberList;
					return ClassTypeList;
				}
			}else
			{
				UTILS.Error("Enter to SemanteMeCLass but object not a class" + type ,this.getClass().getName(),this.posY, this.posX);
				return null;
			}

	}

	public TYPE_LIST SemantMeDeclaretion()
	{
		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/

		TYPE t= null;
		TYPE_LIST res= null;

		if (head != null) t = head.SemantMe();
		if (tail != null) res= tail.SemantMeDeclaretion();

		return null;
	}

		public TYPE_CLASS_VAR_DEC_LIST SemantMeDataMembers()
		{
			/*************************************/
			/* RECURSIVELY PRINT HEAD + TAIL ... */
			/*************************************/

			TYPE t= null;
			TYPE_CLASS_VAR_DEC tVarDec= null;
			TYPE_CLASS_VAR_DEC_LIST res= null;
			AST_DEC_VAR dc = null;

			if (tail != null)
			{
				res= tail.SemantMeDataMembers();
			}
			if (head != null && head instanceof AST_DEC_VAR)
			{	t = head.SemantMe();
				dc = (AST_DEC_VAR) head;
					if (dc != null) tVarDec = new TYPE_CLASS_VAR_DEC(t,dc.name);
			}
			else return res;

			if(tail == null)
			{
				return new TYPE_CLASS_VAR_DEC_LIST(tVarDec,null);
			}
			else
			{
				return new TYPE_CLASS_VAR_DEC_LIST(tVarDec,res);
			}


		}




	public TYPE_CLASS_VAR_DEC_LIST SemantMeMethod()
	{
		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/

		TYPE t= null;
		TYPE_CLASS_VAR_DEC tVarDec= null;
		TYPE_CLASS_VAR_DEC_LIST res= null;
		AST_DEC_FUNC dc = null;
		if (tail != null)
		{
			res= tail.SemantMeMethod();
		}
		if (head != null && head instanceof AST_DEC_FUNC)
		{	t = head.SemantMe();
			dc = (AST_DEC_FUNC) head;
				if (dc != null) tVarDec = new TYPE_CLASS_VAR_DEC(t,dc.name);
		}
		else return res;

		if(tail == null)
		{
			return new TYPE_CLASS_VAR_DEC_LIST(tVarDec,null);
		}
		else
		{
			return new TYPE_CLASS_VAR_DEC_LIST(tVarDec,res);
		}


	}

/*
	public TYPE_CLASS_VAR_DEC_LIST SemantMeMethod()
	{
		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
/*
		TYPE t= null;
		TYPE_CLASS_VAR_DEC_LIST res= null;

		if (tail != null) res= tail.SemantMeMethod();
		if (head != null && head instanceof AST_DEC_FUNC) t = head.SemantMe();
		else return res;

		if(tail == null)
		{
			return new TYPE_CLASS_VAR_DEC_LIST(t,null);
		}
		else
		{
			return new TYPE_CLASS_VAR_DEC_LIST(t,res);
		}
	}
*/
	public TYPE_LIST SemantMeMethodBody()
	{
		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/

		if (tail != null) tail.SemantMeMethodBody();
		if (head != null && head instanceof AST_DEC_FUNC)
		{
			AST_DEC_FUNC temp = (AST_DEC_FUNC)head;
			temp.SemantMeBody();
		}
		return null;
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








	void EnterAllSuperMemberTypeToSymbolTable( TYPE_CLASS_VAR_DEC_LIST t)

    {
        TYPE_CLASS_VAR_DEC h = null;
        if (t != null) {


            while (t != null) {
                h =t.head;

                if (h.t instanceof TYPE_INT)
                    SYMBOL_TABLE.getInstance().enter(h.name, TYPE_INT.getInstance());

                if (h.t instanceof TYPE_FUNCTION) {
                    TYPE_FUNCTION temp = (TYPE_FUNCTION) h.t;
                    SYMBOL_TABLE.getInstance().enter(h.name, new TYPE_FUNCTION(temp.returnType, temp.name, temp.params,true));
                }

                t= t.tail;

            }

        }
    }
}
