/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.gov.asd.tac.constellation.views.notes;

import au.gov.asd.tac.constellation.views.JavaFxTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 *
 * @author sol695510
 */
@TopComponent.Description(
        preferredID = "NotesViewTopComponent",
//        iconBase = "au/gov/asd/tac/constellation/views/layers/resources/layers-view.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(
        mode = "explorer",
        openAtStartup = false)
@ActionID(
        category = "Window",
        id = "au.gov.asd.tac.constellation.views.notes.NotesViewTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Experimental/Views", position = 0),
    @ActionReference(path = "Shortcuts", name = "CS-N")})
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_NotesViewAction",
        preferredID = "NotesViewTopComponent")
@Messages({
    "CTL_NotesViewAction=Notes View",
    "CTL_NotesViewTopComponent=Notes View",
    "HINT_NotesViewTopComponent=Notes View"})

public class NotesViewTopComponent extends JavaFxTopComponent<NotesViewPane> {
    
    private final NotesViewController notesViewController;
    private final NotesViewPane notesViewPane;
    
//    private Object bean;

    /**
     * Creates new customizer NotesViewTopComponent
     */
    public NotesViewTopComponent() {
        
        notesViewController = new notesViewController(NotesViewTopComponent.this);
        notesViewPane = new notesViewPane(notesViewController);
        
        initContent();
        initComponents();
    }
    
//    public void setObject(Object bean) {
//        this.bean = bean;
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        setLayout(new java.awt.BorderLayout());

    }//GEN-END:initComponents

    @Override
    protected String createStyle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected NotesViewPane createContent() {
        return notesViewPane;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
