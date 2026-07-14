package io.github.andrewmatzureff.arg.component.mob;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.component.RigidBody;
import io.github.andrewmatzureff.arg.state.MobState;
import io.github.andrewmatzureff.arg.system.AbstractMobStateIteratingSystem;

import java.util.HashSet;
import java.util.Set;

import static io.github.andrewmatzureff.arg.util.Optionals.remap;
import static io.github.andrewmatzureff.arg.util.Optionals.some;

/**
 * {@link MobTraits} is a {@link Component} that, when attached to an {@link Entity}, represents a collection of "traits" which describe its current state in a more dynamic, fluid way than what is possible by attaching a fixed {@link MobState}. Put another way, "{@code MobTraits}" is to "{@code MobState}" as "Composition" is to "Inheritance". However, one is not a replacement for the other. {@code MobTraits} is meant to be read directly by {@link AbstractMobStateIteratingSystem} objects from within their update method. That is to say: {@code MobStates} persist or transition based on the combination of {@link Trait} they encounter. For example, you may have defined a {@code Trait} which is given when the player's {@link RigidBody} is colliding with the ground. Now, any of your {@code AbstractMobStateIteratingSystem} objects may simply query the existence of your "{@code IS_GROUNDED}" trait and then transition to the appropriate grounded or falling state based on your requirements.
 * <p>
 * Performing state transitions based on the existence of these flag-like traits means that your state processors and their state transition logic is totally decoupled from, not only the relevant systems, but also the underlying components! For instance, you could conceivably reuse the exact same states and state processors between entities with wildly different underlying locomotive strategies (e.g.: some entities which are positioned based on physically simulated rigid bodies and some which exist entirely independent of the physics engine, etc.).
 * <p>
 * Note that the {@code Traits} contained by {@code MobTraits} are not intended to persist beyond the moment of updating the {@code MobState}. After {@code AbstractMobStateIteratingSystem} has executed its update method, ideally, {@code MobTraits} will be reset via {@link MobTraits#clear()} in anticipation of fresh new traits to be added over the remainder of the current frame.
 */
public class MobTraits implements Component {
    private final Set<Trait> traits = new HashSet<>();

    /**
     * Query whether the given {@code Trait} exists among the traits of this {@code MobTraits}.
     * @param trait to be checked
     * @return whether the given {@code Trait} has been added to this {@code MobTraits}
     */
    public boolean have(Trait trait) {return traits.contains(trait);}

    /**
     * Query whether the given {@code Trait} does not exist among the traits of this {@code MobTraits}.
     * @param trait to be checked
     * @return whether the given {@code Trait} has not been added to this {@code MobTraits}
     */
    public boolean lack(Trait trait) {return !have(trait);}

    /**
     * Add the given {@code Trait} to the traits of this {@code MobTraits}. Additionally, this method implicitly adds all ancestor traits of the given trait as well (e.g.: "{@code add(MOVING_X_POSITIVE)}" → {@code MOVING_X_POSITIVE, MOVING_X, MOVING}).
     * @param trait to be added
     * @return whether the addition of the given {@code Trait} and its ancestors resulted in any new additions to this {@code MobTraits}
     */
    public boolean add(Trait trait) {return traits.addAll(trait.ancestors());}

    /**
     * Reset this {@code MobTraits} by removing all traits.
     */
    public void clear() {traits.clear();}

    /// See {@link ComponentMapper}.
    public static final ComponentMapper<MobTraits> MAPPER = ComponentMapper.getFor(MobTraits.class);

    /**
     * A {@link Trait} is a flag-like representation of some elementary facet of an {@link Entity}'s state. {@code Traits} can be defined hierarchically so that {@code Trait} consumers need not know the precise nature of a particular {@code Trait} in order to respond to its presence.
     */
    public interface Trait {

        /**
         * Obtain the {@link Set} of {@code Traits} to which this class of {@code Trait} belongs.
         * @return this {@code Trait}'s set of super-traits including itself as an immutable set
         */
        Set<Trait> ancestors();

        /**
         * Obtain the {@link Set} of {@code Traits} upon which this class of {@code Trait} encloses.
         * @return this {@code Trait}'s set of sub-traits including itself as an immutable set
         */
        Set<Trait> descendants();

        /**
         * Obtain the {@link Set} of {@code Traits} describing the entire lineage associated with this {@code Trait}; including ancestors starting from the root, passing through itself as well as descendants extending down to the leaf level. Traits in the same family must share a direct ancestral relationship to be included (e.g.: parent, child, grandparent, grandchild, etc.). Thus, the notion of "siblings" are not acknowledged as a valid familial relationship in this context by default.
         * @return the union of this {@code Trait}'s {@link Trait#ancestors()} and {@link Trait#descendants()}, including itself, as an immutable set
         */
        default Set<Trait> family() {
            return some(this)
                .map(remap(new HashSet<MobTraits.Trait>()
                    , (t, m) -> t.addAll(m.ancestors())
                    , (t, m) -> t.addAll(m.descendants())))
                .map(Set::copyOf)
                .orElseThrow(); // TODO: throw a more descriptive error
        }
    }
}
