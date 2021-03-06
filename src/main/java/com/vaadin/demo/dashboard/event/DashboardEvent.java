package com.vaadin.demo.dashboard.event;

import com.sisintegrados.generic.bean.Usuario;
import java.util.Collection;


import com.vaadin.demo.dashboard.view.DashboardViewType;

/*
 * Event bus events used in Dashboard are listed here as inner classes.
 */
public abstract class DashboardEvent {

    public static final class UserLoginRequestedEvent {
//        private final String userName, password;
//        public UserLoginRequestedEvent(final String userName,
//                final String password) {
//            this.userName = userName;
//            this.password = password;
//        }

//        private final Integer station;
//        private final String nombre, apellido;
        private final Usuario usuario;
        public UserLoginRequestedEvent(Usuario usuario
//                final String userName, final String password
//                , final Integer station, final String nombre, final String apellido
        ) {
//            this.userName = userName;
//            this.password = password;
//            this.station = station;
//            this.nombre = nombre;
//            this.apellido = apellido;
this.usuario = usuario;
        }
//        public Integer getStation() {
//            return station;
//        }
//
//        public String getNombre() {
//            return nombre;
//        }
//
//        public String getApellido() {
//            return apellido;
//        }

        public Usuario getUsuario() {
            return usuario;
        }
        
        
        
        
        
        

//        public String getUserName() {
//            return userName;
//        }
//
//        public String getPassword() {
//            return password;
//        }
    }

    public static class BrowserResizeEvent {

    }

    public static class UserLoggedOutEvent {

    }

    public static class NotificationsCountUpdatedEvent {
    }

    public static final class ReportsCountUpdatedEvent {
        private final int count;

        public ReportsCountUpdatedEvent(final int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

    }

    public static final class TransactionReportEvent {
//        private final Collection<Transaction> transactions;

//        public TransactionReportEvent(final Collection<Transaction> transactions) {
//            this.transactions = transactions;
//        }

//        public Collection<Transaction> getTransactions() {
//            return transactions;
//        }
    }

    public static final class PostViewChangeEvent {
        private final DashboardViewType view;

        public PostViewChangeEvent(final DashboardViewType view) {
            this.view = view;
        }

        public DashboardViewType getView() {
            return view;
        }
    }

    public static class CloseOpenWindowsEvent {
    }

    public static class ProfileUpdatedEvent {
    }

}
