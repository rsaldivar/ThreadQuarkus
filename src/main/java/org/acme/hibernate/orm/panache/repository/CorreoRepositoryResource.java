package org.acme.hibernate.orm.panache.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.panache.common.Sort;
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
import java.util.List;

@Path("repository/correos")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class CorreoRepositoryResource {

    @Inject
    CorreoRepository CorreoRepository;

    private static final Logger LOGGER = Logger.getLogger(CorreoRepositoryResource.class.getName());

    @GET
    public List<Correo> get() {
        return CorreoRepository.listAll(Sort.by("id"));
    }

    @GET
    @Path("{id}")
    public Correo getSingle(@PathParam Long id) {
        Correo entity = CorreoRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Correo with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(Correo Correo) {
        if (Correo.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        CorreoRepository.persist(Correo);
        return Response.ok(Correo).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Correo update(@PathParam Long id, Correo Correo) {
        if (Correo.name == null) {
            throw new WebApplicationException("Correo Name was not set on request.", 422);
        }

        Correo entity = CorreoRepository.findById(id);

        if (entity == null) {
            throw new WebApplicationException("Correo with id of " + id + " does not exist.", 404);
        }

        entity.name = Correo.name;

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam Long id) {
        Correo entity = CorreoRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Correo with id of " + id + " does not exist.", 404);
        }
        CorreoRepository.delete(entity);
        return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}
