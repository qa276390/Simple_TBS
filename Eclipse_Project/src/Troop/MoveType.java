package Troop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MoveType {
	public String name;
	public MoveCost movecost;
	public Defense defense;
	public Resist resist;
	
	
	MoveType(String N,MoveCost M,Defense D,Resist R){
		this.name = N;
		this.movecost = M;
		this.defense = D;
		this.resist = R;
	}
	static MoveType getMoveType(String type)
	{
		String line;
		for(int i = 0; i < MoveList.size(); i++)
		{
			line = MoveList.get(i).name;
			if(line.equals(type))
			{
				return MoveList.get(i);
			}
		}
		return null;
	}
	static ArrayList<MoveType> MoveList;
	static boolean InitialMoveType() throws NumberFormatException, IOException{
		FileReader fr = new FileReader("C:\\GDATA\\ex_move.txt");
		BufferedReader br = new BufferedReader(fr);
		String line,tempstring;
	    String[] tempArray= new String[3];
	    ArrayList myList = new ArrayList();
	    int i=0;
	    int idx = 0;
	    boolean start,attstart = false;
	    String Name = null;
	    MoveCost Cost = null;
	    Defense Def = null;
	    Resist Res = null;
	    
	    int W = 0;
		int G = 0;
		int D = 0;
		int F = 0;
		int C = 0;
		int blade = 0;
		int pierce = 0;
		int impact = 0;
		int fire = 0;
		int cold = 0;
		int arcane = 0;
	    
	    MoveList = new ArrayList<MoveType>();
	    
	    while((line = br.readLine())!=null)
	    {
	    	line = line.trim();
	    	tempstring = line; 
	    	tempArray = tempstring.split("=");
	    	if(tempArray.length>1)
	    		tempArray[1] = tempArray[1].trim();
	    	//basic info
	    	if(start = true && tempArray[0].matches("(.*)name(.*)")){
	    		Name = tempArray[1]; 			
	    	}
	    	
	    	//cost and defense
	    	if(attstart = true && tempArray[0].matches("(.*)shallow_water(.*)")){
	    		W = Integer.parseInt(tempArray[1]); 			
	    	}
	    	if(attstart = true && tempArray[0].matches("(.*)flat(.*)")){
	    		G = Integer.parseInt(tempArray[1]); 			
	    	}
	    	if(attstart = true && tempArray[0].matches("(.*)sand(.*)")){
	    		D = Integer.parseInt(tempArray[1]); 			
	    	}
	    	if(attstart = true && tempArray[0].matches("(.*)forest(.*)")){
	    		//System.out.println("f:"+tempArray[1]);
	    		F = Integer.parseInt(tempArray[1]); 
	    		//System.out.println("----f----");
	    	}
	    	if(attstart = true && tempArray[0].matches("(.*)castle(.*)")){
	    		C = Integer.parseInt(tempArray[1]); 			
	    	}
	    	//resist
	    	if(attstart = true && tempArray[0].matches("(.*)blade(.*)")){
	    		blade = Integer.parseInt(tempArray[1]); 			
	    	}
	    	if(attstart = true && tempArray[0].matches("(.*)pierce(.*)")){
	    		pierce = Integer.parseInt(tempArray[1]); 			
	    	}
	    	if(attstart = true && tempArray[0].matches("(.*)impact(.*)")){
	    		impact = Integer.parseInt(tempArray[1]); 			
	    	}
	    	if(attstart = true && tempArray[0].matches("(.*)fire(.*)")){
	    		fire = Integer.parseInt(tempArray[1]); 			
	    	}
	    	if(attstart = true && tempArray[0].matches("(.*)cold(.*)")){
	    		cold = Integer.parseInt(tempArray[1]); 			
	    	}
	    	if(attstart = true && tempArray[0].matches("(.*)arcane(.*)")){
	    		arcane = Integer.parseInt(tempArray[1]); 			
	    	}
	    	//mode
	    	if(line.equals("[movement_costs]"))
	    		attstart = true;
	    	
	    	if(line.equals("[/movement_costs]"))
	    	{
	    		attstart = false;
	    		Cost = new MoveCost(W, G, D, F, C);   
	    	}
	    	
	    	if(line.equals("[defense]"))
	    		attstart = true;
	    	if(line.equals("[/defense]"))
	    	{
	    		attstart = false;
	    		Def = new Defense(W, G, D, F, C);   
	    	}
	    	
	    	if(line.equals("[resistance]"))
	    		attstart = true;
	    	if(line.equals("[/resistance]"))
	    	{
	    		attstart = false;
	    		Res = new Resist(blade, pierce, impact, fire, cold, arcane);   
	    	}
	    	
	    	if(line.equals("[movetype]"))
	    		start = true; 	
	    	if(line.equals("[/movetype]"))
	    	{
	    		start = false;
	    		MoveList.add(new MoveType(Name, Cost, Def, Res));
	    	
	   
	    	}
	    }
		
		return true;
	}
	
	
}
