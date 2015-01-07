package ru.alexlen.trance;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.xml.XmlFileImpl;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.xml.XmlText;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.util.indexing.ID;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author Almazko
 */
public class PaneFactory implements ToolWindowFactory, Writer {

    public static final ID<String, String[]> XLIFF = ID.create("ru.alexlen.trance.xliff");

    //      File
    HashMap<String, Dictionary> all = new HashMap<>();
    private Project mProject;

    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull ToolWindow toolWindow)
    {

        mProject = project;
        TransResource.prjPrefixSize = project.getBaseDir().toString().length();

        PsiManager psiManager = PsiManager.getInstance(project);

        //LocalFileSystem.getInstance().findFileByPath()
        String projectName = project.getName();
        StringBuilder sourceRootsList = new StringBuilder();
        VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();
        for (VirtualFile file : vFiles) {
            sourceRootsList.append(file.getUrl()).append("\n");
        }

//        Messages.showInfoMessage("Source roots for the " + projectName + " plugin:\n" + sourceRootsList, "Project Properties");

        final List<PsiFile> results = new ArrayList<>();

        Collection<VirtualFile> files = FilenameIndex.getAllFilesByExt(project, "xliff");


        PsiFile psiFile;
        XliffVisitor visitor;

        int x = 1;
        Dictionary dic;
        TransResource res;
        for (VirtualFile file : files) {
            psiFile = psiManager.findFile(file);


//            return;
            if (psiFile != null) {
                res = new TransResource(file, psiFile);


                if (!all.containsKey(res.getName())) {
                    dic = new Dictionary();
                    all.put(res.getName(), dic);
                } else {
                    dic = all.get(res.getName());
                }

                List<Unit> tr = getFileStructure(psiFile);
                dic.addTranslations(res, tr);

            }
        }

        // drawTable(toolWindow.getComponent(), model);

        ContentManager contentManager = toolWindow.getContentManager();
        Content content = contentManager.getFactory().createContent(new TransEditor(all, this).createComponent(),
                null, true);
        contentManager.addContent(content);
        contentManager.setSelectedContent(content, true);


//        Collection<VirtualFile> files = FileTypeIndex.getFiles(XliffFileType.INSTANCE, ProjectScope.getProjectScope(project));

//
//        FileBasedIndexImpl.readRegisteredIndexNames();
//
//
//        FileBasedIndexImpl.getInstance().getFilesWithKey(XLIFF,
//                new HashSet<String>(Arrays.asList("en")),
//                new Processor<VirtualFile>() {
//                    @Override
//                    public boolean process(VirtualFile virtualFile)
//                    {
//
////                        if (uniqueFileList.contains(virtualFile)) {
////                            return true;
////                        }
//
//                        PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
//                        if (psiFile != null) {
//                            //uniqueFileList.add(virtualFile);
//                            results.add(psiFile);
//                        }
//
//                        return true;
//                    }
//                }, new EverythingGlobalScope(project));


//
//        TableModel dm = new DefaultTableModel(10,2);
//        JTable table = new JBTable(dm);
//        component.getParent().add(new JLabel("Hello, World!"));
//        component.getParent().add(table);
    }


    private List<Unit> getFileStructure(PsiFile psiFile)
    {
        XliffVisitor visitor;
        visitor = new XliffVisitor();
        psiFile.accept(visitor);


        return visitor.getResult();
    }


    public void writeTarget(TransResource resource, int transId, final String value)
    {
        Log.i("WRITE: " + resource.getName() + ", value: '" + value + "'");

        TagFinder finder = new TagFinder(transId);
        resource.getPsiFile().accept(finder);
        final XmlTagImpl target = (XmlTagImpl) finder.getTarget();
        if (target == null) return; // TODO add error throw
        final XmlText text = (XmlText) target.getValue().getChildren()[0];


        XmlFileImpl xmlFile = ((XmlFileImpl) resource.getPsiFile());

        new WriteCommandAction.Simple(mProject, "Creating string ", xmlFile) {
            @Override
            public void run()
            {

                if (value != null && !value.isEmpty()) text.setValue(value);

//                    target.removeAllChildren();
//
//                   // target.getValue()
//                    // AndroidResourceUtil.createValueResource tries to format the value it is passed (e.g., by escaping quotation marks)
//                    // We want to save the text exactly as entered by the user, so we create and add the XML tag directly
//                    XmlTag child = target.createChildTag("trans-unit", "app:namepsase", "value_LALA", false);
//                    child.setAttribute("testAttr", "valAttr");
                // XmlTagImpl handles a null value by deleting the attribute, which is our desired behavior
                //noinspection ConstantConditions
                // root.addSubTag(child, false);
            }
        }.execute();
    }

//    private void drawTable(JComponent  component, TransModel model)
//    private void drawTable(final Project project, @NotNull ToolWindow toolWindow)
//    {
//        //
////        TableModel dm = new DefaultTableModel(10,2);
////        JTable table = new JBTable(model);
//
//        ContentManager contentManager = toolWindow.getContentManager();
//        Content content = contentManager.getFactory().createContent(new EditorTable(project).createComponent(), null, true);
//        contentManager.addContent(content);
//        contentManager.setSelectedContent(content, true);
//
//       // component.add(new EditorTable());
//    }


//    static public VirtualFile[] getDomainFilePsiElements(Project project, String domainName)
//    {
//
//        DomainMappings domainMappings = ServiceXmlParserFactory.getInstance(project, DomainMappings.class);
//        List<VirtualFile> virtualFiles = new ArrayList<VirtualFile>();
//
//        for (DomainFileMap domain : domainMappings.getDomainFileMaps()) {
//            if(domain.getDomain().equals(domainName)) {
//                VirtualFile virtualFile = domain.getFile();
//                if(virtualFile != null) {
//                    virtualFiles.add(virtualFile);
//                }
//            }
//        }
//
//        return virtualFiles.toArray(new VirtualFile[virtualFiles.size()]);
//    }
//
//    public static List<PsiFile> getDomainPsiFiles(final Project project, String domainName) {
//
//        final List<PsiFile> results = new ArrayList<PsiFile>();
//        final List<VirtualFile> uniqueFileList = new ArrayList<VirtualFile>();
//
//        // get translation files from compiler
//        for(VirtualFile virtualFile : getDomainFilePsiElements(project, domainName)) {
//            PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
//            if(psiFile != null) {
//                uniqueFileList.add(virtualFile);
//                results.add(psiFile);
//            }
//        }
//
//        FileBasedIndexImpl.getInstance().getFilesWithKey(XLIFF,
//                new HashSet<String>(Arrays.asList(domainName)),
//                new Processor<VirtualFile>() {
//            @Override
//            public boolean process(VirtualFile virtualFile) {
//
//                if(uniqueFileList.contains(virtualFile)) {
//                    return true;
//                }
//
//                PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
//                if(psiFile != null) {
//                    uniqueFileList.add(virtualFile);
//                    results.add(psiFile);
//                }
//
//                return true;
//            }
//        }, PhpIndex.getInstance(project).getSearchScope());
//
//        return results;
//    }
}
