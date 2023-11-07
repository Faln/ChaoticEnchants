package me.faln.chaoticenchants.utils;

import com.iridium.iridiumcolorapi.IridiumColorAPI;

import java.util.List;
import java.util.regex.Pattern;

public class Color {

    private Color() {
        throw new UnsupportedOperationException("Cannot instantiate utils class");
    }

    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F-0-9]{6}");

    public static String colorize(String string) {

        if (string == null || string.isEmpty()) {
            return string;
        }

        return IridiumColorAPI.process(string);
    }


    public static List<String> colorize(List<String> list) {
        return list.stream().map(Color::colorize).toList();
    }

}
