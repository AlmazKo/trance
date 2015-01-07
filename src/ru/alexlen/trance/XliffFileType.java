package ru.alexlen.trance;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeBundle;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class XliffFileType extends LanguageFileType {
    public static final XliffFileType INSTANCE = new XliffFileType();
    @NonNls
    public static final String DEFAULT_EXTENSION = "xliff";

    private XliffFileType() {
        super(XMLLanguage.INSTANCE);
    }

    @NotNull
    public String getName() {
        return "XLIFF";
    }

    @NotNull
    public String getDescription() {
        return IdeBundle.message("filetype.description.xml", new Object[0]);
    }

    @NotNull
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    public Icon getIcon() {
        return AllIcons.FileTypes.Xml;
    }
}

