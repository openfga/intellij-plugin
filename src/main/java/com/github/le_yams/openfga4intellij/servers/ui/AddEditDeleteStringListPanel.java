package com.github.le_yams.openfga4intellij.servers.ui;

import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.NonEmptyInputValidator;
import com.intellij.ui.AddEditDeleteListPanel;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddEditDeleteStringListPanel extends AddEditDeleteListPanel<String> {

    private final String inputDialogMessage;

    public AddEditDeleteStringListPanel(String title, String inputDialogMessage) {
        super(title, new ArrayList<>());
        this.inputDialogMessage = inputDialogMessage;
    }

    @Override
    protected @Nullable String editSelectedItem(String item) {
        var intput = Messages.showInputDialog(this, inputDialogMessage, "Edit", null, item, new NonEmptyInputValidator());
        return intput != null ? intput : item;
    }

    @Override
    protected @Nullable String findItemToAdd() {
        return Messages.showInputDialog(this, inputDialogMessage, "Add", null, "", new NonEmptyInputValidator());
    }

    public List<String> getItems() {
        return Collections.list(myListModel.elements());
    }

    public void setItems(List<String> items) {
        myListModel.clear();
        myListModel.addAll(items);
    }
}
