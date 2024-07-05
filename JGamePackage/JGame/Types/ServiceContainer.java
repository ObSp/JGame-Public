package JGamePackage.JGame.Types;

import JGamePackage.JGame.JGame;
import JGamePackage.JGame.Services.*;

public class ServiceContainer {
    @SuppressWarnings("unused")
    private JGame parent;
    
    public final ParserService ParserService;
    public final PhysicsService PhysicsService;
    public final TweenService TweenService;
    public final SceneService SceneService;
    public final InputService InputService;
    public final RaycastService RaycastService;
    public final ShapecastService ShapecastService;
    public final CollisionService CollisionService;
    public final DebrisService DebrisService;


    public ServiceContainer(JGame parent){
        ParserService = new ParserService(parent);
        PhysicsService = new PhysicsService(parent);
        TweenService = new TweenService(parent);
        SceneService = new SceneService(parent);
        InputService = new InputService(parent);
        RaycastService = new RaycastService(parent);
        ShapecastService = new ShapecastService(parent);
        CollisionService = new CollisionService(parent);
        DebrisService = new DebrisService(parent);
    }
}
