import java.util.concurrent.RecursiveAction;

public class SunThread extends RecursiveAction{
    int low;
    int high;
    int layerStart;
    int layerStop;

 //SunThread(layerStart,layerStop,sundata.trees,0, sundata.trees.length))
    static final int SEQUENTIAL_CUTOFF = 250001; //for a dataset of 1 million, this SC will result in 4 threads
    
    SunThread(int lstart, int lstop, int l, int h){
        layerStop = lstop;
        layerStart = lstart;
        low = l;
        high = h;
    }
    
    protected void compute(){
        if((high-low)<SEQUENTIAL_CUTOFF){ // this is run sequentially 
            //System.out.println("im a new thread calculating for layer: "+layerStart+" to "+layerStop);
                      
            
             for(int i=low;i<high;i++){
                 Tree t = Simulator.sundataLocal.trees[i];
                 
                 if(t.inrange(layerStart,layerStop)){ //if this tree is in the current level
                        //System.out.println("Processing tree: "+i+" "+t.getExt());
                        t.sungrow(Simulator.sundataLocal.sunmap);
                        //System.out.println(t.getExt());
                 }
            
                          
             }
        }
        else{
            SunThread left = new SunThread(layerStart, layerStop, low, (high+low)/2);
            SunThread right = new SunThread(layerStart, layerStop, (high+low)/2, high);
            left.fork(); //left thread starts (.compute())
            right.compute(); 
            left.join(); //wait for left thread to complete
        }
    
    }//end compute
     
}//end class