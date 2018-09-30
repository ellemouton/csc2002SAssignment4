 import java.lang.Math;

public class Land{
	
	// sun exposure data here
	float[][] sunArrV1; //origional sunmap read from text file
	//float[][] sunArrChange; //changing sunmap
	LandChunk[][] splitSunArr;
	int dimX;
	int dimY;
	int blockYdim;
	int blockXdim;
	int landChunkXdim;
	int landChunkYdim;

	static float shadefraction = 0.1f; // only this fraction of light is transmitted by a tree

	Land(int dx, int dy) {
		sunArrV1 = new float[dx][dy];
		//sunArrChange = new float[dx][dy];

		dimY = dy;
		dimX = dx;

		blockYdim = 50;
		blockXdim = 50;

		landChunkXdim =dimX/blockXdim;
		landChunkYdim = dimY/blockYdim;
		splitSunArr = new LandChunk[landChunkXdim][landChunkYdim];

	}

	// return the number of landscape cells in the x dimension
	int getDimX() {
		return dimX;
	}
	
	// return the number of landscape cells in the y dimension
	int getDimY() {
		return dimY; 
	}
	
	// Reset the shaded landscape to the same as the initial sun exposed landscape
	// Needs to be done after each growth pass of the simulator
	void resetShade() {
		for(int x =0;x<sunArrV1.length;x++){
			for(int y =0;y<sunArrV1.length;y++){
				//INTERMEDIATE VALUES HERE. THUS NEED TO  MAKE IT ATOMIC
				float temp = getFull(x,y);
				setShade(x,y,temp);
			}
		}
	}
	
	// return the sun exposure of the initial unshaded landscape at position <x,y>
	float getFull(int x, int y) { // This array 
		return new Float(sunArrV1[x][y]); 
	}
	
	// set the sun exposure of the initial unshaded landscape at position <x,y> to <val>
	void setFull(int x, int y, float val) { // this is only called when sunmap is being initialised. No other threads will be running. This is safe/
		sunArrV1[x][y] = val;
	}
	
	// return the current sun exposure of the shaded landscape at position <x,y>
	float getShade(int x, int y) {
		int [] info = getChunkCoordinates(x,y);
		return (splitSunArr[info[0]][info[1]]).getSun(info[2],info[3]);
		//return sunArrChange[x][y];
	}
	
	// set the sun exposure of the shaded landscape at position <x,y> to <val>
	void setShade(int x, int y, float val){
		int [] info = getChunkCoordinates(x,y);
		splitSunArr[info[0]][info[1]].setSun(info[2],info[3],val);
		//sunArrChange[x][y]=val;
	}
	
	void splitFullIntoChunks(){
		for(int x =0; x<landChunkXdim;x++){
			for(int y = 0; y<landChunkYdim; y++){

				splitSunArr[x][y] = new LandChunk(blockXdim,blockYdim);

				for(int i = 0;i<blockXdim;i++){
					for(int j = 0;j<blockYdim;j++){
						splitSunArr[x][y].setSun(i, j, getFull(x*blockXdim+i,y*blockYdim+j));
					}
				}
			}
		}

	}

	int [] getChunkCoordinates(int x, int y){ //returns chunk location, given the location on the enitre sunmap
		int [] blockCoordinates  = new int[4];
		blockCoordinates[0]= (int)Math.floor(x/blockXdim);// x position of the block in the splitSunArr array
		blockCoordinates[1]= (int)Math.floor(y/blockYdim);// y position of the block in the splitSunArr array
		blockCoordinates[2] = x - (blockXdim*((int)Math.floor(x/blockXdim))); //x position of the sun point within the block
		blockCoordinates[3] = y - (blockYdim*((int)Math.floor(y/blockYdim))); //y position of the sun point within the block
		return blockCoordinates;
	}



	// reduce the sun exposure of the shaded landscape to 10% of the original
	// within the extent of <tree> 
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