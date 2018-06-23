package Main;

import Troop.Soldier;

public class Terrain {

	String id;
	char icon;
	//Ter ter;
	boolean unwalkable;
	//Soldier OnSoldier;
	
	public Terrain(){id="NULL";icon='X';}
	Terrain(String id,char icon){
		this.id = id;
		this.icon = icon;
		//this.ter = 
		//his.OnSoldier = null;
		if(this.id.equals("unwalkable"))
			this.unwalkable = true;
		
	}
	void Insert(String id,char icon){
		this.id = id;
		this.icon = icon;
		if(this.id.equals("unwalkable"))
			this.unwalkable = true;
		
	}
}
