/*
 * Copyright 2010-2020 Australian Signals Directorate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.gov.asd.tac.constellation.views.notes.state;

import au.gov.asd.tac.constellation.plugins.reporting.PluginReport;
import java.time.LocalDateTime;

/**
 * A note entry into the Notes View.
 *
 * @author sol695510
 */
public class NotesViewEntry {

    private static int noteIdCounter = 1;
    private final int noteId;
    
    private final Boolean userCreated;
    private final String dateTime;
    private String noteTitle;
    private String noteContent;

    // Constructor.
    public NotesViewEntry(final String noteDateTime, final String noteTitle, final String noteContent, final Boolean userCreated) {
        this.userCreated = userCreated;
        this.dateTime = noteDateTime;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteId = noteIdCounter++;
    }

    // Copy constructor.
    public NotesViewEntry(final NotesViewEntry note) {
        this.userCreated = note.isUserCreated();
        this.dateTime = note.getDateTime();
        this.noteTitle = note.getNoteTitle();
        this.noteContent = note.getNoteContent();
        this.noteId = noteIdCounter++;
    }
    
    public void setNoteTitle(final String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNoteContent(final String noteContent) {
        this.noteContent = noteContent;
    }

    public Boolean isUserCreated() {
        return userCreated;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }
    
    public int getNoteId() {
        return noteId;
    }
}
