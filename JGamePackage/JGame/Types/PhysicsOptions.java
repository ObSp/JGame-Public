package JGamePackage.JGame.Types;

public class PhysicsOptions {
    //max speed
    public double AirResistance = 30;
    public double GlobalGravity = 9.8;
    public boolean doesMassAffectGravity = false;
    public boolean BorderCollideable = false;

    public PhysicsOptions(double airRes, double grav, boolean dMAG){
        AirResistance = airRes;
        GlobalGravity = grav;
        doesMassAffectGravity = dMAG;
    }

    public PhysicsOptions(){}
}
