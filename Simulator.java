import java.util.concurrent.ForkJoinPool;

public class Simulator extends java.lang.Thread{
	static SunData sundataLocal;
	int year;

	Simulator(SunData sundata){
		sundataLocal=sundata;
	}

	static final ForkJoinPool fjPool = new ForkJoinPool();
	
	public void reset(){
		//reset all ext to 0.4:. this always runs only after the tree growth calculations are done and so no protection is needed
		for(Tree t : sundataLocal.trees){
			t.setExt(0.4f);
		}

		//reset year counter to 0
		year = 0;

		TreeGrow.resetbtn = false;
	}

	public void nextRun(){
		year ++;

		//seqeuntially go through layers from top to bottom
		for(int i =18; i>=0;i-=2){
			int layerStart =i;
			int layerStop = i+2;

			//start a thread to handle the calculations for the current layer
			fjPool.invoke(new SunThread(layerStart,layerStop,0, sundataLocal.trees.length));

		}
	}

	public void run(){
		System.out.println("sim is running");
		
		//set year counter to 0
		year = 0;

		while(true){ //while the program is running

			nextRun(); //calls nextRun() method to calculate new sun values for a new year which then grows all the trees

			//update year counter text field
			TreeGrow.yearLabel.setText("year "+year);

			//check buttons
			//if reset button pressed, reset extents to 0.4
			if(TreeGrow.resetbtn){
				reset();
			}
			
			if(TreeGrow.pausebtn){
				TreeGrow.pausebtn = false;

				while(!TreeGrow.playbtn){
					//wait until play button is pressed to continue
					try{
						Thread.sleep(1);
					}
					catch(InterruptedException ex){
						Thread.currentThread().interrupt();
					}
				} 
				TreeGrow.playbtn = false;
			}

			else if(TreeGrow.endbtn){
				System.exit(0);
			}

			//reset the sun for the new year
			sundataLocal.sunmap.resetShade();

			//this pause is longer than the rendering pause of 20ms. Thus allowing the rendering to catch up with this thread.
			try{
				Thread.sleep(50);
			}
			catch(InterruptedException ex){
				Thread.currentThread().interrupt();
			}

			
		}


	}
}