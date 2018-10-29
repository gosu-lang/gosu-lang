package gw.gosudoc.tags;

import com.sun.javadoc.Doc;
import com.sun.javadoc.SourcePosition;
import com.sun.javadoc.Tag;

public class TextTag implements Tag
{
  protected final String text;
  protected final String name = "Text";
  protected final Doc holder;

  /**
   *  Constructor
   */
  public TextTag(Doc holder, String text) {
    super();
    this.holder = holder;
    this.text = text;
  }

  /**
   * {@inheritDoc}
   */
  public String name() {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  public Doc holder() {
    return holder;
  }

  /**
   * {@inheritDoc}
   */
  public String kind() {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  public String text() {
    return text;
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    return name + ":" + text;
  }

  /**
   * {@inheritDoc}
   */
  public Tag[] inlineTags() {
    return new Tag[] {this};
  }

  /**
   * {@inheritDoc}
   */
  public Tag[] firstSentenceTags() {
    return new Tag[] {this};
  }

  /**
   * {@inheritDoc}
   */
  public SourcePosition position() {
    return holder.position();
  }
}
