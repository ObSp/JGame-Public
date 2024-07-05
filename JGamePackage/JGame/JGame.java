package JGamePackage.JGame;

import javax.swing.*;

import JGamePackage.JGame.GameObjects.Camera;
import JGamePackage.JGame.Instances.*;
import JGamePackage.JGame.Types.*;
import JGamePackage.lib.*;
import JGamePackage.lib.ArrayTable;

import java.awt.Color;

/**The main class of the {@code JGame} game framework. It contains basic
 * functions and is in charge of the main game loop. All complicated actions
 * and functions are located under a {@code Service} inside the JGame's 
 * {@code ServiceContainer} such as {@code InputService}, {@code CollisionService},
 * and {@code TweenService}.
 * 
 * <h2>Information About Threads</h2>
 * In most implementations of this class, there will be 3 threads running at the same time.
 * The first will be the class using {@JGame} and any other class not part of the JGame framework.<p>
 * The second thread will be the {@code JST}, or JGame Service Thread. All {@code Services} run
 * in this thread to make sure not to disturb the main rendering thread.<p>
 * The third thread running will be the {@code JRT}, or JGame Render Thread. This thread
 * takes care of the render and tick loops.<p>
 * <b>NOTE:</b> <p>
 * All connections connected to {@code JGame.OnTick} will run in the {@code JRT}. Unless you are intentionally
 * halting the rendering loop, there should be no halting in those connections as they will effectively freeze
 * the game.
 * 
 * 
 * 
 */
public class JGame{
    public int TickCount = 0;

    public double TickSpeed = .016; //exactly 60 fps

    public double tickMult = 1000;

    private double elapsed = 0;

    public ArrayDictionary<Object, Object> Globals = new ArrayDictionary<>();

    //Signals
    private Signal<Double> ontick = new Signal<>();
    public final SignalWrapper<Double> OnTick = new SignalWrapper<>(ontick);

    private StartArgs startArgs;

    public boolean runPhysics = true;

    public Color BackgroundColor = Color.white;


    public String Title = "JGame";

    private JFrame gameWindow = new JFrame(Title);
    private DrawGroup drawGroup = new DrawGroup(this);

    public ArrayTable<Instance> instances = new ArrayTable<>();
    @SuppressWarnings("unused")
    private ArrayTable<Instance> instancesToRemove = new ArrayTable<>();

    public Instance[] instanceArray;

    public Camera Camera;


    public ServiceContainer Services;

    private final Object waitMutex = new Object();

    private void staticConstruct(){
        Promise.await(this.start());
        Camera = new Camera(this);
        task.spawn(()->Services = new ServiceContainer(this));
        while (this.Services == null) {
            waitForTick();
        }
    }

    public JGame(){
        startArgs = new StartArgs();
        staticConstruct();
    }

    public JGame(StartArgs args){
        startArgs = args;
        staticConstruct();
    }

    private void render(){
        synchronized (instances){
            instanceArray = utilFuncs.toInstArray(instances);
            drawGroup.instances = instanceArray;
            gameWindow.repaint();
        }
    }

    private void tick(double dtSeconds){
        elapsed += dtSeconds;
        TickCount++;

        ontick.Fire(dtSeconds);

        if (this.Services != null && runPhysics)
            Services.PhysicsService.runPhysics(dtSeconds);

        render();

        synchronized (waitMutex){
            waitMutex.notify();
        }
    }

    /**Returns the number of seconds that have passed since the initialization of this JGame.
     * 
     */
    public double GetElapsedSeconds(){
        return elapsed;
    }

    private double curSeconds(){
        return ((double)System.currentTimeMillis())/1000;
    }

    private void run(){
        render();
        double lastTick = curSeconds();

        while (true) {
            double curSecs = curSeconds();
            if (curSecs-lastTick>=TickSpeed){
                var temp = curSecs-lastTick;
                lastTick = curSecs;
                tick(temp);
            }
        }
    }


    /**Initializes the {@code JGame} framework and returns a {@code Promise} that is resolved once
     * initialization is complete.
     * 
     * <p>
     * 
     * <b>NOTE:</b> This method <i>must</i> be called before anything else related to JGame, as it constructs and initializes
     * the {@code JFrame} window that is essential to many {@code JGame} functions.
     * 
     * <p>
     * 
     * <b>NOTE:</b> This should be called inside a {@code Promise.await(Promise)} function to ensure
     * the framework has been initialized before running any code related to it.
     * 
     * @return A promise that is resolved once initialization is complete
     * 
     * 
     * @see Promise
     * @see JFrame
     */
    public Promise start(){
        return new Promise(self ->{
            if (startArgs.BorderlessFullscreen){
                gameWindow.setUndecorated(true);
            }
            gameWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
            gameWindow.setIconImage(new ImageIcon("JGamePackage\\JGame\\Files\\icon.png").getImage());

            gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            gameWindow.add(drawGroup);
            gameWindow.setVisible(true);
            self.resolve();

            run();

        });
    }

    public void waitForTick(){
        int lastTick = this.TickCount;
        synchronized (waitMutex){
            while (this.TickCount == lastTick)
                try {waitMutex.wait();} catch (InterruptedException e) {}
        }
    }

    public void waitTicks(int ticksToWait){
        for (int t = 0; t < ticksToWait; t++)
            waitForTick();
    }

    public void waitSeconds(double seconds){
        double start = (double) System.currentTimeMillis()/1000.0;
        while (true){
            waitForTick();
            double curTime = (double) System.currentTimeMillis()/1000.0;
            if (curTime-start > seconds) break;
        }
    }

    public void addInstance(Instance x){
        x.Parent = this;
        instances.add(x);
    }

    public void removeInstance(Instance x){
        for (int i = instances.getLength()-1; i > -1; i--)
            if (instances.get(i)==x)
                instances.remove(i);
        x.Parent = null;
    }

    public Instance[] getInstances(){
        return utilFuncs.toInstArray(instances);
    }

    public Instance getInstanceByName(String name){
        for (Instance x : instances){
            if (x.Name.equals(name))
                return x;
        }

        return null;
    }

    /**Returns the current JFrame window.<p>
     * <b>NOTE:</b> This should only be used if root-level access to the window itself is needed. This should almost never be needed,
     * so developer-implemented methods like addInstance are preferred.
     * 
     */
    public JFrame getWindow(){
        if (this.gameWindow == null){
            while (gameWindow == null){
               this.waitForTick();
            }
        }
        return gameWindow;
    }

    /**Returns a {@code Vector2} containing the {@code X} and {@code Y} size of the current {@code JFrame window}.
     * 
     * @return : The current size of the {@code JFrame window} as a {@code Vector2}
     */
    public Vector2 getTotalScreenSize(){
        return new Vector2(getScreenWidth(), getScreenHeight());
    }

    public int getScreenHeight(){
        return gameWindow.getContentPane().getHeight();
    }

    public int getScreenWidth(){
        return gameWindow.getContentPane().getWidth();
    }

    public void setWindowTitle(String newTitle){
        gameWindow.setTitle(newTitle);
    }

    public void setWindowIcon(String path){
        gameWindow.setIconImage(new ImageIcon(path).getImage());
    }

}



class utilFuncs{
    static Instance[] toInstArray(ArrayTable<Instance> x){
        Instance[] arr = new Instance[x.getLength()];
        for (int i = 0; i<arr.length; i++){
            arr[i] = x.get(i);
        }
        return arr;
    }

    static boolean blacklistContains(Instance[] blacklist, Instance x){
        for (Instance j : blacklist){
            if (j.equals(x)) return true;
        }
        return false;
    }

    static void invertY(Vector2 v){
        v.Y*=-1;
    }
}