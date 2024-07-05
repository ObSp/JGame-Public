package JGamePackage.JGame.Services;

import JGamePackage.JGame.*;
import JGamePackage.JGame.Instances.Instance;
import JGamePackage.JGame.Types.*;

public class RaycastService extends Service {
    static JGame game;

    public RaycastService(JGame parent){
        super(parent);
        game = parent;
    }

    private double lerp(double a, double b, double t){
        return (1-t)*a + t*b;
    }

    private boolean blacklistContains(Instance[] bl, Instance inst){
        for (Instance x : bl){
            if (x == inst) return true;
        }

        return false;
    }

    private Instance checkInstancesAtPoint(Vector2 p, RaycastParams params){
        for (int i = game.instances.getLength()-1; i > -1; i--){
            if (i >= game.instances.getLength()){
                break;
            }
            Instance inst = game.instances.get(i);

            if (inst.isCoordinateInBounds(p) && (!params.SolidsOnly || inst.Solid) && !blacklistContains(params.Blacklist, inst)){
                return inst;
            }
        }
        return null;
    }

    public RaycastResult Raycast(Vector2 origin, Vector2 destination, RaycastParams params){
        if (params == null)
            params = new RaycastParams();

        Vector2 curPoint = origin.clone();

        for (double t = 0.0; t <= 1.0; t += .01){
            var inst = checkInstancesAtPoint(curPoint, params);
            if (inst!=null){
                return new RaycastResult(inst, curPoint);
            }

            curPoint.X = (int) lerp(origin.X, destination.X, t);
            curPoint.Y = (int) lerp(origin.Y, destination.Y, t);
        }

        var inst = checkInstancesAtPoint(destination, params);
        if (inst != null){
            return new RaycastResult(inst, destination);
        }

        return null;
    }
}
