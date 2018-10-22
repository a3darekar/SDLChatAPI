package sdl_apps.sdlchatapi.services.service_configs;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogConfig {
    public static ProgressDialog config(Context context, String message) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        return progressDialog;
    }
}
