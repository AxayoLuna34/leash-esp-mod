package net.leashedmobtracker;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import net.minecraft.text.Text;

public class ModConfig implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        LeashESPConfig.init();

        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("Leash ESP Settings"));

            ConfigCategory general = builder.getOrCreateCategory(Text.translatable("Display Options"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // Show Entity Counter (On/Off, Default: On)
            BooleanListEntry entityCounterEntry = entryBuilder
                    .startBooleanToggle(Text.translatable("Show Entity Counter"),
                            LeashESPConfig.getBoolean("showEntityCounter", true))
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> LeashESPConfig.setBoolean("showEntityCounter", newValue))
                    .build();
            general.addEntry(entityCounterEntry);

            // Show Entity Outline (On/Off, Default: On)
            BooleanListEntry entityOutlineEntry = entryBuilder
                    .startBooleanToggle(Text.translatable("Show Entity Outline"),
                            LeashESPConfig.getBoolean("showEntityOutline", true))
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> LeashESPConfig.setBoolean("showEntityOutline", newValue))
                    .build();
            general.addEntry(entityOutlineEntry);

            // Transparency Level (Int: 8-255, Default:128)
            IntegerListEntry transparencyEntry = entryBuilder
                    .startIntField(Text.translatable("Transparency Level"),
                            LeashESPConfig.getInt("transparencyLevel", 128))
                    .setDefaultValue(128)
                    .setMin(8)
                    .setMax(255)
                    .setSaveConsumer(newValue -> LeashESPConfig.setInt("transparencyLevel", newValue))
                    .build();

            general.addEntry(transparencyEntry);

            return builder.build();
        };
    }
}
