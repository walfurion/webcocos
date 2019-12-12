package com.fundamental.model;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.PropertyFormatter;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * @author Henry Barrientos
 */
public class Utils {

    public HorizontalLayout buildHeader(String caption, boolean spacing, boolean responsive) {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(spacing);
        if (responsive) {
            Responsive.makeResponsive(header);
            header.setResponsive(responsive);
        }

        Label title = new Label(caption);
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

        return header;
    }

    public Component buildSeparator() {
        CssLayout result = new CssLayout();
        result.addStyleName("sparks");
        result.setWidth("100%");
        Responsive.makeResponsive(result);
        return result;
    }

    public HorizontalLayout buildHorizontal(String id, boolean margin, boolean spacing, boolean responsive, boolean sizeFull) {
        HorizontalLayout result = new HorizontalLayout();
        result.setId(id);
        result.setSpacing(spacing);
        result.setMargin(margin);
        result.setResponsive(responsive);
        if (sizeFull) {
            result.setSizeFull();
        }
        Responsive.makeResponsive(result);
        return result;
    }

    public VerticalLayout buildVertical(String id, boolean margin, boolean spacing, boolean responsive, boolean setSizeFull, String styleName) {
        VerticalLayout result = new VerticalLayout();
        result.setId(id);
        result.setSpacing(spacing);
        result.setMargin(margin);
        result.setResponsive(responsive);
        Responsive.makeResponsive(result);
        if (setSizeFull) {
            result.setSizeFull();
        }
        if (styleName != null && !styleName.trim().isEmpty()) {
            result.addStyleName(styleName);
        }
        return result;
    }

    public Table buildTable(String caption, float height, float width, BeanContainer bc, Object[] visibleColumns, String[] columnHeaders) {
        Table result = new Table(caption);
        result.setHeight(height, Unit.PERCENTAGE);
        result.setWidth(width, Unit.PERCENTAGE);
        result.setContainerDataSource(bc);
        result.setVisibleColumns(visibleColumns);
        result.setColumnHeaders(columnHeaders);
        return result;
    }

    public CssLayout buildCssLayout(String id, boolean margin, boolean spacing, boolean responsive, boolean setSizeFull, String styleName) {
        CssLayout result = new CssLayout();
        result.setId(id);
//        result.setSpacing(spacing);
//        result.setMargin(margin);
        result.setResponsive(responsive);
        Responsive.makeResponsive(result);
        if (setSizeFull) {
            result.setSizeFull();
        }
        if (styleName != null && !styleName.trim().isEmpty()) {
            result.addStyleName(styleName);
        }
        return result;
    }

    public VerticalLayout vlContainer(Component component) {
        VerticalLayout result = new VerticalLayout();
        result.addComponent(component);
        result.setMargin(new MarginInfo(false, true, false, false));
        result.setSizeUndefined();
        Responsive.makeResponsive(result);
        return result;
    }

    public VerticalLayout vlContainerRB(Component component) {
        VerticalLayout result = new VerticalLayout();
        result.addComponent(component);
        result.setMargin(new MarginInfo(false, true, true, false));
        result.setSizeUndefined();
        Responsive.makeResponsive(result);
        return result;
    }

    public VerticalLayout vlContainerChkBox(Component component) {
        VerticalLayout result = new VerticalLayout();
        result.addComponent(component);
        result.setMargin(new MarginInfo(false, false, false, false));
        result.setSizeUndefined();
        Responsive.makeResponsive(result);
        return result;
    }

    public VerticalLayout vlContainerChkBoxCenter(Component component) {
        VerticalLayout result = new VerticalLayout();
        result.addComponent(component);
        result.setMargin(new MarginInfo(false, true, false, true));
        result.setSizeUndefined();
        Responsive.makeResponsive(result);
        return result;
    }

    public VerticalLayout vlContainer2(Component component) {
        VerticalLayout result = new VerticalLayout();
        result.addComponent(component);
        result.setMargin(new MarginInfo(false, true, false, false));
        result.setSizeUndefined();
        Responsive.makeResponsive(result);
        return result;
    }

    public VerticalLayout vlContainerTable(Component component) {
        VerticalLayout result = new VerticalLayout();
        result.addComponent(component);
        result.setMargin(new MarginInfo(true, true, true, false));
        result.setSizeUndefined();
        Responsive.makeResponsive(result);
        return result;
    }

    public VerticalLayout buildVerticalR(String id, boolean margin, boolean spacing, boolean sizeFull, String styleName) {
        VerticalLayout result = new VerticalLayout();
        result.setId(id);
        result.setMargin(margin);
        result.setSpacing(spacing);
        if (sizeFull) {
            result.setSizeFull();
        }
        result.setStyleName(styleName);
        return result;
    }

    public Component buildHeaderR(String title) {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);

        Label titleLabel = new Label(title);
//        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    }

    public VerticalLayout vlContainerR(Component component) {
        VerticalLayout result = new VerticalLayout();
        result.setSizeUndefined();
        Responsive.makeResponsive(result);
        result.setMargin(new MarginInfo(false, true, true, false));
        result.addComponent(component);
        return result;
    }

    DecimalFormat numberFmt = new DecimalFormat("### ###,##0.00;-#");
    DecimalFormat numberFmt3D = new DecimalFormat("### ###,##0.000;-#");

    public PropertyFormatter getPropertyFormatterDouble(Property pro) {
        return new PropertyFormatter(pro) {
            public String format(Object value) {
                return numberFmt.format(value);
            }

            public Object parse(String formattedValue) throws Exception {
                try {
                    return Double.parseDouble(formattedValue.replaceAll(",", "").replaceAll(" ", ""));
                } catch (Exception exc) {
                    return 0D;
                }
            }
        };
    }

    public PropertyFormatter getPropertyFormatterDouble_3Digit(Property pro) {
        return new PropertyFormatter(pro) {
            public String format(Object value) {
                return numberFmt3D.format(value);
            }

            public Object parse(String formattedValue) throws Exception {
                try {
                    return Double.parseDouble(formattedValue.replaceAll(",", "").replaceAll(" ", ""));
                } catch (Exception exc) {
                    return 0D;
                }
            }
        };
    }

    DecimalFormat numberFmtComma = new DecimalFormat("### ###,###;-#");

    public PropertyFormatter getPropertyFormatterInteger(Property pro) {
        return new PropertyFormatter(pro) {
            public String format(Object value) {
                return numberFmtComma.format(value);
            }

            public Object parse(String formattedValue) throws Exception {
                try {
                    return Integer.parseInt(formattedValue.replaceAll(",", "").replaceAll(" ", ""));
                } catch (Exception exc) {
                    return 0;
                }
            }
        };
    }

    public DateField buildDateField(String caption, String dateFormat, Locale locale, String styleName, Date rangeStart, Date rangeEnd, boolean required, Resolution resolution, Date value) {
        DateField result = new DateField(caption, value);
        result.setDateFormat(dateFormat);
        result.setLocale(locale);
        result.addStyleName(styleName);
        if (rangeStart != null) {
            result.setRangeStart(rangeStart);
        }
        if (rangeEnd != null) {
            result.setRangeEnd(rangeEnd);
        }
        result.setRequired(required);
        result.setResolution(resolution);
        return result;
    }

    public TextField buildTextFilter(String inputPrompt) {
        TextField result = new TextField();
        result.setIcon(FontAwesome.SEARCH);
        result.setInputPrompt(inputPrompt);
        result.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        return result;
    }

    public ComboBox buildCombobox(String caption, Object propertyId, boolean nullSelectionAllowed, boolean required, String styleName, Container newDataSource) {
        ComboBox result = new ComboBox(caption);
        result.setItemCaptionPropertyId(propertyId);
        result.setNullSelectionAllowed(nullSelectionAllowed);
        result.setRequired(required);
        if (styleName != null) {
            result.addStyleName(styleName);
        }
        result.setContainerDataSource(newDataSource);
        return result;
    }

    public int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public TextField buildTextField(String caption, String nullRepresentation, boolean nullSettingAllowed, int maxLength, boolean required, String style) {
        TextField result = new TextField(caption);
        result.setNullRepresentation(nullRepresentation);
        result.setNullSettingAllowed(nullSettingAllowed);
        result.setMaxLength(maxLength);
        result.setRequired(required);
        if (style != null) {
            result.addStyleName(style);
        }
        return result;
    }

    public Button buildButton(String caption, Resource icon, String style) {
        Button result = new Button(caption, icon);
        result.addStyleName(style);
        return result;
    }

    public PopupDateField buildPopupDateField(String caption, Date value, String dateFormat, boolean required, Resolution resolution, Date startDate, Date endDate, Locale locale, String style) {
        PopupDateField result = new PopupDateField(caption, value);
        result.setDateFormat(dateFormat);
        result.setLocale(locale);
        result.setRequired(required);
        result.setResponsive(true);
        result.setResolution(resolution);
        if (startDate != null) {
            result.setRangeStart(startDate);
        }
        if (endDate != null) {
            result.setRangeEnd(endDate);
        }
        if (style != null) {
            result.addStyleName(style);
        }
        return result;
    }

    public TextArea buildTextArea(String caption, int columns, int rows, int maxLength, boolean required, boolean nullSettingAllowed, String nullRepresentation, String style) {
        TextArea result = new TextArea(caption);
        result.setColumns(columns);
        result.setRows(rows);
        result.setRequired(required);
        result.setMaxLength(maxLength);
        result.setNullSettingAllowed(nullSettingAllowed);
        result.setNullRepresentation(nullRepresentation);
        if (style != null) {
            result.addStyleName(style);
        }
        result.setResponsive(true);
        return result;
    }

    public boolean filterByProperty(final String prop, final Item item, final String text) {
        if (item == null || item.getItemProperty(prop) == null || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim().toLowerCase();
        if (val.contains(text.toLowerCase().trim())) {
            return true;
        }
        return false;
    }
    
    public PasswordField buildPasswordField(String caption, String nullRepresentation, boolean nullSettingAllowed, int maxLength, boolean required, String style) {
        PasswordField result = new PasswordField(caption);
        result.setNullRepresentation(nullRepresentation);
        result.setNullSettingAllowed(nullSettingAllowed);
        result.setMaxLength(maxLength);
        result.setRequired(required);
        if (style != null) {
            result.addStyleName(style);
        }
        return result;
    }

}
