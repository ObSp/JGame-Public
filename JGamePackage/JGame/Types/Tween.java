package JGamePackage.JGame.Types;

import JGamePackage.lib.VoidSignal;

public class Tween {
    public final VoidSignal Ended = new VoidSignal();
    public final Object start;
    public final Object end;

    public Tween(Object start, Object end){
        this.start = start;
        this.end = end;
    }
}
