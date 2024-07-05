package JGamePackage.JGame.Services;

import JGamePackage.JGame.JGame;
import JGamePackage.JGame.Instances.Instance;
import JGamePackage.JGame.Types.PhysicsOptions;
import JGamePackage.JGame.Types.Vector2;

public class PhysicsService extends Service {

    public PhysicsOptions PhysicsSettings = new PhysicsOptions();

    public PhysicsService(JGame parent) {
        super(parent);
    }

    public void runPhysics(double dt){
        Instance[] instances = Parent.getInstances();

        for (int i = 0; i < instances.length; i++){
            Instance inst = instances[i];

            if (inst==null || inst.Anchored) continue;

            if (!inst.collidingBottom()){
                inst.inAir = true;
                inst.timeInAir += dt;
                
            } else {
                inst.inAir = false;
                inst.timeInAir = 0;
            }

            //For x: -1 = right, 1 = left
            //For y: -1 = bottom, 1 = top
            Vector2 colDir = inst.getCollideDirection();

            //making sure inst isnt inside of anything
            if (!colDir.isZero()){
                for (int j = 0; j < instances.length; j++){
                    Instance ji = instances[j];
                    if (ji == inst || ji==null) continue;
    
                    if (ji.overlaps(inst) && ji.Solid){
                        /*Vector2 topRight = inst.GetCornerPosition(Enum.InstanceCornerType.TopLeft);
                        if (colDir.Y==-1){
                            inst.SetTopSidePosition(topRight.Y-inst.Size.Y-2);
                        }*/
                        inst.CFrame.Position = inst.CFrame.Position.add(colDir.multiply(5));
                    }
                }
            }

            

            Vector2 vel = inst.Velocity;

            //directions
            int xDir = vel.X > -1 ? 1 : -1;
            //                      up  down
            int yDir = vel.Y > -1 ? -1 : 1;


            if ((xDir == -1 && inst.collidingLeft()) || (xDir==1 && inst.collidingRight())) vel.X = 0;
            if ((yDir == 1 && inst.collidingTop()) || (yDir==-1 && inst.collidingBottom())) vel.Y = 0;

            double posShift = getPositionShift(inst)*(dt*50);

            //touching ground
            if (!inst.inAir && vel.Y > 0){
                vel.Y = 0;
                posShift = 0;
            } else {

                /*Vector2 desiredPosition = inst.CFrame.Position.add(vel.X, vel.Y+ (int) (getPositionShift(inst)*(dt*50)));
                Vector2 sizeToCheck = new Vector2(inst.Size.X, desiredPosition.Y-inst.CFrame.Position.Y);
                
                Instance[] colliding = Parent.Services.CollisionService.GetInstancesInBox(desiredPosition, sizeToCheck, new CollisionOptions(inst, true));
                if (colliding.length != 0){
                    System.out.println(Arrays.toString(colliding));
                    posShift = 0;
                    int lowestY = colliding[0].GetCornerPosition(0).Y;
                    for (Instance x : colliding){
                        Vector2 p = x.GetCornerPosition(0); //top left
                        if (p.Y < lowestY){
                            lowestY = p.Y;
                        }
                    }
                    inst.SetBottomSidePosition(lowestY);
                }*/
            }

            vel.Y += posShift;


            //inst.setPosition(new Vector2(inst.Position.X + vel.X, inst.Position.Y + vel.Y));
            inst.CFrame.Position.X += vel.X;
            inst.CFrame.Position.Y += vel.Y;

            /**
            Vector2 vel = inst.Velocity.clone();
            vel.X *= dt*Parent.tickMult;    
            vel.Y *= dt*Parent.tickMult*-1;
            vel.Y += getPositionShift(inst);//*(dt*Parent.tickMult);

            //                   Right  Left
            int Xdir = vel.X > -1 ? 1 : -1;
            //                    Down  Up
            int Ydir = vel.Y > -1 ? 1 : -1;

            if ((Xdir == -1 && inst.collidingLeft()) || (Xdir == 1 && inst.collidingRight())) vel.X = 0;

            if ((Ydir == -1 && inst.collidingTop()) || (Ydir == 1 && inst.collidingBottom())) vel.Y = 0;

            inst.setPosition(vel.add(inst.Position));

            System.err.println(vel.Y);
             */

        }

    }

    //formula: 2pixels/secondsInAir^2

    private double getPositionShift(Instance inst){
        double grav = inst.timeInAir*(PhysicsSettings.GlobalGravity*inst.WeightPercentage);
        return Math.clamp(grav, 0, PhysicsSettings.AirResistance);
    }
    
}