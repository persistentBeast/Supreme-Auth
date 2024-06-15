package org.supreme.constants;


import org.supreme.models.User;

import java.util.Map;

public class Constant {

    public static Map<String, Class> ddbTableToClassMap = Map.of("users", User.class);

}
