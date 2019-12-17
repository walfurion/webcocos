/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.utils;


import com.sisintegrados.util.listeners.CancelListener;
import com.sisintegrados.util.listeners.CompleteListener;
import com.sisintegrados.util.listeners.ProgressListener;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 *
 * @author Allan G.
 */
public class WorkingDialog extends Window implements ProgressListener<String> {

    private static final long serialVersionUID = 1L;
    private Label messageLabel;
    private ProgressBar progress;
    private VerticalLayout content;
    private Button cancel;
    private CancelListener cancelListener;
    private CompleteListener completeListener;
    private VerticalLayout layout;
    public boolean isFinished = false;
    public String wdname;

    final UI ui;

    /**
     * Displays a dialog designed to be shown when a long running task is in
     * progress.
     *
     * @param caption
     * @param message
     */
    public WorkingDialog(String caption, String message) {
        this(caption, message, null, "200px", "150px");
    }

    /**
     * Displays a dialog designed to be shown when a long running task is in
     * progress.
     *
     * @param caption
     * @param message
     * @param width
     * @param height
     */
    public WorkingDialog(String caption, String message, String width, String height) {
        this(caption, message, null, width, height);
    }

    /**
     * Display the Working Dialog with a Cancel Button. If the user clicks the
     * Cancel button the listener will be sent a cancel button. The setWorker
     * method does not support being cancelled.
     *
     * @param caption
     * @param message
     * @param listener
     */
    public WorkingDialog(String caption, String message, CancelListener listener, String width, String height) {
        super(caption);
        UI.getCurrent().setPollInterval(500);
        ui = UI.getCurrent();
        this.setModal(true);
        this.setClosable(false);
        this.setResizable(false);
        content = new VerticalLayout();
        this.setWidth(width);
        this.setHeight(height);
        content.setSizeFull();
        content.setMargin(true);
        content.setSpacing(true);

        this.cancelListener = listener;

        layout = new VerticalLayout();
        //layout.setSpacing(true);
        layout.setSizeFull();

        HorizontalLayout progressArea = new HorizontalLayout();
        progressArea.setSizeFull();

        //ProgressIndicator pi = new ProgressIndicator(100f);
        progress = new ProgressBar(0f);
        progressArea.addComponents(progress);
        //progressArea.addComponents(progress, pi);
        progressArea.setExpandRatio(progress, 1);
        progress.setSizeFull();
        //progress.setIndeterminate(true);
        progress.setImmediate(true);
        progress.setVisible(true);
        progressArea.setComponentAlignment(progress, Alignment.MIDDLE_CENTER);

        HorizontalLayout progressText = new HorizontalLayout();
        progressText.setSizeFull();

        messageLabel = new Label(message);
        messageLabel.setContentMode(ContentMode.HTML);
        messageLabel.setSizeFull();
        progressText.addComponent(messageLabel);
        //progressText.setExpandRatio(messageLabel, 1);

        layout.addComponent(progressText);
        content.addComponent(layout);
        content.addComponent(progressArea);

//        Resource res = new ThemeResource("../valo/shared/img/spinner.gif");
//        Image img = new Image(null, res);
//        content.addComponent(img);
//        content.setComponentAlignment(img, Alignment.MIDDLE_CENTER);

        /*if (listener != null)
        {
            cancel = new Button("Cancel");
            cancel.addClickListener(new ClickEventLogged.ClickListener()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public void clicked(ClickEvent event)
                {
                    WorkingDialog.this.cancelListener.cancel();
                    WorkingDialog.this.close();

                }
            });
            content.addComponent(cancel);
            content.setComponentAlignment(cancel, Alignment.BOTTOM_RIGHT);

        }*/
        this.setContent(content);
        this.center();

    }

    @Override
    public void close() {
        ui.accessSynchronously(new Runnable() {
            @Override
            public void run() {
                WorkingDialog.super.close();
            }
        });

    }

    /**
     * Pass a Runnable that WorkingDialog will run in a background thread. On
     * completion of the thread the complete listener will be notified and the
     * WorkingDialog will remove itself rom the UI.
     *
     * @param runnable the runnable to be run in a background thread.
     * @param listener a complete listener to be notified when the thread has
     * finished.
     */
    public void setWorker(Runnable runnable, CompleteListener listener, String wdname) {
        this.completeListener = listener;

        Thread worker = new Thread(new Worker(this, runnable),/*"WorkingDialog"*/ wdname);
        worker.start();
    }

    class Worker implements Runnable {

        private Runnable runnable;
        private WorkingDialog parent;

        Worker(WorkingDialog parent, Runnable runnable) {
            this.parent = parent;
            this.runnable = runnable;
        }

        @Override
        public void run() {
            try {
                this.runnable.run();
            } finally {
                ui.access(new Runnable() {
                    public void run() {
                        parent.complete(0);
                    }
                });
            }

        }

    }

    public void addUserComponent(final Component component) {
        ui.accessSynchronously(new Runnable() {
            @Override
            public void run() {
                layout.addComponent(component);
            }
        });
    }

    public void setProgressInderteminate(boolean inderterminate) {
        progress.setIndeterminate(inderterminate);
    }

    public void progress(int count, int max, final String message) {
        progress.setValue(((float) count) / ((float) max));
        messageLabel.setValue(message);

        ui.accessSynchronously(new Runnable() {
            @Override
            public void run() {
                progress.setValue(((float) count) / ((float) max));
                messageLabel.setValue("(" + count + "/" + max + ") " + message);
            }
        });
    }

    public void complete(int sent) {
        ui.accessSynchronously(new Runnable() {

            @Override
            public void run() {
                if (completeListener != null) {
                    completeListener.complete();
                }
                isFinished = true;
                WorkingDialog.this.close();
            }
        });

    }

    @Override
    public void itemError(Exception e, String status) {
        // Ignored.

    }

    @Override
    public void exception(Exception e) {
        ui.accessSynchronously(new Runnable() {

            @Override
            public void run() {
                if (completeListener != null) {
                    completeListener.complete();
                }
                WorkingDialog.super.close();
            }
        });

    }

    public void removeUserComponent(Component component) {
        layout.removeComponent(component);

    }

}
