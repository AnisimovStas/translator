package org.example.repository;

import org.example.model.Word;
import org.example.utils.DatabaseUtils;

import java.sql.ResultSet;

public class WordRepository {


    public  static void init() {
        DatabaseUtils.executeUpdate("""
                create table if not exists word (
                    id serial primary key,
                    original_text text not null,
                    translated_text text not null,
                    is_shown boolean not null
                )
                """);
    }


    public void saveOrUpdate(Word word) {
Word getWordByOriginalText = getWordByOriginalText(word.getOriginalText());
        System.out.println("is word found? " + (getWordByOriginalText != null));
        if (getWordByOriginalText == null) {
        DatabaseUtils.executeUpdate("""
                insert into word(original_text, translated_text, is_shown)
                values(?, ?, ?)
                """, word.getOriginalText(), word.getTranslatedText(), false);
    } else {
            System.out.println("we in else");
            System.out.println("founded word: " + getWordByOriginalText.toString());
        DatabaseUtils.executeUpdate("""
                update word set translated_text = ?, is_shown = ? where id = ?
                """, word.getTranslatedText(), false, word.getId());
        }
    }

    public Word getWordByOriginalText(String originalText) {
        ResultSet resultSet = DatabaseUtils.executeQuery("""
                select * from word where original_text = ?
                LIMIT 1
                """, originalText);

        try {
            while (resultSet != null && resultSet.next()) {
                Word word = new Word();
                word.setId(resultSet.getInt("id"));
                word.setOriginalText(resultSet.getString("original_text"));
                word.setTranslatedText(resultSet.getString("translated_text"));
                word.setShown(resultSet.getBoolean("is_shown"));
                return word;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Word getUnshown() {
        ResultSet resultSet = DatabaseUtils.executeQuery("""
            SELECT * FROM word WHERE is_shown = false
            LIMIT 1
            """);

        System.out.println("result of getUnshown: " + resultSet);
        try {
            while (resultSet != null && resultSet.next()) {
                Word word = new Word();
                word.setId(resultSet.getInt("id"));
                word.setOriginalText(resultSet.getString("original_text"));
                word.setTranslatedText(resultSet.getString("translated_text"));
                word.setShown(resultSet.getBoolean("is_shown"));
              return word;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateShown(Integer id) {
        DatabaseUtils.executeUpdate("""
                update word set is_shown = true where id = ?
                """, id);
    }

}
