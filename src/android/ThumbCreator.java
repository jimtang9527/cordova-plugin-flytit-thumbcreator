import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult;
import android.util.Log;
import android.provider.Settings;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.lang.Exception;
import android.media.ThumbnailUtils;
import android.os.Environment;
import java.io.*;
import com.google.gson.Gson;

public class ThumbCreator extends CordovaPlugin {

    public static final String TAG = "ThumbCreator";
    public static int width = 100;
    public static int height = 100;

    /**
     * Constructor.
     */
    public ThumbCreator() {}

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callback) throws JSONException {

        String fromPath = args.getString(0);

        if(fromPath.startsWith("file://")) {
            fromPath = fromPath.substring(6);
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            File file = new File(fromPath);

            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            Bitmap thumb = ThumbnailUtils.extractThumbnail(bitmap, ThumbCreator.width, ThumbCreator.height);

            Log.i("thumbnailSallImage",fromPath+" existed");
 
            OutputStream fOut = null;
 
            File sourceFile = new File(fromPath);
            String fileName = sourceFile.getName();
            int index = fileName.lastIndexOf('.');
            String name = fileName.substring(0, index);
            String ext = fileName.substring(index);
            File targetFile = File.createTempFile("thumb_" + name, ext);

            if(targetFile.exists()) {
                fOut = new FileOutputStream(targetFile);
                thumb.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();

                ThumbData data = new ThumbData();
                data.setAbsolutePath(targetFile.getAbsolutePath());
                data.setSuccess(true);

                Gson gson = new Gson();
                String json = gson.toJson(data);
                callback.success(json);
                return true;
            }
            callback.error("An errror occured.");
            return false;
        } catch (Exception e) {
            callback.error("An errror occured: " + e.toString());
            return false;
        }
    }
 }