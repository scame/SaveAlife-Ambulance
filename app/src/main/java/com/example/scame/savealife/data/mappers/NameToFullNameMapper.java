package com.example.scame.savealife.data.mappers;


import com.example.scame.savealife.data.repository.FileDataManagerImp;
import com.graphhopper.util.Helper;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class NameToFullNameMapper {

    public static Map<String, String> getNameToFullNameMap(List<String> nameList) {
        final Map<String, String> nameToFullName = new TreeMap<>();

        for (String fullName : nameList) {
            String tmp = Helper.pruneFileEnd(fullName);
            if (tmp.endsWith("-gh"))
                tmp = tmp.substring(0, tmp.length() - 3);

            tmp = FileDataManagerImp.getFileName(tmp);
            nameToFullName.put(tmp, fullName);
        }

        return nameToFullName;
    }
}
