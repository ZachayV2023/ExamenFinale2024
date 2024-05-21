package org.kickmyb.server.task;


import java.util.List;

public interface ServiceTask {

    // entity handling
    void attente();
    int calcul(int nombre) throws NotMultiple;
}