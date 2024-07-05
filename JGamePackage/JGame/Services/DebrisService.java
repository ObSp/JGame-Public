package JGamePackage.JGame.Services;

import java.util.ArrayList;

import JGamePackage.JGame.JGame;
import JGamePackage.JGame.Instances.Instance;

public class DebrisService extends Service {

    private ArrayList<DebrisItem> removalList = new ArrayList<>();

    //optimisation, as to not call removalList.size() every 1/60 seconds
    private int numRemovalItems = 0;

    private double curTime;

    public DebrisService(JGame parent) {
        super(parent);

        curTime = (double) System.currentTimeMillis()/1000.0;

        parent.OnTick.Connect(dt->cycle(dt));
    }

    private void cycle(double dt){
        curTime += dt;
        for (int i = removalList.size()-1; i > -1; i--){
            DebrisItem item = removalList.get(i);
            if (curTime > item.endSeconds){
                Parent.removeInstance(item.Instance);
                removalList.remove(i);
                numRemovalItems--;
            }
        }
    }

    public void AddItem(Instance inst, double bufferSeconds){
        numRemovalItems++;
        removalList.add(new DebrisItem(inst, bufferSeconds, (double) System.currentTimeMillis()/1000.0));
    }

    public void RemoveItem(Instance inst){
        for (int i = 0; i < numRemovalItems; i++){
            if (removalList.get(i).Instance == inst){
                removalList.remove(i);
                return;
            }
        }
    }

    private class DebrisItem {
        public Instance Instance;
        public double endSeconds;

        public DebrisItem(Instance inst, double buffer, double startTime){
            this.Instance = inst;
            this.endSeconds = startTime+buffer;
        }
    
    }
}