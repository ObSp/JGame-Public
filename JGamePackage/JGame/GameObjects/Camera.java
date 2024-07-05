package JGamePackage.JGame.GameObjects;

import JGamePackage.JGame.JGame;
import JGamePackage.JGame.Instances.Instance;
import JGamePackage.JGame.Types.*;
import JGamePackage.JGame.Types.Enum;

/**A class representing the end users' viewport of the screen.
 * Instances refer to this class while rendering to make sure they should render. <p>
 * This class is not meant to be instantiated by anything other than a {@code JGame},
 * but may be viable to in same special cases.
 * 
 */
public class Camera extends GameObject {
    private final JGame game;


    /**The position of the Camera, which dictates the render position of all objects. By default, this is the center of the screen
     * 
     */
    public Vector2 Position;
    public Vector2 AnchorPoint = new Vector2(0);

    public Camera(JGame game){
        this.game = game;

        Position = new Vector2(0,0);
    }

    public Vector2 GetInstancePositionRelativeToCameraPosition(Instance obj){
        return GetVector2RelativeToCameraPosition(obj.CFrame.Position);
    }

    public Vector2 GetVector2RelativeToCameraPosition(Vector2 pos){
        return pos.subtract(getActualPos(game.getTotalScreenSize()));
    }

    private Vector2 getActualPos(Vector2 size){
        return Position.subtract(getAnchorPointOffset(size));
    }

    public Vector2 GetTopLeftCorner(){
        return getActualPos(game.getTotalScreenSize());
    }

    protected Vector2 getAnchorPointOffset(Vector2 screenSize){
        return new Vector2(getAnchorPointOffsetX(screenSize.X), getAnchorPointOffsetY(screenSize.Y));
    }

    protected double getAnchorPointOffsetX(double xSize){
        return (double)xSize*(AnchorPoint.X/100.0);
    }

    protected double getAnchorPointOffsetY(double ySize){
        return (double)ySize*(AnchorPoint.Y/100.0);
    }

    public boolean isInstanceInViewport(Instance obj){
        return overlaps(obj, obj.GetCornerPosition(Enum.InstanceCornerType.TopLeft));
    }

    public boolean areBoundsInViewport(Instance obj, Vector2 renderpos){
        return overlaps(obj, renderpos);
    }



    private boolean overlaps(Instance other, Vector2 renderpos){
        if (other==null) return false;

        Vector2 screenSize = game.getTotalScreenSize();

        double left = 0;
        double top = 0;
        double right = left + screenSize.X;
        double bottom = top + screenSize.Y;

        Vector2 topLeft = renderpos;
        double otherLeft = topLeft.X;
        double otherRight = topLeft.X+other.Size.X;
        double otherTop = topLeft.Y;
        double otherBottom = otherTop+other.Size.Y;

        boolean visibleLeft = otherRight > left;
        boolean visibleRight = otherLeft < right;
        boolean visibleTop = otherBottom > top;
        boolean visibleBottom = otherTop < bottom;

        return visibleLeft && visibleRight && visibleTop && visibleBottom;
    }


}