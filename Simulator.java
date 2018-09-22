import java.util.concurrent.ForkJoinPool;

public class Simulator extends java.lang.Thread{
	static SunData sundataLocal;

	Simulator(SunData sundata){
		sundataLocal=sundata;
	}

	static final ForkJoinPool fjPool = new ForkJoinPool();
	
	public void run(){
		System.out.println("sim is running");
		
		int year = 0;

		//reset all ext to 0.4:
		for(Tree t : sundataLocal.trees){
			t.setExt(0.4f);
		}

		while(true){ // while running (later should exit when END button pressed)
			year ++;

			//seqeuntially go through layers from top to bottom
			for(int i =18; i>=0;i-=2){
				int layerStart =i;
				int layerStop = i+2;

				fjPool.invoke(new SunThread(layerStart,layerStop,0, sundataLocal.trees.length));

			}
			System.out.println("year "+year);
			sundataLocal.sunmap.resetShade();

			/*try{
				Thread.sleep(1000);
			}
			catch(InterruptedException ex){
				Thread.currentThread().interrupt();
			}*/
		}


	}
}