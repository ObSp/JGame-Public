package JGamePackage.JGame.Instances;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import JGamePackage.JGame.Types.Vector2;

public class Box2D extends Instance {

    public Box2D(){
        Name = "Box2D";
    }

    
    public void paint(Graphics g) {

        Vector2 actualPos = RenderPosition != null ? RenderPosition : GetRenderPosition();
        
        if (!Parent.Camera.areBoundsInViewport(this, actualPos) || opacity == 0.0)
            return;



        Graphics2D g2 = (Graphics2D) g;
        int rotationX = (int) (actualPos.X + (RotationAnchorPoint.X/100.0)*Size.X);
        int rotationY = (int) (actualPos.Y + (RotationAnchorPoint.Y/100.0)*Size.Y);

        AffineTransform previous = g2.getTransform();
        AffineTransform rotated = new AffineTransform();
        rotated.rotate(CFrame.Rotation, rotationX, rotationY);

        g2.transform(rotated);

        if (BorderSizePixel>0){
            g2.setColor(BorderColor);
            g2.fillRect((int) (actualPos.X+BorderSizePixel), (int) actualPos.Y+BorderSizePixel, (int) Size.X+BorderSizePixel, (int) Size.Y+BorderSizePixel);
        }
        
        g2.setColor(FillColor);
        g2.fillRect((int) actualPos.X, (int) actualPos.Y, (int) Size.X, (int) Size.Y);

        //reset rotation and translation
        g2.setTransform(previous);
    }

    @Override
    public void setPosition(Vector2 pos){
        CFrame.Position = pos;
    }

    @Override
    public Box2D clone(){
        Box2D b = new Box2D();

        b.CFrame = this.CFrame.clone();
        b.FillColor = new Color(FillColor.getRed(), FillColor.getGreen(), FillColor.getBlue(), FillColor.getAlpha());
        b.AnchorPoint = this.AnchorPoint.clone();
        b.Size = this.Size.clone();
        b.Anchored = this.Anchored;
        b.Associate = this.Associate;
        b.MoveWithCamera = this.MoveWithCamera;
        b.Name = new String(this.Name);
        b.opacity = this.opacity;
        b.Tags = this.Tags.clone();
        b.Solid = this.Solid;
        b.WeightPercentage = this.WeightPercentage;
        b.ZIndex = this.ZIndex;

        return b;
    }
}
