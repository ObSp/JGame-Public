package JGamePackage.JGame.Types;

import JGamePackage.JGame.Instances.*;

public class RaycastResult {
    public final Instance HitInstance;
    public final Vector2 FinalPosition;
    
    public RaycastResult(Instance hit, Vector2 finalPos){
        HitInstance = hit;
        FinalPosition = finalPos;
    }

    @Override
    public String toString(){
        return "Hit instance "+HitInstance+" at position "+FinalPosition;
    }
}
