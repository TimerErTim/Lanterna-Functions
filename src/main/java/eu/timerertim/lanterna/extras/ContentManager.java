package eu.timerertim.lanterna.extras;

import com.googlecode.lanterna.TerminalSize;
import eu.timerertim.lanterna.extras.utils.WrappingMode;

import java.util.*;

class ContentManager {
    // Primary fields
    private final TerminalSize size; //Size of the managed content display
    private final String[] displayContent; //This are the lines that are actually shown on the console
    private final List<String> content, wrappedContent; //This effectively are the lines users of this class want to use
    private final WrappingMode wrapping;

    // Helper fields
    private int previousModifiedIndex;

    ContentManager(TerminalSize size, WrappingMode wrapping) {
        this.size = size;
        this.content = new LinkedList<>();
        this.wrappedContent = new ArrayList<>();
        this.displayContent = new String[size.getRows()];
        this.wrapping = wrapping;
        this.previousModifiedIndex = 0;

        Arrays.fill(displayContent, "");
    }

    void addLine(String line) {
        previousModifiedIndex = wrappedContent.size();
        content.add(line);
        Collections.addAll(wrappedContent, wrapping.wrap(line, size.getColumns()));
    }

    /**
     * Replaces last line with a new line
     *
     * @param line the new line
     */
    void replaceLine(String line) {
        wrappedContent.subList(previousModifiedIndex, wrappedContent.size()).clear();
        content.remove(content.size() - 1);
        content.add(line);
        Collections.addAll(wrappedContent, wrapping.wrap(line, size.getColumns()));
    }

    void fillDisplayContent(int scrollPosition) {
        for (int index = 0; index < displayContent.length; index++) {
            int wrappedIndex = index + scrollPosition;
            if (wrappedIndex < wrappedContent.size()) {
                displayContent[index] = wrappedContent.get(wrappedIndex);
            } else {
                displayContent[index] = "";
            }
        }
    }

    /**
     * Returns a reference to the displayContent.
     * Should be recalled after a resize.
     *
     * @return the displayContent
     */
    String[] getDisplayContent() {
        return displayContent;
    }
}