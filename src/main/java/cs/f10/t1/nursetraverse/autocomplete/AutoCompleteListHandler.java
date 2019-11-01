package cs.f10.t1.nursetraverse.autocomplete;

import java.util.Arrays;
import java.util.LinkedList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A handler class that updates list of autocomplete words to be shown according to userinput
 */
public class AutoCompleteListHandler {
    private AutoCompleteWordStorage autoCompleteWordStorage;

    public AutoCompleteListHandler(AutoCompleteWordStorage autoCompleteWordStorage) {
        this.autoCompleteWordStorage = autoCompleteWordStorage;
    }

    /**
     * Choose initial list to be suggested
     */
    public ObservableList<AutoCompleteWord> chooseInitialList(LinkedList<AutoCompleteWord> matchedAutoCompleteWords) {
        ObservableList<AutoCompleteWord> currentList;
        if (matchedAutoCompleteWords.size() == 0) {
            // Set to object list
            currentList = autoCompleteWordStorage.getOListAllObjectWord();
        } else if (matchedAutoCompleteWords.size() == 1) {
            // Set to command list
            currentList = autoCompleteWordStorage.getOListAllCommandWord();
        } else if (matchedAutoCompleteWords.size() == 2) {
            // Set to prefix/index/empty list
            if (((CommandWord) matchedAutoCompleteWords.get(1)).hasIndex()) {
                currentList = autoCompleteWordStorage
                        .generateOListAllIndexWord((ObjectWord) matchedAutoCompleteWords.get(0));
            } else if (((CommandWord) matchedAutoCompleteWords.get(1)).hasPrefix()) {
                currentList = autoCompleteWordStorage.getOListAllPrefixWord();
            } else {
                currentList = FXCollections.observableArrayList();
            }
        } else {
            // Set to prefix/empty list
            if (((CommandWord) matchedAutoCompleteWords.get(1)).hasPrefix()) {
                currentList = autoCompleteWordStorage.getOListAllPrefixWord();
            } else {
                currentList = FXCollections.observableArrayList();
            }
        }
        return currentList;
    }

    /**
     * Update list of autocomplete words to be suggested according to current phrase in command box textfield
     *
     * @param currentList       list to be updated
     * @param segments          Array of segments of the full command in textfield
     * @param firstSegmentParts Linkedlist of parts in first segment of segments array
     * @return updatedList
     */
    public ObservableList<AutoCompleteWord> updateList(LinkedList<AutoCompleteWord> matchedAutoCompleteWords,
                                                       ObservableList<AutoCompleteWord> currentList, String[] segments,
                                                       LinkedList<String> firstSegmentParts) {
        ObservableList<AutoCompleteWord> updatedList = FXCollections.observableArrayList();

        LinkedList<String> combinedList = new LinkedList<>(firstSegmentParts);
        combinedList.addAll(Arrays.asList(segments).subList(1, segments.length));

        if (matchedAutoCompleteWords.size() == combinedList.size()) {
            return currentList;
        } else {
            for (AutoCompleteWord autoCompleteWord : currentList) {
                if (autoCompleteWord.getSuggestedWord().startsWith(combinedList.get(matchedAutoCompleteWords.size()))) {
                    updatedList.add(autoCompleteWord);
                }
            }
            // Add '-' to each object word displayed for user understanding
            addDashToObjectWordList(updatedList);
            return updatedList;
        }
    }

    /**
     * Filter unrelated words from either prefix or command wordlist
     *
     * @param listToBeSuggested list to be filtered
     * @return filteredList
     */
    public ObservableList<AutoCompleteWord> filterList(LinkedList<AutoCompleteWord> matchedAutoCompleteWords,
                                                       ObservableList<AutoCompleteWord> listToBeSuggested) {
        ObservableList<AutoCompleteWord> filteredList = FXCollections.observableArrayList();
        // Perform filter only if listToBeSuggested is not empty and is a prefix or command list
        if (listToBeSuggested.size() != 0 && (listToBeSuggested.get(0) instanceof PrefixWord
                || listToBeSuggested.get(0) instanceof CommandWord)) {
            // filter based on first 2 correct word
            for (AutoCompleteWord autoCompleteWord : listToBeSuggested) {
                boolean isAssociable = true;

                // Checks what if word in list are associable to the correctly matched words

                // Checks for first match word
                if (matchedAutoCompleteWords.size() >= 1) {
                    boolean isAssociableToFirstWord = matchedAutoCompleteWords.get(0).getSuggestedWord()
                            .equals(((AssociableWord) autoCompleteWord).getAssociatedWordList().get(0));
                    if (!isAssociableToFirstWord) {
                        isAssociable = false;
                    }
                }
                // Checks for second match word
                if (matchedAutoCompleteWords.size() >= 2) {
                    boolean isAssociableToSecondWord = matchedAutoCompleteWords.get(1).getSuggestedWord()
                            .equals(((AssociableWord) autoCompleteWord).getAssociatedWordList().get(1));
                    if (!isAssociableToSecondWord) {
                        isAssociable = false;
                    }
                }
                if (isAssociable) {
                    filteredList.add(autoCompleteWord);
                }
            }
            return filteredList;
        } else {
            return listToBeSuggested;
        }
    }

    /**
     * Add - for each word in object list, for user understanding in the autocomplete panel ui
     *
     * @param objectList list for words to add dash to
     */
    public void addDashToObjectWordList(ObservableList<AutoCompleteWord> objectList) {
        if (objectList.size() != 0 && objectList.get(0) instanceof ObjectWord) {
            for (int i = 0; i < objectList.size(); i++) {
                objectList.set(i, new ObjectWord(objectList.get(i).getSuggestedWord() + "-"));
            }
        }
    }
}
