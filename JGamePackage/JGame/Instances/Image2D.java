package JGamePackage.JGame.Instances;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import JGamePackage.JGame.Types.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Image2D extends Instance{
    /**The path to the Image's file location. Note that changing this doesn't affect anything, to 
     * chane the Image please call the SetImagePath function.
     * 
     */
    public String ImagePath = "JGamePackage\\JGame\\Files\\IMAGEDEFAULT.png";
    public String RealPath = new String(ImagePath);
    public BufferedImage Image;

    public boolean BackgroundTransparent = true;

    public boolean FlipHorizontally = false;
    public boolean FlipVertically = false;

    public Image2D(){
        Name = "Image";
        UpdateImagePath();
    }


    @Override
    public void paint(Graphics g) {
        if (Image == null) return;

        Vector2 actualPos = RenderPosition != null ? RenderPosition : GetRenderPosition();
        
        
        if (!Parent.Camera.areBoundsInViewport(this, actualPos) || opacity == 0.0)
            return;

        if (!ImagePath.equals(RealPath))
            this.SetImagePath(ImagePath);
        
        
        
        Graphics2D g2 = (Graphics2D) g;
        int rotationX = (int) (actualPos.X + (RotationAnchorPoint.X/100.0)*Size.X);
        int rotationY = (int) (actualPos.Y + (RotationAnchorPoint.Y/100.0)*Size.Y);

        AffineTransform previous = g2.getTransform();
        AffineTransform rotated = new AffineTransform();
        rotated.rotate(CFrame.Rotation, rotationX, rotationY);

        g2.transform(rotated);

        if (!BackgroundTransparent){
            g2.setColor(FillColor);
            g2.fillRect((int) actualPos.X, (int) actualPos.Y, (int) Size.X, (int) Size.Y);
        }

        double xSize = Size.X;

        if (FlipHorizontally){
            xSize = -xSize;
            actualPos.X += Size.X;
        }

        g2.drawImage(Image, (int) actualPos.X, (int) actualPos.Y, (int)  xSize, (int) (FlipVertically ? -Size.Y : Size.Y), null);

        if (RenderPosition != null){
            RenderPosition = null;
        }

        g2.setTransform(previous);
    }

    public void SetImagePath(String path){
        try {
            this.Image = ImageIO.read(new File(path));
            RealPath = path;
            ImagePath = path;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**Updates the image path to this file to the value of the {@code ImagePath} instance variable
     * 
     */
    public void UpdateImagePath(){
        try {
            this.Image = ImageIO.read(new File(ImagePath));
            RealPath = ImagePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @Override
    public void setPosition(Vector2 velpos){
        CFrame.Position = velpos;
    }

    @Override
    public Image2D clone(){
        Image2D b = new Image2D();

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
        b.SetImagePath(ImagePath);
        b.FlipHorizontally = FlipHorizontally;
        b.FlipVertically = FlipVertically;
        b.BackgroundTransparent = BackgroundTransparent;

        return b;
    }
    
}
