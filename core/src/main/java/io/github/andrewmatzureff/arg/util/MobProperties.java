package io.github.andrewmatzureff.arg.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MobProperties {

    public enum Type {Player, Prop}

    public static final String TYPE = "type";
}
