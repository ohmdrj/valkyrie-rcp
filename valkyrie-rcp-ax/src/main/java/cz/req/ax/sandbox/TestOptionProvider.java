package cz.req.ax.sandbox;

import cz.req.ax.options.OptionProvider;

import java.util.Arrays;
import java.util.List;

/**
 * TestOptionProvider
 *
 * @author Ondrej Burianek
 */
public class TestOptionProvider implements OptionProvider {

    @Override
    public List<String> optionValues() {
        return Arrays.asList(new String[]{"Uno", "Due", "Tre"});
    }
    
}
