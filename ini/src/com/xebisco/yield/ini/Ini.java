/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.ini;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ini {
    private HashMap<String, IniProperties> sections = new HashMap<>();
    private static final Pattern titlePattern = Pattern.compile("^\\[([^]]*)]$");

    public void load(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            ArrayList<String> props = null;
            String header = null;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = titlePattern.matcher(line);
                boolean isHeader = matcher.matches();
                if (props == null) {
                    if (isHeader)
                        props = new ArrayList<>();
                }
                if (!isHeader) {
                    assert props != null;
                    props.add(line);
                } else {
                    createSection(props, header);
                    header = matcher.group(1);
                    props.clear();
                }
            }
            assert props != null;
            createSection(props, header);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void store(OutputStream outputStream) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            for (String section : sections.keySet()) {
                writer.append('[').append(section).append(']');
                writer.newLine();
                sections.get(section).store(writer, null);
            }
        }
    }

    private void createSection(ArrayList<String> props, String header) throws IOException {
        if (props.size() > 0) {
            IniProperties properties = new IniProperties();
            File temp = File.createTempFile("ini_section", "properties");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(temp))) {
                for (String l : props)
                    writer.append(l).append('\n');
            }
            properties.load(new BufferedReader(new FileReader(temp)));
            sections.put(header, properties);
            temp.delete();
        }
    }

    public HashMap<String, IniProperties> getSections() {
        return sections;
    }

    public void setSections(HashMap<String, IniProperties> sections) {
        this.sections = sections;
    }
}
