package gw.lang.parser;

/**
 * Marker interface for symbols that have "locked down" values that are not associated with a symbol table
 * i.e., calling getValue() on a locked down symbol returns the symbol's value directly
 */
public interface ILockedDownSymbol
{
}
