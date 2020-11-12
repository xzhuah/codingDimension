package nodes.stockinfoNode.impls.annota;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * With this annotation, you can upgrade the PriceAutoUpdater to updater with no delay
 * Whether to update information will be fully based on the real time data in online API
 * But that also means
 */

@BindingAnnotation
@Target({ FIELD, PARAMETER, METHOD })
@Retention(RUNTIME)
public  @interface ZeroDelay {}