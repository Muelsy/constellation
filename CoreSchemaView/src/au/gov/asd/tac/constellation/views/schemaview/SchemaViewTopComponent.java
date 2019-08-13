/*
 * Copyright 2010-2019 Australian Signals Directorate
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
package au.gov.asd.tac.constellation.views.schemaview;

import au.gov.asd.tac.constellation.functionality.views.JavaFxTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays the type tree of the current graph.
 */
@TopComponent.Description(
        preferredID = "SchemaViewTopComponent",
        iconBase = "au/gov/asd/tac/constellation/views/schemaview/resources/schema_view.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(
        mode = "explorer",
        openAtStartup = false)
@ActionID(
        category = "explorer",
        id = "au.gov.asd.tac.constellation.views.schemaview.SchemaViewTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Views", position = 1100),
    @ActionReference(path = "Shortcuts", name = "CS-S")})
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_SchemaViewAction",
        preferredID = "SchemaViewTopComponent")
@Messages({
    "CTL_SchemaViewAction=Schema View",
    "CTL_SchemaViewTopComponent=Schema View",
    "HINT_SchemaViewTopComponent=Schema View"})
public final class SchemaViewTopComponent extends JavaFxTopComponent<SchemaViewPane> {

    private final SchemaViewPane schemaViewPane;

    public SchemaViewTopComponent() {
        super();

        // initialise top component
        setName(Bundle.CTL_SchemaViewTopComponent());
        setToolTipText(Bundle.HINT_SchemaViewTopComponent());
        initComponents();

        this.schemaViewPane = new SchemaViewPane();
        initContent();
    }

    @Override
    protected String createStyle() {
        return null;
    }

    @Override
    protected SchemaViewPane createContent() {
        return schemaViewPane;
    }

    @Override
    protected void handleComponentOpened() {
        schemaViewPane.populate();
    }

    @Override
    protected void handleComponentClosed() {
        schemaViewPane.clear();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
