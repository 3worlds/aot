package au.edu.anu.rscs.aot.init.old;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Shayne Flint - 2012?
 * refactored by Jacques Gignoux May 2019
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface InitialiseAfterClass {

	Class<? extends Initialisable>[] value();           // initialise after all instances of the specified node classes

	boolean includeSubclasses() default true;  // include all subclasses in initialisation
}
