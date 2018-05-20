package TYPES;

public class TYPE_FOR_SCOPE_BOUNDARIES extends TYPE
{
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FOR_SCOPE_BOUNDARIES(String name)
	{
		this.name = name;
	}

	public String TypeName()
	{
		return String.format("SCOPE_BOUNDARIES %s",this.name);
	}
}
