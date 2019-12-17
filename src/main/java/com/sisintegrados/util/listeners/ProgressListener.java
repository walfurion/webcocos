/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.util.listeners;

/**
 *
 * @author Allan G.
 */
public interface ProgressListener<T> {

    /**
     * Count and max can be used to indicate the progress towards completion. If
     * the max number of steps is unknown max should be set to -1.
     *
     * @param count the count towards the max value
     * @param max the max value. When count = max the job is complete.
     * @param status a status message to display in the progress bar.
     */
    void progress(int count, int max, T status);

    /**
     * Called when the job is complete.
     *
     * @param sent umh some value?
     */
    void complete(int sent);

    /**
     * Used to flag that an error occurred during a transmission.
     *
     * @param e the exception that was thrown
     * @param status a more detailed status message.
     */
    void itemError(Exception e, T status);

    /**
     * An unrecoverable error occurred during transmission.
     *
     * @param e the exception that was throw that stopped the job
     */
    void exception(Exception e);

}
