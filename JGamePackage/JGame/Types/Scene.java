package JGamePackage.JGame.Types;

import java.util.ArrayList;

import JGamePackage.JGame.JGame;
import JGamePackage.JGame.Instances.Instance;

public abstract class Scene {
    protected JGame parent;
    protected ArrayList<Instance> instances = new ArrayList<>();

    public abstract void init(JGame parent);
    public void show(){
        for (Instance x : instances){
            parent.addInstance(x);
        }
    };
    public void hide(){
        for (Instance x : instances){
            parent.removeInstance(x);
        }
    };
}
