import github.iamanna.PhraseUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

public class PhraseUtilsTest {
    @Test
    public void testWordListToIgnorePunctuation() {
        ArrayList<String> expectedResult = new ArrayList<String>() {{
            add("this");
            add("is");
            add("pi");
            add("3.14159");
            add("day");
        }};
        ArrayList<String> result = new ArrayList<>();
        PhraseUtils.getWordListFromSentence("this is pi (3.14159) day!", result);
        assertLinesMatch(expectedResult, result);

    }

    @Test
    public void testSortRepeatedPhrasesBySize() {
        Map<String, Integer> initialMap = new HashMap<>();
        initialMap.put("woody wilderness without wonder", 2);
        initialMap.put("a man from Nantucket", 1);
        initialMap.put("once upon a time", 2);
        initialMap.put("once upon a", 4);
        initialMap.put("the path less", 1);
        initialMap.put("flaming flamingos flying", 5);
        initialMap.put("all the difference", 1);

        LinkedHashMap<String, Integer> expected = new LinkedHashMap<>();
        expected.put("once upon a", 4);
        expected.put("once upon a time", 2);
        expected.put("flaming flamingos flying", 5);
        expected.put("woody wilderness without wonder", 2);

        LinkedHashMap<String, Integer> result = PhraseUtils.sortRepeatedPhrasesBySize(initialMap);
        assertIterableEquals(new ArrayList<>(expected.entrySet()), new ArrayList<>(result.entrySet()));


    }

    @Test
    public void testRemoveSubstringPhrases() {

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        sortedMap.put("once upon a", 4);
        sortedMap.put("flamingos fly in", 3);
        sortedMap.put("three miles long", 4);
        sortedMap.put("upon a time there", 2);
        sortedMap.put("wilderness without wonder", 2);
        sortedMap.put("once upon a time there was", 2);
        sortedMap.put("in woody wilderness without", 2);
        sortedMap.put("flaming flamingos fly in without sight", 5);
        sortedMap.put("wandering in woody wilderness without wonder", 2);

        LinkedHashMap<String, Integer> expected = new LinkedHashMap<>();
        expected.put("three miles long", 4);
        expected.put("once upon a time there was", 2);
        expected.put("flaming flamingos fly in without sight", 5);
        expected.put("wandering in woody wilderness without wonder", 2);
        PhraseUtils.removeAllSubstringPhrasesFromMap(sortedMap);

        assertIterableEquals(new ArrayList<>(expected.entrySet()), new ArrayList<>(sortedMap.entrySet()));


    }
    @Test
    public void testRemoveSubstringPhrasesWithSameLength() {

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        sortedMap.put("this is the same size", 4);
        sortedMap.put("that is the same size", 3);
        sortedMap.put("flop is the same size", 4);
        sortedMap.put("slop is the same size", 2);
        sortedMap.put("chop is the same size", 2);
        sortedMap.put("flip is the same size", 2);
        sortedMap.put("trip is the same size", 2);
        sortedMap.put("ship is the same size", 5);
        sortedMap.put("shop is the same size", 2);

        LinkedHashMap<String, Integer> expected = new LinkedHashMap<>(sortedMap);
        PhraseUtils.removeAllSubstringPhrasesFromMap(sortedMap);

        assertIterableEquals(new ArrayList<>(expected.entrySet()), new ArrayList<>(sortedMap.entrySet()));
    }
}
