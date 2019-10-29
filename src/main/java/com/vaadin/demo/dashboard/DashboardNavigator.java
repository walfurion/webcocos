package com.vaadin.demo.dashboard;

import com.fundamental.model.Acceso;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.services.Dao;
import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

import com.vaadin.demo.dashboard.event.DashboardEvent.BrowserResizeEvent;
import com.vaadin.demo.dashboard.event.DashboardEvent.CloseOpenWindowsEvent;
import com.vaadin.demo.dashboard.event.DashboardEvent.PostViewChangeEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.DashboardViewType;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;

@SuppressWarnings("serial")
public class DashboardNavigator extends Navigator {

    // Provide a Google Analytics tracker id here
    private static final String TRACKER_ID = null;// "UA-658457-6";
    private GoogleAnalyticsTracker tracker;

    private static final DashboardViewType ERROR_VIEW = DashboardViewType.HOME;
    private ViewProvider errorViewProvider;

    public DashboardNavigator(final ComponentContainer container) {
        super(UI.getCurrent(), container);

        String host = getUI().getPage().getLocation().getHost();
        if (TRACKER_ID != null && host.endsWith("demo.vaadin.com")) {
            initGATracker(TRACKER_ID);
        }
        initViewChangeListener();
        initViewProviders();

    }

    private void initGATracker(final String trackerId) {
        tracker = new GoogleAnalyticsTracker(trackerId, "demo.vaadin.com");

        // GoogleAnalyticsTracker is an extension add-on for UI so it is
        // initialized by calling .extend(UI)
        tracker.extend(UI.getCurrent());
    }

    private void initViewChangeListener() {
        addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                System.out.println("beforeViewChange ");
                //TODO: Aplicar segurida aca para accesos NO autorizados.
                //Para evitar accesos NO autorizados via URL.
                boolean exists = false;
                 Dao servicio = new Dao();
                if(servicio.getAccess(event.getViewName()).isVer()){
                    exists = true;
                }
                servicio.closeConnections();
//                Usuario user = ((Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName()));
//                for (Acceso a : user.getRoles().get(0).getAccesos()) {
//                    if (a.getRecursoInterno().trim().toUpperCase().equals(event.getViewName())) {
//                        System.out.println(event.getViewName() + " - " + a.getTitulo() + " ACCIONES  " + a.isVer() + " " + a.isCambiar() + " - " + a.isAgregar());
//                        exists = true;
//                        break;
//                    }
//                }
                
//                Dao servicio = new Dao();
//                List<Acceso> misAccessos = servicio.getAccesosByUsuarioid(user.getUsuarioId(), user.isSysadmin());
//                servicio.closeConnections();
//                List<Acceso> misAccessos2 = user.getRoles().get(0).getAccesos();
//                boolean exists = false;
//                 for (Acceso accPadre : misAccessos2) {    //estos son padres
//                     System.out.println("accPadre "+accPadre.getNombrePadre()+" - "+accPadre.getDescripcion());
//                    for (Acceso acc : accPadre.getAccesos()) {
//                        System.out.println("2 "+acc.getRecursoInterno()+" - "+event.getViewName());
//                        if (acc.getRecursoInterno().trim().toUpperCase().equals(event.getViewName())) {
//                            System.out.println(event.getViewName()+" -  "+acc.isVer()+" "+acc.isCambiar()+" - "+acc.isAgregar());
//                            exists = true;
//                            break;
//                        }
//                    }
//                }
////                List<Acceso> misAccessos = user.getRoles().get(0).getAccesos();
//                for (Acceso accPadre : misAccessos) {    //estos son padres
//                    for (Acceso acc : accPadre.getAccesos()) {
//                        System.out.println("acc.getRecursoInterno() "+acc.getRecursoInterno());
//                        if (acc.getRecursoInterno().trim().toUpperCase().equals(event.getViewName())) {
//                            System.out.println(event.getViewName()+" - "+acc.getTitulo()+" ACCIONES  "+acc.isVer()+" "+acc.isCambiar()+" - "+acc.isAgregar());
//                            exists = true;
//                            break;
//                        }
//                    }
//                }
                    System.out.println(event.getViewName() + " event.getViewName().equals(DashboardViewType.HOME.getViewName() " + DashboardViewType.HOME.getViewName());
                    if (!exists && !(event.getViewName().equals(DashboardViewType.HOME.getViewName()))) {
                        System.out.println(event.getViewName() + " event.getViewName().equals(DashboardViewType.HOME.getViewName() " + DashboardViewType.HOME.getViewName());
                        return false;
                    }

                    // Since there's no conditions in switching between the views
                    // we can always return true.
                    return true;
                }

                @Override
                public void afterViewChange
                (final ViewChangeEvent event
                
                    ) {
                System.out.println("afterViewChange");
                    DashboardViewType view = DashboardViewType.getByViewName(event.getViewName());
                    // Appropriate events get fired after the view is changed.
                    DashboardEventBus.post(new PostViewChangeEvent(view));
                    DashboardEventBus.post(new BrowserResizeEvent());
                    DashboardEventBus.post(new CloseOpenWindowsEvent());

                    if (tracker != null) {
                        // The view change is submitted as a pageview for GA tracker
                        tracker.trackPageview("/dashboard/" + event.getViewName());
                    }
                }
            }
        );
    }

    private void initViewProviders() {
        // A dedicated view provider is added for each separate view type
        for (final DashboardViewType viewType : DashboardViewType.values()) {
            ViewProvider viewProvider = new ClassBasedViewProvider(
                    viewType.getViewName(), viewType.getViewClass()) {

                // This field caches an already initialized view instance if the
                // view should be cached (stateful views).
                private View cachedInstance;

                @Override
                public View getView(final String viewName) {
                    System.out.println("getView");
                    View result = null;
                    if (viewType.getViewName().equals(viewName)) {
                        if (viewType.isStateful()) {
                            // Stateful views get lazily instantiated
                            if (cachedInstance == null) {
                                cachedInstance = super.getView(viewType
                                        .getViewName());
                            }
                            result = cachedInstance;
                        } else {
                            // Non-stateful views get instantiated every time
                            // they're navigated to
                            result = super.getView(viewType.getViewName());
                        }
                    }
                    return result;
                }
            };

            if (viewType == ERROR_VIEW) {
                errorViewProvider = viewProvider;
            }

            addProvider(viewProvider);
        }

        setErrorProvider(new ViewProvider() {
            @Override
            public String getViewName(final String viewAndParameters) {
                return ERROR_VIEW.getViewName();
            }

            @Override
            public View getView(final String viewName) {
                return errorViewProvider.getView(ERROR_VIEW.getViewName());
            }
        });
    }
}
