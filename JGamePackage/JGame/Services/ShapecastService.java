package JGamePackage.JGame.Services;

import java.awt.Color;

import JGamePackage.JGame.JGame;
import JGamePackage.JGame.Instances.Box2D;
import JGamePackage.JGame.Instances.Instance;
import JGamePackage.JGame.Types.RaycastParams;
import JGamePackage.JGame.Types.RaycastResult;
import JGamePackage.JGame.Types.Vector2;

public class ShapecastService extends Service {
    private JGame game;

    public ShapecastService(JGame parent){
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

    private Instance checkInstancesWithInstance(Instance p, RaycastParams params){
        for (int i = game.instances.getLength()-1; i > -1; i--){
            if (i >= game.instances.getLength()){
                break;
            }

            Instance inst = game.instances.get(i);

            if (inst.overlaps(p) && (!params.SolidsOnly || inst.Solid) && !blacklistContains(params.Blacklist, inst) && !(inst == p)){
                return inst;
            }
        }
        return null;
    }


    public RaycastResult RectangleCast(Vector2 origin, Vector2 destination, Vector2 castSize, RaycastParams params){
        //making sure no optional arguments are null
        if (params == null)
            params = new RaycastParams();

        if (castSize == null)
            castSize = new Vector2(100);

        Vector2 start = origin.clone();

        Box2D rayBox = new Box2D();
        rayBox.Size = castSize.clone();
        rayBox.FillColor = Color.red;
        rayBox.CFrame.Position = start;

        for (double t = 0.0; t <= 1.0; t += .001){
            var foundInst = checkInstancesWithInstance(rayBox, params);

            //collided with something
            if (foundInst != null){
                return new RaycastResult(foundInst, rayBox.CFrame.Position);
            }

            rayBox.CFrame.Position = new Vector2(lerp(start.X, destination.X, t), lerp(start.Y, destination.Y, t));
        }

        return null;

    }
}
