package gw.plugin.ij.extensions;

import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.file.PsiPackageImpl;

/**
 */
public class NonDirectoryPackage extends PsiPackageImpl
{
  public NonDirectoryPackage( PsiManager manager, String qualifiedName )
  {
    super( manager, qualifiedName );
  }
}
