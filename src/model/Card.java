package model;

public class Card {
	String name;
	String type;
	
	Card(String name, String type)
	{
		this.name = name;
		this.type = type;
	}
	
	String getName()
	{
		return this.name;
	}
	
	String getType()
	{
		return this.type;
	}
}
