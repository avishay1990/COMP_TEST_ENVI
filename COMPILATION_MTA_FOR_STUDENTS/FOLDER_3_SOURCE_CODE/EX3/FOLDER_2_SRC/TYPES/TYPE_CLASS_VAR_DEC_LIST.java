package TYPES;

public class TYPE_CLASS_VAR_DEC_LIST
{
	public TYPE_CLASS_VAR_DEC head;
	public TYPE_CLASS_VAR_DEC_LIST tail;

	public TYPE_CLASS_VAR_DEC_LIST(TYPE_CLASS_VAR_DEC head,TYPE_CLASS_VAR_DEC_LIST tail)
	{
		this.head = head;
		this.tail = tail;
	}


	public String TypeName()
	{
		return String.format("CLASS_VAR_DEC_LIST");
	}

	public void  ConcateTypeList(TYPE_CLASS_VAR_DEC_LIST t)
	{
		TYPE_CLASS_VAR_DEC_LIST temp = this.tail;

		if (this.tail== null) this.tail =  t;
		else{
			while (temp.tail != null) temp = temp.tail;
			temp.tail = t;
		}
	}

}
