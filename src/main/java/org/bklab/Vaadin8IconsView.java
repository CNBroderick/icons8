package org.bklab;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.jsclipboard.JSClipboard;
import com.vaadin.jsclipboard.JSClipboardButton;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@SuppressWarnings("unused")
class Vaadin8IconsView {

    public static Component create() {
        Vaadin8IconsView view = new Vaadin8IconsView();
        VerticalLayout layout = new VerticalLayout();
        TextField field = new TextField();
        field.setPlaceholder("输入一些英文以过滤...");
        field.addStyleName(ValoTheme.TEXTFIELD_TINY);
        final AtomicReference<Component> icons = new AtomicReference<>();
        icons.set(view.createVaadinIcons8(null));

        field.addValueChangeListener(e -> {
            layout.removeComponent(icons.get());
            icons.set(view.createVaadinIcons8(e.getValue()));
            layout.addComponent(icons.get());
        });

        layout.addComponents(field, icons.get());
        return layout;
    }


    private Component createVaadinIcons8(String filter) {
        AbsoluteLayout absoluteLayout = new AbsoluteLayout();
        VerticalLayout layout = new VerticalLayout();
        List<Button> icons = new ArrayList<>();

        Stream.of(VaadinIcons.values()).forEach(i -> {
            if (filter != null && !i.name().toLowerCase().contains(filter.toLowerCase())) return;
            final Label select = new Label(i.name());
            absoluteLayout.addComponent(select, "right: 0px; top: 0px;");
            JSClipboardButton jsb = new JSClipboardButton(select);
            jsb.setDescription(i.name());
            jsb.setIcon(i);
            jsb.addSuccessListener((JSClipboard.SuccessListener) () -> Notification.show(getSuccessMessage(i.name()), getPasteDescription(), Notification.Type.WARNING_MESSAGE));
            jsb.addErrorListener((JSClipboard.ErrorListener) () -> Notification.show("复制失败", i.name(), Notification.Type.ERROR_MESSAGE));
            jsb.addStyleNames(ValoTheme.BUTTON_HUGE, ValoTheme.BUTTON_LINK);
            jsb.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
            jsb.setEnabled(true);
            icons.add(jsb);
        });
        if (icons.isEmpty()) return layout;
        int columnSize = 10;
        for (int i = 0; i <= icons.size() / columnSize; i++) {

            HorizontalLayout insideRow = new com.vaadin.ui.HorizontalLayout();
            insideRow.addComponent(new com.vaadin.ui.Label(String.format("%03d to %03d: ", i * columnSize, (i + 1) * columnSize - 1)));
            icons.subList(i * columnSize, (i + 1) * columnSize - 1 < icons.size() ? (i + 1) * columnSize - 1 : icons.size() - 1).forEach(insideRow::addComponent);
            layout.addComponent(insideRow);
        }
        layout.addComponent(absoluteLayout);
        return layout;
    }


    private String getSuccessMessage(String content) {
        return "成功复制 " + content + " 到您的剪贴板上";
    }

    private String getPasteDescription() {
        return "请使用键盘快捷键 \"Ctrl + V\" 粘贴到相应位置";
    }

    private String getErrorMessage(String content) {
        return "复制\n" + content + "\n失败";
    }

}
