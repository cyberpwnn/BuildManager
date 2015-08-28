package com.ulticraft.core;

import java.util.ArrayList;
import java.util.List;

public class ListUtil
{
	public static ArrayList<String> toArrayListString(List<?> list)
	{
		ArrayList<String> f = new ArrayList<String>();
		
		for(Object i : list)
		{
			f.add(i.toString());
		}
		
		return f;
	}
	
	public static String toCommaString(ArrayList<Object> s)
	{
		String f = "";
		
		for(Object i : s)
		{
			f = f + ", " + i.toString();
		}
		
		f = f.substring(2);
		
		return f;
	}
}
