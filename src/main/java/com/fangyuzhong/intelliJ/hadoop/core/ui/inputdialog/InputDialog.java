package com.fangyuzhong.intelliJ.hadoop.core.ui.inputdialog;

import com.fangyuzhong.intelliJ.hadoop.core.ui.dialog.FileSystemDialog;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-24.
 */
public class InputDialog extends FileSystemDialog<InputDialogForm>
{
    private JButton bApply;
    private InputDialogForm inputDialogForm;
    private boolean isOKAction=false;

    public boolean isOKAction()
    {
        return isOKAction;
    }

    private String strInputText="";

    public String getInputText()
    {
        return strInputText;
    }

    public InputDialog(Project project,String strTitle,String strText)
    {
        super(project, strTitle, true);
        setModal(true);
        setResizable(true);
        component = inputDialogForm=new InputDialogForm();
        inputDialogForm.setInput(strText);
        init();
    }

    public void dispose()
    {
        super.dispose();
    }

    @NotNull
    protected final Action[] createActions()
    {
        return new Action[]{
                getOKAction(),
                getCancelAction(),

        };
    }

    public void doCancelAction()
    {
        isOKAction=false;
        super.doCancelAction();
    }


    public void doOKAction()
    {
        super.doOKAction();
        isOKAction=true;
        strInputText = inputDialogForm.getInput();
    }

}
