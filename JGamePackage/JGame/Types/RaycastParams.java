package JGamePackage.JGame.Types;

import java.util.Arrays;

import JGamePackage.JGame.Instances.Instance;

public class RaycastParams {
    public Instance[] Blacklist;
    public boolean SolidsOnly = false;

    public RaycastParams(Instance[] blacklist, Boolean solidsOnly){
        Blacklist = blacklist;
        SolidsOnly = solidsOnly;
    }

    public RaycastParams(){
        Blacklist = new Instance[0];
    }

    @Override
    public String toString(){
        return Arrays.toString(Blacklist)+", "+SolidsOnly;
    }
}
