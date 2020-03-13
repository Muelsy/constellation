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
package au.gov.asd.tac.constellation.views.namedselection.panes;

import org.openide.util.NbBundle.Messages;

/**
 * JPanel used in dialogs to alert user to protection of named selection from
 * modification.
 * <p>
 * Informational only. No interactivity.
 *
 * @author betelgeuse
 */
@Messages({
    "ProtectedSelectionNotification=has been protected and cannot currently be modified in any way."
})
public class NamedSelectionProtectedPanel extends javax.swing.JPanel {

    /**
     * Creates new form NamedSelectionProtectedPanel
     *
     * @param name The name of the named selection being notified on.
     */
    public NamedSelectionProtectedPanel(final String name) {
        initComponents();

        final String notification = String.format("The named selection '%s' %s",
                name, Bundle.ProtectedSelectionNotification());

        lblMessagePart1.setText(notification);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblMessagePart1 = new javax.swing.JLabel();
        lblMessagePart2 = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(lblMessagePart1, org.openide.util.NbBundle.getMessage(NamedSelectionProtectedPanel.class, "NamedSelectionProtectedPanel.lblMessagePart1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lblMessagePart2, org.openide.util.NbBundle.getMessage(NamedSelectionProtectedPanel.class, "NamedSelectionProtectedPanel.lblMessagePart2.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMessagePart2)
                    .addComponent(lblMessagePart1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMessagePart1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblMessagePart2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblMessagePart1;
    private javax.swing.JLabel lblMessagePart2;
    // End of variables declaration//GEN-END:variables
}
