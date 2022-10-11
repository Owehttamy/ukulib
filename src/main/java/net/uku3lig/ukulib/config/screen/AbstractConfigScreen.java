package net.uku3lig.ukulib.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.Option;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.IConfig;

/**
 * A screen used to edit a config.
 * Instances of this class should NOT be reused.
 *
 * @param <T> The type of the config
 */
public abstract class AbstractConfigScreen<T extends IConfig<T>> extends Screen {
    private final Screen parent;

    /**
     * The config manager. Used to load and save the config.
     */
    protected final ConfigManager<T> manager;

    /**
     * The widget used to display the options.
     * @see AbstractConfigScreen#getOptions(IConfig)
     */
    protected ButtonListWidget buttonList;

    /**
     * Creates a config screen.
     * @param parent The parent screen
     * @param title The title of the screen
     * @param manager The config manager
     */
    protected AbstractConfigScreen(Screen parent, Text title, ConfigManager<T> manager) {
        super(title);
        this.manager = manager;
        this.parent = parent;
    }

    /**
     * The list of options that will be shown to the user when this screen is displayed.
     * @param config The config
     * @return An array of {@link Option}
     */
    protected abstract Option[] getOptions(T config);

    @Override
    protected void init() {
        super.init();
        buttonList = new ButtonListWidget(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
        buttonList.addAll(getOptions(manager.getConfig()));
        this.children.add(buttonList);
        drawFooterButtons();
    }

    /**
     * Draws the buttons in the footer.
     */
    @SuppressWarnings("ConstantConditions")
    protected void drawFooterButtons() {
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, I18n.translate("gui.done"),
                button -> this.minecraft.openScreen(this.parent)));
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.buttonList.render(mouseX, mouseY, delta);
        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 20, 0xFFFFFF);
        super.render(mouseX, mouseY, delta);
    }

    @Override
    public void removed() {
        manager.saveConfig();
    }
}
