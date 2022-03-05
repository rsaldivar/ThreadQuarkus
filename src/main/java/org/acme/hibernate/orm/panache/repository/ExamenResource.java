package org.acme.hibernate.orm.panache.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.panache.common.Sort;

import org.acme.hibernate.orm.panache.Procesar;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import java.util.ArrayList;
import java.util.List;

@Path("examen")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class ExamenResource {

    @Inject
    CorreoRepository CorreoRepository;

    private static final Logger LOGGER = Logger.getLogger(CorreoRepositoryResource.class.getName());

    @GET
    public String get() {
        List<Correo> listaCorreos = CorreoRepository.listAll(Sort.by("id"));

        long initialTime = System.currentTimeMillis();
        // Todo dividir la lista, en la cantidad de hilos que sean necesarios

        List<List<Correo>> listas = averageAssign(listaCorreos, 4);
        Procesar procesar1 = new Procesar(listas.get(0).toString(), listas.get(0), "1", initialTime);
        procesar1.run();
        Procesar procesar2 = new Procesar(listas.get(1).toString(), listas.get(1), "2", initialTime);
        procesar2.run();
        Procesar procesar3 = new Procesar(listas.get(2).toString(), listas.get(2), "3", initialTime);
        procesar3.run();
        Procesar procesar4 = new Procesar(listas.get(3).toString(), listas.get(3), "4", initialTime);
        procesar4.run();
        return "procesado en backegrond";
    }

    /**
     * Divide una lista en n listas, principalmente realizadas por desplazamiento
     * 
     * @param source
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remaider = source.size() % n; // (Calcule el resto primero)
        int number = source.size() / n; // entonces el cociente
        int offset = 0; // offset
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

}
