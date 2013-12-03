/*
 * Copyright 2009-2013 Ondřej Buránek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.req.ax;

import org.valkyriercp.rules.constraint.Constraint;
import org.valkyriercp.rules.constraint.property.PropertyConstraint;
import org.valkyriercp.rules.support.DefaultRulesSource;


/**
 * @author Ondrej Burianek
 */
public class AxValidationRulesSource extends DefaultRulesSource {

    public final Constraint ALFANUM_CONSTRAINT = allx(regexp("[-'./sa-zA-Z0-9 ]*", "alphabeticConstraint"));
    public final Constraint ALFANUMCZ_CONSTRAINT = allx(regexp("[-'./sa-zA-Z0-9ěščřžýáíéúůóďťňĚŠČŘŽÝÁÍÉÚŮÓĎŤŇ ]*", "alphabeticConstraint"));
    public final Constraint CZ_ZIP_CONSTRAINT = allx(regexp("([0-9]{3} [0-9]{2})?", "zipcodeConstraint"));
    public final Constraint EMAIL_CONSTRAINT = allx(regexp("([-a-zA-Z0-9.]+@[-a-zA-Z0-9.]+)?", "emailConstraint"));
    public final Constraint CZ_PHONE_CONSTRAINT = allx(regexp("([0-9]{3} [0-9]{3} [0-9]{3})?", "phoneConstraint"));

    public Constraint allx(Constraint... c) {
        return super.all(c);
    }

    public PropertyConstraint allx(String propertyName, Constraint... constraints) {
        return super.all(propertyName, constraints);
    }

    public Constraint require(Constraint c) {
        return allx(required(), c);
    }
}
