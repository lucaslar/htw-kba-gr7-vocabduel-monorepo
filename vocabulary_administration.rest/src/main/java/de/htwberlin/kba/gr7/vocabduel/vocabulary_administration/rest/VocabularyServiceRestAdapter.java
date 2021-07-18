package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Controller
@Path("/vocabulary")
public class VocabularyServiceRestAdapter {

    private final VocabularyService VOCABULARY_SERVICE;

    @Inject
    public VocabularyServiceRestAdapter(VocabularyService vocabularyService) {
        VOCABULARY_SERVICE = vocabularyService;
    }

    @POST
    @Path("/import-list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response importGnuVocableList(final String gnuContent, final User user) {
        try {
            VOCABULARY_SERVICE.importGnuVocableList(gnuContent, user);
        } catch (IncompleteVocableListException | DataAlreadyExistsException |
                InvalidVocableListException | UnknownLanguagesException |
                DuplicateVocablesInSetException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        }
        System.out.println("Successfully imported GnuVocableList.");
        return Response
                .status(Response.Status.CREATED)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }


    @POST
    @Path("/delete-list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response deleteVocableList(final VocableList vocableList, final User user) {
        try {
            VOCABULARY_SERVICE.deleteVocableList(vocableList, user);
        } catch (DifferentAuthorException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        }
        System.out.println("Successfully deleted VocableList.");
        return Response.ok().build();
    }

    @GET
    @Path("/get-list")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getVocableListById(final long id) {
        VocableList list = VOCABULARY_SERVICE.getVocableListById(id);
        return Response.ok(list).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/get-lists")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getVocableListsByUser(final User user) {
        List<VocableList> lists = VOCABULARY_SERVICE.getVocableListsOfUser(user);
        return Response.ok(lists).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/get-lang-sets")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getAllLanguageSets() {
        List<LanguageSet> sets = VOCABULARY_SERVICE.getAllLanguageSets();
        return Response.ok(sets).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/get-languages")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getAllSupportedLanguages() {
        List<SupportedLanguage> languages = VOCABULARY_SERVICE.getAllSupportedLanguages();
        return Response.ok(languages).type(MediaType.TEXT_PLAIN).build();
    }

    @GET
    @Path("/get-language-references")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getSupportedLanguageReferences(final SupportedLanguage lang) {
        List<String> refs = VOCABULARY_SERVICE.getSupportedLanguageReferences(lang);
        return Response.ok(refs).type(MediaType.TEXT_PLAIN_TYPE).build();
    }
}
