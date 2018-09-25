
// Trees define a canopy which covers a square area of the landscape
import java.lang.Math;

public class Tree{
	
private
	int xpos;	// x-coordinate of center of tree canopy
	int ypos;	// y-coorindate of center of tree canopy
	float ext;	// extent of canopy out in vertical and horizontal from center
	int numBlockThatCount;
	
	static float growfactor = 1000.0f; // divide average sun exposure by this amount to get growth in extent
	
public	
	Tree(int x, int y, float e){
		xpos=x; ypos=y; ext=e; 
	}
	
	int getX() {
		return xpos;
	}
	
	int getY() {
		return ypos;
	}
	
	float getExt() {
		return ext;
	}
	
	void setExt(float e) {
		ext = e;
	}

	// return the average sunlight for the cells covered by the tree
	float sunexposure(Land land){
		float sun = 0;
		int xlimit = land.getDimX();
		int ylimit = land.getDimY();
		int size = Math.round(ext);
		numBlockThatCount=0;

		//loops through the blocks that the tree covers and adds up the sunlight in each of those blocks
		for(int x = xpos-size; x<=xpos+size; x++){
			for(int y = ypos-size; y<=ypos+size; y++){
				if(0<=x && x<xlimit && 0<=y && y<ylimit){
					sun+=land.getShade(x,y);	//does a READ from SunData Land map. must NOT be allowed to do this if another tree with some of the same blocks are being processed.
					numBlockThatCount++;
				}
			}
		}
		return sun; //return total sunlight that tree gets
	}
	
	// is the tree extent within the provided range [minr, maxr)
	boolean inrange(float minr, float maxr) {
		return (ext >= minr && ext < maxr);
	}
	
	// grow a tree according to its sun exposure
	void sungrow(Land land) {
		float averageSun = sunexposure(land)/numBlockThatCount; //calc average sun for tree by calling the sunexposure() method and dividing the total sunlight by the number of blocks that the tree covers
		land.shadow(this);//update sunmap by passing it the tree
		float tempExt = getExt()+(averageSun/growfactor); //does a READ of this tree (should be fine if re-entrant lock)
		setExt(tempExt); //sets a new extent for this tree. Performs a WRITE. This is fine becuase no other threads will use this particular trees extent.
	}
}







