package Main;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

import Troop.Arm;
import Troop.Soldier;

public class State {
public
	int id;
	Soldier Sd;
	int x;
	int y;
	public static int Money;
	public static int eMoney;
	public static ArrayList<State> StateList;
	public static ArrayList<State> eStateList;
	public static int idcount;
	
	State(int id,char A,int x,int y,boolean f)
	{
		this.x = x;
		this.y = y;
		this.id = id;
		Arm arm = Arm.getArm(A);
		Sd = new Soldier(arm, id, f);
		if(f)
			Money = Money - arm.cost;
		else
			eMoney = eMoney - arm.cost;
	}
	State(int id,Arm A,int x,int y,boolean f)
	{
		this.x = x;
		this.y = y;
		this.id = id;
		Arm arm = A;
		Sd = new Soldier(arm, id, f);
		if(f)
			Money = Money - arm.cost;
		else
			eMoney = eMoney - arm.cost;
		
	}
	public static void GenerateState(boolean initiative)
	{
		StateList = new ArrayList<State>();
		boolean SmallMap = true;
		boolean EvilMap = false;
		double Value;
		double BestValue;
		Arm BestChoice = null;
		char[] TerChar = {'W','D','F','G','C','M','Q'};
		
		for(Double d:Map.MapRatio)
		{
			//System.out.println("ratio:"+d);
			if(d>0.35)
				EvilMap = true;
		}
		if(Map.W*Map.H > 100)
			SmallMap = false;
		if(true)
		{
			do
			{
				BestValue = 0;
			    BestChoice = null;
				for(Arm e:Arm.ArmList)
				{
					if(Money-e.cost<0)
						continue;
					if(EvilMap)
					{
						Value = e.cp;
						for(int i = 0;i < 7;i++)
						{
							Value = Value - e.movetype.defense.GetDefense(TerChar[i])*Map.MapRatio.get(i)*0.01;
						}
						//System.out.println("[E]Value:"+Value+", BestValue:"+BestValue );
					}
					else
					{
						Value = e.cp;
						//System.out.println("Value:"+Value+", BestValue:"+BestValue );
					}
					if(Value > BestValue)
					{
						BestValue = Value;
						BestChoice = e;
					}
				}
				if(BestChoice!=null)
				{
					//System.out.println("Money:"+Money+", Cost:"+BestChoice.cost );
					//Money = Money - BestChoice.cost;
					AddState(BestChoice, initiative);
				}
				
			}while(BestChoice!=null);
		}
		else{
			System.out.println("big map");
		}
		
	}
	private static boolean AddState(Arm A, boolean initiative)
	{
		int W = Map.W;
		int H = Map.H;
		int X = 0, Y = 0;
		State S; 
		if(initiative)
		{
			for(int x = 0;x < W; x++)
			{
				for(int y = 0 ; y < H; y++)
				{
					if(Map.Matrix[y * (2 * W) + x].Eter!='Q'&&Map.Matrix[y * (2 * W) + x].SdOn==null)
					{	
						X = x;
						Y = y;
						S = new State(idcount,A, X, Y, true);
						StateList.add(S);
						Map.Matrix[y * (2 * W) + x].SdOn = S.Sd;
						idcount++;
						return true;
					}
				}
				
			}
			
		}
		else
		{
			for(int x = 2 * W - 1;x >= W; x--)
			{
				for(int y = 0 ; y < H; y++)
				{
					if(Map.Matrix[y * (2 * W) + x].Eter!='Q'&&Map.Matrix[y * (2 * W) + x].SdOn==null)
					{	
						X = x;
						Y = y;
						S = new State(idcount,A, X, Y, true);
						StateList.add(S);
						Map.Matrix[y * (2 * W) + x].SdOn = S.Sd;
						idcount++;
						return true;
					}
				}
				
			}
		}
		
		return false;
		
		
	}
	public static void ShowState()
	{
		System.out.println("MyState:");
		for(State e:StateList)
			System.out.println(e.id+".(x, y, HP): "+e.x+", "+e.y+", "+e.Sd.blood);
		System.out.println("UrState:");
		for(State e:eStateList)
			System.out.println(e.id+".(x, y, HP): "+e.x+", "+e.y+", "+e.Sd.blood);
	}
	public int[] EvaluateAttack()
	{
		
		int []dir = new int [2];
		if(this.Sd.blood<=0)
			return dir;
		else{
			dir[0]= this.FindMinHP();
			if(dir[0]==0)
				dir[1] = 0;
			else
				dir[1]= this.ChooseAtt(dir[0]);
		}
		return dir;
	}
	private int ChooseAtt(int dir)
	{

		int x = this.x;
		int y = this.y;
		int W2 = 2 * Map.W;
		int W = Map.W;
		int Max = W2 * Map.H;
		Troop.Soldier SD = null;
		int damage1, damage2 = 0; 
		if(this.Sd!=null){
			SD = this.Sd;
		}else{
			return 0;
		}
		int []List = Map.Surrounding(x, y);
		Troop.Soldier Target = Map.Matrix[List[dir]].SdOn;
		char Ton = Map.Matrix[List[dir]].Eter;
		
		String AttType = SD.arm.attack1.type;
		damage1 = SD.arm.attack1.damage * SD.arm.attack1.number * Target.arm.movetype.defense.GetDefense(Ton) * Target.arm.movetype.resist.GetResist(AttType);
		if(SD.arm.attack2!=null){
		AttType = SD.arm.attack2.type;
		damage2 = SD.arm.attack2.damage * SD.arm.attack2.number * Target.arm.movetype.defense.GetDefense(Ton) * Target.arm.movetype.resist.GetResist(AttType);
		}
		if(damage2>damage1)
			return 1;
		else
			return 0;
	}
	private int FindMinHP()
	{
		int MinHP = 10000;
		int D = 0;
		//State MinS = null;
		int x = this.x;
		int y = this.y;
		int W2 = 2 * Map.W;
		int W = Map.W;
		int Max = W2 * Map.H;
		Troop.Soldier SD;
		
		int []List = Map.Surrounding(x, y);
		for(int ind=1;ind<List.length;ind++)
		{
			int e = List[ind];
			if(e >= 0)
			{
				SD = Map.Matrix[e].SdOn;
				if(SD!=null && !SD.friendly){
					if(SD.blood<MinHP && SD.blood>0)
					{
						MinHP = (int) SD.blood;
						D = ind;
					}
				}
				else
					continue;
			}
		}
		return D;
	}
	public boolean Attack(int dir, int mode)
	{
		if(dir==0)
			return false;
		int []List = Map.Surrounding(this.x, this.y);
		int ind = List[dir];
		District district = Map.Matrix[ind];
		Soldier Target = district.SdOn;
		char Ton = district.Eter;
		double damage = 0.0;
		Troop.Attack myAtt; 
		if(mode == 1)
			myAtt = this.Sd.arm.attack2;
		else
			myAtt = this.Sd.arm.attack1;
		String AttType = myAtt.type;
		damage = myAtt.damage * myAtt.number * Target.arm.movetype.defense.GetDefense(Ton) * Target.arm.movetype.resist.GetResist(AttType) / 10000.0;
	    System.out.println("Damage caused: "+damage);
		
		Target.blood -= damage;
		return true;
	}
	public String EvaluateMove(ArrayList<District> Reachable)
	{
		String path;
		State min = this.FindMin(eStateList);
		State Fmin = this.FindMin(StateList);
		if(min==null)
			return "0";
		//System.out.println("target(x, y):"+Integer.toString(min.x)+", "+Integer.toString(min.y));
		//System.out.println(min.y)

		
		//若血比敵軍少 就退
		if(min.Sd.blood>=this.Sd.blood)
		{	
			//沒隊友了
			if(Fmin==null)
				return this.Leave(min, Reachable);
			else if(min==null)
			{
				System.out.println("Game Over (no enemy found)");
				return "0";
			}
			
			int Distance2 = DfromT(min, Reachable);
			int FDistance2 = DfromT(Fmin, Reachable);
			//若可以一擊斃命 就往前
			if(Distance2<=2 && this.Sd.arm.attack1.damage >= min.Sd.blood){
				System.out.println(">>");
				path = this.Closer(min, Reachable);
			}else if(Distance2>=FDistance2){
				//若已經靠近隊友 就進攻
				System.out.println(">>");
				path = this.Closer(min, Reachable);
			}else{
				//若血少又離隊友遠 先靠近隊友
				System.out.println("<");
				path = this.Leave(Fmin, Reachable);
			}
		}else{
			System.out.println(">>>");
			path = this.Closer(min, Reachable);
		}
		return path;
	}
	private int DfromT(State target, ArrayList<District> Reachable)
	{
		int MinD = 10000;
		int D;
		District Min = null;
		for(District e:Reachable)
		{
			//System.out.println("R(x, y):"+Integer.toString(e.x)+", "+Integer.toString(e.y));
			//System.out.println(e.Path);
			D = (int) (Math.pow(e.x - target.x,2) + Math.pow(e.y - target.y,2));
			
			if(D < MinD)
			{	
				Min = e;
				MinD = D;
			}	
		}
		return MinD;
	}
	private String Leave(State target, ArrayList<District> Reachable)
	{
		int MaxD = 0;
		int D;
		District Max = null;
		for(District e:Reachable)
		{	
			//System.out.println("R(x, y):"+Integer.toString(e.x)+", "+Integer.toString(e.y));
			//System.out.println(e.Path);
			D = (int) (Math.pow(e.x - target.x,2) + Math.pow(e.y - target.y,2));
			
			if(D > MaxD)
			{	
				Max = e;
				MaxD = D;
			}	
		}
		return Max.Path;
	}
	private String Closer(State target, ArrayList<District> Reachable)
	{
		int MinD = 10000;
		int D;
		District Min = null;
		for(District e:Reachable)
		{
			D = (int) (Math.pow(e.x - target.x,2) + Math.pow(e.y - target.y,2));
			
			//System.out.println("R(x, y, path, D):"+Integer.toString(e.x)+", "+Integer.toString(e.y)+", "+e.Path+", "+D);
			//System.out.println(e.Path);
			
			if(D < MinD)
			{	
				Min = e;
				MinD = D;
			}	
		}
		return Min.Path;
	}
	private State FindMin(ArrayList<State> StateList)
	{
		double MinD = 10000;
		double D;
		State MinS = null;
		for(State e:StateList)
		{
			D = Math.pow(e.x - this.x,2) + Math.pow(e.y - this.y,2);
			//System.out.println("Distance(x, y, D):"+e.x+", "+e.y+","+D);
			if(D < MinD && D!=0 && e.Sd.blood>0)
			{	
				MinS = e;
				MinD = D;
			}	
		}
		//System.out.println("Min(x, y):"+MinS.x+", "+MinS.y);
		return MinS;
	}
	
	public static boolean InitialeState(String ePath) throws IOException
	{
		
		
		//StateList = new ArrayList<State>();
		FileReader fr;
		BufferedReader br;
		String line,tempstring;
	    String[] tempArray= new String[5];	    
	    State temp;
	    int x, y;
	   
	    eStateList = new ArrayList<State>();
		fr = new FileReader(ePath);
		br = new BufferedReader(fr);
		
	    while((line = br.readLine())!=null)
	    {

	    	tempstring = line; 
	    	tempArray = tempstring.split(",");
	    	x = Integer.parseInt(tempArray[2]);
	    	y = Integer.parseInt(tempArray[3]);
	    	if(x>=Map.W*2 || y>=Map.H)
	    	{
	    		System.out.println("Index:"+Integer.parseInt(tempArray[0])+", Invalid Position");
	    		continue;
	    	}
	    	Arm arm = Arm.getArm(tempArray[1].charAt(0));
	    	if(arm.cost > eMoney)
	    	{
	    		System.out.println("Index:"+Integer.parseInt(tempArray[0])+", Not enough money");
	    		continue;
	    	}
	    	
	    	temp = new State(Integer.parseInt(tempArray[0]), tempArray[1].charAt(0), x, y, false);
	    	eStateList.add(temp);
	    }
		return true;
	}
	
	public static boolean ReadState(String Path, String ePath) throws IOException
	{
		
		//不用考慮錢 要多讀血量//

		FileReader fr = new FileReader(Path);
		BufferedReader br = new BufferedReader(fr);
		String line,tempstring;
	    String[] tempArray= new String[5];	    
	    State temp;
	    int x, y, id;
	    float HP;
	    
	    while((line = br.readLine())!=null)
	    {

	    	tempstring = line; 
	    	tempArray = tempstring.split(",");
	    	x = Integer.parseInt(tempArray[2]);
	    	y = Integer.parseInt(tempArray[3]);
	    	id = Integer.parseInt(tempArray[0]);
	    	HP = Float.parseFloat(tempArray[4]);
	    	
	    	if(x>=Map.W*2 || y>=Map.H)
	    	{
	    		System.out.println("Index:"+Integer.parseInt(tempArray[0])+", Invalid Position");
	    		continue;
	    	}
	    	
	    	temp = StateList.get(id);
	    	if(temp.id!=id)
	    	{
	    		System.out.println("Index:"+Integer.parseInt(tempArray[0])+", ID change");
	    		continue;
	    	}
	    	else
	    	{
	    		temp.x = x;
	    		temp.y = y;
	    		temp.Sd.blood = HP;
	    	}
	    }
	   
	    
		fr = new FileReader(ePath);
		br = new BufferedReader(fr);
		
	    while((line = br.readLine())!=null)
	    {

	    	tempstring = line; 
	    	tempArray = tempstring.split(",");
	    	x = Integer.parseInt(tempArray[2]);
	    	y = Integer.parseInt(tempArray[3]);
	    	id = Integer.parseInt(tempArray[0]);
	    	HP = Float.parseFloat(tempArray[4]);
	    	
	    	if(x>=Map.W*2 || y>=Map.H)
	    	{
	    		System.out.println("Index:"+Integer.parseInt(tempArray[0])+", Invalid Position");
	    		continue;
	    	}
	    	
	    	temp = eStateList.get(id);
	    	if(temp.id!=id)
	    	{
	    		System.out.println("Index:"+Integer.parseInt(tempArray[0])+", ID change");
	    		continue;
	    	}
	    	else
	    	{
	    		temp.x = x;
	    		temp.y = y;
	    		temp.Sd.blood = HP;
	    	}
	    }
		return true;
	}
	public boolean Move(int path, boolean FirstIn)
	{
		if(path<=0)
			return true;
		if(Move(path/10, false)==false)
			return false;
		
		int workPath = path % 10;
		//System.out.println("workPath:"+workPath);
		int x, y;
		int ind = 0;
		int W2 = 2 * Map.W;
		int W = Map.W;
		int MAX = W2 * Map.H;
		int Max = MAX;
		x = this.x;
		y = this.y;
		//System.out.println("x:"+x);
		//System.out.println("y:"+y);
		int ori = y * W2 + x;
		int [] List = Map.Surrounding(x, y);
		ind = List[workPath];
		if(Map.Matrix[ind].ShowPath=='O'|| (Map.Matrix[ind].SdOn!=null && Map.Matrix[ind].SdOn.friendly && !FirstIn)){
			Map.Matrix[ori].SdOn = null;
			Map.Matrix[ind].SdOn = this.Sd;
			this.x = Map.Matrix[ind].x;
			this.y = Map.Matrix[ind].y;
			
		}
		else{
			System.out.println("Invalid Path");
			return false;
		}
		return true;
	}
	public static boolean WriteState(String Path, String ePath) throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter(Path,"UTF-8");

		for(State e:StateList)
		{
			writer.println(e.id+","+e.Sd.arm.icon+","+e.x+","+e.y+","+e.Sd.blood);	  
		}
		writer.close();	
		if(ePath=="")
			return true;
		
		PrintWriter ewriter = new PrintWriter(ePath,"UTF-8");
		for(State e:eStateList)
		{
			ewriter.println(e.id+","+e.Sd.arm.icon+","+e.x+","+e.y+","+e.Sd.blood);	  
		}
		ewriter.close();

		return true;
	}
	public static int CheckifOver()
	{
		boolean ialive = false;
		boolean ealive = false;
		for(State e:State.StateList)
		{
			if(e.Sd.blood>0)
				ialive = true;
		}
		for(State e:State.eStateList)
		{
			if(e.Sd.blood>0)
				ealive = true;
		}
		if(ialive&&ealive)
			return 0;
		if(ialive&&!ealive)
			return 1;
		if(!ialive&&ealive)
			return 2;
		return 0;
		
	}
	public static void main(String[] args) throws IOException {
		Arm.InitialArm();
		//InitialState("state_B04505023.txt");
		for(int i = 0; i < StateList.size(); i++)
		{
			 State tmp = StateList.get(i);
			 System.out.println("id:"+tmp.id);
			 System.out.println("x:"+tmp.x);
			 System.out.println("y:"+tmp.y);
			 System.out.println("Soldier:"+tmp.Sd.arm.id);
			
		}
		

	}
}
