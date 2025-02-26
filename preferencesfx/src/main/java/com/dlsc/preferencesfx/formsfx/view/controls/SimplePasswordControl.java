package com.dlsc.preferencesfx.formsfx.view.controls;

/*-
 * ========================LICENSE_START=================================
 * FormsFX
 * %%
 * Copyright (C) 2017 DLSC Software & Consulting
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */

import com.dlsc.formsfx.model.structure.PasswordField;
import com.dlsc.preferencesfx.util.VisibilityProperty;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * This class provides the base implementation for a simple control to edit
 * password values.
 *
 * @author Rinesch Murugathas
 * @author Sacha Schmid
 * @author Andres Almiray
 */
public class SimplePasswordControl extends SimpleControl<PasswordField, StackPane> {

    /**
     * - The fieldLabel is the container that displays the label property of
     *   the field.
     * - The editableField allows users to modify the field's value.
     * - The readOnlyLabel displays the field's value if it is not editable.
     */
    protected javafx.scene.control.PasswordField editableField;
    protected Label readOnlyLabel;
    protected Label fieldLabel;

    /*
     * Translates characters found in user input into '*'
     */
    protected StringBinding obfuscatedUserInputBinding;

    /**
     * Constructs a SimplePasswordControl of {@link SimplePasswordControl} type, with visibility condition.
     *
     * @param visibilityProperty property for control visibility of this element
     *
     * @return the constructed SimplePasswordControl
     */
    public static SimplePasswordControl of(VisibilityProperty visibilityProperty) {
        SimplePasswordControl simplePasswordControl = new SimplePasswordControl();

        simplePasswordControl.visibilityProperty = visibilityProperty;

        return simplePasswordControl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParts() {
        super.initializeParts();

        getStyleClass().add("simple-password-control");

        this.node = new StackPane();

        editableField = new javafx.scene.control.PasswordField();
        editableField.setText(field.getValue());

        readOnlyLabel = new Label(obfuscate(field.getValue()));
        fieldLabel = new Label(field.labelProperty().getValue());
        editableField.setPromptText(field.placeholderProperty().getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutParts() {
        readOnlyLabel.getStyleClass().add("read-only-label");

        readOnlyLabel.setPrefHeight(26);

        this.node.getChildren().addAll(editableField, readOnlyLabel);

        this.node.setAlignment(Pos.CENTER_LEFT);

        Node labelDescription = field.getLabelDescription();
        Node valueDescription = field.getValueDescription();

        int columns = field.getSpan();

        if (columns < 3) {
            int rowIndex = 0;
            add(fieldLabel, 0, rowIndex++, columns, 1);
            if (labelDescription != null) {
                GridPane.setValignment(labelDescription, VPos.TOP);
                add(labelDescription, 0, rowIndex++, columns, 1);
            }
            add(this.node, 0, rowIndex++, columns, 1);
            if (valueDescription != null) {
                GridPane.setValignment(valueDescription, VPos.TOP);
                add(valueDescription, 0, rowIndex, columns, 1);
            }
        } else {
            add(fieldLabel, 0, 0, 2, 1);
            if (labelDescription != null) {
                GridPane.setValignment(labelDescription, VPos.TOP);
                add(labelDescription, 0, 1, 2, 1);
            }
            add(this.node, 2, 0, columns - 2, 1);
            if (valueDescription != null) {
                GridPane.setValignment(valueDescription, VPos.TOP);
                add(valueDescription, 2, 1, columns - 2, 1);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupBindings() {
        super.setupBindings();

        editableField.visibleProperty().bind(field.editableProperty());
        readOnlyLabel.visibleProperty().bind(field.editableProperty().not());

        editableField.textProperty().bindBidirectional(field.userInputProperty());
        obfuscatedUserInputBinding = Bindings.createStringBinding(() -> obfuscate(field.getUserInput()), field.userInputProperty());
        readOnlyLabel.textProperty().bind(obfuscatedUserInputBinding);
        fieldLabel.textProperty().bind(field.labelProperty());
        editableField.promptTextProperty().bind(field.placeholderProperty());
        editableField.managedProperty().bind(editableField.visibleProperty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupValueChangedListeners() {
        super.setupValueChangedListeners();

        field.errorMessagesProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(editableField));

        editableField.focusedProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(editableField));
    }

    protected String obfuscate(String input) {
        if (input == null) { return ""; }
        int length = input.length();
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < length; i++) {
            b.append('*');
        }
        return b.toString();
    }
}
