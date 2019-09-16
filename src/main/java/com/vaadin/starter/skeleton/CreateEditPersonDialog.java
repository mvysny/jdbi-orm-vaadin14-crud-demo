package com.vaadin.starter.skeleton;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author mavi
 */
public class CreateEditPersonDialog extends Dialog {
    @NotNull
    public final Person person;

    @NotNull
    public Runnable onSaveOrCreateListener = () -> {};

    private final PersonForm form = new PersonForm();

    public CreateEditPersonDialog(@NotNull Person person) {
        this.person = Objects.requireNonNull(person);

        final VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        content.add(form);

        final HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setSpacing(true);
        content.setAlignSelf(FlexComponent.Alignment.CENTER, buttonBar);
        final Button persist = new Button(isCreating() ? "Create" : "Save");
        persist.addClickListener(e -> okPressed());
        persist.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        final Button cancel = new Button("Cancel");
        cancel.addClickListener(e -> close());
        buttonBar.add(persist, cancel);
        content.add(buttonBar);

        add(content);

        form.binder.readBean(person);
    }

    private boolean isCreating() {
        return person.getId() == null;
    }

    private void okPressed() {
        if (!form.binder.validate().isOk() || !form.binder.writeBeanIfValid(person)) {
            return;
        }
        person.save();
        onSaveOrCreateListener.run();
        close();
    }
}
