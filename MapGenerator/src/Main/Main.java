package Main;

import java.io.IOException;
import java.util.Scanner;

import Troop.Arm;

public class Main {
	static int W;
	static int H;

	public static void main(String[] args) throws IOException {
		String myPath = "C:\\GDATA\\state_B04505026.txt";
		String myMove = "C:\\GDATA\\move_B04505026.txt";
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Are you initiative? 1.true 0.false");
		int init = scan.nextInt();
		boolean A = Arm.InitialArm(); 
		boolean initiative = false;
		if(init==1)
			initiative = true;
		if(initiative){
			boolean M = Map.InitialMap();
			System.out.println("Map Saved.");
		}else{
			 Map.ReadMap("C:\\GDATA\\map.txt");
		}
		
		System.out.println("Enter the initial money you wish...");
		scan = new Scanner(System.in);
		int Money = scan.nextInt();
		State.Money = Money;
		State.eMoney = Money;
		
		State.GenerateState(initiative);
		State.WriteState(myPath, "");
		System.out.println("MyState Generated, MyState Saved.");
		//write state to file//
		
		
		System.out.println("Enter enemy student id...");
		String ID = scan.next();
		String ePath = "C:\\GDATA\\state_"+ID+".txt";
		
		
		System.out.println("Press enter if your enemy's state is ready...");
		scan = new Scanner(System.in);
		String enter = scan.nextLine();
		boolean B = State.InitialeState(ePath);				
		boolean S = Map.SyncStateToMap();
		
		Map.Show();
		State.ShowState();
		System.out.println("Press enter if you are ready to go...");
		scan = new Scanner(System.in);
		enter = scan.nextLine();
		Move.InitialMove();
		boolean GO = true;
		State tmp;
		Move move;
		int path;
		int epoch = 1;
		while(GO&&epoch<=30)
		{
			
			State.ReadState(myPath, ePath);
			Map.SyncStateToMap();

			for(int i = 0; i < State.StateList.size(); i++)
			{
				tmp = State.StateList.get(i);
				move = Move.MoveList.get(i);
				/*
				System.out.println("Money:"+State.Money);
				System.out.println("Now on:");
				System.out.println("  id: "+tmp.id);
				System.out.println("  x, y: "+tmp.x+", "+tmp.y);
				System.out.println("  HP: "+tmp.Sd.blood);
				System.out.println("  Arm type: "+tmp.Sd.arm.id);
				System.out.println("    attack 1: "+tmp.Sd.arm.attack1.name);
				if(tmp.Sd.arm.attack2!=null)
					System.out.println("    attack 2: "+tmp.Sd.arm.attack2.name);
				*/
				if(tmp.Sd.blood>0)
				{
					Map.Show(tmp);
					String Path = tmp.EvaluateMove(Map.ReachableDist);		
					/*
					System.out.println("Input your move. (path, direction, attack)");
					System.out.println("|4|3|2|");
					System.out.println("|5|0|1|");
					System.out.println("|6|7|8|");
					System.out.println("#Move: " + Path);
					*/
					path = Integer.valueOf(Path);
	
					tmp.Move(path, true);
					int []attdir = tmp.EvaluateAttack();		
					//System.out.println("#Attack: " + attdir[0] + ", "+ attdir[1]);
					tmp.Attack(attdir[0], attdir[1]);
					
					move.UpdateMove(tmp.id, Path, attdir[0], attdir[1]);
					//Map.Show(tmp);
					//State.ShowState();				
					Map.ClearPath();
				}
				else
				{
					move.UpdateMove(tmp.id, "0", 0, 0);
				}
			}
			Move.ShowMove();
			Move.WriteMove(myMove);
			State.ShowState();
			State.WriteState(myPath, ePath);
			int check = State.CheckifOver();
			if(check==1)
			{
				System.out.println("GameOver, You Win!");
				System.exit(0);
			}else if(check==2)
			{
				System.out.println("GameOver, You Lose!");
				System.exit(0);
			}
			
			epoch++;
			Map.Show();
			
			System.out.println("Press Enter to start round " + epoch +"...");
			String Enter = scan.nextLine();
			
			 
	
			
		}
				
		System.exit(0);

	}

}
