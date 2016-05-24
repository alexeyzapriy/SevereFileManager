package com.roman.zapriy.severefilemanager.content_for_list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ContentOfFileSystem {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<FileModel> ITEMS = new ArrayList<FileModel>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, FileModel> ITEM_MAP = new HashMap<String, FileModel>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createFileModel(i));
        }
    }

    private static void addItem(FileModel item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static FileModel createFileModel(int position) {
        return new FileModel(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class FileModel {
        public final String id;
        public final String content;
        public final String details;

        public FileModel(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
