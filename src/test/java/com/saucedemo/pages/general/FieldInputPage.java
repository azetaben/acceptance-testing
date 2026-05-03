package com.saucedemo.pages.general;


import com.saucedemo.models.FieldInputModel;

import java.util.List;

public interface FieldInputPage {
    void inputFieldModelDataIntoAllPageFields(FieldInputModel fieldInputModel);

    void inputFieldModelDataIntoSpecifiedPageFields(FieldInputModel fieldInputModel, List<String> field);

    void assertOnAllInputFieldsBeingCorrectlyPopulated(FieldInputModel fieldInputModel);

    void assertEditableFieldsCanBeEdited();
}
