package Troop;


public class MoveCost {
	int W;
	int G;
	int D;
	int F;
	int C;
	
	MoveCost(int w,int g,int d,int f,int c)
	{
		W=w;G=g;D=d;F=f;C=c;
	}
	public int GetCost(char t)
	{
		int Cost = 0;
		switch(t){
		case 'W':
			Cost = W;
			break;
		case 'G':
			Cost = G;
			break;
		case 'D':
			Cost = D;
			break;
		case 'F':
			Cost = F;
			break;
		case 'C':
			Cost = C;
			break;
		default:
			Cost = 100;
			
		
		}
		return Cost;
	}
}
