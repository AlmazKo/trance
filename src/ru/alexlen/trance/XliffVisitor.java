package ru.alexlen.trance;

import com.intellij.psi.XmlRecursiveElementVisitor;
import com.intellij.psi.xml.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Almazko
 */
public class XliffVisitor extends XmlRecursiveElementVisitor {

    private List<Unit> dictionary;

    final static String TAG_TRANS_UNIT     = "trans-unit";
    final static String ATTR_TRANS_UNIT_ID = "id";
    final static String sourceTag          = "source";
    final static String targetTag          = "target";

    private String lastTag;

    private int id;
    private StringBuilder target = new StringBuilder();
    private String source;


    List<Unit> getResult()
    {
        return dictionary;
    }

    @Override
    public void visitXmlElement(XmlElement element)
    {
        super.visitXmlElement(element);
    }

    @Override
    public void visitXmlFile(XmlFile file)
    {
        super.visitXmlFile(file);

        if (dictionary == null) {
            dictionary = new ArrayList<>();
        } else {
            dictionary.add(new Unit(id, source, target.toString()));
        }

    }

    @Override
    public void visitXmlAttribute(XmlAttribute attribute)
    {
        if (!TAG_TRANS_UNIT.equals(lastTag)) return;
        String attrName = attribute.getName();

        if (ATTR_TRANS_UNIT_ID.equals(attrName) && attribute.getValue() != null) {
            id = Integer.valueOf(attribute.getValue());
        }

        super.visitXmlAttribute(attribute);
    }

    @Override
    public void visitXmlTag(XmlTag tag)
    {
        String tagName = tag.getName();

        if (TAG_TRANS_UNIT.equals(tagName)) {

            if (dictionary == null) {
                dictionary = new ArrayList<>();
            } else {
                dictionary.add(new Unit(id, source, target.toString()));
            }
            target = new StringBuilder();
        }


        if (TAG_TRANS_UNIT.equals(tagName) || sourceTag.equals(tagName) || targetTag.equals(tagName)) {

            lastTag = tag.getName();
        } else {

            lastTag = null;
        }

        super.visitXmlTag(tag);
    }

    @Override
    public void visitXmlText(XmlText text)
    {

        if (sourceTag.equals(lastTag)) {
            source = text.getValue();
        }

        if (targetTag.equals(lastTag)) {
            target.append(text.getValue());
        }

        //super.visitXmlText(text);
    }


}
