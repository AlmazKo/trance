package ru.alexlen.trance;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.XmlRecursiveElementVisitor;
import com.intellij.psi.impl.source.xml.XmlFileImpl;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Almazko
 */
public class TextBoxes extends AnAction {

    private StringBuilder total      = new StringBuilder();
    //    private List<Unit>    dictionary = new ArrayList<>();
    private List<Unit>    dictionary = new ArrayList<>();

    // If you register the action from Java code, this constructor is used to set the menu item name
    // (optionally, you can specify the menu description and an icon to display next to the menu item).
    // You can omit this constructor when registering the action in the plugin.xml file.
    public TextBoxes()
    {
        // Set the menu item name.
        super("Text _Boxes");
        // Set the menu item name, description and icon.
        // super("Text _Boxes","Item description",IconLoader.getIcon("/Mypackage/icon.png"));
    }


    //https://github.com/Haehnchen/idea-php-symfony2-plugin/blob/489eb8345cc75ac83ddbe0811cef863af957e231/src/fr/adrienbrault/idea/symfony2plugin/translation/dict/TranslationUtil.java
    public void actionPerformed(AnActionEvent event)
    {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        XmlFileImpl file = (XmlFileImpl) event.getData(PlatformDataKeys.PSI_FILE);

        if (file == null) return;

        file.accept(new XmlRecursiveElementVisitor() {

            final static String unitTag = "trans-unit";
            final static String unitTagIdAttr = "id";
            final static String sourceTag = "source";
            final static String targetTag = "target";

            private String lastTag;

            private boolean unitStarted = false;


            private int id;
            private StringBuilder target = new StringBuilder();
            private String source;


            @Override
            public void visitXmlElement(XmlElement element)
            {
                super.visitXmlElement(element);
            }

            @Override
            public void visitXmlAttribute(XmlAttribute attribute)
            {
                if (!unitTag.equals(lastTag)) return;
                String attrName = attribute.getName();

                if (unitTagIdAttr.equals(attrName) && attribute.getValue() != null) {
                    id = Integer.valueOf(attribute.getValue());
                }

                super.visitXmlAttribute(attribute);
            }

            @Override
            public void visitXmlTag(XmlTag tag)
            {

                String tagName = tag.getName();

                if (unitTag.equals(tagName)) {
                    if (unitStarted) {
                        unitStarted = false;
                        dictionary.add(new Unit(id, source, target.toString()));
                    } else {
                        unitStarted = true;
                        target = new StringBuilder();
                    }
                }


                if (unitTag.equals(tagName) || sourceTag.equals(tagName) || targetTag.equals(tagName)) {

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
        });


        Messages.showMessageDialog(project, total.toString(), "Information", Messages.getInformationIcon());
//        String txt = Messages.showInputDialog(project, "What is your name?", "Input your name", Messages.getQuestionIcon());
//        Messages.showMessageDialog(project, "Hello, " + txt + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());
    }


}