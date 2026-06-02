package io.github.andrewmatzureff.arg.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Units {

    /// Suppose the player occupies 18 pixels worth of vertical space within a 32 x 32 sprite. If we accept the player to be exactly 6 feet tall in the world or 1.8288 meters then we can deduce our scale factor to be 18 pixels per 1.8288 meters. Multiply this value by any length in meters to obtain the equivalent length in pixels.
    public static final float METERS_TO_PIXELS = 18 / 1.8288f;

    /// Multiply this value by any length in pixels to obtain the equivalent length in meters.
    public static final float PIXELS_TO_METERS = 1 / METERS_TO_PIXELS;
}
