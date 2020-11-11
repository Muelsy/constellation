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
package au.gov.asd.tac.constellation.functionality.welcome.plugins;

import au.gov.asd.tac.constellation.functionality.welcome.WelcomePageProvider;
import au.gov.asd.tac.constellation.functionality.welcome.WelcomeTopComponent;
import au.gov.asd.tac.constellation.plugins.PluginInfo;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 * The plugin for the Welcome Page that allows the user to join the 
 * Constellation community
 *
 * @author Delphinus8821
 */

@ServiceProvider(service = WelcomePageProvider.class, position = 8)
@PluginInfo(tags = {"WELCOME"})
@NbBundle.Messages("JoinCommWelcomePlugin=Join Comm Welcome Plugin")
public class JoinCommWelcomePlugin extends WelcomePageProvider {
    
    public static final String JOIN = "resources/welcome_join.png";
    ImageView joinView = new ImageView(new Image(WelcomeTopComponent.class.getResourceAsStream(JOIN)));
    Button joinBtn = new Button("Join our Community\nBecome a member", joinView);
        
    /**
     * Get a unique reference that is used to identify the plugin 
     *
     * @return a unique reference
     */
    @Override
    public String getName() {
        return JoinCommWelcomePlugin.class.getName();
    }
    
    /**
     * Get a description for the link that will appear on the Welcome Page 
     *
     * @return a unique reference
     */
    @Override
    public String getLinkDescription() {
        return "Allows for users to join the Constellation Community";
    }
    
    /**
     * Get an optional textual description that appears on the Welcome Page.
     *
     * @return a unique reference
     */
    @Override
    public String getDescription() {
        StringBuilder buf = new StringBuilder();
        buf.append("<br>");
        buf.append("Allows for users to join the Constellation Community. ");
        return buf.toString();
    }
    
    /**
     * Returns a link to a resource that can be used instead of text.
     *
     * @return a unique reference
     */
    @Override
    public String getImage() {
        return null;
    }
    /**
     * This method describes what action should be taken when the 
     * link is clicked on the Welcome Page
     *
     */
    @Override
    public void run() {
    }

    /**
     * Determines whether this analytic appear on the Welcome Page 
     *
     * @return true is this analytic should be visible, false otherwise.
     */
    @Override
    public boolean isVisible() {
        return true;
    }
    
     /**
     * Creates the button object to represent this plugin
     * 
     * @return the button object
     */
    @Override
    public Button getButton(){
        joinView.setFitHeight(25);
        joinView.setFitWidth(25);
        return joinBtn;
    }
}