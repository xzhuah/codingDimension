package nodes.stockinfoNode.constants;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Xinyu Zhu on 2020/12/2, 23:53
 * nodes.stockinfoNode.constants in codingDimensionTemplate
 */
@BindingAnnotation
@Target({ FIELD, PARAMETER, METHOD })
@Retention(RUNTIME)
public @interface CnStock {
}
