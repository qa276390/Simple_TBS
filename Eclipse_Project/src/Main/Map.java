package Main;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import Troop.Arm;
import Troop.Soldier;


public class Map {
	static HashMap<Character, Character> Dict = new HashMap<Character, Character>();
	static HashMap<Character, Integer> NtoC = new HashMap<Character, Integer>();
	static int W;
	static int H;
	public static District Matrix[];
	public static ArrayList<District> ReachableDist;
	public static ArrayList<Double> MapRatio;
	
	public static boolean ReadMap(String path) throws IOException{
		MapRatio = new ArrayList<Double>();
		char[] TerChar = {'W','D','F','G','C','M','Q'};
		for(int i = 0;i<7;i++)
			NtoC.put(TerChar[i], i);
		FileReader fr = new FileReader(path); 
        BufferedReader br = new BufferedReader(fr);
        String line,tempstring;
        String[] tempArray= new String[20];
        ArrayList myList = new ArrayList();
        int y, idx = 0, len = 0;
        while((line = br.readLine())!=null)
        {
          
             tempstring = line;
             len = tempstring.length();
             tempArray[idx] = tempstring;
             idx++;
        }
        H = idx;
        W = len/2;
        int [] RAmount = new int [7];
        System.out.println("(H, W): "+H+", "+W);
        Matrix = new District[(W*2)*H];
        for(int i=idx-1; i>=0; i--){
        	line = tempArray[i];
        	y = idx - i - 1;
        	for(int k = 0; k<W*2; k++)
        	{
        		Matrix[y * (2 * W) + k] = new District(line.charAt(k), k, y);
        		int ind = NtoC.get(Matrix[y * (2 * W) + k].Eter);
        		RAmount[ind]++;
        		//System.out.println("(x, y): "+k+", "+y);
        		//System.out.println(Matrix[y * (2 * W) + k].Eter);
        	}
        	
        }
        for(int i=0;i<7;i++)
		{
				System.out.print(RAmount[i]+", ");
				MapRatio.add(((double)RAmount[i]/(double)(W*H)));
		}
        Dict.put('C', '@');
		Dict.put('M', '&');
		Dict.put('G', '_');
		Dict.put('F', '$');
		Dict.put('D', '.');
		Dict.put('W', '~');
		Dict.put('Q', '#');
       
		
		return true;
	}
	public static boolean InitialMap() throws IOException {
		
		
		//boolean A = Arm.InitialArm(); 
		//boolean B = State.InitialState();
		MapRatio = new ArrayList<Double>();
		
		char[] TerChar = {'W','D','F','G','C','M','Q'};
	
		for(int i = 0;i<7;i++)
			NtoC.put(TerChar[i], i);
		//HashMap<Character, Character> Dict = new HashMap<Character, Character>();
		Dict.put('C', '@');
		Dict.put('M', '&');
		Dict.put('G', '_');
		Dict.put('F', '$');
		Dict.put('D', '.');
		Dict.put('W', '~');
		Dict.put('Q', '#');
	
		W = 10;
		H = 5;
		int Mode = 3;
		
		//Parse();
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the Size of Map");
		System.out.println("(W, H) <= 20");
		W = scan.nextInt();
		H = scan.nextInt();
		System.out.println("Do you want to enter 1.ratio or 2.random? ");
		Mode = scan.nextInt();
		
		
		double [] DemandRatio = {0.5,0.2,0,0,0.1,0,0.1};
		int [] DemandAmount = {8,8,15,-1,15,-1,-1};
		double [] RandomRatio = {-1,-1,-1,-1,0.1,0,0.1};
		int [] RAmount = new int [7];
		switch(Mode){
			case 1:
				System.out.println("Enter the ratio for each terrain, enter -1 if you don't care.");
				for(int i=0;i<4;i++)
				{
					System.out.println(TerChar[i]+":");
					DemandRatio[i] = scan.nextDouble();
				}
				DemandAmount = toAmount(DemandRatio, W, H);
				break;
			default:
				DemandAmount = toAmount(RandomRatio, W, H);
		}
		/*
		if(Mode == 1)
		{	
			System.out.println("Enter the amount for each terrain, enter -1 if you don't care.");
			for(int i=0;i<5;i++)
			{
				System.out.println(TerChar[i]+":");
				DemandAmount[i] = scan.nextInt();
			}
		}
		*/
		RAmount = CorrectAmount(DemandAmount,RAmount,W,H);
		
		//-----------------show--------------------//
		System.out.print("{ ");
		for(int i=0;i<7;i++)
		{
				System.out.print(RAmount[i]+", ");
				MapRatio.add(((double)RAmount[i]/(double)(W*H)));
		}
		System.out.print("}");
		System.out.println();
		///----------------show-------------------///
		
		char Mat[] = new char[W*H];
		Matrix = new District[(W*2)*H];
		Mat = genMatrix(RAmount,W,H,TerChar);		
		Matrix = CharToDist(Mat,W,H);
		Save(Matrix);
		
		return true;
		
	}
	
	public static boolean SyncStateToMap()
	{
		int x, y;
		for(int ind = 0; ind < H * 2 * W; ind++)
		{
			Matrix[ind].SdOn = null;
		}
		for(int i = 0; i < State.StateList.size(); i++)
		{
			if(State.StateList.get(i).Sd.blood <= 0)
				continue;
			x = State.StateList.get(i).x;
			y = State.StateList.get(i).y;
			if(Matrix[y * (2 * W) + x].SdOn!=null)
				System.out.println("Position ambiguous.");
			Matrix[y * (2 * W) + x].SdOn = State.StateList.get(i).Sd;
			
			
		}
		for(int i = 0; i < State.eStateList.size(); i++)
		{
			if(State.eStateList.get(i).Sd.blood <= 0)
				continue;
			x = State.eStateList.get(i).x;
			y = State.eStateList.get(i).y;
			if(Matrix[y * (2 * W) + x].SdOn!=null)
				System.out.println("Position ambiguous.");		
			Matrix[y * (2 * W) + x].SdOn = State.eStateList.get(i).Sd;			
		}
		return true;
	}
	
	
	public static int [] toAmount(double [] DemandRatio,int W,int H)
	{
		int [] DemandAmount = new int[7];
		for(int i=0;i<7;i++)
		{
			if(DemandRatio[i]>=0)
				DemandAmount[i] = (int)Math.floor(DemandRatio[i] * W * H);
			else
				DemandAmount[i] = (int)DemandRatio[i];
		}
		return DemandAmount;
	}
	public static char [] invMatrix(char [] Mat,int W,int H)
	{
		char invMat[] = new char[W*H];
		for(int i=0;i<H;i++)
		{
			for(int j=0;j<W;j++)
		   	{
		   		
		   		invMat[i * W + j] = Mat[(H - i - 1) * W + (W - j - 1)];
		   	}   
		   	//System.out.println();
		}
		return invMat;
	}
	
	public static char [] genMatrix(int [] RAmount,int W,int H,char[] TerChar)
	{
		char Mat[] = new char[W*H];
		int total = 0;
		for(int i=0;i<7;i++)
		{
			int am = RAmount[i];
			for (int j = 0; j < am; j++)
			{
				Mat[total] = TerChar[i];
				total++;
			}
		}
		return RandomArray(Mat);
	}
	public static District [] CharToDist(char [] Mat, int W,int H)
	{
		District Matrix[] = new District[(W*2)*(H)];
		//System.out.println(Mat.length);
		char invMat[] = new char[W * H];
		invMat = invMatrix(Mat,W,H);
		//System.out.println(invMat.length);
		for(int i = 0; i < H; i++)
		{
			for(int j = 0; j < W; j++)
			{
				Matrix[i * 2 * W + j] = new District(Mat[i * W + j], j, i);				
			}
			for(int j = 0; j < W; j++)
			{
				Matrix[i * 2 * W + (j + W)] = new District(invMat[i * W + j], (j + W), i);
			}			
		}
		return Matrix;		
	}
	public static char[] RandomArray(char[] mArray)
	{      
	     int mLength = mArray.length;
	     int mRandom;
	     char mNumber;

	     for(int i = 0; i < mLength; i++) 
	     {      
	         mRandom = (int)(Math.random() * mLength);
	         mNumber = mArray[i];
	         mArray[i] = mArray[mRandom];
	         mArray[mRandom] = mNumber;
	     }

	    return mArray;
	}

	public static int [] CorrectAmount(int [] DemandAmount, int [] RAmount, int W, int H)
	{
		int sum = 0;
		int NAssin = 0;
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		for(int i=0;i<7;i++)
		{
			if(DemandAmount[i]>=0)
			{
				RAmount[i] = DemandAmount[i];
				sum += DemandAmount[i];
			}
			else
			{	
				RAmount[i] = 0;
				indexList.add(i);
				NAssin++;	
			}
		}
		if(sum > W * H || (sum != W * H && NAssin ==0) )
		{	System.out.println("Can't Generate Such Map! Please input a correct number.");
			System.exit(0);
		}else{
			int r = W * H - sum;
			int n = NAssin;
			double ans = factorial(W * H);
			//System.out.print(ans);
			/**
			 * 先排列已選定的，再乘上未選定的 (r^n)
			 * (W * H)! / ((W!)*(D!)*...) * (r^n) / 2
			 * 
			**/
			for(int l = 0; l < 7; l++)
			{
				if(DemandAmount[l] > 0)
				{
					ans /= factorial(DemandAmount[l]);
				}
				
			}
			ans /= factorial(r);
			ans *= r^n;
			ans /= 2;
			System.out.println("Number of Possible Map: " + ans);
			
			
		}
		int num;
		int catgo;
		for(int j = 0; j < (W * H - sum) ; j++)
		{
			num = (int)((Math.random())*NAssin);
	   		catgo = indexList.get(num);
			RAmount[catgo]++;
		}
		return RAmount;
	}
	static double factorial(int n){    
		if (n == 0)    
			return 1;    
		else    
			return(n * factorial(n-1));    
	}    
	public static Terrain[] Parse() throws IOException {
		
		FileReader fr = new FileReader("C:\\GDATA\\ex_terrain.txt");
		BufferedReader br = new BufferedReader(fr);
		String line,tempstring;
	    String[] tempArray= new String[3];
	    ArrayList myList = new ArrayList();
	    int i=0;
	    int idx = 0;
	    boolean start = false;
	    String tempid = "NULL";
	    String tempstr = "NULL";
	    Terrain TerArray[] = new Terrain[8];
	    
	    while((line = br.readLine())!=null)
	    {

	    	tempstring = line; 

	    	if(start = true){
	    		tempArray = tempstring.split("=");
	    		if(tempArray[0].matches("(.*)id(.*)"))
	    			tempid = tempArray[1];
	    	}
	    	if(start = true){
	    		tempArray = tempstring.split("=");
	    		if(tempArray[0].matches("(.*)string(.*)"))
	    			tempstr = tempArray[1];
	    	}
	    	if(line.equals("[terrain_type]"))
	    		start = true;
	    	
	    	if(line.equals("[/terrain_type]"))
	    	{
	    		start = false;
	    		TerArray[idx] = new Terrain(tempid, tempstr.charAt(0));
	    		
	    		
	    		idx ++;
	    	}
	    }
	    return TerArray;
	}
	public static void Show()
	{	
		char Symbol;
		int x,y;
		for(int i=H-1;i>=0;i--)
		{
			for(int j=0;j<W;j++)
		   	{				
				y = i;
				x = j;
				System.out.print("|");
				//Symbol = Dict.get(Matrix[y*2*W+x].Eter);
				Symbol = ' ';
				System.out.print(Symbol);
				//System.out.print(" ");
				if(Matrix[y*2*W+x].SdOn!=null&&Matrix[y*2*W+x].SdOn.blood>0)
				{
					if(Matrix[y*2*W+x].SdOn.friendly)
						System.out.printf("%2d", Matrix[y*2*W+x].SdOn.id);
					else{
						System.out.print(" ");
						System.out.print('E');
					}		
				}
				else{
					System.out.print(" ");
					System.out.print(Matrix[y*2*W+x].ShowPath);	
				}
		   	}
			System.out.print("| ");
			for(int j=0;j<W;j++)
		   	{
				
				y = i;
				x = j + W;
				System.out.print("|");
				//Symbol = Dict.get(Matrix[y*2*W+x].Eter);
				Symbol = ' ';
				System.out.print(Symbol);
				//System.out.print(" ");
				if(Matrix[y*2*W+x].SdOn!=null&&Matrix[y*2*W+x].SdOn.blood>0){
					if(Matrix[y*2*W+x].SdOn.friendly)
						System.out.printf("%2d", Matrix[y*2*W+x].SdOn.id);
						//System.out.print(Matrix[y*2*W+x].SdOn.id);
					else{
						System.out.print(" ");
						System.out.print('E');
					}
				}else{
					System.out.print(" ");
					System.out.print(Matrix[y*2*W+x].ShowPath);
				}
		   	}
			System.out.print("|");
		   	System.out.println();
		}
	}
	public static void Show(State S)
	{
		ReachableDist = new ArrayList<District>();
		char Symbol;
		int x,y;
		x = S.x;
		y = S.y;
		District tmp = Matrix[y * 2 * W + x];
		String path = "";
		TracePath(S,tmp,S.Sd.arm.Movement,true,path);
		
		for(int i=H-1;i>=0;i--)
		{
			for(int j=0;j<W;j++)
		   	{				
				y = i;
				x = j;
				System.out.print("|");
				//Symbol = Dict.get(Matrix[y*2*W+x].Eter);
				Symbol = ' ';
				System.out.print(Symbol);
				//System.out.print(" ");
				if(Matrix[y*2*W+x].SdOn!=null)
				{
					if(Matrix[y*2*W+x].SdOn.friendly)
						System.out.printf("%2d", Matrix[y*2*W+x].SdOn.id);
					else{
						System.out.print(" ");
						System.out.print('E');
					}		
				}
				else{
					System.out.print(" ");
					System.out.print(Matrix[y*2*W+x].ShowPath);	
				}
		   	}
			System.out.print("| ");
			for(int j=0;j<W;j++)
		   	{
				
				y = i;
				x = j + W;
				System.out.print("|");
				//Symbol = Dict.get(Matrix[y*2*W+x].Eter);
				Symbol = ' ';
				System.out.print(Symbol);
				//System.out.print(" ");
				if(Matrix[y*2*W+x].SdOn!=null){
					if(Matrix[y*2*W+x].SdOn.friendly)
						System.out.printf("%2d", Matrix[y*2*W+x].SdOn.id);
						//System.out.print(Matrix[y*2*W+x].SdOn.id);
					else{
						System.out.print(" ");
						System.out.print('E');
					}
				}else{
					System.out.print(" ");
					System.out.print(Matrix[y*2*W+x].ShowPath);
				}
		   	}
			System.out.print("|");
		   	System.out.println();
		}
		//Recover
		//ClearPath();
	}
	public static boolean ClearPath()
	{
		for(int k = 0; k < Matrix.length; k++)
		{
			Matrix[k].ShowPath = ' ';
			Matrix[k].Path = "";
			
		}
		ReachableDist = new ArrayList<District>();
		return true;
	}
	
	public static int[] Surrounding(int x, int y)
	{
		int W2 = 2 * Map.W;
		int W = Map.W;
		int Max = W2 * Map.H;
		int []List;
		List = new int [9];
		for(int i = 0;i<9;i++)
		{
			List[i] = -1;
		}
		List[0] = y * W2 + x;
		if(x!=W2-1)
		{
			List[1] = (y * (2 * W) + x + 1) % (Max);
			List[2] = ((y + 1) * (W2) + x + 1) % (Max);
			List[8] = ((y - 1) * (W2) + x + 1 + Max) % (Max);
		}
		if(x!=0)
		{
			List[5] = ((y * (2 * W) + x - 1) + Max) % (Max);
			List[4] = (((y + 1) * (W2) + x - 1) + Max)% (Max);
			List[6] = (((y - 1) * (W2) + x - 1) + Max)% (Max);
		}
		
		List[3] = (((y + 1) * (W2) + x) + Max) % (Max);		
		List[7] = (((y - 1) * (W2) + x) + Max) % (Max);				
		
		
		
		return List;
		
	}
	public static boolean TracePath(State S, District T,int Rm ,boolean FirstIn,String path)
	{
		//System.out.println("RM:"+Integer.toString(Rm)+", Path:"+path);
		int x, y, ind, Max;
		Max = 2 * W * H;
		x = T.x;
		y = T.y;
		int [] List;
		List = Surrounding(x,y);
		String p;
		int NeedStep = S.Sd.arm.movetype.movecost.GetCost(T.Eter);
		if(FirstIn){
			T.ShowPath = 'O';
			for(int idx = 0;idx < 9;idx++)
			{
				p = path + Integer.toString(idx);
				ind = List[idx];
				if(ind >= 0)
					TracePath(S, Matrix[ind], Rm, false, p);
			}
			
		}else{
			
			Rm = Rm - NeedStep;
			if(T.Eter == 'Q' || (T.SdOn!=null && !T.SdOn.friendly && T.SdOn.blood > 0))
				return false;
			else if(Rm<0)
				return false;
			else{
					if(T.SdOn!=null&&T.SdOn.friendly)
					{
						for(int idx = 0;idx < 9;idx++)
						{
							p = path + Integer.toString(idx);
							ind = List[idx];
							if(ind >= 0)
								TracePath(S, Matrix[ind], Rm, false, p);
						}
					}
					else{
						T.ShowPath = 'O';
						T.Path = path;
						ReachableDist.add(T);
		
						for(int idx = 0;idx < 9;idx++)
						{
							p = path + Integer.toString(idx);
							ind = List[idx];
							if(ind >= 0)
								TracePath(S, Matrix[ind], Rm, false, p);
						}
					}
				
				}
			}
		return true;
	}
	public static void Save(District Matrix []) throws FileNotFoundException, UnsupportedEncodingException
	{
		
			PrintWriter writer = new PrintWriter("C:\\GDATA\\map.txt","UTF-8");
			//int W = Wh;
			int x, y;
			char Symbol;
			for(int i=H-1;i>=0;i--)
			{
				for(int j=0;j<W;j++)
			   	{
					y = i;
					x = j;
					Symbol = Matrix[y*W+x].Eter;
					writer.print(Symbol);
			   		
			   	}   
				for(int j=0;j<W;j++)
			   	{
					y = i;
					x = j + W;
					Symbol = Matrix[y*W+x].Eter;
					writer.print(Symbol);
				
			   	}   
				writer.println();
			}
			writer.close();
	
		
	}
}
