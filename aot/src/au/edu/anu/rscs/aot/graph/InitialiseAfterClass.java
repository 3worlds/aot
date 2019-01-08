package au.edu.anu.rscs.aot.graph;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface InitialiseAfterClass {

	Class<? extends AotNode>[] value();           // initialise after all instances of the specified node classes

	boolean includeSubclasses() default true;  // include all subclasses in initialisation
}
