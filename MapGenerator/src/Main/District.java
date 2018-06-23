package Main;
import Troop.Soldier;

public class District {

	Soldier SdOn;
	char Eter;
	char ShowPath = ' ';
	String Path = "";
	int x;
	int y;
	//Terrain Ter; 
public
	District(char t, int X, int Y)
	{	
		SdOn = null;
		Eter = t;
		x = X;
		y = Y;
	}
	void AssignSoldier(Soldier s)
	{
		SdOn = s;
	}
	void RemoveSoldier()
	{
		SdOn = null;
	}
}
