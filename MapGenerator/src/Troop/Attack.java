package Troop;

public class Attack {
	public String name;
	public String type;
	public String range;
	public int damage;
	public int number;
	
	Attack(String n,String t,String r, int d, int nb){
		//System.out.println("Attack Constructor");
		name = n;
		type = t;
		range = r;
		damage = d;
		number = nb;
	}
}
