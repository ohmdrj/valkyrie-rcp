package org.valkyriercp.sample.dataeditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.valkyriercp.application.support.ApplicationWindowCommandManager;
import org.valkyriercp.command.config.DefaultCommandConfig;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.CommandGroupFactoryBean;
import org.valkyriercp.command.support.ShowViewCommand;

@Configuration
public class DataEditorCommandConfig extends DefaultCommandConfig {
    @Autowired
    private DataEditorApplicationConfig appConfig;

    @Override
    public ApplicationWindowCommandManager applicationWindowCommandManager() {
        ApplicationWindowCommandManager applicationWindowCommandManager = super.applicationWindowCommandManager();
        applicationWindowCommandManager.addSharedCommandIds("universalSearchCommand");
        return applicationWindowCommandManager;
    }

    @Bean
    public AbstractCommand itemDataEditorCommand() {
        return new ShowViewCommand("itemDataEditorCommand", appConfig.itemView());
    }

    @Bean
    public AbstractCommand supplierDataEditorCommand() {
        return new ShowViewCommand("supplierDataEditorCommand", appConfig.supplierView());
    }

    @Bean
    @Qualifier("menubar")
    public CommandGroup menuBarCommandGroup() {
        CommandGroupFactoryBean menuFactory = new CommandGroupFactoryBean();
        menuFactory.setGroupId("menu");
        menuFactory.setMembers(fileMenu(), windowMenu(), helpMenu());
        return menuFactory.getCommandGroup();
    }

    @Bean
    @Qualifier("navigation")
    public CommandGroup navigationCommandGroup() {
        CommandGroupFactoryBean menuFactory = new CommandGroupFactoryBean();
        menuFactory.setGroupId("navigation");
        menuFactory.setMembers(viewMenu());
        return menuFactory.getCommandGroup();
    }

     @Bean
    public CommandGroup fileMenu() {
        CommandGroupFactoryBean fileMenuFactory = new CommandGroupFactoryBean();
        fileMenuFactory.setGroupId("fileMenu");
        fileMenuFactory.setMembers(exitCommand());
        return fileMenuFactory.getCommandGroup();
    }

    @Bean
    public CommandGroup windowMenu() {
        CommandGroupFactoryBean windowMenuFactory = new CommandGroupFactoryBean();
        windowMenuFactory.setGroupId("windowMenu");
        windowMenuFactory.setMembers(newWindowCommand());
        return windowMenuFactory.getCommandGroup();
    }

    @Bean
    public CommandGroup viewMenu() {
        CommandGroupFactoryBean windowMenuFactory = new CommandGroupFactoryBean();
        windowMenuFactory.setGroupId("viewMenu");
        windowMenuFactory.setMembers(itemDataEditorCommand(), supplierDataEditorCommand());
        return windowMenuFactory.getCommandGroup();
    }

   @Bean
    public CommandGroup helpMenu() {
        CommandGroupFactoryBean factory = new CommandGroupFactoryBean();
        factory.setGroupId("helpMenu");
        factory.setMembers(aboutCommand());
        return factory.getCommandGroup();
    }
}
