package com.alien.common.property;

import com.alien.Alien;
import com.blib.api.BLibAPI;
import com.blib.api.common.property.v1.BLibProperties;
import com.blib.api.common.property.v1.BLibPropertyContainer;
import com.blib.api.common.property.v1.BLibPropertyKey;
import com.just.core.functional.option.Option;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;

public class AlienPropertyAccess {

    private static final Path PATH = BLibAPI.getGameDirectory().resolve(Path.of("config/" + Alien.MOD.id() + ".properties"));

    public static final AlienPropertyAccess INSTANCE = new AlienPropertyAccess();

    private BLibPropertyContainer propertyContainer;

    public void save() {
        BLibProperties.save(PATH, getOrCreatePropertyContainer(), AlienPropertySchema.SCHEMA);
    }

    private BLibPropertyContainer getOrCreatePropertyContainer() {
        if (this.propertyContainer == null) {
            var result = BLibProperties.load(PATH, AlienPropertySchema.SCHEMA);

            result
                .inspect(propertyContainer -> this.propertyContainer = propertyContainer)
                .inspectErr(loadError -> {
                    switch (loadError) {
                        case BLibProperties.LoadError.DoesNotExist ignored -> {
                            this.propertyContainer = new BLibPropertyContainer(new HashMap<>(), AlienPropertySchema.SCHEMA);
                            save();
                        }
                        case BLibProperties.LoadError.ReadFailure readFailure -> throw new RuntimeException(readFailure.exception());
                    }
                });
        }

        return propertyContainer;
    }

    public <T> @Nullable T getOrNull(BLibPropertyKey.Leaf<T> propertyKey) {
        return getOrCreatePropertyContainer().getOrNull(propertyKey);
    }

    public <T> Option<T> get(BLibPropertyKey.Leaf<T> propertyKey) {
        return Option.ofNullable(getOrNull(propertyKey));
    }

    public <T> T getOrThrow(BLibPropertyKey.Leaf<T> propertyKey) {
        return Objects.requireNonNull(getOrNull(propertyKey));
    }

    public <T> T getOrDefault(BLibPropertyKey.Leaf<T> propertyKey, T defaultValue) {
        var value = getOrNull(propertyKey);

        return value == null
            ? defaultValue
            : value;
    }

    public <T> void set(BLibPropertyKey.Leaf<T> propertyKey, T value) {
        getOrCreatePropertyContainer().set(propertyKey, value);
    }
}
