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

package cz.req.ax.command;

import cz.req.ax.options.AxOptionsCommand;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.valkyriercp.command.config.AbstractCommandConfig;
import org.valkyriercp.command.support.*;

import java.util.ArrayList;

/**
 * @author Ondrej Burianek
 */
@Configuration
public class AxCommandConfig extends AbstractCommandConfig {

    @Bean
    @Qualifier("menubar")
    @Override
    public CommandGroup menuBarCommandGroup() {
        CommandGroupFactoryBean menuFactory = new CommandGroupFactoryBean();
        menuFactory.setGroupId("menu");
        ArrayList menuMembers = new ArrayList();
        menuMembers.add(applicationMenu());
        menuMembers.add(commandMenu());
        menuBarInitialize(menuMembers);
        menuFactory.setMembers(menuMembers.toArray());
        return menuFactory.getCommandGroup();
    }

    public void menuBarInitialize(ArrayList menuMembers) {
    }

    @Bean
    public CommandGroup applicationMenu() {
        CommandGroupFactoryBean factory = new CommandGroupFactoryBean();
        factory.setGroupId("applicationMenu");
        factory.setMembers(optionsCommand(), aboutCommand(), "separator", closeCommand(), exitCommand());
        return factory.getCommandGroup();
    }

    @Bean
    public CommandGroup commandMenu() {
        CommandGroupFactoryBean factory = new CommandGroupFactoryBean();
        factory.setGroupId("commandMenu");
        factory.setMembers("sharedRefreshCommand");
        factory.setMembers("sharedSearchCommand");
        /*
         * <list> <value>sharedRefreshCommand</value> <value>sharedSearchCommand</value> <value>separator</value>
         * <value>sharedAddCommand</value> <value>sharedEditCommand</value> <value>sharedRemoveCommand</value>
         * <value>separator</value> <value>sharedDetailCommand</value> <value>sharedCommitCommand</value>
         */
        return factory.getCommandGroup();
    }

//    @Bean
//    public ActionCommand restartCommand() {
//        return new ActionCommand("restartCommand") {
//
//            @Override
//            protected void doExecuteCommand() {
//                ClassPathXmlApplicationContext context = (ClassPathXmlApplicationContext) applicationConfig.applicationContext();
//                String[] configLocations = (String[]) PrivateClassUtils.readField(context, AbstractRefreshableConfigApplicationContext.class, "configLocations");
//                new AxApplicationLauncher(configLocations[0]);
//                applicationConfig.windowManager().getActiveWindow().getControl().setVisible(false);
//            }
//        };
//    }

    @Bean
    public ActionCommand optionsCommand() {
        return new AxOptionsCommand();
    }

    @Bean
    public AbstractCommand closeCommand() {
        return new ClosePageCommand();
    }

    @Bean
    @Override
    public AbstractCommand exitCommand() {
        return new ExitCommand();
    }
}
