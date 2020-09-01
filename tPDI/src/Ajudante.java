
public class Ajudante{
	
	public static int[][] matrizFloatParaMatrizInt(float[][] mFloat){
		
		int[][] mInt = new int[mFloat.length][mFloat.length];
		
		for(int i = 0; i < mFloat.length; i++)
	    	for(int j = 0; j < mFloat.length; j++)
	    		mInt[i][j] = (int) mFloat[i][j];
		
		return mInt;
		
	}
	
	// Function to rotate the matrix 90 degree clockwise 
	public static int[][] rotate90Clockwise(int[][] m){
		
		int N = m.length;
		int[][] a = new int[N][N];
		
		for(int i = 0; i < N; i++)
	    	for(int j = 0; j < N; j++)
	    		a[i][j] = m[i][j];
		
		// Traverse each cycle 
		for(int i = 0; i < N / 2; i++)
	    	for(int j = i; j < N - i - 1; j++){
	        	
	        	// Swap elements of each cycle 
	        	// in clockwise direction 
	        	int temp = a[i][j]; 
	            a[i][j] = a[N - 1 - j][i]; 
	            a[N - 1 - j][i] = a[N - 1 - i][N - 1 - j]; 
	            a[N - 1 - i][N - 1 - j] = a[j][N - 1 - i]; 
	            a[j][N - 1 - i] = temp; 
	        }
		
		return a;
	    
	} 

}
