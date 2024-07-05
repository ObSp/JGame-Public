package JGamePackage.JGame.Types;

public class TweenInfo {
    public final double Speed;
    public final int Repeats;

    public TweenInfo(double tweenSpeed, int reps){
        if (tweenSpeed == 0){
            this.Speed = .00001;
            this.Repeats = reps;
            return;
        }
        this.Speed = tweenSpeed;
        this.Repeats = reps;
    }

    public TweenInfo(double tweenSpeed){
        this.Speed = tweenSpeed != 0 ? tweenSpeed : .00001;
        this.Repeats = 0;
    }

    public TweenInfo(){
        this.Speed = 1;
        this.Repeats = 0;
    }
}
