package wikiradio.neslihan.tur.org.wikiradio.data.callback;

import java.util.ArrayList;

/**
 * Call when category information get data process end with success or error
 */

public interface CategoryListCallback {
    void onSuccess(ArrayList<String> categoryList);
    void onError(Class sender);
}
