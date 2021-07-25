package de.htwberlin.kba.gr7.vocabduel.game_administration.rest.exceptions;

public class GameMappingRuntimeException extends RuntimeException{
    public GameMappingRuntimeException(Exception hidden){
        super("An Optimistic Lock Exception occurred in Game Module while mapping VocableLists. Please try again.");
        System.err.println("The following exception is only logged here but will not be made public:");
        hidden.printStackTrace();
    }
}
