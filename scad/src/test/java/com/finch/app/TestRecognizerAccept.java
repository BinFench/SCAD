package com.finch.app;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.runners.Parameterized.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.List;
import java.util.*; 
import java.util.stream.*; 
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

@RunWith(Parameterized.class)
public class TestRecognizerAccept
{
    @Parameter(0)
    public String input;

    @Parameters
    public static Collection<String> data() {
        String path = System.getProperty("user.dir");
        List<String> data = new ArrayList<String>();
        try (Stream<Path> paths = Files.walk(Paths.get(path + "/src/test/accept/"))) {
            paths
                .filter(Files::isRegularFile)
                .forEach((Path file) -> {
                    try {
                        data.add(new String(Files.readAllBytes(file)));
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                });
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return data;
    }

    @Test
    public void shouldAcceptInput()
    {
        assertTrue(SCADRecognizer.parse(input));
    }
}
