package TYPES;

public class TYPE_LIST
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public TYPE head;
	public TYPE_LIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public TYPE_LIST(TYPE head,TYPE_LIST tail)
	{
		this.head = head;
		this.tail = tail;
	}

	public String TypeName()
	{
		return "TYPE";
	}

	public void  ConcateTypeList(TYPE_LIST t)
	{
		TYPE_LIST temp = this.tail;

		if (this.tail== null) this.tail =  t;
		else{
			while (temp.tail != null) temp = temp.tail;
			temp.tail = t;
		}
	}

}
