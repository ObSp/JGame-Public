package JGamePackage.JGame.Instances;

import java.awt.Color;
import java.awt.Graphics;

import JGamePackage.JGame.*;
import JGamePackage.JGame.Types.*;
import JGamePackage.JGame.Types.Enum;
import JGamePackage.lib.ArrayTable;
import JGamePackage.lib.BiSignal;

/**An abstract class that all {@code JGame} classes are a subclass of. <p>
 * It contains basic methods, like Instance.Destroy() that are needed when working with basic rendering objects.
 * There is currently only one constructor present, as the instance variables are intended to be set after creating the object.<p>
 * 
 * The {@code Parent} property should only be set once <b><i>all inital properties have been set</i></b>, as events and rendering will start as soon
 * as this object has been added to a JGame's rendering list.<p>
 * In addition, the {@code Parent} property should <b>never</b> be set by the user.
 * Instead, the intended use is to call {@code JGame.addInstance(this)}.
 * 
 */
public abstract class Instance {
    /**The size of the Instance in 2D space.
     * 
     */
    public Vector2 Size = new Vector2(100, 100);

    /**The CFrame of the Instance, representing both Position and Rotation in 2D space
     * 
     */
    public CFrame CFrame = new CFrame();

     /**Allows the setting of Render Position per frame. This will be erased after rendering.
     * 
     */
    public Vector2 RenderPosition;

    /**The Anchor Point of the Instance, specifying where the position pivot of the object is located. 
     * The most common anchor points are listed here: <p>
     * {@code (0,0)} - The top left corner <p>
     * {@code (50,50)} - The middle <p>
     * {@code (100,100)} - The bottom right corner <p>
     * <p>
     * Note that the X and Y values should usually stay true to {@code 0 <= x <= 100} and {@code 0 <= y <= 100}
     * 
     */
    public Vector2 AnchorPoint = new Vector2(0);

    /**The Rotation Anchor Point of the Instance, specifying where the rotation pivot of the object is located. 
     * The most common anchor points are listed here: <p>
     * {@code (0,0)} - The top left corner <p>
     * {@code (50,50)} - The middle <p>
     * {@code (100,100)} - The bottom right corner <p>
     * <p>
     * Note that the X and Y values should usually stay true to {@code 0 <= x <= 100} and {@code 0 <= y <= 100}. 
     * By default, all instances will rotate around their center point, unless this field is changed.
     * 
     */
    public Vector2 RotationAnchorPoint = new Vector2(50);

    /**A non-unique identifier that can be used to access this object through the {@code Parent}.
     * 
     */
    public String Name = "Instance";

    /**An instance variable that can be used to associate this Instance with other Objects.
     * 
     */
    public Object Associate;

    public JGame Parent;

    public double WeightPercentage = 1;

    /**What color this object will be drawn as
     * 
     */
    public Color FillColor = Color.white;

    /**The number of pixels of border that will surround this instance.
     * 
     */
    public int BorderSizePixel = 0;

    /**The color of this instance's outline. Will only be applied if {@code BorderSizePixel} is greater than 0.
     * 
     */
    public Color BorderColor = Color.black;

    /**A list of non-unique strings that can be used to gather collections of objects
     * 
     */
    public ArrayTable<String> Tags = new ArrayTable<>();

    /**By how much this object will be displaced every tick. <p>
     * <b>NOTE:</b> This will only be applied on non-anchored instances.
     * 
     */
    public Vector2 Velocity = new Vector2(0, 0);

    /**Controls whether or not other solid objects can pass through this object
     * 
     */
    public boolean Solid = false;

    /**Controls whether this instance will be affected by physics
     * 
     */
    public boolean Anchored = true;

    /**Whether this Instance should be affected by the Camera's offset. <p>
     * This is useful for static background images that should always stay in the same place no matter what.
     * 
     */
    public boolean MoveWithCamera = true;

    /**This instance's priority in rendering.
     * A higher ZIndex means that this Instance will rendered on top of other Instances.
     * 
     */
    public int ZIndex = 0;

    protected double opacity = 1;

    public boolean wasDrawn = false;



    /**A signal fired when the user left-clicks on this Instance
     */
    public BiSignal<Double, Double> MouseButton1Down = new BiSignal<>();

    /**A signal fired when the user left-clicks on this Instance
     */
    public BiSignal<Double, Double> MouseButton1Up = new BiSignal<>();

    /**A signal fired when the mouse pointer enters this Instance
     */
    public BiSignal<Double, Double> MouseEntered = new BiSignal<>();

    /**A signal fired when the mouse pointer exits this Instance
     */
    public BiSignal<Double, Double> MouseExited = new BiSignal<>();

    public boolean inAir = false;
    public double timeInAir = 0.0;


    /**Sets {@code this.Parent} to null, removes itself from the rendering list of {@code this.Parent}, and sets all instance variables to null
     * 
     */
    public void Destroy(){
        Parent.removeInstance(this);
        CFrame = null;
        Size = null;
        Name = null;
        FillColor = null;
        BorderSizePixel = 0;
        BorderColor = null;
        Tags = null;
        Velocity = null;
        Solid = false;
        Anchored = false;
    }



    /**Adds the tag {@code tag} to this Instance's tags
     * 
     * @param tag The tag to be added to this instance
     */
    public void addTag(String tag){
        if (Tags.indexOf(tag)!=-1) return; // already added tag

        Tags.add(tag);
    }

    /**Removes the tag {@code tag} from this Instance's tags
     * 
     * @param tag The tag to be removed from this instance
     */
    public void removeTag(String tag){
        Tags.remove(tag);
    }
    /**Returns whether or not the Tag {@code tag} is present in the list of this instance's tags
     * 
     * @param tag : The tag to check for
     * @return Whether or not the tag is present in this instance's list of tags
     */
    public boolean hasTag(String tag){
        return Tags.indexOf(tag) != -1;
    }


    /**Returns a shallow array copy of this instance's tags
     * 
     * @return A String[] array of this instance's tags
     */
    public String[] getTags(){
        return Tags.toArray();
    }

    /**Returns whether or not Instance {@code is touching the top border of the screen}
     * 
     * @return
     */
    public boolean touchingBorderTop(){
        return GetRenderPosition().Y<0;
    }

    public boolean touchingBorderLeft(){
        return GetRenderPosition().X<0;
    }

    public boolean touchingBorderRight(){
        return topRightCorner().X>=Parent.getTotalScreenSize().X;
    }

    public boolean touchingBorderBottom(){
        return bottomRightCorner().Y>=Parent.getTotalScreenSize().Y;
    }

    public boolean collidingRight(){
        Instance[] bl = {this};
        Vector2 pos = topRightCorner();
        CollisionOptions opts = new CollisionOptions(bl, true);
        Instance hit = Parent.Services.CollisionService.CheckCollisionInBox(pos, new Vector2(3, Size.Y), opts);


        return hit != null;
    }


    public boolean collidingLeft(){
        Instance[] bl = {this};
        Vector2 pos = topLeftCorner();
        CollisionOptions opts = new CollisionOptions(bl, true);
        Instance hit = Parent.Services.CollisionService.CheckCollisionInBox(pos.add(-3, 0), new Vector2(3, Size.Y), opts);


        return hit != null;
    }

    public boolean collidingBottom(){
        Instance[] bl = {this};
        Vector2 pos = bottomLeftCorner();
        CollisionOptions opts = new CollisionOptions(bl, true);
        Instance hit = Parent.Services.CollisionService.CheckCollisionInBox(pos.add(0, 3), new Vector2(Size.X, 3), opts);

        return hit != null;
    }

    public boolean collidingTop(){
        Instance[] bl = {this};
        Vector2 pos = topLeftCorner();
        CollisionOptions opts = new CollisionOptions(bl, true);
        Instance hit = Parent.Services.CollisionService.CheckCollisionInBox(pos.add(0, -3), new Vector2(Size.X, 3), opts);


        return hit != null;
    }


    public boolean canMoveLeft(){
        return !collidingLeft() && !touchingBorderLeft();
    }

    public boolean canMoveRight(){
        return !collidingRight() && !touchingBorderRight();
    }

    public boolean canMoveUp(){
        return !collidingTop() && !touchingBorderTop();
    }

    public boolean canMoveDown(){
        return !collidingBottom() && !touchingBorderBottom();
    }

    public Vector2 getCollideDirection(){
        Vector2 vect = new Vector2(0, 0);

        boolean bottom = this.collidingBottom();
        boolean top = this.collidingTop();
        boolean left = this.collidingLeft();
        boolean right = this.collidingRight();

        if (top) vect.Y = 1;

        if (bottom) vect.Y = -1;

        //if (top && bottom) vect.Y = 0;

        if (left) vect.X = 1;

        if (right) vect.X = -1;

        if (left && right) vect.X = 0;

        return vect;
    }



    public boolean overlaps(Instance other){
        if (other==null) return false;

        double leftCorner = topLeftCorner().X;
        double top = topLeftCorner().Y;
        double rightCorner = topRightCorner().X;
        double bottom = bottomLeftCorner().Y;

        double otherLeft = other.GetCornerPosition(Enum.InstanceCornerType.TopLeft).X;
        double otherTop = other.GetCornerPosition(Enum.InstanceCornerType.TopLeft).Y;
        double otherRight = other.GetCornerPosition(Enum.InstanceCornerType.TopRight).X;
        double otherBottom = other.GetCornerPosition(Enum.InstanceCornerType.BottomLeft).Y;


        return 

        leftCorner < otherRight &&

        rightCorner > otherLeft &&

        top < otherBottom &&

        bottom > otherTop;
    }
    
    public boolean overlaps(Vector2 pos, Vector2 size){
        double leftCorner = topLeftCorner().X;
        double top = topLeftCorner().Y;
        double rightCorner = topRightCorner().X;
        double bottom = bottomLeftCorner().Y;

        double otherLeft = pos.X;
        double otherTop = pos.Y;
        double otherRight = pos.X+size.X;
        double otherBottom = pos.Y + size.Y;


        return 

        leftCorner < otherRight &&

        rightCorner > otherLeft &&

        top < otherBottom &&

        bottom > otherTop;
    }

    public boolean isCoordinateInBounds(Vector2 coord){
        if (this.CFrame == null) return false;

        if (coord.equals(CFrame.Position)) return true;

        double x = coord.X;
        double y = coord.Y;

        //distance between top left corner and anchor point percentage
        double leftSide = topLeftCorner().X;
        double rightSide = topRightCorner().X;
        double top = topLeftCorner().Y;
        double bottom = bottomLeftCorner().Y;

        return (leftSide<x && x <rightSide && y<bottom && y>top);

        /**if (this.CFrame == null) return false;

        int x = coord.X;
        int y = coord.Y;

        int leftCorner = this.CFrame.Position.X;
        int rightCorner = this.CFrame.Position.X + this.Size.X;
        int top = this.CFrame.Position.Y;
        int bottom = this.CFrame.Position.Y + this.Size.Y;

        boolean inBounds = (leftCorner<x && x <rightCorner && y<bottom && y>top);

        return inBounds;*/




    }


    abstract public void paint(Graphics g);

    abstract public Instance clone();
    
    /**An internal method used by the physics handler to move instances, letting them handle the new position on their own
     * 
     * @param velPos : The position to move to
     */
    public void setPosition(Vector2 velPos){
        CFrame.Position = velPos;
    };

    public void setInstanceVariableByName(String variable, Object value){
        if (variable.equals("CFrame")) CFrame = (CFrame) value;
        if (variable.equals("Size")) Size = ((Vector2)value);
        if (variable.equals("FillColor")) FillColor = (Color) value;
        if (variable.equals("Name")) Name = (String) value;
        if (variable.equals("Velocity")) Velocity = (Vector2) value;
        if (variable.equals("Solid")) Solid = (boolean) value;
        if (variable.equals("Anchored")) Anchored = (boolean) value;
        if (variable.equals("Rotation")) CFrame.Rotation = (double) value;
        if (variable.equals("Position")) CFrame.Position = (Vector2) value;
        if (variable.equals("Opacity")) this.SetOpacity((double) value);
    }

    public Object getInstanceVariableByName(String variable){
        if (variable.equals("CFrame")) return CFrame;
        if (variable.equals("Size")) return Size;
        if (variable.equals("FillColor")) return FillColor;
        if (variable.equals("Name")) return Name;
        if (variable.equals("Velocity")) return Velocity;
        if (variable.equals("Solid")) return Solid;
        if (variable.equals("Anchored")) return Anchored;
        if (variable.equals("Rotation")) return CFrame.Rotation;
        if (variable.equals("Position")) return CFrame.Position;
        if (variable.equals("Opacity")) return opacity;
        return null;
    }


    protected Vector2 getAnchorPointOffset(){
        return new Vector2(getAnchorPointOffsetX(), getAnchorPointOffsetY());
    }

    protected int getAnchorPointOffsetX(){
        return (int) ((double)Size.X*(((double)AnchorPoint.X)/100.0));
    }

    protected int getAnchorPointOffsetY(){
        return (int) ((double)Size.Y*(((double)AnchorPoint.Y)/100.0));
    }

    public Vector2 GetRenderPosition(){
        if (MoveWithCamera && this.Parent != null){
            return Parent.Camera.GetInstancePositionRelativeToCameraPosition(this).subtract(getAnchorPointOffset());
        }
        return CFrame.Position.subtract(getAnchorPointOffset());
    }

    //cornerstuff

    protected Vector2 topLeftCorner(){
        return new Vector2(CFrame.Position.X - getAnchorPointOffsetX(), CFrame.Position.Y - getAnchorPointOffsetY());
    }

    protected Vector2 topRightCorner(){
        return new Vector2((CFrame.Position.X + Size.X) - getAnchorPointOffsetX(), CFrame.Position.Y - getAnchorPointOffsetY());
    }

    protected Vector2 bottomLeftCorner(){
        return new Vector2(CFrame.Position.X - getAnchorPointOffsetX(), (CFrame.Position.Y + Size.Y) - getAnchorPointOffsetY());
    }

    protected Vector2 bottomRightCorner(){
        return new Vector2((CFrame.Position.X + Size.X) - getAnchorPointOffsetX(), (CFrame.Position.Y + Size.Y) - getAnchorPointOffsetY());
    }

    public Vector2 GetCornerPosition(int corner){
        if (corner == Enum.InstanceCornerType.TopLeft) return topLeftCorner();
        if (corner == Enum.InstanceCornerType.TopRight) return topRightCorner();
        if (corner == Enum.InstanceCornerType.BottomLeft) return bottomLeftCorner();
        if (corner == Enum.InstanceCornerType.BottomRight) return bottomRightCorner();
        throw new Error("No Such Corner Type Exception: corner identifier "+corner+" is not a valid corner type.");
    }

    public Vector2 GetCenterPosition(){
        return topLeftCorner().add(Size.X/2, Size.Y/2);
    }

    public void SetTopLeftCornerPosition(Vector2 pos){
        CFrame.Position.Y = pos.Y-getAnchorPointOffsetY();
        CFrame.Position.X = pos.X-getAnchorPointOffsetX();
    }

    public void SetLeftSidePosition(int x){
        CFrame.Position.X = x-getAnchorPointOffsetX();
    }

    public void SetTopSidePosition(int y){
        CFrame.Position.Y = y-getAnchorPointOffsetY();
    }

    public void SetBottomSidePosition(int y){
        CFrame.Position.Y = (y+getAnchorPointOffsetY())-Size.Y;
    }

    //--TWEENING--//
    public Tween TweenPosition(Vector2 goal, TweenInfo tweenInfo){
        return Parent.Services.TweenService.TweenVector2(this, "Position", goal, tweenInfo, false);
    }

    public Tween TweenSize(Vector2 goal, TweenInfo tweenInfo){
        return Parent.Services.TweenService.TweenVector2(this, "Size", goal, tweenInfo, false);
    }

    public Tween TweenRotation(Double goal, TweenInfo tweenInfo){
        return Parent.Services.TweenService.TweenDoubleProperty(this, "Rotation", goal, tweenInfo, false);
    }

    public Tween TweenOpacity(Double goal, TweenInfo tweenInfo){
        return Parent.Services.TweenService.TweenDoubleProperty(this, "Opacity", goal, tweenInfo, false);
    }

    public Tween TweenPositionParallel(Vector2 goal, TweenInfo tweenInfo){
        return Parent.Services.TweenService.TweenVector2(this, "Position", goal, tweenInfo, true);
    }

    public Tween TweenSizeParallel(Vector2 goal, TweenInfo tweenInfo){
        return Parent.Services.TweenService.TweenVector2(this, "Size", goal, tweenInfo, true);
    }

    public Tween TweenRotationParallel(Double goal, TweenInfo tweenInfo){
        return Parent.Services.TweenService.TweenDoubleProperty(this, "Rotation", goal, tweenInfo, true);
    }

    public Tween TweenOpacityParallel(Double goal, TweenInfo tweenInfo){
        return Parent.Services.TweenService.TweenDoubleProperty(this, "Opacity", goal, tweenInfo, true);
    }


    public void SetOpacity(double Opacity){
        opacity = Opacity;
        FillColor = new Color(FillColor.getRed(), FillColor.getGreen(), FillColor.getBlue(), (int) (opacity*255.0));
    }

    public double GetOpacity(){
        return opacity;
    }

    @Override
    public String toString(){
        return Name != null ? Name : "";
    }

    @Override
    public boolean equals(Object other){
        if (other==this) return true;

        if (!(other instanceof Instance) || this.Name == null) return false;

        Instance obj = (Instance) other;


        return Name.equals(obj.Name) && CFrame.equals(obj.CFrame) && Size.equals(obj.Size) && FillColor.equals(obj.FillColor);
    }
}