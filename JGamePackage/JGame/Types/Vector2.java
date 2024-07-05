package JGamePackage.JGame.Types;


/**An object representing a point in 2D space
 * 
 */
public class Vector2 {
    public double X;
    public double Y;


    //static stuff

    /**Shorthand for writing Vector2(0,0). */
    public static final Vector2 zero = new Vector2(0, 0);
    /**Shorthand for writing Vector2(-1,0). */
    public static final Vector2 left = new Vector2(-1, 0);
    /**Shorthand for writing Vector2(0,1). */
    public static final Vector2 up = new Vector2(0, 1);
    /**Shorthand for writing Vector2(1,0). */
    public static final Vector2 right = new Vector2(1, 0);
    /**Shorthand for writing Vector2(0,-1). */
    public static final Vector2 down = new Vector2(0, -1);

    /**Converts a {@code String} to a {@code Vector2}. The input string must follow this format:
     * (X, Y). Note that whitespace doesn't matter and parantheses don't matter, only the comma is required.
     * 
     * @param str : The input string, following the format specified above
     * @return A new Vector2
     */
    public static Vector2 fromString(String str){
        String stripped = str.replace("(", "").replace(")", "").replace(" ", "");

        String[] split = stripped.split(",");

        double xCoord = Double.parseDouble(split[0]);
        double yCoord = Double.parseDouble(split[1]);


        return new Vector2(xCoord, yCoord);
    }


    /**Creates a new Vector2 with the specified X and Y coordinate points as doubles.
     * 
     * @param x
     * @param y
     */
    public Vector2(int x, int y){
        X=x;
        Y=y;
    }

    /**Creates a new Vector2 with the specified X and Y coordinate points.
     * 
     * @param x
     * @param y
     */
    public Vector2(double x, double y){
        X= x;
        Y= y;
    }

    /**Creates a new Vector2 with the X and Y coordinates set to 0
     * 
     */
    public Vector2(){
        X = 0;
        Y = 0;
    }

    /**Creates a new Vector2 and sets the Vector2's X and Y coordinates equal to the {@code n} parameter.
     * 
     * @param n : The number to set the X and Y coordinates to
     */
    public Vector2(double n){
        X = n;
        Y = n;
    }


    public Vector2 add(Vector2 other){
        return new Vector2(X+other.X, Y+other.Y);
    }

    public Vector2 add(double n){
        return new Vector2(X+n, Y+n);
    }

    public Vector2 add(double x, double y){
        return new Vector2(X+x, Y+y);
    }

    public Vector2 subtract(Vector2 other){
        return new Vector2(X-other.X, Y-other.Y);
    }

    public Vector2 subtract(double n){
        return new Vector2(X-n, Y-n);
    }

    public Vector2 multiply(Vector2 other){
        return new Vector2(X*other.X, Y*other.Y);
    }

    public Vector2 divide(double x, double y){
        return new Vector2(X/x, Y/y);
    }

    public Vector2 divide(double n){
        return new Vector2(X/n, Y/n);
    }

    public Vector2 multiply(double x, double y){
        return new Vector2(X*x, Y*y);
    }

    public Vector2 multiply(double n){
        return new Vector2(X*n, Y*n);
    }

    public Vector2 abs(){
        return new Vector2(Math.abs(X), Math.abs(Y));
    }

    public boolean isZero(){
        return X==0 && Y==0;
    }

    public double Magnitude(){
        return Math.sqrt(X*X + Y*Y);
    }

    /** Returns a {@code Vector2} with the same direction as this Vector2 but with a length of 1.
     * 
     * @return A {@code Vector2} with the same direction as this Vector2 but with a length of 1
     */
    public Vector2 Normalized(){
        Vector2 res = new Vector2(X, Y);
        res.Normalize();
        return res;
    }

    /**Make this Vector2Double have a Magnitude of 1.0;
     * 
     */
    public void Normalize(){
        double mag = Magnitude();
        if (mag > 1E-05){
            this.X /= mag;
            this.Y /= mag;
        } else {
            this.X = 0;
            this.Y = 0;
        }
    }

    private double lerp1(double a, double b, double t){
        return (1-t) * a + t*b;
    }

    public void lerp(Vector2 b, double t){
        X = lerp1(X, b.X, t);
        Y = lerp1(Y, b.Y, t);
    }

    public static Vector2 lerp(Vector2 a, Vector2 b, double t){
        Vector2 n = a.clone();
        n.lerp(b, t);
        return n;
    }

    @Override
    public String toString(){
        return "("+X+", "+Y+")";
    }

    @Override
    public Vector2 clone(){
        return new Vector2(X, Y);
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;

        if (!(obj instanceof Vector2)) return false;

        Vector2 other = (Vector2) obj;

        return X==other.X && Y == other.Y;
    }
}