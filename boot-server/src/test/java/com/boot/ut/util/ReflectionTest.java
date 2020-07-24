package com.boot.ut.util;

import com.boot.ut.common.AbstractTestCase;
import com.boot.web.model.UserVO;
import org.junit.Test;

import java.lang.reflect.Field;

public class ReflectionTest extends AbstractTestCase {

    @Test
    public void testReflect() {
        UserVO vo = new UserVO();
        Field field = null;
        try {
            field = vo.getClass().getDeclaredField("name");
        } catch (Exception ignored) {

        }

        if (field != null) {
            try {
                field.set(vo, "new Name");
            } catch (IllegalAccessException ignored) {
            }
        }
    }
}
