package JGamePackage.lib;

import java.awt.*;

import javax.swing.JPanel;

import JGamePackage.JGame.JGame;
import JGamePackage.JGame.Instances.Instance;

public class DrawGroup extends JPanel {
    public Instance[] instances;
    private JGame game;

    public DrawGroup(JGame g){
        game = g;
    }

    @Override
    public void paintComponent(Graphics g){
        g.setColor(game.BackgroundColor);
        g.fillRect(0, 0, game.getScreenWidth(), game.getScreenHeight());

        if (instances == null) return;

        synchronized (instances){
            int size = instances.length;
            for (int i = 0; i < size - 1; i++) {
                if (instances[i] == null)
                    continue;

                int mindex = i;
                for (int j = i + 1; j < size; j++) {
                    if (instances[j] == null)
                        continue;
                    if (instances[j].ZIndex < instances[mindex].ZIndex)
                        mindex = j;
                }

                Instance itemAtIndex = instances[i];
                instances[i] = instances[mindex];
                instances[mindex] = itemAtIndex;
            }
        }

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

        for (Instance x : instances){
            if (x==null || x.Parent == null || x.GetOpacity() == 0.0) continue;
            x.paint(g);
        }
    }
}