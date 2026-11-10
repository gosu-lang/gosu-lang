package gw.lang.parser.restrictions;

public final class Result
{
  public enum Status
  {
    /** A deny rule matched the check -> explicitly denied */
    DENIED,
    /** An allow rule matched the check -> explicitly allowed */
    ALLOWED,
    /** No rules (allow or deny) matched the check -> implicitly not allowed */
    NOT_ALLOWED
  }

  public static final Result ALLOWED = new Result( Status.ALLOWED, null, null );

  private final Status _status;
  private final String _matchedRule;
  private final String _message;

  private Result( Status status, String matchedRule, String message )
  {
    this._status = status;
    this._matchedRule = matchedRule;
    this._message = message;
  }

  public boolean isAllowed()
  {
    return _status == Status.ALLOWED;
  }

  public Status getStatus()
  {
    return _status;
  }

  /**
   * The deny rule (DENIED) or allow rule (ALLOWED) that decided this result; null for NOT_ALLOWED.
   */
  public String getMatchedRule()
  {
    return _matchedRule;
  }

  public String getMessage()
  {
    return _message;
  }

  @Override
  public String toString()
  {
    return _status + (_message != null ? ": " + _message : "");
  }

  static Result allowed( Rule rule )
  {
    return rule == null ? ALLOWED : new Result( Status.ALLOWED, rule.text(), null );
  }

  static Result denied( String kind, String subject, Rule rule )
  {
    String source = rule._origin == Builder.Origin.BASELINE ? "baseline" : "profile";
    return new Result( Status.DENIED, rule.text(),
                       kind + " '" + subject + "' is blocked by the restrictions " + source
                       + " (rule: " + rule.text() + ")." );
  }

  static Result notAllowed( String kind, String subject )
  {
    return new Result( Status.NOT_ALLOWED, null,
                       kind + " '" + subject + "' is not an allowed type or package in the restrictions profile." );
  }

  static Result featureDenied( LanguageFeature f )
  {
    return new Result( Status.NOT_ALLOWED, null,
                       "Language feature '" + f.getDisplayName() + "' is not permitted by the restrictions profile." );
  }
}
