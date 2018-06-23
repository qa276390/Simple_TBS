package Troop;

import java.util.ArrayList;

public class Soldier {

	public double blood;
	public Arm arm;
	public int id;
	public boolean friendly;
public
	static ArrayList<Soldier> SoldierList;
	public Soldier(Arm a, int ID){arm = a;blood = a.HitPoint;id = ID;friendly = true;}
	public Soldier(Arm a, int ID, boolean f){arm = a;blood = a.HitPoint;id = ID;friendly = f;}
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
