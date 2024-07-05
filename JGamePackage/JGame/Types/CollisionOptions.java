package JGamePackage.JGame.Types;

import java.util.Arrays;

import JGamePackage.JGame.Instances.Instance;

public class CollisionOptions {
    public Instance[] Blacklist;
    public boolean SolidsOnly;

    public CollisionOptions(Instance[] blacklist){
        Blacklist = blacklist;
        SolidsOnly = false;
    }

    public CollisionOptions(Instance[] blacklist, boolean solidOnly){
        Blacklist = blacklist;
        SolidsOnly = solidOnly;
    }

    public CollisionOptions(Instance blacklist, boolean solidOnly){
        Blacklist = new Instance[] {blacklist};
        SolidsOnly = solidOnly;
    }

    public CollisionOptions(boolean solidOnly){
        Blacklist = new Instance[0];
        SolidsOnly = solidOnly;
    }

    public CollisionOptions(){
        Blacklist = new Instance[0];
        SolidsOnly = false;
    }

    public String toString(){
        return Arrays.toString(Blacklist)+", "+SolidsOnly;
    }
}
