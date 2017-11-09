package Helper;

import java.util.BitSet;

public class Helper {
    public static String bitSetToString(BitSet bs) {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < bs.length(); i++) {
            if (bs.get(i) == true) {
                res.append(1);
            } else {
                res.append(0);
            }
        }

        return res.toString();
    }
}
