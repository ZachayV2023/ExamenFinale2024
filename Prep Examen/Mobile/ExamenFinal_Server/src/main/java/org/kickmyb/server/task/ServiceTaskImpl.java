package org.kickmyb.server.task;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class ServiceTaskImpl implements ServiceTask {

    @Override
    public void attente() {
        try {
            Thread.sleep(5000);
        }catch (Exception e){

        }
    }

    @Override
    public int calcul(int nombre) throws NotMultiple {
        if (nombre % 10 == 0 ){
            return nombre / 10;
        }

        // Mettre texte dans body response html
        throw new NotMultiple("Pas un multiple de 10");

    }
}
