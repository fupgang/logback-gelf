/*
 * Logback GELF - zero dependencies Logback GELF appender library.
 * Copyright (C) 2016 Oliver Siegmar
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package de.siegmar.logbackgelf;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class GelfMessageTest {

    @Test
    public void simple() {
        final Map<String, Object> additionalFields = Map.of("foo", (Object) "bar");

        final GelfMessage message = new GelfMessage("host", "short message", null,
                1584271169123L, 6, additionalFields);

        assertEquals("host", message.getHost());
        assertEquals("short message", message.getShortMessage());
        assertNull(message.getFullMessage());
        assertEquals(1584271169123L, message.getTimestamp());
        assertEquals(6, message.getLevel());
        assertEquals(additionalFields, message.getAdditionalFields());

        assertEquals("{"
            + "\"version\":\"1.1\","
            + "\"host\":\"host\","
            + "\"short_message\":\"short message\","
            + "\"timestamp\":1584271169.123,"
            + "\"level\":6,"
            + "\"_foo\":\"bar\""
            + "}",
            asString(message.toJSON()));
    }

    @Test
    public void complete() {
        final Map<String, Object> additionalFields = Map.of("foo", (Object) "bar");

        final GelfMessage message = new GelfMessage("host", "short message", "full message",
            1584271169123L, 6, additionalFields);

        assertEquals("{"
            + "\"version\":\"1.1\","
            + "\"host\":\"host\","
            + "\"short_message\":\"short message\","
            + "\"full_message\":\"full message\","
            + "\"timestamp\":1584271169.123,"
            + "\"level\":6,"
            + "\"_foo\":\"bar\""
            + "}",
            asString(message.toJSON()));
    }

    @Test
    void filterEmptyFullMessage() {
        final GelfMessage message = new GelfMessage("host", "short message", "",
                1584271169123L, 6, Map.of());

        assertEquals("short message", message.getShortMessage());
        assertNull(message.getFullMessage());
    }

    private String asString(final byte[] data) {
        return new String(data, UTF_8);
    }

}
