package ru.alexlen.trance;

import com.intellij.psi.XmlRecursiveElementVisitor;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.Nullable;

/**
 * @author Almazko
 */
public class TagFinder extends XmlRecursiveElementVisitor {


    XmlTag found;
    int transId;

    public TagFinder(int foundId)
    {
        transId = foundId;
    }

    @Override
    public void visitXmlTag(XmlTag tag)
    {
        String tagName = tag.getName();

        if (XliffVisitor.TAG_TRANS_UNIT.equals(tagName)) {

            XmlAttribute attr = tag.getAttribute(XliffVisitor.ATTR_TRANS_UNIT_ID);

            if (attr!= null && attr.getValue() != null) {
                int id = Integer.valueOf(attr.getValue());
                if (id == transId) {
                    found = tag;
                    return;
                }
            }

        }

        super.visitXmlTag(tag);
    }

    @Nullable
    XmlTag getTarget()
    {
        if (found == null) return null;
        return (XmlTag) found.getValue().getChildren()[1];
    }

}
