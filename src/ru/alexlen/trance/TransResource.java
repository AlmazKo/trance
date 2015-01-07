package ru.alexlen.trance;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

/**
 * @author Almazko
 */
public class TransResource {
    final static int XLIFF_EXT_SIZE = 6;
    static       int prjPrefixSize  = -1;


    private final String mNameWithoutLang;
    String  mLang;

    PsiFile mPsiFile;

    public TransResource(VirtualFile file, PsiFile psiFile)
    {

        mPsiFile  = psiFile;
        String fullName = file.getUrl().substring(prjPrefixSize);
        String uniqueName = fullName.substring(0, fullName.length() - XLIFF_EXT_SIZE);
        mLang = uniqueName.substring(uniqueName.lastIndexOf('.') + 1);
        mNameWithoutLang = uniqueName.substring(0, uniqueName.length() - mLang.length() - 1);

    }

    public String getLang()
    {
        return mLang;
    }

    public String getName()
    {
        return mNameWithoutLang;
    }

    @Override
    public String toString()
    {
        return mNameWithoutLang;
    }

    public PsiFile getPsiFile()
    {
        return mPsiFile;
    }
}
