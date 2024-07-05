package JGamePackage.JGame.Types;

/**A class that combines Rotation and Position into a single object,
 * also including many utility functions to make the relation between them easier.
 * 
 */
public class CFrame {
    /**The rotation of the CFrame in {@code Radians}.
     * 
     */
    public double Rotation;

    /**The translation of the CFrame as a {@code Vector2}.
     * 
     */
    public Vector2 Position;

    /**Creates a CFrame with a translation of {@code Position} and a rotation of {@code Rotation}.
     * 
     * @param Position : The translation of the CFrame
     * @param Rotation : The rotation of the CFrame
     */
    public CFrame(Vector2 Position, double Rotation){
        this.Rotation = Rotation;
        this.Position = Position;
    }

    /**Creates a blank CFrame with no translation or rotation.
     * 
     */
    public CFrame(){
        this.Rotation = 0;
        this.Position = new Vector2();
    }

    /**Creates a new CFrame with a translation of {@code position} and no rotation.
     * 
     * @param position : The translation of the {@code CFrame}
     */
    public CFrame(Vector2 position){
        this.Position = position;
        this.Rotation = 0;
    }

    /**Creates a new CFrame with a rotation of {@code rotation} and no translation.
     * 
     * @param rotation : The rotation of the {@code CFrame}
     */
    public CFrame(double rotation){
        this.Rotation = rotation;
        this.Position = new Vector2();
    }

    /**Modifies this CFrame's rotation to look at the other position
     * 
     * @param position : The position to look at
     */
    public void LookAt(Vector2 position){
        double xDiff = position.X-Position.X;
        if (xDiff == 0){ //not sure if this can cause any potential errors, but avoids the arithmetic "tried to divide by zere/NaN" error
            return;
        }
        double yDiff = position.Y-Position.Y;
        Rotation = Math.atan(yDiff/xDiff);
    }

    /**Returns a new CFrame with a Position of {@code origin} and a rotation
     * of {@code LookAt(lookAt)}.
     * 
     * @param origin : The origin position of the CFrame
     * @param lookAt : The position to look at
     * @return
     */
    public static CFrame LookAt(Vector2 origin, Vector2 lookAt){
        CFrame c = new CFrame(origin, 0);
        c.LookAt(lookAt);
        return c;
    }

    @Override
    public boolean equals(Object other){
        if (other==this) return true;
        if (!(other instanceof CFrame)) return false;

        CFrame c = (CFrame) other;

        return Position.equals(c.Position) && Rotation == c.Rotation;
    }

    @Override
    public CFrame clone(){
        return new CFrame(Position.clone(), Rotation);
    }

    @Override
    public String toString(){
        return "Position: "+Position+"  Rotation: "+Rotation;
    }
}
