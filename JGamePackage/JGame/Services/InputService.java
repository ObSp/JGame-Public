package JGamePackage.JGame.Services;

import JGamePackage.JGame.JGame;
import JGamePackage.JGame.Instances.Instance;
import JGamePackage.JGame.Types.Vector2;

import JGamePackage.lib.*;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class InputService extends Service{
    

    //signals
    private Signal<KeyEvent> onkeypress = new Signal<>();
    public SignalWrapper<KeyEvent> OnKeyPress = new SignalWrapper<>(onkeypress);

    private VoidSignal onclick = new VoidSignal();
    public VoidSignalWrapper OnMouseClick = new VoidSignalWrapper(onclick);

    private VoidSignal windowClosing = new VoidSignal();
    public VoidSignalWrapper GameClosing = new VoidSignalWrapper(windowClosing);

    public int CloseHotKey = KeyEvent.VK_DELETE;


    private ArrayList<String> heldKeys = new ArrayList<>();

    //input vars
    private boolean isMouse1Down = false;
    @SuppressWarnings("unused")
    private boolean isMouse2Down = false;


    public InputService(JGame game){
        super(game);
        initInput();
    }

    //KEY RELATED STUFF//

        /**Returns an {@code int} ranging from {@code -1} to {@code 1} based on whether
     * keys that are typically associated with horizontal movement, such as the {@code A and D} keys
     * are currently being pressed by the user, returning {@code 0} if no such keys are being pressed.
     * 
     * @return An int corresponding to the horizontal direction of keys currently pressed by the user
     * 
     * @see InputService#IsKeyDown(int)
     * @see InputService#GetInputVertical()
     */
    public int GetInputHorizontal(){
        int val = 0;

        if (IsKeyDown(KeyEvent.VK_A)){
            val--;
        }else if(IsKeyDown(KeyEvent.VK_D)){
            val++;
        }
        return val;
    }

        /**Returns an {@code int} ranging from {@code -1} to {@code 1} based on whether
     * keys that are typically associated with vertical movement, such as the {@code W and S} keys
     * are currently being pressed by the user, returning {@code 0} if no such keys are being pressed.
     * 
     * @return An int corresponding to the vertical direction of keys currently pressed by the user
     * 
     * @see InputService#IsKeyDown(int)
     * @see InputService#GetInputHorizontal()
     */
    public int GetInputVertical(){
        if (IsKeyDown(KeyEvent.VK_S)){
            return -1;
        }else if(IsKeyDown(KeyEvent.VK_W)){
            return 1;
        }
        return 0;
    }

        /**Returns whether or not the Key corresponding to the provided {@code KeyCode} is currently being pressed by the user.
     * 
     * @param KeyCode : The KeyCode of the Key to be checked
     * @return Whether or not the Key is currently being pressed by the user
     * 
     * @see KeyEvent
     */
    public boolean IsKeyDown(int KeyCode){
        String keyText = KeyEvent.getKeyText(KeyCode);

        return heldKeys.indexOf(keyText)>-1 ? true : false;
    }



    //MOUSE RELATED STUFF

    /**Returns the current {@code Instance} the mouse is hovering over,
     * returning {@code null} if it's not hovering over anything.
     * 
     * @return The Instance the mouse is currently focused on
     */
    public Instance GetMouseTarget(){
        Vector2 mouseLoc = GetMouseLocation();
        Instance cur = null;
        synchronized (Parent.instances){
            for (Instance i : Parent.instances){
                if (i== null) continue;
                if (i.isCoordinateInBounds(mouseLoc) && (cur == null || i.ZIndex > cur.ZIndex)){
                    cur = i;
                }
            }
        }

        return cur;
    }

    public Vector2 GetMouseLocation(){
        Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
        Vector2 topLeft = Parent.Camera.GetTopLeftCorner();
        return topLeft.add(new Vector2(mouseLoc.getX(), mouseLoc.getY()-20));
    }

    public boolean IsMouseDown(){
        return isMouse1Down;
    }


    private void initInput(){
        var gameWindow = this.Parent.getWindow();
        //var instances = this.Parent.instances;
        //Key input
        gameWindow.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (heldKeys.indexOf(KeyEvent.getKeyText(e.getKeyCode()))!=-1) return;

                if (e.getKeyCode() == CloseHotKey) System.exit(0);

                //if (heldKeys.indexOf(KeyEvent.getKeyText(e.getKeyCode()))==-1) heldKeys.add(KeyEvent.getKeyText(e.getKeyCode()));
                heldKeys.add(KeyEvent.getKeyText(e.getKeyCode()));
                onkeypress.Fire(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String keyText = KeyEvent.getKeyText(e.getKeyCode());
                heldKeys.remove(keyText);
            }
        });
    
        gameWindow.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1){
                    isMouse1Down = true;
                    onclick.Fire();

                    Instance target = GetMouseTarget();
                    if (target != null){
                        Vector2 mouseLoc = GetMouseLocation();
                        target.MouseButton1Down.Fire(mouseLoc.X, mouseLoc.Y);
                    } else if(e.getButton() == MouseEvent.BUTTON2){
                        isMouse2Down = true;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1){
                    isMouse1Down = false;
                    Instance target = GetMouseTarget();
                    if (target != null){
                        Vector2 mouseLoc = GetMouseLocation();
                        target.MouseButton1Up.Fire(mouseLoc.X, mouseLoc.Y);
                    }
                } else if(e.getButton() == MouseEvent.BUTTON2){
                    isMouse2Down = false;
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        gameWindow.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                windowClosing.Fire();
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
            
        });
    
    }
}