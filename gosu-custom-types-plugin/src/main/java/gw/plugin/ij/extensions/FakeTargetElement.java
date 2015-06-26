package gw.plugin.ij.extensions;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.PsiElementBase;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

/**
*/
class FakeTargetElement extends PsiElementBase
{
  private final PsiFile _file;
  private final int _iOffset;
  private final int _iLength;
  private final String _name;

  FakeTargetElement( PsiFile file, int iOffset, int iLength, String name )
  {
    _file = file;
    _iOffset = iOffset;
    _iLength = iLength;
    _name = name;
  }

  @Override
  @NotNull
  public Language getLanguage()
  {
    return _file.getLanguage();
  }

  @Override
  public PsiManager getManager()
  {
    return _file.getManager();
  }

  @Override
  public PsiElement getParent()
  {
    return _file;
  }

  @Override
  @NotNull
  public PsiElement[] getChildren()
  {
    return PsiElement.EMPTY_ARRAY;
  }

  @Override
  public PsiFile getContainingFile()
  {
    return _file;
  }

  @Override
  public TextRange getTextRange()
  {
    return new TextRange( _iOffset, _iOffset + _iLength );
  }

  @Override
  public int getStartOffsetInParent()
  {
    return _iOffset;
  }

  @Override
  public final int getTextLength()
  {
    return _iLength;
  }

  @Override
  @NotNull
  public char[] textToCharArray()
  {
    return getText().substring( _iOffset, _iOffset + _iLength ).toCharArray();
  }

  @Override
  public boolean textMatches( @NotNull CharSequence text )
  {
    return getText().equals( text.toString() );
  }

  @Override
  public boolean textMatches( @NotNull PsiElement element )
  {
    return getText().equals( element.getText() );
  }

  @Override
  public PsiElement findElementAt( int offset )
  {
    return this;
  }

  @Override
  public int getTextOffset()
  {
    return _iOffset;
  }

  @Override
  public boolean isValid()
  {
    return true;
  }

  @Override
  public boolean isWritable()
  {
    return false;
  }

  @Override
  public boolean isPhysical()
  {
    return true;
  }

  @Override
  public String toString()
  {
    return _name;
  }

  @Override
  public void checkAdd( @NotNull PsiElement element ) throws IncorrectOperationException
  {
    throw new IncorrectOperationException( getClass().getName() );
  }

  @Override
  public PsiElement add( @NotNull PsiElement element ) throws IncorrectOperationException
  {
    throw new IncorrectOperationException( getClass().getName() );
  }

  @Override
  public PsiElement addBefore( @NotNull PsiElement element, PsiElement anchor ) throws IncorrectOperationException
  {
    throw new IncorrectOperationException( getClass().getName() );
  }

  @Override
  public PsiElement addAfter( @NotNull PsiElement element, PsiElement anchor ) throws IncorrectOperationException
  {
    throw new IncorrectOperationException( getClass().getName() );
  }

  @Override
  public void delete() throws IncorrectOperationException
  {
    throw new IncorrectOperationException( getClass().getName() );
  }

  @Override
  public void checkDelete() throws IncorrectOperationException
  {
    throw new IncorrectOperationException( getClass().getName() );
  }

  @Override
  public PsiElement replace( @NotNull PsiElement newElement ) throws IncorrectOperationException
  {
    throw new IncorrectOperationException( getClass().getName() );
  }

  @Override
  public ASTNode getNode()
  {
    return null;
  }

  @Override
  public String getText()
  {
    return _name;
  }

  @Override
  public void accept( @NotNull PsiElementVisitor visitor )
  {
  }

  @Override
  public PsiElement copy()
  {
    return null;
  }

  @NotNull
  @Override
  public PsiElement getNavigationElement()
  {
    return this;
  }

  public void setNavigationElement( @NotNull PsiElement navigationElement )
  {
  }

  @Override
  public PsiElement getPrevSibling()
  {
    return null;
  }

  @Override
  public PsiElement getNextSibling()
  {
    return null;
  }

}
