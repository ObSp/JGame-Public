package JGamePackage.lib;


import java.util.Iterator;








/**
 * A data structure that utilizes the primitive array type.
 * Tables grow and shrink as items are added and removed using
 * the {@code add} and {@code remove} methods.
 * 
 * Additionally, the methods {@code get}, {@code set}, {@code indexOf}, {@code swap} and
 * {@code clear}
 * are implemented.
 * 
 */
public class ArrayTable<E extends Object> implements Iterator<E>, Iterable<E> {

    //CONSTANCS
    int SELF_TABLE_DEFAULT_VALUE = 0;

    //OBJECT VARIABLES

    private E[] SELF_TABLE;


    //Implementation Variables
    private int currentIndex = 0;




    //PRIVATE UTILITY FUNCTIONS


    //Push an item up
    private void CorrectIndexUp(int index, int nextindex) {
        if (SELF_TABLE[index] == null) {
            SELF_TABLE[index] = SELF_TABLE[nextindex];
            SELF_TABLE[nextindex] = null;
        }
    }

    //Replace all indexes equal to null with the next index
    private void PushItemsUp() {
        for (int i = 0; i < SELF_TABLE.length-1; i++) {
            CorrectIndexUp(i, i + 1);
        }
    }

    @SuppressWarnings("unchecked")
    //Push all items after a certain index down one spot
    private void PushItemsDown(int FromIndex) {
        E[] ITEMS_PUSHED_DOWN = (E[]) new Object[SELF_TABLE.length+1];

        for (int i = 0; i<FromIndex; i++){
            ITEMS_PUSHED_DOWN[i] = SELF_TABLE[i];
        }
        for (int i = SELF_TABLE.length-1; i >=FromIndex; i--){
            ITEMS_PUSHED_DOWN[i+1] = SELF_TABLE[i];
        }
        SELF_TABLE = ITEMS_PUSHED_DOWN;
    }


    @SuppressWarnings("unchecked")
    //CONTRUCTORS

    /**Constructs a new, blank ArrayTable
     * 
     */
    public ArrayTable() {
        SELF_TABLE = (E[]) new Object[SELF_TABLE_DEFAULT_VALUE];
    }


    /**Constructs a new {@code ArrayTable} with the specified items already in it
     * 
     * @param items The items to be inserted
     */
    @SuppressWarnings("unchecked")
    public ArrayTable(E... items) {
        SELF_TABLE = (E[]) new Object[items.length];
        for (int i = 0; i < items.length; i++) {
            SELF_TABLE[i] = items[i];
        }
    }




    //METHODS


    //-------------SWAPPING-------------

    /**Swaps the values of the first and second index
     * 
     * @param i1 The first index to be swapped with the 2nd index
     * @param i2 The 2nd index to be swapped with the 1st index
     */
    public void swap(int i1, int i2){
        E i1val = SELF_TABLE[i1];
        SELF_TABLE[i1] = SELF_TABLE[i2];
        SELF_TABLE[i2] = i1val;
    }

    /**Swaps the values of the first occurence of Object o1 with the first occurence of Object o2
     * 
     * @param o1 The first Object to be swapped with the 2nd Object
     * @param o2 The 2nd Object to be swapped with the 1st Object
     */
    public void swap(E o1, E o2){
        swap(indexOf(o1), indexOf(o2));
    }

    /**Returns the Object o at index i in this table
     * 
     * @param index
     * @return The object at index i
     */
    public E get(int i) {
        E o;
        try {
            o = (E) SELF_TABLE[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Table does not contain the index of " + i);
        }

        return o;
    }

    /**Inserts Object o into the end of this table
     * 
     * @param o The object to be inserted
     */
    @SuppressWarnings("unchecked")
    public void add(E o) {
        E[] NEW_TABLE = (E[]) new Object[SELF_TABLE.length + 1];
        for (int i = 0; i < SELF_TABLE.length; i++) {
            NEW_TABLE[i] = SELF_TABLE[i];
        }

        NEW_TABLE[NEW_TABLE.length - 1] = o;

        SELF_TABLE = NEW_TABLE;
    }

    /**Inserts Object o at index i in this table
     * 
     * @param index The index where Object o should be added to the table
     * @param o The item to be added to the table
     */
    public void add(int i, E o){
        PushItemsDown(i);
        SELF_TABLE[i] = o;
    }

    @SuppressWarnings("unchecked") 
    public void add(E... items){
        for (E o : items){
            this.add(o);
        }
    }

    /**Returns the index of the first occurence of Object o in this ArrayTable
     * 
     * @param o
     * @return The index of Object o
     */
    public int indexOf(E o) {

        for (int i = 0; i < SELF_TABLE.length; i++) {
            if (o.equals(SELF_TABLE[i]))
                return i;
        }

        return -1;
    }

    public boolean contains(E o){
        return indexOf(o) > -1;
    }

    /**
     * Sets the value of index i to object o
     * 
     * @param i : The index of the item to be changed
     * @param o : The object to set the given index to
     */
    public void set(int i, E o) {
        SELF_TABLE[i] = o;
    }


    /**Sets the index of the first occurence of Object o1 to Object o2
     * 
     * @param o1 - The object to replace
     * @param o2 - The object to set the index of Object o1 equal to
     */
    public void set(E o1, E o2){
        set(indexOf(o1), o2);
    }

    /**Removes the object at index i from this ArrayTable
     * 
     * @param i - index of the item to be removed
     * @return The object that was removed
     */
    public E remove(int i) {
        E o = SELF_TABLE[i];
        SELF_TABLE[i] = null;
        PushItemsUp();

        int null_count = 0;
        for (E e : SELF_TABLE) {
            if (e==null) null_count++;
        }

        @SuppressWarnings("unchecked")
        //Remove null values
        E[] NO_NULL_TABLE = (E[]) new Object[SELF_TABLE.length-null_count];
        for (int index = 0; index < NO_NULL_TABLE.length; index++) {
            NO_NULL_TABLE[index] = SELF_TABLE[index];
        }
        SELF_TABLE = NO_NULL_TABLE;

        //return the object that was removed
        return o;
    }



    /**Removes the first occurence of Object o from this ArrayTable
     * 
     * @param o - The object to be removed
     * @return the object that was removed
     */
    public E remove(E o){
        int index = indexOf(o);
        if (index==-1) return null;
        return remove(index);
    }

    /**Clears this table to lenght 0
     * 
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        SELF_TABLE = (E[]) new Object[SELF_TABLE_DEFAULT_VALUE];
    }

    /**Returns the lenght of this table
     * 
     * @return The lenght of this table
     */
    public int getLength(){
        return SELF_TABLE.length;
    }

    /**Returns whether there are any items in this table
     * 
     * @return Returns true if there are more than 0 items in this table
     */
    public boolean isEmpty(){
        return getLength()==0;
    }

    /**Returns a new array with all items of this table in it
     * 
     * @return
     */
    public E[] toArray(){
        return (E[]) SELF_TABLE.clone();
    }


    // OVERRIDES

    @Override
    public String toString() {
        if (SELF_TABLE.length < 1) {
            return "{}";
        }

        String result = "{";

        for (E o : SELF_TABLE) {
            result += o + ", ";
        }

        result = result.substring(0, result.length() - 2);

        result += "}";

        return result;
    }




    //ITERATORS
    @Override
    public boolean hasNext() {
        if (currentIndex < SELF_TABLE.length){
            return true;
        }
        currentIndex = 0;
        return false;
    }

    @Override
    public E next() {
        return SELF_TABLE[currentIndex++];
    }

    @Override
    public Iterator<E> iterator(){
        return this;
    }

    @Override
    public ArrayTable<E> clone(){
        ArrayTable<E> tbl = new ArrayTable<>();

        for (E o : SELF_TABLE)
            tbl.add(o);

        return tbl;
    }

}