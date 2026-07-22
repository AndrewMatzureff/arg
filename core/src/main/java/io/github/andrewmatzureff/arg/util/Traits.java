package io.github.andrewmatzureff.arg.util;

import java.util.function.Predicate;

public interface Traits<TraitOwner extends Traits<TraitOwner, TraitType>, TraitType extends Traits.Trait<TraitOwner>> {
    default boolean is(TraitType trait) {
        // noinspection unchecked
        return trait.getMarker()
            .test((TraitOwner) this);
    }

    default boolean not(TraitType trait) {return !is(trait);}
    interface Trait<TraitOwner> {Predicate<TraitOwner> getMarker();}
}
