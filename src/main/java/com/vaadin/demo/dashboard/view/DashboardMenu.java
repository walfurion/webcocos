package com.vaadin.demo.dashboard.view;

import com.fundamental.model.Acceso;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.services.Dao;

import com.google.common.eventbus.Subscribe;
import com.vaadin.demo.dashboard.event.DashboardEvent.NotificationsCountUpdatedEvent;
import com.vaadin.demo.dashboard.event.DashboardEvent.PostViewChangeEvent;
import com.vaadin.demo.dashboard.event.DashboardEvent.ProfileUpdatedEvent;
import com.vaadin.demo.dashboard.event.DashboardEvent.ReportsCountUpdatedEvent;
import com.vaadin.demo.dashboard.event.DashboardEvent.UserLoggedOutEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;

/**
 * A responsive menu component providing user information and the controls for
 * primary navigation between the views.
 */
@SuppressWarnings({"serial", "unchecked"})
public final class DashboardMenu extends CustomComponent {

    public static final String ID = "dashboard-menu";
    public static final String REPORTS_BADGE_ID = "dashboard-menu-reports-badge";
    public static final String NOTIFICATIONS_BADGE_ID = "dashboard-menu-notifications-badge";
    private static final String STYLE_VISIBLE = "valo-menu-visible";
    private Label notificationsBadge;
    private Label reportsBadge;
    private MenuItem settingsItem;

    public DashboardMenu() {
//        ValoTheme.menu
        setPrimaryStyleName("valo-menu");
        setId(ID);
        setSizeUndefined();

        // There's only one DashboardMenu per UI so this doesn't need to be
        // unregistered from the UI-scoped DashboardEventBus.
        DashboardEventBus.register(this);

        setCompositionRoot(buildContent());
    }

    private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildUserMenu());
//        menuContent.addComponent(buildToggleButton());
        menuContent.addComponent(buildMenuItems());

        return menuContent;
    }

    private Component buildTitle() {
        Label logo = new Label("<strong>COCO's</strong>",
                ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        return logoWrapper;
    }

    private Usuario getCurrentUser() {
//    private User getCurrentUser() {
//        return (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        return (Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName());
    }

    private Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");
//        final User user = getCurrentUser();
//        final Usuario user = getCurrentUser();
        settingsItem = settings.addItem("", new ThemeResource("img/profile-pic-300px.jpg"), null);
        updateUserName(null);

//        settingsItem.addItem("Edit Profile", new Command() {
//            @Override
//            public void menuSelected(final MenuItem selectedItem) {
//                ProfilePreferencesWindow.open(user, false);
//            }
//        });
//        settingsItem.addItem("Preferences", new Command() {
//            @Override
//            public void menuSelected(final MenuItem selectedItem) {
//                ProfilePreferencesWindow.open(user, true);
//            }
//        });
        settingsItem.addSeparator();
        settingsItem.addItem("Salir", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                DashboardEventBus.post(new UserLoggedOutEvent());
            }
        });
        return settings;
    }

    private Component buildToggleButton() {
        Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
                    getCompositionRoot().removeStyleName(STYLE_VISIBLE);
                } else {
                    getCompositionRoot().addStyleName(STYLE_VISIBLE);
                }
            }
        });
        valoMenuToggleButton.setIcon(FontAwesome.LIST);
        valoMenuToggleButton.addStyleName("valo-menu-toggle");
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
        return valoMenuToggleButton;
    }

    private Component buildMenuItems() {
        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");

//        menuItemsLayout.addComponent(new ValoMenuItemButton(DashboardViewType.HOME));
        Usuario user = ((Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName()));
        Dao servicio = new Dao();
        List<Acceso> misAccessos = servicio.getAccesosByUsuarioid(user.getUsuarioId(), user.isSysadmin());
        servicio.closeConnections();
        
        DashboardViewType dvt = DashboardViewType.HOME;
        dvt.setViewTittle(dvt.getViewName());
        menuItemsLayout.addComponent(new ValoMenuItemButton(dvt));

        for (Acceso accPadre : misAccessos) {    //estos son padres
            Label separator = new Label(accPadre.getTitulo(), ContentMode.HTML);
            separator.addStyleName(ValoTheme.MENU_SUBTITLE);
            separator.setSizeUndefined();
            menuItemsLayout.addComponent(separator);
            for (final DashboardViewType view : DashboardViewType.values()) {
                for (Acceso acc : accPadre.getAccesos()) {
                    if (view.getViewName().trim().toUpperCase().equals(acc.getRecursoInterno().trim().toUpperCase())) {
                        view.setViewTittle(acc.getTitulo());
                        menuItemsLayout.addComponent(new ValoMenuItemButton(view));
                    }
                }
            }
        }

//            if (view == DashboardViewType.REPORTS) {
//                // Add drop target to reports button
//                DragAndDropWrapper reports = new DragAndDropWrapper(
//                        menuItemComponent);
//                reports.setSizeUndefined();
//                reports.setDragStartMode(DragStartMode.NONE);
//                reports.setDropHandler(new DropHandler() {
//
//                    @Override
//                    public void drop(final DragAndDropEvent event) {
//                        UI.getCurrent()
//                                .getNavigator()
//                                .navigateTo(
//                                        DashboardViewType.REPORTS.getViewName());
//                        Table table = (Table) event.getTransferable()
//                                .getSourceComponent();
//                        DashboardEventBus.post(new TransactionReportEvent(
//                                (Collection<Transaction>) table.getValue()));
//                    }
//
//                    @Override
//                    public AcceptCriterion getAcceptCriterion() {
//                        return AcceptItem.ALL;
//                    }
//
//                });
//                menuItemComponent = reports;
//            }
//---------------------------------------------------------------------------------------
//            if (view == DashboardViewType.REPORTS) {
//                reportsBadge = new Label();
//                reportsBadge.setId(REPORTS_BADGE_ID);
//                menuItemComponent = buildBadgeWrapper(menuItemComponent, reportsBadge);
//            }
//        }
        return menuItemsLayout;

    }

    private Component buildBadgeWrapper(final Component menuItemButton, final Component badgeLabel) {
        CssLayout dashboardWrapper = new CssLayout(menuItemButton);
        dashboardWrapper.addStyleName("badgewrapper");
        dashboardWrapper.addStyleName(ValoTheme.MENU_ITEM);
//        badgeLabel.addStyleName(ValoTheme.MENU_BADGE);
//        badgeLabel.setWidthUndefined();
//        badgeLabel.setVisible(false);
//        dashboardWrapper.addComponent(badgeLabel);
        return dashboardWrapper;
    }

    @Override
    public void attach() {
        super.attach();
        updateNotificationsCount(null);
    }

    @Subscribe
    public void postViewChange(final PostViewChangeEvent event) {
        // After a successful view change the menu can be hidden in mobile view.
        getCompositionRoot().removeStyleName(STYLE_VISIBLE);
    }

    @Subscribe
    public void updateNotificationsCount(
            final NotificationsCountUpdatedEvent event) {
//        int unreadNotificationsCount = DashboardUI.getDataProvider().getUnreadNotificationsCount();
//        notificationsBadge.setValue(String.valueOf(unreadNotificationsCount));
//        notificationsBadge.setVisible(unreadNotificationsCount > 0);
    }

    @Subscribe
    public void updateReportsCount(final ReportsCountUpdatedEvent event) {
        reportsBadge.setValue(String.valueOf(event.getCount()));
        reportsBadge.setVisible(event.getCount() > 0);
    }

    @Subscribe
    public void updateUserName(final ProfileUpdatedEvent event) {
//        User user = getCurrentUser();
        Usuario user = getCurrentUser();
//        settingsItem.setText(user.getFirstName() + " " + user.getLastName());
        if (user != null) {
            settingsItem.setText(user.getNombre() + " " + user.getApellido());
        }
    }

    public final class ValoMenuItemButton extends Button {

        private static final String STYLE_SELECTED = "selected";
        private final DashboardViewType view;

        public ValoMenuItemButton(final DashboardViewType view) {
            this.view = view;
            setPrimaryStyleName("valo-menu-item");
            setIcon(view.getIcon());
//            setCaption(view.getViewName().substring(0, 1).toUpperCase() + view.getViewName().substring(1));
            setCaption(view.getViewTittle());
            DashboardEventBus.register(this);
            addClickListener(new ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                    UI.getCurrent().getNavigator().navigateTo(view.getViewName());
                }
            });
        }

        @Subscribe
        public void postViewChange(final PostViewChangeEvent event) {
            removeStyleName(STYLE_SELECTED);
            if (event.getView() == view) {
                addStyleName(STYLE_SELECTED);
            }
        }
    }
}
