package JGamePackage.JGame.Instances;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import JGamePackage.JGame.Types.Tween;
import JGamePackage.JGame.Types.TweenInfo;
import JGamePackage.JGame.Types.Vector2;

public class Text2D extends Instance {

    public Color TextColor = Color.black;

    public String Text = null;

    public Font Font;

    public boolean BackgroundTransparent = true;

    public double HorizontalOffsetPercentage = .5;

    public double VerticalOffsetPercentage = .5;

    private double textOpacity = 1;

    public Text2D(){
        this.Name = "Text2D";
        this.Font = new java.awt.Font("Arial", java.awt.Font.PLAIN, 10);
    }

    @Override
    public void paint(Graphics g) {
        if (Text == null || Text.equals("") || this.Font == null) return;

        Vector2 actualPos = GetRenderPosition();
        
        if (!Parent.Camera.areBoundsInViewport(this, actualPos) || opacity == 0.0)
            return;

        Graphics2D g2 = (Graphics2D) g;
        int centerX = (int) (actualPos.X+(Size.X/2));
        int centerY = (int) (actualPos.Y+(Size.Y/2));

        AffineTransform previous = g2.getTransform();
        AffineTransform rotated = new AffineTransform();
        rotated.rotate(CFrame.Rotation, centerX, centerY);

        g2.transform(rotated);

        g2.setFont(Font);

        if (!BackgroundTransparent){
            g2.setColor(FillColor);
            g2.fillRect((int) actualPos.X, (int) actualPos.Y, (int) Size.X, (int) Size.Y);
        }

        FontMetrics fm = g2.getFontMetrics();

        int pixelHeight = (int) Math.round((double) Font.getSize()*.75);

        int xStringPos = (int)(centerX-fm.stringWidth(Text)*(HorizontalOffsetPercentage));
        int yStringPos = (int)((actualPos.Y+Size.Y)-(pixelHeight*VerticalOffsetPercentage));

        g2.setColor(TextColor);
        g2.drawString(Text, xStringPos, yStringPos);


        g2.setTransform(previous);
    }

    @Override
    public Text2D clone(){
        Text2D t = new Text2D();

        t.CFrame = this.CFrame.clone();
        t.FillColor = new Color(FillColor.getRed(), FillColor.getGreen(), FillColor.getBlue(), FillColor.getAlpha());
        t.AnchorPoint = this.AnchorPoint.clone();
        t.Size = this.Size.clone();
        t.Anchored = this.Anchored;
        t.Associate = this.Associate;
        t.MoveWithCamera = this.MoveWithCamera;
        t.Name = new String(this.Name);
        t.opacity = this.opacity;
        t.Tags = this.Tags.clone();
        t.Solid = this.Solid;
        t.WeightPercentage = this.WeightPercentage;
        t.ZIndex = this.ZIndex;

        t.Font = this.Font;
        t.HorizontalOffsetPercentage = this.HorizontalOffsetPercentage;
        t.VerticalOffsetPercentage = this.VerticalOffsetPercentage;
        t.Text = new String(this.Text);
        t.TextColor = this.TextColor;
        t.BackgroundTransparent = this.BackgroundTransparent;

        return t;
    }

    @Override
    public void setInstanceVariableByName(String variable, Object value){
        if (variable.equals("TextOpacity")) {
            this.SetTextOpacity((double) value);
        } else {
            super.setInstanceVariableByName(variable, value);
        }
    }

    @Override
    public Object getInstanceVariableByName(String variable){
        if (variable.equals("TextOpacity"))
            return this.textOpacity;

        return super.getInstanceVariableByName(variable);
    }


    public Tween TweenTextOpacity(Double goal, TweenInfo tweenInfo){
        return Parent.Services.TweenService.TweenDoubleProperty(this, "TextOpacity", goal, tweenInfo, false);
    }

    public Tween TweenTextOpacityParallel(Double goal, TweenInfo tweenInfo){
        return Parent.Services.TweenService.TweenDoubleProperty(this, "TextOpacity", goal, tweenInfo, true);
    }

    public void SetTextOpacity(double newOpacity){
        textOpacity = newOpacity;
        TextColor = new Color(TextColor.getRed(), TextColor.getGreen(), TextColor.getBlue(), (int) (textOpacity*255.0));
    }

    public double GetTextOpacity(){
        return textOpacity;
    }

    
}