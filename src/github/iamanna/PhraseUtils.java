package github.iamanna;

import java.text.BreakIterator;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class PhraseUtils {
    private static final BreakIterator biWord = BreakIterator.getWordInstance();

    public static void getWordListFromSentence (String sentence, List<String> wordList) {
        biWord.setText(sentence);
        int wordIndex = 0;
        while (biWord.next() != BreakIterator.DONE) {
            String word = sentence.substring(wordIndex, biWord.current());
            // Ignore any 'words' that are punctuation or white space
            if(Character.isLetterOrDigit(sentence.codePointAt(wordIndex))) {
                wordList.add(word);
            }
            wordIndex = biWord.current();
        }
    }

    public static LinkedHashMap<String, Integer> sortRepeatedPhrasesBySize(Map<String, Integer> phraseCountMap) {
        return phraseCountMap.entrySet()
                .stream()
                .filter(map -> map.getValue()>1)
                .sorted(Map.Entry.comparingByKey(Comparator.comparingInt(String::length)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

    }

    public static void removeAllSubstringPhrasesFromMap (Map<String, Integer> sortedPhraseMap) {
        String [] arrayOfPhrases = sortedPhraseMap.keySet().toArray(new String [0]);
        int startIndex = 0;
        while (startIndex < arrayOfPhrases.length) {
            boolean allTheSameLength = false;
            int endIndx = startIndex+1;
            int phraseLength = arrayOfPhrases[startIndex].length();

            while (endIndx < arrayOfPhrases.length) {
                // iterate until you find phrase that's longer than current phrase
                // if you're at the end of the list and haven't found a longer phrase to compare then you can stop looking for substrings
                if (phraseLength>=arrayOfPhrases[endIndx].length() && endIndx == arrayOfPhrases.length-1) {
                    allTheSameLength = true;
                    break;
                } else {
                    // if a substring is found, remove the smaller string from the map of phrases, and move on to next phrase
                    if (arrayOfPhrases[endIndx].contains(arrayOfPhrases[startIndex])) {
                        sortedPhraseMap.remove(arrayOfPhrases[startIndex]);
                        break;
                    }
                    endIndx++;
                }
            }
            if (allTheSameLength) {
                break;
            }
            startIndex++;
        }
    }

}
