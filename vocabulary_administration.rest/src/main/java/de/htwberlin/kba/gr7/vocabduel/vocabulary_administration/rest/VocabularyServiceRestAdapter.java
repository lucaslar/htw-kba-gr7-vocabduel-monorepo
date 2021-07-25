package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.shared_logic.rest.AuthInterceptor;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.UserOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import org.springframework.stereotype.Controller;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Controller
@Path("/vocabulary")
public class VocabularyServiceRestAdapter {

    private final VocabularyService VOCABULARY_SERVICE;
    private final UserService USER_SERVICE;

    @Inject
    public VocabularyServiceRestAdapter(final VocabularyService vocabularyService, final UserService userService) {
        VOCABULARY_SERVICE = vocabularyService;
        USER_SERVICE = userService;
    }

    @GET
    @PermitAll
    @Path("/list/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getVocableListById(@PathParam("id") final long id) {
        try {
            final VocableList list = VOCABULARY_SERVICE.getVocableListById(id);
            return list == null
                    ? Response.status(Response.Status.NOT_FOUND).entity("No list found for the given ID.").type(MediaType.TEXT_PLAIN).build()
                    : Response.ok(list).type(MediaType.APPLICATION_JSON).build();
        } catch (VocabularyOptimisticLockException e){
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (RuntimeException e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @GET
    @PermitAll
    @Path("/lists-of-author/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getVocableListsByUser(@PathParam("id") final long id) {
        try {
            final User user = USER_SERVICE.getUserDataById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("No user found for the given ID.").type(MediaType.TEXT_PLAIN).build();
            }
            final List<VocableList> lists = VOCABULARY_SERVICE.getVocableListsOfUser(user);
            return Response.ok(lists).type(MediaType.APPLICATION_JSON).build();
        } catch (UserOptimisticLockException | VocabularyOptimisticLockException e) {
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (RuntimeException e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/language-sets")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLanguageSets() {
        try {
            final List<LanguageSet> sets = VOCABULARY_SERVICE.getAllLanguageSets();
            return Response.ok(sets).type(MediaType.APPLICATION_JSON).build();
        } catch (VocabularyOptimisticLockException e){
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (RuntimeException e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/language-references/{lang}")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupportedLanguageReferences(@PathParam("lang") final SupportedLanguage lang) {
        final List<String> refs = VOCABULARY_SERVICE.getSupportedLanguageReferences(lang);
        return Response.ok(refs).build();
    }

    @GET
    @Path("/supported-languages")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSupportedLanguages() {
        List<SupportedLanguage> languages = VOCABULARY_SERVICE.getAllSupportedLanguages();
        return Response.ok(languages).build();
    }

    @POST
    @Path("/import-gnu")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public Response importGnuVocableList(@HeaderParam(AuthInterceptor.USER_HEADER) final Long userId, final String gnuContent) {
        VocableList imported;
        try {
            final User user = USER_SERVICE.getUserDataById(userId);
            imported = VOCABULARY_SERVICE.importGnuVocableList(gnuContent, user);
        } catch (IncompleteVocableListException | DataAlreadyExistsException | InvalidVocableListException | DuplicateVocablesInSetException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        } catch (UnknownLanguagesException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        } catch (UserOptimisticLockException | VocabularyOptimisticLockException e) {
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (RuntimeException e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
        System.out.println("Successfully imported GNU Vocable list.");
        return Response.status(Response.Status.CREATED).entity(imported).type(MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Path("/delete-list/{listId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteVocableList(@HeaderParam(AuthInterceptor.USER_HEADER) final Long userId, @PathParam("listId") final Long listId) {
        VocableList list = null;
        try {
            list = VOCABULARY_SERVICE.getVocableListById(listId);
            if (list == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("No list found for the given ID.").build();
            }
            VOCABULARY_SERVICE.deleteVocableList(list, USER_SERVICE.getUserDataById(userId));
        } catch (DifferentAuthorException | UndeletableListException e) {
            e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (UserOptimisticLockException | VocabularyOptimisticLockException e) {
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (RuntimeException e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
        System.out.println("Successfully deleted vocable list with ID " + list.getId());
        return Response.noContent().build();
    }
}
