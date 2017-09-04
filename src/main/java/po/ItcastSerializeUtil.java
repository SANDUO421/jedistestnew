package po;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ItcastSerializeUtil {
   
   public static byte[] serialize(Object object) {
       ObjectOutputStream oos = null;
       ByteArrayOutputStream baos = null;
       try {
           if (object != null){
               baos = new ByteArrayOutputStream();
               oos = new ObjectOutputStream(baos);
               oos.writeObject(object);
               return baos.toByteArray();
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }

   
   
   
   public static Object unserialize(byte[] bytes) {
       ByteArrayInputStream bais = null;
       try {
           if (bytes != null && bytes.length > 0){
               bais = new ByteArrayInputStream(bytes);
               ObjectInputStream ois = new ObjectInputStream(bais);
               return ois.readObject();
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }
   
}
