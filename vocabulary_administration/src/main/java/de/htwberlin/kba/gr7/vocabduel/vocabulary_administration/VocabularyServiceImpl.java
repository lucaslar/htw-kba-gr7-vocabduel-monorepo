package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao.LanguageSetDAOImpl;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao.VocableListDAOImpl;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao.VocableUnitDAOImpl;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage.*;

@Service
public class VocabularyServiceImpl implements VocabularyService {

    private HashMap<String, SupportedLanguage> languageMapping;
    private HashMap<SupportedLanguage, String[]> reverseLanguageMapping;

    private final Pattern THREE_BRACKETS_PATTERN = Pattern.compile("\\{\\{\\{(.*?)}}}");
    private final Pattern ONE_BRACKET_PATTERN = Pattern.compile("\\{(.*?)}");

    private final VocableUnitDAOImpl vocableUnitDAO;
    private final VocableListDAOImpl vocableListDAO;
    private final LanguageSetDAOImpl languageSetDAO;

    VocabularyServiceImpl(final EntityManager entityManager) {
        initializeLangMapping();
        vocableUnitDAO = new VocableUnitDAOImpl(entityManager);
        vocableListDAO = new VocableListDAOImpl(entityManager);
        languageSetDAO = new LanguageSetDAOImpl(entityManager);
    }

    @Override
    public int importGnuVocableList(String gnuContent, User triggeringUser) throws DuplicateVocablesInSetException, IncompleteVocableListException, DataAlreadyExistsException, UnknownLanguagesException, InvalidVocableListException {
        final List<String> lines = Arrays.stream(gnuContent.split("\n")).collect(Collectors.toList());
        if (lines.size() < 2) throw new IncompleteVocableListException("Not enough lines, expected at least two");

        final List<String> headline = parseGnuHeadlineData(lines.get(0));
        if (headline.size() != 4 || headline.stream().anyMatch(e -> e == null || e.isEmpty())) {
            throw new IncompleteVocableListException("Expected four non-empty/null elements in headline");
        }

        lines.remove(0);
        final SupportedLanguage langFrom = mapLanguageReference(headline.get(1));
        final SupportedLanguage langTo = mapLanguageReference(headline.get(2));
        final String listName = headline.get(0);

        final List<Vocable> vocables = parseGnuVocableData(lines);
        validateVocables(vocables);

        final VocableList list = new VocableList();
        list.setTitle(listName);
        list.setAuthor(triggeringUser);
        list.setVocables(parseGnuVocableData(lines));
        list.setTimestamp(new Date());

        final VocableUnit unit = getOrCreateLanguageUnit(headline.get(3), langFrom, langTo);
        if (unit.getVocableLists() == null) unit.setVocableLists(new ArrayList<>());
        else if (unit.getVocableLists().stream().anyMatch(vl -> vl.getTitle().equals(listName))) {
            throw new DataAlreadyExistsException("List named \"" + listName + "\" does already exist!");
        }

        unit.getVocableLists().add(list);

        vocableUnitDAO.insertVocableUnit(unit);

        return 0;
    }

    @Override
    public int deleteVocableList(VocableList vocables, User triggeringUser) throws DifferentAuthorException, PersistenceException {
        final User author = vocables.getAuthor();
        if (author != null && !author.getId().equals(triggeringUser.getId())) {
            throw new DifferentAuthorException("You are not authorized to remove lists imported by " + author + "!");
        }
        final VocableUnit unit = vocableUnitDAO.selectVocableUnitByVocableList(vocables);
        unit.setVocableLists(unit.getVocableLists().stream().filter(l -> !l.getId().equals(vocables.getId())).collect(Collectors.toList()));

        final VocableList list = vocableListDAO.selectVocableList(vocables);
        vocableListDAO.deleteVocableList(list);
        if (unit.getVocableLists().isEmpty()) {
            final LanguageSet languageSet = languageSetDAO.selectLanguageSetByVocableUnit(unit);
            languageSet.setVocableUnits(languageSet.getVocableUnits().stream().filter(u -> !u.getId().equals(unit.getId())).collect(Collectors.toList()));
            vocableUnitDAO.deleteVocableUnit(unit);
            if (languageSet.getVocableUnits().isEmpty()) languageSetDAO.deleteLanguageSet(languageSet);
        }
        return 0;
    }

    @Override
    public VocableList getVocableListById(Long id) {
        return vocableListDAO.selectVocableListById(id);
    }

    @Override
    public List<VocableList> getVocableListsOfUser(User user) {
        return vocableListDAO.selectVocableListsByUserId(user.getId());
    }

    @Override
    public List<LanguageSet> getAllLanguageSets() {
        return languageSetDAO.selectLanguageSets();
    }

    @Override
    public List<SupportedLanguage> getAllSupportedLanguages() {
        return new ArrayList<>(new HashSet<>(languageMapping.values()));
    }

    @Override
    public List<String> getSupportedLanguageReferences(SupportedLanguage language) {
        return Arrays.stream(reverseLanguageMapping.get(language)).collect(Collectors.toList());
    }

    private void initializeLangMapping() {
        reverseLanguageMapping = new HashMap<>();
        reverseLanguageMapping.put(AR, new String[]{"ar", "arabic", "arabisch", "العربية"});
        reverseLanguageMapping.put(CMN, new String[]{"cmn", "chinese", "chinesisch", "中国人"});
        reverseLanguageMapping.put(DE, new String[]{"de", "german", "deutsch"});
        reverseLanguageMapping.put(EN, new String[]{"en", "english", "englisch"});
        reverseLanguageMapping.put(ES, new String[]{"es", "spanish", "spanisch", "español"});
        reverseLanguageMapping.put(FR, new String[]{"fr", "french", "französisch", "français"});
        reverseLanguageMapping.put(HI, new String[]{"hi", "hindi", "हिंदी"});
        reverseLanguageMapping.put(IT, new String[]{"it", "italian", "italienisch", "italiano"});
        reverseLanguageMapping.put(JA, new String[]{"ja", "japanese", "japanisch", "日本語"});
        reverseLanguageMapping.put(KO, new String[]{"ko", "korean", "koreanisch", "한국어"});
        reverseLanguageMapping.put(PT, new String[]{"pt", "portuguese", "portugiesisch", "portugês"});
        reverseLanguageMapping.put(RU, new String[]{"ru", "russian", "russisch", "русский"});

        languageMapping = new HashMap<>();
        reverseLanguageMapping.keySet().forEach(key -> {
            for (final String l : reverseLanguageMapping.get(key)) languageMapping.put(l, key);
        });
    }

    private List<String> parseGnuHeadlineData(final String line) {
        final Matcher hlMatcher = THREE_BRACKETS_PATTERN.matcher(line);
        ArrayList<String> hlData = new ArrayList<>();
        while (hlMatcher.find()) hlData.add(hlMatcher.group(1).trim());
        return hlData;
    }

    private List<Vocable> parseGnuVocableData(final List<String> lines) throws InvalidVocableListException {
        final List<String[]> splitted = lines.stream()
                .map(l -> (l.charAt(0) == '#' ? l.substring(1) : l).split(":"))
                .collect(Collectors.toList());

        if (splitted.stream().anyMatch(s -> s.length != 2)) {
            throw new InvalidVocableListException("Invalid vocable format (must be split by colon character)");
        }

        List<Vocable> vocables = new ArrayList<>();
        for (final String[] vocAndTranslations : splitted) {
            final TranslationGroup vocable = getVocableFromString(vocAndTranslations[0]);
            final List<TranslationGroup> translations = getTranslationVocableTranslationsFromString(vocAndTranslations[1]);
            vocables.add(new Vocable(vocable, translations));
        }
        return vocables;
    }

    private TranslationGroup getVocableFromString(final String str) throws InvalidVocableListException {
        final Matcher vocMatcher = ONE_BRACKET_PATTERN.matcher(str);
        final List<String> vocData = new ArrayList<>();
        while (vocMatcher.find()) vocData.add(vocMatcher.group(1).trim());
        if (vocData.size() == 0) {
            throw new InvalidVocableListException("Invalid vocable data in learnt language (no match for " + ONE_BRACKET_PATTERN + "):\n" + str);
        }

        final TranslationGroup tg = new TranslationGroup();
        tg.setSynonyms(asSynonymList(vocData.get(0)));
        vocData.remove(0);
        if (vocData.size() > 0) tg.setExemplarySentencesOrAdditionalInfo(vocData);
        return tg;
    }

    private List<TranslationGroup> getTranslationVocableTranslationsFromString(final String str) throws InvalidVocableListException {
        final Matcher vocMatcher = ONE_BRACKET_PATTERN.matcher(str);

        final List<List<String>> vocData = new ArrayList<>();
        final Map<List<String>, List<String>> explanations = new HashMap<>();

        while (vocMatcher.find()) {
            if (vocMatcher.group(0).matches("\\{\\{\\{(.*?)}")) {
                final String exp = vocMatcher.group(1).substring(2).trim();
                if (!exp.isEmpty()) explanations.put(vocData.get(vocData.size() - 1), asSynonymList(exp));
            } else vocData.add(asSynonymList(vocMatcher.group(1).trim()));
        }

        if (vocData.size() == 0) {
            throw new InvalidVocableListException("Invalid vocable data in known language (no match for " + ONE_BRACKET_PATTERN + "):\n" + str);
        }

        return vocData.stream().map((synonyms) -> {
            final TranslationGroup tg = new TranslationGroup(synonyms);
            if (explanations.get(synonyms) != null) {
                tg.setExemplarySentencesOrAdditionalInfo(explanations.get(synonyms));
            }
            return tg;
        }).collect(Collectors.toList());
    }

    private List<String> asSynonymList(final String gnuVocable) {
        return Arrays.stream(gnuVocable.split("(?<!\\\\),\\s"))
                .map(v -> v.replace("\\,", ",").replace("\\;", ";").trim())
                .collect(Collectors.toList());
    }

    private SupportedLanguage mapLanguageReference(final String reference) throws UnknownLanguagesException {
        final SupportedLanguage lang = languageMapping.get(reference.toLowerCase());
        if (lang == null) throw new UnknownLanguagesException("Unknown or wrongly referred language: " + reference);
        else return lang;
    }

    private VocableUnit getOrCreateLanguageUnit(final String unitName, final SupportedLanguage from, final SupportedLanguage to) {
        final LanguageSet ls = getOrCreateLanguageSet(from, to);
        VocableUnit unit;
        Optional<VocableUnit> foundUnit = Optional.empty();
        if (ls.getVocableUnits() == null) {
            ls.setVocableUnits(new ArrayList<>());
        } else foundUnit = ls.getVocableUnits().stream().filter(u -> u.getTitle().equals(unitName)).findFirst();

        if (foundUnit.isPresent()) unit = foundUnit.get();
        else {
            unit = new VocableUnit(unitName);
            ls.getVocableUnits().add(unit);
            languageSetDAO.insertLanguageSet(ls);
        }

        return unit;
    }

    private LanguageSet getOrCreateLanguageSet(final SupportedLanguage from, final SupportedLanguage to) {
        return languageSetDAO.selectOrInsertLanguageSetBySupportedLanguages(from, to);
    }

    private void validateVocables(final List<Vocable> vocables) throws DuplicateVocablesInSetException {
        final List<String> mappedVocables = vocables.stream().map(vocable -> vocable.getVocable().getSynonyms().get(0)).collect(Collectors.toList());
        final int unique = new ArrayList<>(new HashSet<>(mappedVocables)).size();
        if (unique != vocables.size()) {
            final HashSet<String> stringSet = new HashSet<>();
            final HashSet<String> duplicates = new HashSet<>();
            for (String element : mappedVocables) if (!stringSet.add(element)) duplicates.add(element);
            throw new DuplicateVocablesInSetException("The given list contains duplicate values: " + new ArrayList<>(duplicates));
        }
    }
}
