package TYPES;

public class TYPE_CLASS_VAR_DEC
{
	public TYPE t;
	public String name;
	public boolean isFromSuperClass = false;
// 1 - varDec 2-func dec
	public int type;


	public TYPE_CLASS_VAR_DEC(TYPE t, String name)
	{
		this.t = t;
		this.name = name;
	}

	public TYPE_CLASS_VAR_DEC(TYPE t,String name, int type , boolean isFromSuperClass)
	{
		this.isFromSuperClass= isFromSuperClass;
		this.t = t;
		this.name = name;
	}

	public String TypeName()
	{
		return String.format("CLASS_VAR_DEC %s",this.name);
	}
}
