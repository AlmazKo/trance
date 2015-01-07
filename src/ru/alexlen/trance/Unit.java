package ru.alexlen.trance;

/**
 * @author Almazko
 */
public class Unit {
//    final public String locale; // TODO may be Locale class ?
    final public int    id;
    final public String source;
    final public String target;

    public Unit(int id, String source, String target)
    {
        this.id = id;
        this.source = source;
        this.target = target;
    }
}
