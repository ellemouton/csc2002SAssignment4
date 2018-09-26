public class LandChunk{
	
	float[][] lp;

	public LandChunk(int x, int y){
		lp =  new float[x][y];
	}

	public void setSun(int x,int y, float val){
		lp[x][y]=val;
	}
	public float getSun(int x, int y){
		return lp[x][y];
	}

}