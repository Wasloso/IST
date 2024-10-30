import java.lang.reflect.Field;

public class Debug {
    static void fields(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String fieldType = field.getType().toString();
            String fieldValue;
            try {
                fieldValue = field.get(obj).toString();
            } catch (Exception e) {
                fieldValue = "cannot access value";
                e.printStackTrace();
            }
            field.setAccessible(false);
            System.out.println("Pole: " + fieldName + " => " + fieldType + ", " + fieldValue);
        }
    }
}



