package Troop;

public class Resist {
	int blade;
	int pierce;
	int impact;
	int fire;
	int cold;
	int arcane;
	
	Resist(int b, int p, int i, int f, int c, int a){
		blade = b;
		pierce = p;
		impact = i;
		fire = f;
		cold = c;
		arcane = a;
	}
	public int GetResist(String Type)
	{
		int Cost = 0;
		switch(Type){
		case "blade":
			Cost = blade;
			break;
		case "pierce":
			Cost = pierce;
			break;
		case "impact":
			Cost = impact;
			break;
		case "fire":
			Cost = fire;
			break;
		case "cold":
			Cost = cold;
			break;
		case "arcane":
			Cost = arcane;
			break;
		default:
			Cost = 0;
			
		
		}
		return Cost;
	}

}
