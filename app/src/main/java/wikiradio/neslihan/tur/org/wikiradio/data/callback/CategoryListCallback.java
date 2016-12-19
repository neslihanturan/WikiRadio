package wikiradio.neslihan.tur.org.wikiradio.data.callback;

import java.util.HashSet;
import java.util.Set;

/**
 * Call when category information get data process end with success or error
 */

public interface CategoryListCallback {
    void onSuccess(HashSet<String> categoryList); //save category list as set to prevent duplications
    void onError(Class sender);
}
