package Main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Move {
	int id;
	String MovePath;
	int AttDir;
	int AttMode;
	
	public static ArrayList<Move> MoveList;
	
	public Move(int id,String MovePath, int AttDir, int AttMode)
	{
		this.id = id;
		this.MovePath = MovePath;
		this.AttDir = AttDir;
		this.AttMode = AttMode;
	}
	

	public void UpdateMove(int id,String MovePath, int AttDir, int AttMode)
	{
		this.id = id;
		this.MovePath = MovePath;
		this.AttDir = AttDir;
		this.AttMode = AttMode;
	}
	
	public static void InitialMove(){
		MoveList = new ArrayList<Move>();
		for(State e:State.StateList)
		{
			MoveList.add(new Move(e.id,"0",0,0));
		}
	}
	public static void ShowMove()
	{
		for(Move e:MoveList)
		{
			System.out.println("(id,path,dir,mode): " + e.id + ", "+ e.MovePath + ", "+ e.AttDir + ", "+ e.AttMode);
		}
	}
	public static void WriteMove(String Path) throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter(Path,"UTF-8");

		for(Move e:MoveList)
		{
			writer.println(e.id + ","+ e.MovePath + ","+ e.AttDir + ","+ e.AttMode);
		}
		writer.close();	
	}
	
	
}
