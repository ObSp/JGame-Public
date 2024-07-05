package JGamePackage.JGame.Types;


public class StartArgs {
    public final boolean BorderlessFullscreen;

    public StartArgs(boolean borderlessFullscreen){
        BorderlessFullscreen = borderlessFullscreen;
    }

    public StartArgs(){
        BorderlessFullscreen = false;
    }
    
}
