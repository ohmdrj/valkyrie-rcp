/*
 * Copyright 2009-2013 Ondrej Burianek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.req.ax.sandbox;

import cz.req.ax.AxApplicationPageDescriptor;
import cz.req.ax.view.AxViewDescriptor;
import cz.req.ax.widget.AxWidget;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.valkyriercp.application.PageDescriptor;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.command.support.ActionCommand;

import javax.swing.*;

/**
 * @author Ondrej Burianek
 */
@Configuration
public class StoneboxApplicationConfig {

    @Bean
    PageDescriptor stonePage() {
        AxApplicationPageDescriptor descriptor = new AxApplicationPageDescriptor();
        descriptor.setViewDescriptor(stoneView());
        return descriptor;
    }

    @Bean(autowire = Autowire.BY_NAME)
    ActionCommand stoneCommand() {
        return new ActionCommand("stoneCommand") {

            @Override
            protected void doExecuteCommand() {
                throw new UnsupportedOperationException("Functional :)");
            }
        };
    }

    @Bean
    ViewDescriptor stoneView() {
        return new AxViewDescriptor("stoneView", stoneWidget());
    }

    @Bean
    AxWidget stoneWidget() {
        return new AxWidget("stoneWidget") {

            @Autowired
            ActionCommand stoneCommand;

            @Override
            public void createWidget() {
                addRow(new JLabel("Wooow..."));
                if (stoneCommand == null) {
                    addRow(new JLabel("Shit :("));
                } else {
                    addRow(stoneCommand.createButton());
                }
            }
        };
    }
}
