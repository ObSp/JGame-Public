package JGamePackage.lib;


public abstract class AbstractConnection {

    /**A boolean representing whether or not this BiConnection is currently connected to a Signal.
    * 
    */
    public boolean Connected = true;

    /**Disconnects this BiConnection from the parent signal so it won't be called again
    * 
    */
    public abstract void Disconnect();
}