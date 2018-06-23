package Troop;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Main.Terrain;

public class Arm {

	public String id;
	public char icon;
	public int HitPoint;
	public MoveType movetype;
	public int Movement;
	public int cost;
	public Attack attack1;
	public Attack attack2;
	public double cp;
	
	

	Arm(String ID,char ICON, int HP,MoveType MT,int MM, int C, Attack att1, Attack att2){
		id = ID;
		icon = ICON;
		HitPoint = HP;
		movetype = MT;
		Movement = MM;
		cost = C;
		attack1 = att1;
		attack2 = att2;
		if(att2==null || att1.damage*att1.number>att2.damage*att2.number)
			cp = (att1.damage*att1.number+HP*0.5)/C;
		else
			cp = (att2.damage*att2.number+HP*0.5)/C;
	}
	public static ArrayList<Arm> ArmList;
	public static Arm getArm(char A)
	{
		for(int i = 0;i < ArmList.size();i++)
		{
			if(A==ArmList.get(i).icon)
				return ArmList.get(i);
		}
		return null;
	}
	public static boolean InitialArm() throws IOException{
		//init MoveType
		MoveType.InitialMoveType();
		
		FileReader fr = new FileReader("C:\\GDATA\\ex_unit.txt");
		BufferedReader br = new BufferedReader(fr);
		String line,tempstring;
	    String[] tempArray= new String[3];
	    //ArrayList myList = new ArrayList();
	    int i=0;
	    int idx = 0;
	    boolean start,attstart = false;
	    String ID = null;
	    char ICON = 0;
	    int HP = 0;
	    MoveType MT = null;
	    int MM = 0;
	    int C = 0;
	    Attack att1 = null;
	    Attack att2 = null;
	    
	    String name = null;
		String type = null;
		String range = null;
		int damage = 0;
		int number = 0;
	    
	    ArmList = new ArrayList<Arm>();
	    
	    while((line = br.readLine())!=null)
	    {
	    	line = line.trim();
	    	tempstring = line; 
	    	tempArray = tempstring.split("=");
	    	if(tempArray.length>1)
	    		tempArray[1] = tempArray[1].trim();
	    	//basic info
	    	if(start = true && tempArray[0].matches("(.*)id(.*)")){
	    		ID = tempArray[1]; 			
	    	}
	    	if(start = true && tempArray[0].matches("(.*)hitpoints(.*)")){
	    		HP =  Integer.parseInt(tempArray[1]);
	    	}
	    	if(start = true && tempArray[0].matches("(.*)movement")){
	    		MM =  Integer.parseInt(tempArray[1]);
	    	}
	    	if(start = true && tempArray[0].matches("(.*)cost(.*)")){
	    		C =  Integer.parseInt(tempArray[1]);
	    	}
	    	if(start = true && tempArray[0].matches("(.*)movement_type(.*)")){
	    		MT =  MoveType.getMoveType(tempArray[1]); 
	    	}
	    	//attack
	    	if(attstart = true && tempArray[0].matches("(.*)name(.*)")){
	    		name = tempArray[1]; 			
	    	}
	    	if(attstart = true && tempArray[0].matches("(.*)type")){
	    		type = tempArray[1]; 			
	    	}
	    	if(attstart = true && tempArray[0].matches("(.*)range(.*)")){
	    		range = tempArray[1]; 			
	    	}
	    	if(attstart = true && tempArray[0].matches("(.*)damage(.*)")){
	    		damage = Integer.parseInt(tempArray[1]); 			
	    	}
	    	if(attstart = true && tempArray[0].matches("(.*)number(.*)")){
	    		number = Integer.parseInt(tempArray[1]); 			
	    	}
	    	
	    	
	    	
	    	if(line.equals("[attack]"))
	    		attstart = true;
	    	
	    	if(line.equals("[/attack]"))
	    	{
	    		attstart = false;
	    		if(att1 == null)
	    			att1 = new Attack(name, type, range, damage, number);
	    		else
	    			att2 = new Attack(name, type, range, damage, number);
	    		
	   
	    	}
	    	if(line.equals("[unit_type]"))
	    		start = true;
	    	
	    	if(line.equals("[/unit_type]"))
	    	{
	    		start = false;
	    		ICON = (char) ('A' + idx);
	    		ArmList.add(new Arm(ID, ICON, HP, MT, MM, C, att1, att2));
	    		att1 = null;
	    		att2 = null;
	    		idx++;
	   
	    	}
	    }
		
		return true;
		
	}
	
	public static void main(String[] args) throws IOException {
		InitialArm();
		for(int i = 0; i < ArmList.size(); i++)
		{
			 Arm tmp = ArmList.get(i);
			 System.out.println("id:"+tmp.id);
			 System.out.println("cost:"+tmp.cost);
			 System.out.println("att name:"+tmp.attack1.name);
			 System.out.println("att damage:"+tmp.attack1.damage);
			 System.out.println("move type:"+tmp.movetype.name);
			 System.out.println("move type defense C:"+tmp.movetype.defense.C);
			 System.out.println("move type cost F:"+tmp.movetype.movecost.F);
			 System.out.println("move type resist arcane:"+tmp.movetype.resist.arcane);
		}
		

	}

}
