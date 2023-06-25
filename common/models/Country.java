package common.models;


import java.io.Serializable;

public enum Country implements Serializable {
    RUSSIA,
    UNITED_KINGDOM,
    USA,
    FRANCE,
    SPAIN;

    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var forms : values()) {
            nameList.append(forms.name()).append("\n");
        }
        return nameList.substring(0, nameList.length()-1);
    }
}
