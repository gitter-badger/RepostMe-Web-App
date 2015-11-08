package ru.intcode.repostme.webapp.logic;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.sql2o.Connection;

public class KuponDescription {

    public float discount;
    public String name;
    public String description;
    public String fullDescription;
    public String kupon;

    public static final String selectByUidQuery
            = "SELECT discount, name, description, fullDescription, kupon FROM (SELECT * FROM kupons WHERE uid = :uid) AS tmp LEFT JOIN offers ON offers.id = tmp.oid;";

    public static KuponDescription selectByUid(Database db, long uid) {
        try (Connection c = db.getSql2o().open()) {
            KuponDescription ret = c.createQuery(selectByUidQuery)
                    .addParameter("uid", uid)
                    .executeAndFetchFirst(KuponDescription.class);
            if (ret != null) {
                return ret;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getQR(String kupon) {
        try {
            Charset charset = Charset.forName("UTF-8");
            CharsetEncoder encoder = charset.newEncoder();
            byte[] b = null;

            ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(kupon));
            b = bbuf.array();

            String data;

            data = new String(b, "UTF-8");
            BitMatrix matrix = null;
            int h = 200;
            int w = 200;
            com.google.zxing.Writer writer = new MultiFormatWriter();
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(2);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, w, h, hints);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            OutputStream b64 = new Base64OutputStream(os, true, -1, null);
            MatrixToImageWriter.writeToStream(matrix, "PNG", b64);
            return os.toString("UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
