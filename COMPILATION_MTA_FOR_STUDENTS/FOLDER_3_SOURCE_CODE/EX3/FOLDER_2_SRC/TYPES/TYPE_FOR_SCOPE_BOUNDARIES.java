package TYPES;

public class TYPE_FOR_SCOPE_BOUNDARIES extends TYPE
{

	TYPE type=null;
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FOR_SCOPE_BOUNDARIES(String name)
	{
		this.name = name;
	}
	public TYPE_FOR_SCOPE_BOUNDARIES(String name, TYPE type)
	{
		this.name = name;
		this.type = type;
	}

	public TYPE getType()
	{
		return this.type;
	 }

	public String TypeName()
	{
		return String.format("SCOPE_BOUNDARIES %s",this.name);
	}
}
