package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage.*;

@Service
public class VocabularyServiceImpl implements VocabularyService {

    private HashMap<String, SupportedLanguage> languageMapping;

    private final Pattern THREE_BRACKETS_PATTERN = Pattern.compile("\\{\\{\\{(.*?)}}}");
    private final Pattern ONE_BRACKET_PATTERN = Pattern.compile("\\{(.*?)}");

    // TODO: Mainly for mocking purposes => Keep list in real implementation?
    private List<LanguageSet> allLanguageSets;

    private UserService userService;

    VocabularyServiceImpl() {
        initializeLangMapping();
    }

    @Override
    public int importGnuVocableList(String gnuContent, User triggeringUser) throws DuplicateVocablesInSetException, IncompleteVocableListException, DataAlreadyExistsException {
        final List<String> lines = Arrays.stream(gnuContent.split("\n")).collect(Collectors.toList());
        final List<String> headline = parseGnuHeadlineData(lines.get(0));
        lines.remove(0);

        // TODO: Necessary to collect parent objects at this point? If so => return objects using headline data (prev. called fn)
//        final SupportedLanguage langFrom = languageMapping.get(headline.get(1).toLowerCase());
//        final SupportedLanguage langTo = languageMapping.get(headline.get(2));
//        final String unitName = headline.get(3);
        final String listName = headline.get(0);

        final VocableList list = new VocableList();
        list.setTitle(listName);
        list.setAuthor(triggeringUser);
        list.setVocables(parseGnuVocableData(lines));
        list.setTimestamp(new Date());
        // TODO insert / check errors ...
        return 0;
    }

    @Override
    public int deleteEmptyVocableUnit(VocableUnit unit) throws NotEmptyException {
        return 0;
    }

    @Override
    public int deleteVocableList(VocableList vocables, User triggeringUser) throws DifferentAuthorException {
        return 0;
    }

    @Override
    public VocableList getVocableListById(Long id) {
        return null;
    }

    @Override
    public List<VocableList> getVocableListsOfUser(User user) {
        return null;
    }

    @Override
    public List<LanguageSet> getAllLanguageSets() {
        return allLanguageSets;
    }

    @Override
    public List<SupportedLanguage> getAllSupportedLanguages() {
        return new ArrayList<>(new HashSet<>(languageMapping.values()));
    }

    private void initializeLangMapping() {
        languageMapping = new HashMap<>();
        for (final String l : new String[]{"ar", "arabic", "arabisch", "العربية"}) languageMapping.put(l, AR);
        for (final String l : new String[]{"cmn", "chinese", "chinesisch", "中国人"}) languageMapping.put(l, CMN);
        for (final String l : new String[]{"de", "german", "deutsch"}) languageMapping.put(l, DE);
        for (final String l : new String[]{"en", "english", "englisch"}) languageMapping.put(l, EN);
        for (final String l : new String[]{"es", "spanish", "spanisch", "español"}) languageMapping.put(l, ES);
        for (final String l : new String[]{"fr", "french", "französisch", "français"}) languageMapping.put(l, FR);
        for (final String l : new String[]{"hi", "hindi", "हिंदी"}) languageMapping.put(l, HI);
        for (final String l : new String[]{"it", "italian", "italienisch", "italiano"}) languageMapping.put(l, IT);
        for (final String l : new String[]{"ja", "japanese", "japanisch", "日本語"}) languageMapping.put(l, JA);
        for (final String l : new String[]{"ko", "korean", "koreanisch", "한국어"}) languageMapping.put(l, KO);
        for (final String l : new String[]{"pt", "portuguese", "portugiesisch", "portugês"}) languageMapping.put(l, PT);
        for (final String l : new String[]{"ru", "russian", "russisch", "русский"}) languageMapping.put(l, RU);
    }

    private List<String> parseGnuHeadlineData(final String line) {
        final Matcher hlMatcher = THREE_BRACKETS_PATTERN.matcher(line);
        ArrayList<String> hlData = new ArrayList<>();
        while (hlMatcher.find()) hlData.add(hlMatcher.group(1).trim());
        return hlData;
    }

    private List<Vocable> parseGnuVocableData(final List<String> lines) {
        return lines.stream()
                .map(l -> l.charAt(0) == '#' ? l.substring(1) : l)
                .map(l -> {
                    final String[] vocAndTranslations = l.split(":");
                    final TranslationGroup vocable = getVocableFromString(vocAndTranslations[0]);
                    final List<TranslationGroup> translations = getTranslationVocableTranslationsFromString(vocAndTranslations[1]);
                    return new Vocable(vocable, translations);
                })
                .collect(Collectors.toList());
    }

    private TranslationGroup getVocableFromString(final String str) {
        final Matcher vocMatcher = ONE_BRACKET_PATTERN.matcher(str);
        final List<String> vocData = new ArrayList<>();
        while (vocMatcher.find()) vocData.add(vocMatcher.group(1).trim());

        final TranslationGroup tg = new TranslationGroup();
        tg.setSynonyms(asSynonymList(vocData.get(0)));
        vocData.remove(0);
        if (vocData.size() > 0) tg.setExemplarySentencesOrAdditionalInfo(vocData);
        return tg;
    }

    private List<TranslationGroup> getTranslationVocableTranslationsFromString(final String str) {
        final Matcher vocMatcher = ONE_BRACKET_PATTERN.matcher(str);
        final List<List<String>> vocData = new ArrayList<>();
        final Map<List<String>, List<String>> explanations = new HashMap<>();

        while (vocMatcher.find()) {
            if (vocMatcher.group(0).matches("\\{\\{\\{(.*?)}")) {
                final String exp = vocMatcher.group(1).substring(2).trim();
                if (!exp.isEmpty()) explanations.put(vocData.get(vocData.size() - 1), asSynonymList(exp));
            } else vocData.add(asSynonymList(vocMatcher.group(1).trim()));
        }

        return vocData.stream().map((synonyms) -> {
            final TranslationGroup tg = new TranslationGroup(synonyms);
            if (explanations.get(synonyms) != null) tg.setExemplarySentencesOrAdditionalInfo(explanations.get(synonyms));
            return tg;
        }).collect(Collectors.toList());
    }

    private List<String> asSynonymList(final String gnuVocable) {
        return Arrays.stream(gnuVocable.split("(?<!\\\\),\\s"))
                .map(v -> v.replace("\\\\,", ",").replace("\\;", ";").trim())
                .collect(Collectors.toList());
    }
}
