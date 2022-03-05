package org.acme.hibernate.orm.panache;

import java.util.List;

import org.acme.hibernate.orm.panache.repository.Correo;

public class Procesar extends Thread {
    private long initialTime;

    private List<Correo> correo;

    private String nombreThread;

    public Procesar(String string, List<Correo> listaCorreos, String thread, long initialTime2) {
        this.correo = listaCorreos;
        this.nombreThread = thread;
        String inicial = " Thread " + this.nombreThread  + ":" + (System.currentTimeMillis() - this.initialTime) / 1000    + "seg" + "\n";
        System.out.println(inicial);
    }

    @Override
    public void run() {

        try {

            for (Correo detail : correo) {                
               String x = "Procesado el correo " + " del cliente " + detail.name + "Thread " + this.nombreThread + " ->Tiempo: "
                + (System.currentTimeMillis() - this.initialTime) / 1000
                + "seg";
                System.out.println(x);
                Thread.sleep(1);
            }

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

    }
}
