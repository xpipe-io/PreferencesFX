package com.dlsc.preferencesfx.model;

import com.dlsc.formsfx.model.structure.DataField;
import com.dlsc.formsfx.model.structure.Element;
import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.NodeElement;
import com.dlsc.formsfx.model.validators.Validator;
import com.dlsc.preferencesfx.formsfx.view.controls.DoubleSliderControl;
import com.dlsc.preferencesfx.formsfx.view.controls.IntegerSliderControl;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleChooserControl;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleColorPickerControl;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleComboBoxControl;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleControl;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleDoubleControl;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleIntegerControl;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleListViewControl;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleTextControl;
import com.dlsc.preferencesfx.formsfx.view.controls.ToggleControl;
import com.dlsc.preferencesfx.util.Constants;
import com.dlsc.preferencesfx.util.VisibilityProperty;
import com.dlsc.preferencesfx.util.StorageHandler;
import java.io.File;
import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a setting, which holds the field to be displayed and the property which is bound.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Setting<E extends Element, P extends Property> {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(Setting.class.getName());

  public static final String MARKED_STYLE_CLASS = "simple-control-marked";
  private String description;
  private E element;
  private P value;
  private boolean marked = false;
  private final EventHandler<MouseEvent> unmarker = event -> unmark();
  private final StringProperty breadcrumb = new SimpleStringProperty("");
  private String key = "";

  protected Setting(String description, E element, P value) {
    this.description = description;
    this.element = element;
    this.value = value;
  }

  /**
   * Constructs a setting of {@link Boolean} type, which is represented by a {@link ToggleControl}.
   *
   * @param description the title of this setting
   * @param property    to be bound, saved / loaded and used for undo / redo
   * @return the constructed setting
   */
  public static Setting of(String description, BooleanProperty property) {
    return of(description, property, null);
  }

  public static Setting of(String description, BooleanProperty property, VisibilityProperty visibilityProperty) {
    return new Setting<>(
            description,
            Field.ofBooleanType(property)
                    .label(description)
                    .render(() -> ToggleControl.of(visibilityProperty)),
            property
    );
  }

  /**
   * Constructs a setting of {@link Integer} type, which is represented by a {@link TextField}.
   *
   * @param description the title of this setting
   * @param property    to be bound, saved / loaded and used for undo / redo
   * @return the constructed setting
   */
  public static Setting of(String description, IntegerProperty property) {
    return of(description, property, null);
  }

  /**
   * Constructs a setting of {@link Integer} type, which is represented by a {@link TextField}.
   *
   * @param description the title of this setting
   * @param property    to be bound, saved / loaded and used for undo / redo
   * @param visibilityProperty control visibility of element
   * @return the constructed setting
   */
  public static Setting of(String description, IntegerProperty property, VisibilityProperty visibilityProperty) {
    return new Setting<>(
        description,
        Field.ofIntegerType(property)
            .label(description)
            .render(() -> SimpleIntegerControl.of(visibilityProperty)),
        property);
  }

    /**
     * Constructs a setting of {@link Double} type, which is represented by a {@link TextField}.
     *
     * @param description the title of this setting
     * @param property    to be bound, saved / loaded and used for undo / redo
     * @return the constructed setting
     */
    public static Setting of(String description, DoubleProperty property) {
        return of(description, property, null);
    }

  /**
   * Constructs a setting of {@link Double} type, which is represented by a {@link TextField}.
   *
   * @param description the title of this setting
   * @param property    to be bound, saved / loaded and used for undo / redo
   * @param visibilityProperty control visibility of element
   * @return the constructed setting
   */
  public static Setting of(String description, DoubleProperty property, VisibilityProperty visibilityProperty) {
    return new Setting<>(
        description,
        Field.ofDoubleType(property)
            .label(description)
            .render(() -> SimpleDoubleControl.of(visibilityProperty)),
        property);
  }

  /**
   * Constructs a setting of {@link Double} type, which is represented by a {@link Slider}.
   *
   * @param description the title of this setting
   * @param property    to be bound, saved / loaded and used for undo / redo
   * @param min         minimum value of the {@link Slider}
   * @param max         maximum value of the {@link Slider}
   * @param precision   number of digits after the decimal point
   * @return the constructed setting
   */
  public static Setting of(
      String description, DoubleProperty property, double min, double max, int precision) {
      return of(description, property, null);
  }
  /**
   * Constructs a setting of {@link Double} type, which is represented by a {@link Slider}.
   *
   * @param description the title of this setting
   * @param property    to be bound, saved / loaded and used for undo / redo
   * @param min         minimum value of the {@link Slider}
   * @param max         maximum value of the {@link Slider}
   * @param precision   number of digits after the decimal point
   * @param visibilityProperty control visibility of element
   * @return the constructed setting
   */
  public static Setting of(
      String description, DoubleProperty property, double min, double max, int precision, VisibilityProperty visibilityProperty) {
    return new Setting<>(
        description,
        Field.ofDoubleType(property)
            .label(description)
            .render(() -> DoubleSliderControl.of(min, max, precision, visibilityProperty)),
        property);
  }

  /**
   * Constructs a setting of {@link Integer} type, which is represented by a {@link Slider}.
   *
   * @param description the title of this setting
   * @param property    to be bound, saved / loaded and used for undo / redo
   * @param min         minimum value of the {@link Slider}
   * @param max         maximum value of the {@link Slider}
   * @return the constructed setting
   */
  public static Setting of(String description, IntegerProperty property, int min, int max) {
      return of(description, property, null);
  }
  /**
   * Constructs a setting of {@link Integer} type, which is represented by a {@link Slider}.
   *
   * @param description the title of this setting
   * @param property    to be bound, saved / loaded and used for undo / redo
   * @param min         minimum value of the {@link Slider}
   * @param max         maximum value of the {@link Slider}
   * @param visibilityProperty control visibility of element
   * @return the constructed setting
   */
  public static Setting of(String description, IntegerProperty property, int min, int max, VisibilityProperty visibilityProperty) {
    return new Setting<>(
        description,
        Field.ofIntegerType(property)
            .label(description)
            .render(() -> IntegerSliderControl.of(min, max, visibilityProperty)),
        property);
  }

  /**
   * Constructs a setting of {@link String} type, which is represented by a {@link TextField}.
   *
   * @param description the title of this setting
   * @param property    to be bound, saved / loaded and used for undo / redo
   * @return the constructed setting
   */
  public static Setting of(String description, StringProperty property) {
      return of(description, property, null);
  }

  /**
   * Constructs a setting of {@link String} type, which is represented by a {@link TextField}.
   *
   * @param description the title of this setting
   * @param property    to be bound, saved / loaded and used for undo / redo
   * @param visibilityProperty control visibility of element
   * @return the constructed setting
   */
  public static Setting of(String description, StringProperty property, VisibilityProperty visibilityProperty) {
    return new Setting<>(
        description,
        Field.ofStringType(property)
            .label(description)
            .render(() -> SimpleTextControl.of(visibilityProperty)),
        property);
  }

  /**
   * Creates a combobox with single selection.
   *
   * @param description the title of this setting
   * @param items       the items which are possible to choose in the combobox, which are shown
   *                    in their {@link #toString()} representation
   * @param selection   the currently selected item of the combobox to be bound, saved / loaded and
   *                    used for undo / redo
   * @param <P>         the type of objects which should be displayed in the combobox
   * @return the constructed setting
   */
  public static <P> Setting of(
      String description, ListProperty<P> items, ObjectProperty<P> selection) {
      return of(description, items, selection, null);
  }

  /**
   * Creates a combobox with single selection.
   *
   * @param description the title of this setting
   * @param items       the items which are possible to choose in the combobox, which are shown
   *                    in their {@link #toString()} representation
   * @param selection   the currently selected item of the combobox to be bound, saved / loaded and
   *                    used for undo / redo
   * @param <P>         the type of objects which should be displayed in the combobox
   * @param visibilityProperty control visibility of element
   * @return the constructed setting
   */
  public static <P> Setting of(
      String description, ListProperty<P> items, ObjectProperty<P> selection, VisibilityProperty visibilityProperty) {
    return new Setting<>(
        description,
        Field.ofSingleSelectionType(items, selection)
            .label(description)
            .render(() -> SimpleComboBoxControl.of(visibilityProperty)),
        selection);
  }

  /**
   * Creates a combobox with single selection.
   *
   * @param description the title of this setting
   * @param items       the items which are possible to choose in the combobox, which are shown
   *                    in their {@link #toString()} representation
   * @param selection   the currently selected item of the combobox to be bound, saved / loaded and
   *                    used for undo / redo
   * @param <P>         the type of objects which should be displayed in the combobox
   * @return the constructed setting
   */
  public static <P> Setting of(
      String description, ObservableList<P> items, ObjectProperty<P> selection) {
      return of(description, items, selection, null);
  }

  /**
   * Creates a combobox with single selection.
   *
   * @param description the title of this setting
   * @param items       the items which are possible to choose in the combobox, which are shown
   *                    in their {@link #toString()} representation
   * @param selection   the currently selected item of the combobox to be bound, saved / loaded and
   *                    used for undo / redo
   * @param <P>         the type of objects which should be displayed in the combobox
   * @param visibilityProperty control visibility of element
   * @return the constructed setting
   */
  public static <P> Setting of(
      String description, ObservableList<P> items, ObjectProperty<P> selection, VisibilityProperty visibilityProperty) {
    return new Setting<>(
        description,
        Field.ofSingleSelectionType(new SimpleListProperty<>(items), selection)
            .label(description)
            .render(() -> SimpleComboBoxControl.of(visibilityProperty)),
        selection);
  }

  /**
   * Creates a combobox with multiselection.
   * At least one element has to be selected at all times.
   *
   * @param description the title of this setting
   * @param items       the items which are possible to choose in the combobox, which are shown
   *                    in their {@link #toString()} representation
   * @param selections  the currently selected item(s) of the combobox to be bound, saved / loaded
   *                    and used for undo / redo
   * @param <P>         the type of objects which should be displayed in the combobox
   * @return the constructed setting
   */
  public static <P> Setting of(
      String description, ListProperty<P> items, ListProperty<P> selections) {
      return of(description, items, selections, null);
  }

  /**
   * Creates a combobox with multiselection.
   * At least one element has to be selected at all times.
   *
   * @param description the title of this setting
   * @param items       the items which are possible to choose in the combobox, which are shown
   *                    in their {@link #toString()} representation
   * @param selections  the currently selected item(s) of the combobox to be bound, saved / loaded
   *                    and used for undo / redo
   * @param <P>         the type of objects which should be displayed in the combobox
   * @param visibilityProperty control visibility of element
   * @return the constructed setting
   */
  public static <P> Setting of(
      String description, ListProperty<P> items, ListProperty<P> selections, VisibilityProperty visibilityProperty) {
    return new Setting<>(
        description,
        Field.ofMultiSelectionType(items, selections)
            .label(description)
            .render(() -> SimpleListViewControl.of(visibilityProperty)),
        selections);
  }

  /**
   * Creates a combobox with multiselection.
   * At least one element has to be selected at all times.
   *
   * @param description the title of this setting
   * @param items       the items which are possible to choose in the combobox, which are shown
   *                    in their {@link #toString()} representation
   * @param selections  the currently selected item(s) of the combobox to be bound, saved / loaded
   *                    and used for undo / redo
   * @param <P>         the type of objects which should be displayed in the combobox
   * @return the constructed setting
   */
  public static <P> Setting of(
      String description, ObservableList<P> items, ListProperty<P> selections) {
      return of(description, items, selections, null);
  }

  /**
   * Creates a combobox with multiselection.
   * At least one element has to be selected at all times.
   *
   * @param description the title of this setting
   * @param items       the items which are possible to choose in the combobox, which are shown
   *                    in their {@link #toString()} representation
   * @param selections  the currently selected item(s) of the combobox to be bound, saved / loaded
   *                    and used for undo / redo
   * @param <P>         the type of objects which should be displayed in the combobox
   * @param visibilityProperty control visibility of element
   * @return the constructed setting
   */
  public static <P> Setting of(
      String description, ObservableList<P> items, ListProperty<P> selections, VisibilityProperty visibilityProperty) {
    return new Setting<>(
        description,
        Field.ofMultiSelectionType(new SimpleListProperty<>(items), selections)
            .label(description)
            .render(() -> SimpleListViewControl.of(visibilityProperty)),
        selections);
  }

  /**
   * Creates a setting of a custom defined field.
   *
   * @param <F>         the field type
   * @param <P>         the property type
   * @param description the title of this setting
   * @param field       custom Field object from FormsFX
   * @param property    to be bound, saved / loaded and used for undo / redo
   * @return the constructed setting
   */
  public static <F extends Field<F>, P extends Property> Setting of(
      String description, F field, P property) {
    return new Setting<>(
        description,
        field.label(description),
        property);
  }

  /**
   * Creates a setting of a custom defined node element.
   * <br>
   * This allows for custom elements which just consist of a Node, without showing a description.
   * @apiNote Changed state of the {@link Node} will NOT be saved!
   *          Only use this for {@link Node}s with static content!
   *
   * @param <N>         the node element type
   * @param node        custom node
   * @return the constructed setting
   */
  public static <N extends Node> Setting of(N node) {
    return new Setting<>(
        null,
        NodeElement.of(node),
        null);
  }

  /**
   * Creates a custom color picker control.
   *
   * @param description   the title of this setting
   * @param colorProperty the current selected color value
   * @return the constructed setting
   */
  public static Setting of(String description, ObjectProperty<Color> colorProperty) {
      return of(description, colorProperty, null);
  }

  /**
   * Creates a custom color picker control.
   *
   * @param description   the title of this setting
   * @param colorProperty the current selected color value
   * @param visibilityProperty control visibility of element
   * @return the constructed setting
   */
  public static Setting of(String description, ObjectProperty<Color> colorProperty, VisibilityProperty visibilityProperty) {
    StringProperty stringProperty = new SimpleStringProperty();
    stringProperty.bindBidirectional(
        colorProperty, new StringConverter<Color>() {
          @Override
          public String toString(Color color) {
            return color.toString();
          }

          @Override
          public Color fromString(String value) {
            return Color.valueOf(value);
          }
        }
    );

    return new Setting<>(
        description,
        Field.ofStringType(stringProperty)
            .label(description)
            .render(() -> SimpleColorPickerControl.of(
                Objects.isNull(colorProperty.get()) ? Color.BLACK : colorProperty.get(), visibilityProperty)
            ),
        stringProperty
    );
  }

  /**
   * Creates a file/directory chooser control.
   *
   * @param description       the title of this setting
   * @param fileProperty      the property to which the chosen file / directory should be set to
   * @param directory         true, if only directories are allowed
   * @return the constructed setting
   */
  public static Setting of(String description,
                           ObjectProperty<File> fileProperty,
                           boolean directory) {
    return of(description, fileProperty, null, directory);
  }

  /**
   * Creates a file/directory chooser control.
   *
   * @param description       the title of this setting
   * @param fileProperty      the property to which the chosen file / directory should be set to
   * @param initialDirectory  An optional initial path, can be null. If null, will use the path from
   *                          the previously chosen file if present.
   * @param directory         true, if only directories are allowed
   * @return the constructed setting
   */
  public static Setting of(String description,
                           ObjectProperty<File> fileProperty,
                           File initialDirectory,
                           boolean directory) {
    return of(description, fileProperty, "Browse", initialDirectory, directory);
  }

  /**
   * Creates a file/directory chooser control.
   *
   * @param description       the title of this setting
   * @param fileProperty      the property to which the chosen file / directory should be set to
   * @param buttonText        text of the button to open the file / directory chooser
   * @param initialDirectory  An optional initial path, can be null. If null, will use the path from
   *                          the previously chosen file if present.
   * @param directory         true, if only directories are allowed
   * @return the constructed setting
   */
  public static Setting of(String description,
                           ObjectProperty<File> fileProperty,
                           String buttonText,
                           File initialDirectory,
                           boolean directory) {
    return of(description, fileProperty, buttonText, initialDirectory, directory, null);
  }

  /**
   * Creates a file/directory chooser control.
   *
   * @param description       the title of this setting
   * @param fileProperty      the property to which the chosen file / directory should be set to
   * @param buttonText        text of the button to open the file / directory chooser
   * @param initialDirectory  An optional initial path, can be null. If null, will use the path from
   *                          the previously chosen file if present.
   * @param directory         true, if only directories are allowed
   * @param visibilityProperty control visibility of element
   * @return the constructed setting
   */
  public static Setting of(String description,
                           ObjectProperty<File> fileProperty,
                           String buttonText,
                           File initialDirectory,
                           boolean directory,
      VisibilityProperty visibilityProperty) {
    StringProperty stringProperty = new SimpleStringProperty();
    stringProperty.bindBidirectional(
        fileProperty, new StringConverter<File>() {
          @Override
          public String toString(File file) {
            if (Objects.isNull(file)) {
              return "";
            }
            return file.getAbsolutePath();
          }

          @Override
          public File fromString(String value) {
            return new File(value);
          }
        }
    );

    return new Setting<>(
        description,
        Field.ofStringType(stringProperty)
            .label(description)
            .render(() -> SimpleChooserControl.of(buttonText, initialDirectory, directory, visibilityProperty)
            ),
        stringProperty
    );
  }



  /**
   * Sets the list of validators for the current field. This overrides all
   * validators that have previously been added.
   *
   * @param newValue The validators that are to be used for validating this
   *                 field. Limited to validators that are able to handle the
   *                 field's underlying type.
   * @return the current setting to allow for chaining
   * @throws UnsupportedOperationException if this {@link Field} is not instanceof {@link DataField}
   */
  @SafeVarargs
  public final Setting validate(Validator... newValue) {
    if (element instanceof DataField) {
      ((DataField) element).validate(newValue);
    } else {
      throw new UnsupportedOperationException("Field type must be instance of DataField");
    }
    return this;
  }

  /**
   * Marks a setting.
   * Is used for the search, which marks and unmarks items depending on the match as a form of
   * visual feedback.
   */
  public void mark() {
    if (!hasDescription()) {
      throw new UnsupportedOperationException(
          "Only Fields can be marked, since they have a description."
      );
    }
    // ensure it's not marked yet - so a control doesn't contain the same styleClass multiple times
    if (!marked) {
      SimpleControl renderer = (SimpleControl) ((Field) getElement()).getRenderer();
      Node markNode = renderer.getFieldLabel();
      markNode.getStyleClass().add(MARKED_STYLE_CLASS);
      markNode.setOnMouseExited(unmarker);
      marked = !marked;
    }
  }

  /**
   * Unmarks a setting.
   * Is used for the search, which marks and unmarks items depending on the match as a form of
   * visual feedback.
   */
  public void unmark() {
    if (!hasDescription()) {
      throw new UnsupportedOperationException(
          "Only Fields can be unmarked, since they have a description."
      );
    }
    // check if it's marked before removing the style class
    if (marked) {
      SimpleControl renderer = (SimpleControl) ((Field) getElement()).getRenderer();
      Node markNode = renderer.getFieldLabel();
      markNode.getStyleClass().remove(MARKED_STYLE_CLASS);
      markNode.removeEventHandler(MouseEvent.MOUSE_EXITED, unmarker);
      marked = !marked;
    }
  }

  /**
   * Returns the description of this setting or if i18n is used, it will return the translated
   * description in the current locale.
   *
   * @return the description
   */
  public String getDescription() {
    if (element != null) {

      if (!(element instanceof Field)) {
        throw new UnsupportedOperationException(
            "Cannot get description of an Element which is not a field"
        );
      }

      return ((Field) element).getLabel();
    }
    return description;
  }

  public P valueProperty() {
    return value;
  }

  public E getElement() {
    return element;
  }

  /**
   * Saves the current value of this setting using a {@link StorageHandler}.
   *
   * @param storageHandler the {@link StorageHandler} to use
   */
  public void saveSettingValue(StorageHandler storageHandler) {
    storageHandler.saveObject(key.isEmpty() ? getBreadcrumb() : key, value.getValue());
  }

  /**
   * Loads the value of this setting using a {@link StorageHandler}.
   *
   * @param storageHandler the {@link StorageHandler} to use
   * @implNote differentiates between a {@link ListProperty}, as found in multiselection settings,
   *           and all the other property types, since those need to be handled differently by
   *           the {@link StorageHandler}.
   */
  public void loadSettingValue(StorageHandler storageHandler) {
    if (value instanceof ListProperty) {
      value.setValue(storageHandler.loadObservableList(
          key.isEmpty() ? getBreadcrumb() : key, (ObservableList) value.getValue()
      ));
    } else {
      value.setValue(storageHandler.loadObject(
          key.isEmpty() ? getBreadcrumb() : key, value.getValue()
      ));
    }
  }

  /**
   * Adds the {@code breadCrumb} to this breadcrumb and updates all of its settings accordingly.
   *
   * @param breadCrumb the breadcrumb to add to this group's breadcrumb
   */
  public void addToBreadcrumb(String breadCrumb) {
    setBreadcrumb(breadCrumb + Constants.BREADCRUMB_DELIMITER + description);
  }

  /**
   * Returns whether or not this {@link Setting} has a value.
   * <br>
   * For example, if {@code element} is a {@link Field}, {@code value} is defined, however
   * if {@code element} is a {@link NodeElement}, {@code value} will be null, since there is
   * nothing to persist.
   *
   * @return true if value is not null
   */
  public boolean hasValue() {
    return !Objects.isNull(value);
  }

  /**
   * Returns whether or not this {@link Setting} has a description.
   * <br>
   * For example, if {@code element} is a {@link Field}, {@code description} is defined, however
   * if {@code element} is a {@link NodeElement}, {@code description} will be null, since
   * {@link NodeElement}s don't have a description.
   *
   * @return true if there is a description
   */
  public boolean hasDescription() {
    return !Objects.isNull(description);
  }

  public String getBreadcrumb() {
    return breadcrumb.get();
  }

  public StringProperty breadcrumbProperty() {
    return breadcrumb;
  }

  public void setBreadcrumb(String breadcrumb) {
    this.breadcrumb.set(breadcrumb);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Setting<?, ?> setting = (Setting<?, ?>) o;
    return Objects.equals(breadcrumb, setting.breadcrumb);
  }

  @Override
  public int hashCode() {
    return Objects.hash(breadcrumb);
  }

  @Override
  public String toString() {
    return getBreadcrumb();
  }

  /**
   * Sets the Preference key to be used instead of the breadcrumb.
   * Can be used without hash in a custom {@link StorageHandler}.
   * @param key the string key to be used for the preference
   * @return this Setting
   */
  public Setting customKey(String key) {
    this.key = key;
    return this;
  }

  /**
   * Apply {@link VisibilityProperty} to renderer ({@link SimpleControl}.
   *
   * @param visibilityProperty source visibility condition
   * @param additionalVisibilityCondition define the visibility condition to be added (ANDed) to the current (if any)
   */
  public Setting applyVisibility(VisibilityProperty visibilityProperty, boolean additionalVisibilityCondition) {
    if (element instanceof Field) {
      SimpleControl renderer = (SimpleControl) ((Field) getElement()).getRenderer();

      if (additionalVisibilityCondition) {
        VisibilityProperty existingVP = renderer.getVisibilityProperty();
        if (existingVP != null) {
          visibilityProperty = VisibilityProperty.of(existingVP.get().and(visibilityProperty.get()));
        }
      }
      renderer.setVisibilityProperty(visibilityProperty);
    }
    if (element instanceof NodeElement) {
      ((NodeElement) element).getNode().visibleProperty().bind(visibilityProperty.get());
    }
    return this;
  }

  public Setting applyVisibility(VisibilityProperty visibilityProperty) {
    return applyVisibility(visibilityProperty, false);
  }

}
