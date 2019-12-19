package com.vaadin.demo.dashboard;

import com.sisintegrados.generic.bean.Usuario;
import java.util.Locale;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.demo.dashboard.event.DashboardEvent.BrowserResizeEvent;
import com.vaadin.demo.dashboard.event.DashboardEvent.CloseOpenWindowsEvent;
import com.vaadin.demo.dashboard.event.DashboardEvent.UserLoggedOutEvent;
import com.vaadin.demo.dashboard.event.DashboardEvent.UserLoginRequestedEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.LoginView;
import com.vaadin.demo.dashboard.view.MainView;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.util.logging.Level;
import java.util.logging.LogManager;

@Theme("dashboard")
@Widgetset("com.vaadin.demo.dashboard.DashboardWidgetSet")
@Title("UNO Estaciones coco")
@SuppressWarnings("serial")
public final class DashboardUI extends UI {

    /*
     * This field stores an access to the dummy backend layer. In real
     * applications you most likely gain access to your beans trough lookup or
     * injection; and not in the UI but somewhere closer to where they're
     * actually accessed.
     */
//    private final DataProvider dataProvider = new DummyDataProvider();
    private final DashboardEventBus dashboardEventbus = new DashboardEventBus();
    
    @Override
    protected void init(final VaadinRequest request) {
        setLocale(Locale.US);
        LogManager.getLogManager().getLogger("com.vaadin").setLevel(Level.OFF);
        LogManager.getLogManager().getLogger("org.apache").setLevel(Level.OFF);
        LogManager.getLogManager().getLogger("org.apache").setLevel(Level.SEVERE);
        LogManager.getLogManager().getLogger("com.vaadin").setLevel(Level.SEVERE);
        LogManager.getLogManager().getLogger("com.vaadin").setLevel(Level.WARNING);

        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();

        // Some views need to be aware of browser resize events so a
        // BrowserResizeEvent gets fired to the event bus on every occasion.
        Page.getCurrent().addBrowserWindowResizeListener(
                new BrowserWindowResizeListener() {
                    @Override
                    public void browserWindowResized(
                            final BrowserWindowResizeEvent event) {
                        DashboardEventBus.post(new BrowserResizeEvent());
                    }
                });
    }

    /**
     * Updates the correct content for this UI based on the current user status.
     * If the user is logged in with appropriate privileges, main view is shown.
     * Otherwise login view is shown.
     */
    private void updateContent() {
//        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
//        if (user != null && "admin".equals(user.getRole())) {
//            // Authenticated user
//            setContent(new MainView());
//            removeStyleName("loginview");
//            getNavigator().navigateTo(getNavigator().getState());
//        } else {
//            setContent(new LoginView());
//            addStyleName("loginview");
//        }
        
        Usuario user = (Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName());
        if (user!=null) {
        setContent(new MainView());
        removeStyleName("loginview");
        getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    @Subscribe
    public void userLoginRequested(final UserLoginRequestedEvent event) {
//        User user = getDataProvider().authenticate(event.getUserName(), event.getPassword());

//        User user = new User();
//        user.setId(event.getStation());
//        user.setFirstName(event.getNombre());
//        user.setLastName(event.getApellido());
//        user.setRole("admin");
//        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);

        VaadinSession.getCurrent().setAttribute(Usuario.class.getName(), event.getUsuario());
        updateContent();
    }

    @Subscribe
    public void userLoggedOut(final UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Subscribe
    public void closeOpenWindows(final CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    /**
     * @return An instance for accessing the (dummy) services layer.
     */
//    public static DataProvider getDataProvider() {
//        return ((DashboardUI) getCurrent()).dataProvider;
//    }

    public static DashboardEventBus getDashboardEventbus() {
        return ((DashboardUI) getCurrent()).dashboardEventbus;
    }
}
