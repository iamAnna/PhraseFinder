# Phrase Finder & Counter

Given a string representing a document, write a function which returns the top 10 most frequent repeated phrases. 
* A phrase is a stretch of three to ten consecutive words 
    * cannot span sentences
    * 3 <= wordsInPhrase <= 10
* Omit a phrase if it is a subset of another, longer phrase, even if the shorter phrase occurs more frequently 
    * For example, if “cool and collected” and “calm cool and collected” are repeated, do not include “cool and collected” in the returned set. 
* Do not include any phrases in returned set that were only used once
    * phraseCount >= 2
    * If there are only 7 phrases that are used more than once, only return the set of 7.

Example input:

    The quick brown fox jumped over the lazy dog.
    The lazy dog, peeved to be labeled lazy, jumped over a snoring turtle.
    In retaliation the quick brown fox jumped over ten snoring turtles.
    Then the quick brown fox refueled with some ice cream.

Example output:

    ['the lazy dog', 'the quick brown fox jumped over']
    
    
#### My Initial Approach
1. Find all repeated phrases
    * Use HashMap with < String, Integer > key-value pairs (key = phrase, value = !count ? 1 : count++ )
1. Validate each phrase
    1. Word count within min / max? If no, remove
    1. Phrase count >= 2
    1. Is it a subset of another?  If yes, remove
    1. Does the phrase span more than one sentence?  If yes, remove
    
#### Potential Problems
* How to determine a sentence ending / sentence beginning?
    * REGEX for period (.), exclamation point (!), question mark (?)
        * Cons:
            1. Poor performance
            1. Delicate and easy to break
            1. Tons edge cases to consider (encoding type, see quote example below, etc)
            1. Difficult to read, debug, and maintain (see _b_ above)
    * However what if there is a block quote or a quote within a quote:
        quote within quote example: 

             When I was there the waiter said, "Well let me tell ya, my boss was givin' me the business and 
             hollering, 'Two dollar bills aren't real! Who in their right mind would think that?!?!', and 
             it took all my power not to bust up laughing."
    * Looking at the example above, what if "`think that?!?!', and it`" is a repeated phrase? It's technically part of the same sentence, but contains the sentence ending delimiters that were listed previously


#### Future Features
* Add arguments (with defaults and checks) to let user modify the phrase word min and max
    * Defaults:  min >= 3, max <= 10
    * Check: confirm min <= max
* Output: include count with phrase, perhaps as map entries
* Make input more flexible and realistic - not just a String
    * Eventually allow different file types / file upload

