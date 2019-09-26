package com.vaadin.demo.dashboard.view;


import com.google.common.eventbus.Subscribe;
import com.vaadin.demo.dashboard.DashboardUI;
import com.vaadin.demo.dashboard.event.DashboardEvent.CloseOpenWindowsEvent;
import com.vaadin.demo.dashboard.event.DashboardEvent.NotificationsCountUpdatedEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public final class Home extends Panel implements View
//        ,DashboardEditListener 
{

    public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";

    private Label titleLabel;
//    private NotificationsButton notificationsButton;
    private CssLayout dashboardPanels;
    private final VerticalLayout root;
//    private Window notificationsWindow;

    public Home() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(buildHeader());

//        root.addComponent(buildSparklines());

        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);

        // All the open sub-windows should be closed whenever the root layout
        // gets clicked.
        root.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutClickEvent event) {
                DashboardEventBus.post(new CloseOpenWindowsEvent());
            }
        });
    }

    
    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);

        titleLabel = new Label("Bienvenido");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

//        notificationsButton = buildNotificationsButton();
//        Component edit = buildEditButton();
//        HorizontalLayout tools = new HorizontalLayout(notificationsButton, edit);
//        tools.setSpacing(true);
//        tools.addStyleName("toolbar");
//        header.addComponent(tools);

        return header;
    }

    
    private Component buildContent() {
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);

//        dashboardPanels.addComponent(buildTopGrossingMovies());
//        dashboardPanels.addComponent(buildNotes());
//        dashboardPanels.addComponent(buildTop10TitlesByRevenue());
//        dashboardPanels.addComponent(buildPopularMovies());

        return dashboardPanels;
    }

    @Override
    public void enter(final ViewChangeEvent event) {
//        notificationsButton.updateNotificationsCount(null);
    }

//    @Override
//    public void dashboardNameEdited(final String name) {
//        titleLabel.setValue(name);
//    }

    public static final class NotificationsButton extends Button {
        private static final String STYLE_UNREAD = "unread";
        public static final String ID = "dashboard-notifications";

        public NotificationsButton() {
            setIcon(FontAwesome.BELL);
            setId(ID);
            addStyleName("notifications");
            addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            DashboardEventBus.register(this);
        }

        @Subscribe
        public void updateNotificationsCount(
                final NotificationsCountUpdatedEvent event) {
//            setUnreadCount(DashboardUI.getDataProvider().getUnreadNotificationsCount());
        }

        public void setUnreadCount(final int count) {
            setCaption(String.valueOf(count));

            String description = "Notifications";
            if (count > 0) {
                addStyleName(STYLE_UNREAD);
                description += " (" + count + " unread)";
            } else {
                removeStyleName(STYLE_UNREAD);
            }
            setDescription(description);
        }
    }

}
