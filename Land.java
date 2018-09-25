import java.lang.Math;

public class Land{
	
	// to do/
	// sun exposure data here
	float[][] sunArrV1;
	float[][] sunArrChange;
	int dimX;
	int dimY;

	static float shadefraction = 0.1f; // only this fraction of light is transmitted by a tree

	Land(int dx, int dy) {
		sunArrV1 = new float[dx][dy];
		sunArrChange = new float[dx][dy];
		dimY = dy;
		dimX = dx;
	}

	int getDimX() {
		return dimX;
	}
	
	int getDimY() {
		return dimY; 
	}
	
	// Reset the shaded landscape to the same as the initial sun exposed landscape
	// Needs to be done after each growth pass of the simulator
	void resetShade() {
		for(int x =0;x<sunArrV1.length;x++){
			for(int y =0;y<sunArrV1.length;y++){
				sunArrChange[x][y]=new Float(sunArrV1[x][y]);
			}
		}
	}
	
	float getFull(int x, int y) {
		// to do
		return 0.0f; // incorrect value
	}
	
	void setFull(int x, int y, float val) {
		sunArrV1[x][y] = val;
	}
	
	float getShade(int x, int y) {
		return sunArrChange[x][y];
	}
	
	void setShade(int x, int y, float val){
		sunArrChange[x][y]=val;
	}
	
	// reduce the 
	void shadow(Tree tree){
		int size = Math.round(tree.getExt());
		int xpos = tree.getX();
		int ypos = tree.getY();

		for(int x = xpos-size; x<=xpos+size; x++){
			for(int y = ypos-size; y<=ypos+size; y++){
				if(0<=x && x<dimX && 0<=y && y<dimY){
					float tempShade = getShade(x,y)*shadefraction;//INTERMEDIATE VALUE HERE. WILL NEED TO PROTECT
					setShade(x,y,tempShade);
				}
			}
		}
	}
}