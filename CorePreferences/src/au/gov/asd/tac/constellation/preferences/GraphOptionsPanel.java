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
package au.gov.asd.tac.constellation.preferences;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.LayoutStyle;
import org.openide.awt.Mnemonics;
import org.openide.util.NbBundle;

/**
 * UI panel to define the session parameters
 *
 * @author aldebaran30701
 */
final class GraphOptionsPanel extends javax.swing.JPanel {

    private final GraphOptionsPanelController controller;

    public GraphOptionsPanel(final GraphOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        
        /**
         * REMOVE this section when implementing custom blaze colours
         * Introduced with fix for issue #158
         * FROM HERE
         */
        BlazeColourPanel.setVisible(false);
        BlazeColourPlaceholder.setVisible(false);
        BlazeColourDescription.setVisible(false);
        /**
         * TO HERE
         */
    }
    
    public void setBlazeSize(final int value){
        blazeSlider.setValue(value);
    }
    
    public void setBlazeOpacity(final int value){
        blazeOpacitySlider.setValue(value);
    }
    
    public int getBlazeSize(){
        return blazeSlider.getValue();
    }
    
    public int getBlazeOpacity(){
        return blazeOpacitySlider.getValue();
    }




    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        blazeSizePanel = new JPanel();
        blazeSlider = new JSlider();
        BlazeSizeDescription = new JLabel();
        blazeOpacitySlider = new JSlider();
        BlazeSizeDescription1 = new JLabel();
        BlazeColourPanel = new JPanel();
        BlazeColourDescription = new JLabel();
        BlazeColourPlaceholder = new JLabel();

        blazeSizePanel.setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(GraphOptionsPanel.class, "GraphOptionsPanel.blazeSizePanel.border.title"))); // NOI18N

        Mnemonics.setLocalizedText(BlazeSizeDescription, NbBundle.getMessage(GraphOptionsPanel.class, "GraphOptionsPanel.BlazeSizeDescription.text")); // NOI18N

        Mnemonics.setLocalizedText(BlazeSizeDescription1, NbBundle.getMessage(GraphOptionsPanel.class, "GraphOptionsPanel.BlazeSizeDescription1.text")); // NOI18N

        GroupLayout blazeSizePanelLayout = new GroupLayout(blazeSizePanel);
        blazeSizePanel.setLayout(blazeSizePanelLayout);
        blazeSizePanelLayout.setHorizontalGroup(blazeSizePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(blazeSizePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(blazeSizePanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addGroup(blazeSizePanelLayout.createSequentialGroup()
                        .addComponent(BlazeSizeDescription1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(blazeOpacitySlider, GroupLayout.PREFERRED_SIZE, 253, GroupLayout.PREFERRED_SIZE))
                    .addGroup(blazeSizePanelLayout.createSequentialGroup()
                        .addComponent(BlazeSizeDescription)
                        .addGap(46, 46, 46)
                        .addComponent(blazeSlider, GroupLayout.PREFERRED_SIZE, 253, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        blazeSizePanelLayout.setVerticalGroup(blazeSizePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(blazeSizePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(blazeSizePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(BlazeSizeDescription)
                    .addComponent(blazeSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(blazeSizePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(blazeOpacitySlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(BlazeSizeDescription1))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        BlazeColourPanel.setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(GraphOptionsPanel.class, "GraphOptionsPanel.blazeColourPanel.border.title"))); // NOI18N

        Mnemonics.setLocalizedText(BlazeColourDescription, NbBundle.getMessage(GraphOptionsPanel.class, "GraphOptionsPanel.BlazeColourDescription.text")); // NOI18N

        Mnemonics.setLocalizedText(BlazeColourPlaceholder, NbBundle.getMessage(GraphOptionsPanel.class, "GraphOptionsPanel.BlazeColourPlaceholder.text")); // NOI18N

        GroupLayout BlazeColourPanelLayout = new GroupLayout(BlazeColourPanel);
        BlazeColourPanel.setLayout(BlazeColourPanelLayout);
        BlazeColourPanelLayout.setHorizontalGroup(BlazeColourPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(BlazeColourPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BlazeColourDescription)
                .addGap(115, 115, 115)
                .addComponent(BlazeColourPlaceholder)
                .addContainerGap(587, Short.MAX_VALUE))
        );
        BlazeColourPanelLayout.setVerticalGroup(BlazeColourPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(BlazeColourPanelLayout.createSequentialGroup()
                .addGroup(BlazeColourPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(BlazeColourDescription)
                    .addComponent(BlazeColourPlaceholder))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(blazeSizePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(BlazeColourPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(blazeSizePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BlazeColourPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(346, Short.MAX_VALUE))
        );

        blazeSizePanel.getAccessibleContext().setAccessibleName(NbBundle.getMessage(GraphOptionsPanel.class, "GraphOptionsPanel.blazeSizePanel.AccessibleContext.accessibleName")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel BlazeColourDescription;
    private JPanel BlazeColourPanel;
    private JLabel BlazeColourPlaceholder;
    private JLabel BlazeSizeDescription;
    private JLabel BlazeSizeDescription1;
    private JSlider blazeOpacitySlider;
    private JPanel blazeSizePanel;
    private JSlider blazeSlider;
    // End of variables declaration//GEN-END:variables
}
