package TYPES;

public class TYPE_FUNCTION extends TYPE
{
	/***********************************/
	/* The return type of the function */
	/***********************************/
	public TYPE returnType;

	/*************************/
	/* types of input params */
	/*************************/
	public TYPE_CLASS_VAR_DEC_LIST params;


	public boolean isBelongToSuperClass = false;


	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FUNCTION(TYPE returnType,String name,TYPE_CLASS_VAR_DEC_LIST params, boolean isBelongToSuperClass)
	{
		this(returnType,name,params);
		this.isBelongToSuperClass = isBelongToSuperClass;
	}


	public TYPE_FUNCTION(TYPE returnType,String name,TYPE_CLASS_VAR_DEC_LIST params)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
	}
	public String TypeName()
	{
		return String.format("FUNCTION %s",this.name);
	}
public TYPE Get_Return_Type ()
{
	return this.returnType;
}

}
