package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.andrewmatzureff.arg.util.CopyPool;
import io.github.andrewmatzureff.arg.util.Restorable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.badlogic.gdx.math.MathUtils.isZero;

public class RigidBody implements Component, Restorable { //Traits<RigidBody, RigidBody.Trait> {

    private final   @Getter Body body;
    private final   @Getter boolean debugDraw = false;

    // flags
    private @Setter @Getter boolean upright;
    // TODO: falling, inert, equilibrium...?

    // accumulators
    private @Getter boolean grounded;
    private @Setter @Getter float angularMotion = 0;
    private @Setter @Getter float angularThrust = 0;
    private final Vector2 linearMotion = new Vector2();
    private final Vector2 linearThrust = new Vector2();

    // misc
    private final CopyPool<Vector2> vectorPool = new CopyPool<>(true, Vector2::cpy, Vector2::set);

    public RigidBody(World world, BodyDef.BodyType type, List<FixtureDef> fixtureDefs) {
        this(world, type, Vector2.Zero, fixtureDefs);
    }

    public RigidBody(World world, BodyDef.BodyType type, Vector2 position, List<FixtureDef> fixtureDefs) {

        final var bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(position);
        this.body = world.createBody(bodyDef);
        body.setUserData(this);
        fixtureDefs.forEach(body::createFixture);
        fixtureDefs.forEach(def -> def.shape.dispose()); // TODO: implement pooling strategy
        // average human density: 985kg/m^3
        // cube root density: 9.9497478956
        // cube root squared: 98.99748318599662849936
    }

    public Vector2 getLinearMotion() {return vectorPool.obtain(linearMotion);}
    public Vector2 getLinearThrust() {return vectorPool.obtain(linearThrust);}

    public void addGrounded() {grounded = true;}

    public void addLinearMotion(float x, float y) {
        // NOTE: maybe make this and others abstract so that more complex rigid bodies can define their own modes of locomotion
        linearMotion.add(x, y);
    }

    public void addAngularMotion(float strength) {
        // NOTE: maybe make this and others abstract so that more complex rigid bodies can define their own modes of locomotion
        angularMotion += strength;
    }

    public void addLinearThrust(float x, float y) {
        // NOTE: maybe make this and others abstract so that more complex rigid bodies can define their own modes of locomotion
        linearThrust.add(x, y);
    }

    public void addAngularThrust(float strength) {
        // NOTE: maybe make this and others abstract so that more complex rigid bodies can define their own modes of locomotion
        angularThrust += strength;
    }

    public boolean hasLinearMotion() {return !linearMotion.isZero();}
    public boolean hasLinearThrust() {return !linearThrust.isZero();}
    public boolean hasAngularMotion() {return !isZero(angularMotion);}
    public boolean hasAngularThrust() {return !isZero(angularThrust);}

    @Override
    public void restore() {
        linearMotion.set(Vector2.Zero);
        linearThrust.set(Vector2.Zero);
        angularMotion = 0;
        angularThrust = 0;
        grounded = false;
    }

    // static \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public static final ComponentMapper<RigidBody> MAPPER = ComponentMapper.getFor(RigidBody.class);

//    public @AllArgsConstructor enum Trait implements Traits.Trait<RigidBody> {
//        GROUNDED(RigidBody::isGrounded)
//        , UPRIGHT(rb -> true);
//        private final @Getter Predicate<RigidBody> marker;
//    }
}
