package JGamePackage.JGame.Services;

import java.util.ArrayList;

import JGamePackage.JGame.JGame;
import JGamePackage.JGame.Types.Scene;

public class SceneService extends Service {
    private ArrayList<Scene> scenes = new ArrayList<>();
    private Scene current = null;

    public SceneService(JGame parent){
        super(parent);
    }

    public void ShowScene(Scene scene){
        if (current != null)
            current.hide();

        current = scene;
        current.show();
    }

    public void HideCurrentScene(){
        if (current == null) return;

        current.hide();
        current = null;
    }

    public void AddScene(Scene scene){
        scenes.add(scene);
        scene.init(Parent);
    }

    public void RemoveScene(Scene scene){
        scenes.remove(scene);
    }

    public void RemoveScene(int index){
        scenes.remove(index);
    }
}